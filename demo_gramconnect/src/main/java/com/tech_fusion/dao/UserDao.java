package com.tech_fusion.dao;

import com.google.cloud.firestore.Firestore;
import com.tech_fusion.config.FirebaseConfig;
import com.tech_fusion.model.user.User;

public class UserDao {

    Firestore db = FirebaseConfig.getDb();

    public void setUsers(User user) {

        try {

            db.collection("Users").document(user.getEmail()).set(user);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}
