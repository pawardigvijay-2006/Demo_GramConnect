package com.tech_fusion.view.villager;

import java.awt.Desktop;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * GramConnect - Razorpay UPI checkout helper (beginner-friendly version).
 *
 * ============================================================
 * HOW THIS WORKS - IN PLAIN STEPS
 * ============================================================
 * This class does NOT use a WebView or any JavaScript. It only uses:
 * - Plain JavaFX (Stage, Label, Button, ProgressIndicator, Timeline)
 * - Java's built-in HttpClient to call Razorpay's REST API
 * - Java's built-in Desktop class to open the user's web browser
 *
 * The flow, step by step:
 * 1. We call Razorpay's "Payment Links" API to create a payment link.
 * This is a single HTTP POST request - Razorpay gives us back a
 * short URL like https://rzp.io/i/abc123.
 * 2. We open that URL in the user's normal web browser (Desktop.browse).
 * The villager completes the UPI payment there, using whichever UPI
 * app they already have installed.
 * 3. Back in our JavaFX app, we show a small "Waiting for payment..."
 * dialog and check Razorpay every few seconds (a Timeline "tick")
 * to see if the payment link's status changed to "paid".
 * 4. As soon as it's "paid", we call callback.onSuccess(...). If the
 * villager cancels, or the link expires, we call callback.onFailure(...).
 *
 * ============================================================
 * BEFORE THIS WORKS FOR REAL PAYMENTS
 * ============================================================
 * 1. Replace KEY_ID and KEY_SECRET below with your real Razorpay API
 * keys (Settings -> API Keys in the Razorpay dashboard).
 * 2. IMPORTANT SECURITY NOTE: KEY_SECRET should never really live inside
 * a desktop app that villagers install, because anyone could extract
 * it. The correct production setup is: your own backend server holds
 * KEY_SECRET, and this app calls YOUR server instead of calling
 * Razorpay directly. The two methods below - createPaymentLink() and
 * getPaymentLinkStatus() - are exactly the two calls you would move
 * to your backend. Everything else in this file (the JavaFX dialog,
 * opening the browser, polling) stays the same either way.
 * 3. This class needs internet access, since it talks to
 * https://api.razorpay.com.
 * ============================================================
 */
public class RazorpayUpiCheckout {

        // TODO: replace with your real Razorpay key id and key secret (test or live).
        // See the security note above before shipping this to real users.
        private static final String KEY_ID = "rzp_test_YOUR_KEY_ID";
        private static final String KEY_SECRET = "YOUR_KEY_SECRET";

        private static final String API_BASE = "https://api.razorpay.com/v1";

        /** How often we check Razorpay to see if the payment is done. */
        private static final Duration POLL_INTERVAL = Duration.seconds(3);

        public interface PaymentCallback {
                /** Called when Razorpay confirms the payment went through. */
                void onSuccess(String razorpayPaymentId);

                /** Called if the payment failed, expired, or the villager cancelled. */
                void onFailure(String reason);
        }

