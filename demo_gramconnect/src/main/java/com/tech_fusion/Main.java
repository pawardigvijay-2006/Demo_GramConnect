package com.tech_fusion;

import com.tech_fusion.view.admin.Dashboard;
import com.tech_fusion.view.login.SplashScreen;
import com.tech_fusion.view.sarpanch.SarpanchDashboard;
import com.tech_fusion.view.villager.VillagerDashboard;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Application.launch(VillagerDashboard.class, args);
    }
}