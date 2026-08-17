package com.tech_fusion;

import com.tech_fusion.view.gramsevak.NewDashboard;
// import com.tech_fusion.view.sarpanch.SarpanchDashboard;
// import com.tech_fusion.view.villager.GovernmentSchemes;
// import com.tech_fusion.view.villager.ProjectTransparency;
import com.tech_fusion.view.sarpanch.SarpanchDashboard;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Application.launch(SarpanchDashboard.class, args);
    }
}