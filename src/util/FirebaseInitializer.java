package util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FirebaseInitializer {
    public static void initialize() {
        try {
            InputStream serviceAccount = new FileInputStream("firebase/api-bibliotech-firebase-adminsdk-fbsvc-c2df28b743.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("api-bibliotech.firebasestorage.app")
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("Firebase inicializado com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao inicializar Firebase: " + e.getMessage());
            throw new RuntimeException("Falha na inicialização do Firebase", e);
        }
    }
}