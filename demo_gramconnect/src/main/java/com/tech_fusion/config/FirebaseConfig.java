package com.tech_fusion.config;

import java.io.FileInputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class FirebaseConfig {

    static{
        firebaseConfig();
    }

    private static void firebaseConfig() {

        try {

            FileInputStream serviceAccount = new FileInputStream("demo_gramconnect\\src\\main\\resources\\gramconnect-Firebase.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Firestore getDb() {
        return FirestoreClient.getFirestore();
    }
}
