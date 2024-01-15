package com.demo.config;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.notification.firebase")
@Log4j2
public class FirebaseConfig {
    private String firebaseConfigFile = "firebase.json";

    @PostConstruct
    private void initializeFirebase() {
        try {
            log.info("---------------------------------");
            log.info("Initializing FIREBASE from: " + firebaseConfigFile);
            log.info("---------------------------------");
            ClassPathResource classPathResource = new ClassPathResource(firebaseConfigFile);
            InputStream serviceAccount = classPathResource.getInputStream();

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            } else {
                log.info("FirebaseApp is already initialized.");
            }
        } catch (IOException e) {
            log.error("Error initializing FirebaseApp", e);
        }
    }
}
