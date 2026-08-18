package com.tech_fusion.controller;

import java.awt.Desktop;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.json.JSONObject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class Razorpay {

    private static final String KEY_ID = "rzp_test_TR7g43zC472aPa";
    private static final String KEY_SECRET = "R8awtM39WLObmPyaiW70rgyi";

    public Parent getView() {

        Label titleLabel = new Label("Razorpay Payment");

        titleLabel.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;"
        );

        Label amountLabel = new Label("Enter Amount");

        TextField amountField = new TextField();

        amountField.setPromptText("Enter amount");

        amountField.setMaxWidth(320);

        amountField.setStyle(
                "-fx-padding: 12px;" +
                "-fx-font-size: 15px;"
        );

        Button payButton = new Button("Pay Now");

        payButton.setStyle(
                "-fx-background-color: #2563EB;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 12px 40px;" +
                "-fx-background-radius: 8px;"
        );

        payButton.setOnAction(event -> {

            try {

                String amountText =
                        amountField.getText();

                if (amountText == null ||
                        amountText.trim().isEmpty()) {

                    showError(
                            "Please enter amount"
                    );

                    return;
                }

                double amount =
                        Double.parseDouble(
                                amountText
                        );

                if (amount <= 0) {

                    showError(
                            "Please enter valid amount"
                    );

                    return;
                }

                String paymentUrl =
                        createPaymentLink(
                                amount
                        );

                Desktop.getDesktop().browse(
                        new URI(paymentUrl)
                );

            } catch (NumberFormatException e) {

                showError(
                        "Amount must be a number"
                );

            } catch (Exception e) {

                e.printStackTrace();

                showError(
                        "Unable to start payment"
                );
            }
        });


        VBox root = new VBox(
                20,
                titleLabel,
                amountLabel,
                amountField,
                payButton
        );

        root.setAlignment(
                Pos.CENTER
        );

        root.setPadding(
                new Insets(40)
        );

        root.setStyle(
                "-fx-background-color: #F5F7FB;"
        );

        return root;
    }


    private String createPaymentLink(
            double amount) throws Exception {

        long amountInPaise =
                Math.round(
                        amount * 100
                );

        JSONObject requestBody =
                new JSONObject();

        requestBody.put(
                "amount",
                amountInPaise
        );

        requestBody.put(
                "currency",
                "INR"
        );

        requestBody.put(
                "description",
                "AI Startup Builder Payment"
        );


        String credentials =
                KEY_ID + ":" + KEY_SECRET;


        String encodedCredentials =
                Base64.getEncoder()
                        .encodeToString(
                                credentials.getBytes(
                                        StandardCharsets.UTF_8
                                )
                        );


        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        "https://api.razorpay.com/v1/payment_links"
                                )
                        )
                        .header(
                                "Authorization",
                                "Basic " + encodedCredentials
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .POST(
                                HttpRequest.BodyPublishers.ofString(
                                        requestBody.toString()
                                )
                        )
                        .build();


        HttpClient client =
                HttpClient.newHttpClient();


        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );


        System.out.println(
                "Razorpay Response : "
                        + response.body()
        );


        if (response.statusCode() < 200 ||
                response.statusCode() >= 300) {

            throw new RuntimeException(
                    "Razorpay Error : "
                            + response.body()
            );
        }


        JSONObject responseJson =
                new JSONObject(
                        response.body()
                );


        return responseJson.getString(
                "short_url"
        );
    }


    private void showError(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                "Payment Error"
        );

        alert.setHeaderText(null);

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }


	
}