        /**
         * Opens a simple JavaFX "Pay with UPI" dialog, creates a Razorpay payment
         * link, opens it in the browser, and waits for the payment to complete.
         *
         * @param ownerStage      the window that owns this dialog (pass
         *                        VillagerDashboard.homeStage)
         * @param amountInRupees  amount to charge, in rupees
         * @param customerName    shown to the villager on Razorpay's page
         * @param customerPhone   villager's phone number (10 digits, no +91)
         * @param description     short text shown on Razorpay's page (e.g. "Water Bill")
         * @param callback        called with the result - always on the JavaFX thread
         */
        public static void openUpiCheckout(Stage ownerStage, double amountInRupees, String customerName,
                        String customerPhone, String description, PaymentCallback callback) {

                // ---- 1. Build the small "please wait" dialog ----
                Label statusLabel = new Label("Creating your payment link...");
                statusLabel.setWrapText(true);
                statusLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #10251A;");

                ProgressIndicator spinner = new ProgressIndicator();
                spinner.setPrefSize(40, 40);

                Button cancelButton = new Button("Cancel Payment");

                VBox box = new VBox(16, spinner, statusLabel, cancelButton);
                box.setAlignment(Pos.CENTER);
                box.setPadding(new Insets(28));
                box.setStyle("-fx-background-color: #F4F8FB;");

                Stage dialog = new Stage(StageStyle.UTILITY);
                dialog.initOwner(ownerStage);
                dialog.initModality(Modality.WINDOW_MODAL);
                dialog.setTitle("GramConnect - UPI Payment");
                dialog.setScene(new Scene(box, 340, 240));
                dialog.setResizable(false);

                // A Timeline may be running to poll for payment status - we keep a
                // reference so the Cancel button (or dialog close) can stop it.
                Timeline[] pollingTimelineHolder = new Timeline[1];

                cancelButton.setOnAction(e -> {
                        if (pollingTimelineHolder[0] != null) {
                                pollingTimelineHolder[0].stop();
                        }
                        dialog.close();
                        callback.onFailure("Payment cancelled by user");
                });

                dialog.show();

                // ---- 2. Create the payment link (this talks to the network, so it
                // runs on a background Task, not the JavaFX thread) ----
                Task<PaymentLink> createLinkTask = new Task<>() {
                        @Override
                        protected PaymentLink call() throws Exception {
                                return createPaymentLink(amountInRupees, customerName, customerPhone, description);
                        }
                };

                createLinkTask.setOnSucceeded(e -> {
                        PaymentLink link = createLinkTask.getValue();
                        statusLabel.setText("Opening the payment page in your browser...\n\n"
                                        + "Complete the payment there, then come back here.");
                        openInBrowser(link.shortUrl);

                        // ---- 3. Start checking Razorpay every few seconds ----
                        Timeline polling = startPolling(link.id, statusLabel, dialog, callback);
                        pollingTimelineHolder[0] = polling;
                });

                createLinkTask.setOnFailed(e -> {
                        dialog.close();
                        Throwable error = createLinkTask.getException();
                        callback.onFailure("Could not start payment: "
                                        + (error == null ? "unknown error" : error.getMessage()));
                });

                // Run the network call on its own background thread so the JavaFX
                // window never freezes while we wait for Razorpay to respond.
                Thread backgroundThread = new Thread(createLinkTask);
                backgroundThread.setDaemon(true);
                backgroundThread.start();
        }

        /**
         * Repeatedly checks the payment link's status every POLL_INTERVAL seconds.
         * Returns the Timeline so the caller can stop it early (e.g. on Cancel).
         */
        private static Timeline startPolling(String linkId, Label statusLabel, Stage dialog,
                        PaymentCallback callback) {

                Timeline timeline = new Timeline();
                timeline.setCycleCount(Timeline.INDEFINITE);

                KeyFrame checkStatus = new KeyFrame(POLL_INTERVAL, event -> {
                        Task<String> statusTask = new Task<>() {
                                @Override
                                protected String call() throws Exception {
                                        return getPaymentLinkStatus(linkId);
                                }
                        };

                        statusTask.setOnSucceeded(e -> {
                                String status = statusTask.getValue();

                                if ("paid".equals(status)) {
                                        timeline.stop();
                                        dialog.close();
                                        callback.onSuccess(linkId);
                                } else if ("cancelled".equals(status) || "expired".equals(status)) {
                                        timeline.stop();
                                        dialog.close();
                                        callback.onFailure("Payment link " + status);
                                } else {
                                        // still "created" / "issued" - waiting for the villager to pay
                                        statusLabel.setText("Waiting for you to complete the payment...");
                                }
                        });

                        statusTask.setOnFailed(e -> {
                                // A single failed status check isn't fatal - we just try again
                                // on the next tick instead of cancelling the whole payment.
                                statusLabel.setText("Still checking... (having trouble reaching Razorpay)");
                        });

                        Thread backgroundThread = new Thread(statusTask);
                        backgroundThread.setDaemon(true);
                        backgroundThread.start();
                });

                timeline.getKeyFrames().add(checkStatus);
                timeline.play();
                return timeline;
        }

