//package com.greenfarm.service;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//import org.springframework.stereotype.Service;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//
//import jakarta.annotation.PostConstruct;
//
//// ...
//
//@Service
//public class FirebaseService {
//    @PostConstruct
//    public void initialize() {
//        try {
//            // Load Firebase Admin SDK credentials from a JSON file (you need to provide this file).
//            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("serviceAccountKey.json");
//
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .setStorageBucket("imageProduct")
//                .build();
//
//            FirebaseApp.initializeApp(options);
//        } catch (IOException e) {
//            // Handle initialization error
//        }
//    }
//}
