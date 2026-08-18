package com.tech_fusion.view.login;

import com.tech_fusion.view.admin.Dashboard;
import com.tech_fusion.view.login.SplashScreen;
// import com.tech_fusion.view.sarpanch.SarpanchDashboard;
// import com.tech_fusion.view.villager.GovernmentSchemes;
// import com.tech_fusion.view.villager.ProjectTransparency;
import com.tech_fusion.view.sarpanch.SarpanchDashboard;
import com.tech_fusion.view.villager.VillagerDashboard;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {

        try {
            Class.forName("demo_gramconnect\\src\\main\\java\\com\\tech_fusion\\config\\FirebaseConfig.java");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Hello world!");
        Application.launch(SplashScreen.class, args);
    }
}