        // =================================================================
        // NETWORK CALLS
        // These two methods are the only ones that talk to Razorpay. In a
        // production app, move both of these onto your own backend server -
        // the JavaFX code above wouldn't need to change at all.
        // =================================================================

        /** A tiny holder for the two pieces of info we need back from Razorpay. */
        private static class PaymentLink {
                final String id;
                final String shortUrl;

                PaymentLink(String id, String shortUrl) {
                        this.id = id;
                        this.shortUrl = shortUrl;
                }
        }

        private static PaymentLink createPaymentLink(double amountInRupees, String customerName,
                        String customerPhone, String description) throws Exception {

                long amountInPaise = Math.round(amountInRupees * 100);

                // Razorpay expects a small JSON body describing the payment link.
                String requestBody = "{"
                                + "\"amount\": " + amountInPaise + ","
                                + "\"currency\": \"INR\","
                                + "\"description\": \"" + escapeJson(description) + "\","
                                + "\"customer\": {"
                                + "  \"name\": \"" + escapeJson(customerName) + "\","
                                + "  \"contact\": \"" + escapeJson(customerPhone) + "\""
                                + "},"
                                + "\"notify\": { \"sms\": false, \"email\": false },"
                                + "\"method\": \"upi\""
                                + "}";

                HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(API_BASE + "/payment_links"))
                                .header("Content-Type", "application/json")
                                .header("Authorization", basicAuthHeader())
                                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                                .build();

                HttpResponse<String> response = HttpClient.newHttpClient()
                                .send(request, HttpResponse.BodyHandlers.ofString());

                String id = extractJsonString(response.body(), "id");
                String shortUrl = extractJsonString(response.body(), "short_url");

                if (id == null || shortUrl == null) {
                        throw new Exception("Razorpay did not return a payment link. Response: " + response.body());
                }

                return new PaymentLink(id, shortUrl);
        }

        private static String getPaymentLinkStatus(String linkId) throws Exception {
                HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(API_BASE + "/payment_links/" + linkId))
                                .header("Authorization", basicAuthHeader())
                                .GET()
                                .build();

                HttpResponse<String> response = HttpClient.newHttpClient()
                                .send(request, HttpResponse.BodyHandlers.ofString());

                String status = extractJsonString(response.body(), "status");
                return status == null ? "created" : status;
        }

        /** Razorpay uses HTTP Basic Auth: base64("key_id:key_secret"). */
        private static String basicAuthHeader() {
                String credentials = KEY_ID + ":" + KEY_SECRET;
                String encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
                return "Basic " + encoded;
        }

        /**
         * Very small helper that pulls a single string value out of a JSON
         * response, e.g. extractJsonString(body, "id") finds "id":"abc123" and
         * returns "abc123". This avoids needing an extra JSON library for such a
         * small app - for anything bigger, use a real JSON library instead.
         */
        private static String extractJsonString(String json, String key) {
                Pattern pattern = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"([^\"]*)\"");
                Matcher matcher = pattern.matcher(json);
                return matcher.find() ? matcher.group(1) : null;
        }

        private static String escapeJson(String value) {
                if (value == null) {
                        return "";
                }
                return value.replace("\\", "\\\\").replace("\"", "\\\"");
        }

        /** Opens a URL in the villager's normal web browser. */
        private static void openInBrowser(String url) {
                try {
                        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                                Desktop.getDesktop().browse(URI.create(url));
                        }
                } catch (Exception e) {
                        // If we can't auto-open the browser, the villager can still copy the
                        // link manually - in a real app you'd show the URL in the dialog too.
                        e.printStackTrace();
                }
        }
}