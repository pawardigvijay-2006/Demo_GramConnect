package com.tech_fusion.controller;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

public class AuthController {

    private String API_KEY = "AIzaSyA6ZK44flkozopci4KcHV_cFzOcP97RtI8";

    UserController userController = new UserController();

    public boolean signUp(String email, String password, String name, String mobile, String villagename, String role) {

        JSONObject payload = new JSONObject()
                .put("email", email)
                .put("password", password);

        try {

            HttpClient client = HttpClient.newHttpClient();

            URI uri = URI.create("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" +API_KEY);

            HttpRequest request = (HttpRequest) HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .build();

            System.out.println(request);

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response);
            System.out.println(response.statusCode());
            System.out.println(response.body());

            if (response.statusCode() == 200) {
                userController.createUsers(email, name, mobile, villagename, role);
                return true;
            } 
    

        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public boolean signIn(String email, String password) {

        JSONObject payload = new JSONObject()
                .put("email", email)
                .put("password", password);

        try {

            HttpClient client = HttpClient.newHttpClient();

            URI uri = URI.create("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" +API_KEY);

            HttpRequest request = (HttpRequest) HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .build();

            System.out.println(request);

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response);
            System.out.println(response.statusCode());
            System.out.println(response.body());

            if (response.statusCode() == 200) {
                 return true;
            } else return false;

    

        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;

    }
}