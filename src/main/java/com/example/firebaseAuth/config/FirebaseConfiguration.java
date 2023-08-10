package com.example.firebaseAuth.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class FirebaseConfiguration {

    @Value("classpath:serviceAccountKey.json")
    Resource resourceFile;


//    @Bean
//    public FirebaseAuth firebaseAuth() {
//        return FirebaseAuth.getInstance();
//    }

    @Bean
    FirebaseAuth firebaseAuth() throws IOException {
        var options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(resourceFile.getInputStream()))
                .build();

        var firebaseApp = FirebaseApp.initializeApp(options);

        return FirebaseAuth.getInstance(firebaseApp);
    }

//    @PostConstruct
//    public void initializeFirebaseApp() throws IOException {
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setCredentials(GoogleCredentials.fromStream(resourceFile.getInputStream()))
//                .build();
//        FirebaseApp.initializeApp(options);
//    }

}
