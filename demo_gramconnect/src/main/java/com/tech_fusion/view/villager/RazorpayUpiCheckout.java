package com.tech_fusion.view.villager;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import netscape.javascript.JSObject;

/**
 * GramConnect - Razorpay UPI checkout helper.
 *
 * ============================================================
 * HOW THIS WORKS
 * ============================================================
 * Razorpay Checkout is a web widget (checkout.js), so on a JavaFX
 * desktop app the standard integration is: open a small modal Stage
 * with a WebView, load an HTML page that pulls in
 * https://checkout.razorpay.com/v1/checkout.js, configure it for the
 * UPI payment method, and bridge the JS success/failure callbacks
 * back into Java via a JSObject "window.javaConnector" object.
 *
 * ============================================================
 * BEFORE THIS WORKS FOR REAL PAYMENTS
 * ============================================================
 * 1. Replace RAZORPAY_KEY_ID below with your real Razorpay **key id**
 *    (the public one, safe to ship in client code - never the key
 *    secret).
 * 2. Razorpay strongly recommends (and some payment methods require)
 *    creating an Order first via the Orders API, which needs your
 *    key secret and MUST be done on a server you control, not from
 *    this desktop app. createOrderIdOnYourServer() below is a stub
 *    showing where that call goes - wire it to your backend
 *    (e.g. POST /api/payments/create-order) and pass the returned
 *    order_id into openUpiCheckout().
 * 3. This app requires internet access + the JavaFX WebView (jfxwebkit)
 *    module to be present, since checkout.js is loaded from Razorpay's
 *    CDN at runtime.
 * ============================================================
 */
public class RazorpayUpiCheckout {

        // TODO: replace with your real Razorpay key id (test or live).
        private static final String RAZORPAY_KEY_ID = "rzp_test_YOUR_KEY_ID";

        public interface PaymentCallback {
                /** Called on the JavaFX Application Thread when Razorpay reports success. */
                void onSuccess(String razorpayPaymentId);

                /** Called on the JavaFX Application Thread on failure/cancel, with a human-readable reason. */
                void onFailure(String reason);
        }

        /**
         * TODO: call your backend's order-creation endpoint here (it should call
         * Razorpay's Orders API server-side using your key secret) and return the
         * resulting order_id. Returning null lets checkout.js run in test mode
         * without a pre-created order, which is fine for local testing but NOT
         * recommended for production.
         */
        public static String createOrderIdOnYourServer(double amountInRupees) {
                // Example (pseudo-code) once you have a backend:
                // return YourApiClient.post("/api/payments/create-order",
                // Map.of("amount", amountInRupees)).get("orderId");
                return null;
        }

        /**
         * Opens a modal Razorpay Checkout window configured for UPI payment.
         *
         * @param ownerStage       the stage to center/own the modal (pass
         *                         VillagerDashboard.homeStage)
         * @param orderId          Razorpay order id from your backend, or null to
         *                         skip (test mode only - see class docs)
         * @param amountInRupees   amount to charge, in rupees (converted to paise
         *                         internally)
         * @param customerName     prefilled customer name
         * @param customerContact  prefilled customer phone number (10 digits)
         * @param description      shown in the Razorpay checkout header
         * @param callback         success/failure callback, always invoked on the
         *                         JavaFX Application Thread
         */
        public static void openUpiCheckout(Stage ownerStage, String orderId, double amountInRupees,
                        String customerName, String customerContact, String description, PaymentCallback callback) {

                long amountInPaise = Math.round(amountInRupees * 100);

                String orderIdField = orderId == null || orderId.isBlank()
                                ? ""
                                : "\"order_id\": \"" + escapeJs(orderId) + "\",";

                String html = "<!DOCTYPE html><html><head><meta charset='utf-8'>"
                                + "<script src='https://checkout.razorpay.com/v1/checkout.js'></script>"
                                + "<style>html,body{margin:0;height:100%;background:#F4F8FB;"
                                + "font-family:Arial,sans-serif;display:flex;align-items:center;"
                                + "justify-content:center;color:#10251A;}</style></head>"
                                + "<body><div id='status'>Opening secure UPI checkout...</div>"
                                + "<script>"
                                + "function openCheckout(){"
                                + "var options={"
                                + "\"key\": \"" + escapeJs(RAZORPAY_KEY_ID) + "\","
                                + orderIdField
                                + "\"amount\": " + amountInPaise + ","
                                + "\"currency\": \"INR\","
                                + "\"name\": \"GramConnect\","
                                + "\"description\": \"" + escapeJs(description) + "\","
                                + "\"method\": { \"upi\": true },"
                                + "\"prefill\": {"
                                + "  \"name\": \"" + escapeJs(customerName) + "\","
                                + "  \"contact\": \"" + escapeJs(customerContact) + "\","
                                + "  \"method\": \"upi\""
                                + "},"
                                + "\"theme\": { \"color\": \"#005B1B\" },"
                                + "\"handler\": function(response){"
                                + "  window.javaConnector.onSuccess(response.razorpay_payment_id);"
                                + "},"
                                + "\"modal\": {"
                                + "  \"ondismiss\": function(){ window.javaConnector.onFailure('Payment cancelled by user'); }"
                                + "}"
                                + "};"
                                + "var rzp = new Razorpay(options);"
                                + "rzp.on('payment.failed', function (resp){"
                                + "  window.javaConnector.onFailure(resp.error && resp.error.description ? resp.error.description : 'Payment failed');"
                                + "});"
                                + "rzp.open();"
                                + "}"
                                + "window.onload = openCheckout;"
                                + "</script></body></html>";

                Stage dialog = new Stage(StageStyle.UTILITY);
                dialog.initOwner(ownerStage);
                dialog.initModality(Modality.WINDOW_MODAL);
                dialog.setTitle("GramConnect - Secure UPI Payment");

                WebView webView = new WebView();
                webView.setPrefSize(480, 640);
                WebEngine engine = webView.getEngine();

                engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                        if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                                JSObject window = (JSObject) engine.executeScript("window");
                                window.setMember("javaConnector", new Bridge(dialog, callback));
                        }
                });

                engine.loadContent(html);

                StackPane root = new StackPane(webView);
                root.setPadding(new Insets(0));
                dialog.setScene(new Scene(root, 480, 640));
                dialog.showAndWait();
        }

        private static String escapeJs(String value) {
                if (value == null) {
                        return "";
                }
                return value.replace("\\", "\\\\").replace("\"", "\\\"");
        }

        /** JS-callable bridge object exposed as window.javaConnector inside the checkout page. */
        public static class Bridge {
                private final Stage dialog;
                private final PaymentCallback callback;
                private boolean resolved = false;

                Bridge(Stage dialog, PaymentCallback callback) {
                        this.dialog = dialog;
                        this.callback = callback;
                }

                // Called from JavaScript - method name/signature must stay exactly as used in the HTML above.
                public void onSuccess(String razorpayPaymentId) {
                        if (resolved) {
                                return;
                        }
                        resolved = true;
                        Platform.runLater(() -> {
                                dialog.close();
                                callback.onSuccess(razorpayPaymentId);
                        });
                }

                // Called from JavaScript - method name/signature must stay exactly as used in the HTML above.
                public void onFailure(String reason) {
                        if (resolved) {
                                return;
                        }
                        resolved = true;
                        Platform.runLater(() -> {
                                dialog.close();
                                callback.onFailure(reason);
                        });
                }
        }
}