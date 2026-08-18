package com.tech_fusion.view.login;

//import com.tech_fusion.view.gramsevak.Dashboard;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {

        try {
            Class.forName("demo_gramconnect\\src\\main\\java\\com\\tech_fusion\\config\\FirebaseConfig.java");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Hello world!");
        Application.launch(SplashScreen.class,args);
    }
}