package util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.ByteArrayInputStream;

public class FirebaseInitializer {
    private static final Dotenv dotenv = loadDotenv();

    public static void initialize() {
        try {
            String privateKey = System.getenv("FIREBASE_PRIVATE_KEY");
            if (privateKey == null && dotenv != null) {
                privateKey = dotenv.get("FIREBASE_PRIVATE_KEY");
            }

            if (privateKey == null) {
                throw new RuntimeException("Variável FIREBASE_PRIVATE_KEY não encontrada");
            }

            // Corrige quebra de linha e remove aspas externas, se existirem
            if (privateKey.startsWith("\"") && privateKey.endsWith("\"")) {
                privateKey = privateKey.substring(1, privateKey.length() - 1);
            }
            privateKey = privateKey.replace("\\n", "\n");

            String serviceAccountJson = """
            {
              "type": "service_account",
              "project_id": "%s",
              "private_key_id": "%s",
              "private_key": "%s",
              "client_email": "%s",
              "client_id": "%s",
              "auth_uri": "%s",
              "token_uri": "%s",
              "auth_provider_x509_cert_url": "%s",
              "client_x509_cert_url": "%s",
              "universe_domain": "%s"
            }
            """.formatted(
                    getEnvOrDotenv("FIREBASE_PROJECT_ID"),
                    getEnvOrDotenv("FIREBASE_PRIVATE_KEY_ID"),
                    privateKey,
                    getEnvOrDotenv("FIREBASE_CLIENT_EMAIL"),
                    getEnvOrDotenv("FIREBASE_CLIENT_ID"),
                    getEnvOrDotenv("FIREBASE_AUTH_URI"),
                    getEnvOrDotenv("FIREBASE_TOKEN_URI"),
                    getEnvOrDotenv("FIREBASE_AUTH_PROVIDER_CERT_URL"),
                    getEnvOrDotenv("FIREBASE_CLIENT_CERT_URL"),
                    getEnvOrDotenv("FIREBASE_UNIVERSE_DOMAIN")
            );

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(
                            new ByteArrayInputStream(serviceAccountJson.getBytes())
                    ))
                    .setStorageBucket(getEnvOrDotenv("FIREBASE_STORAGE_BUCKET"))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase inicializado com sucesso!");
            } else {
                System.out.println("Firebase já foi inicializado.");
            }

        } catch (Exception e) {
            System.err.println("Erro ao inicializar Firebase: " + e.getMessage());
            throw new RuntimeException("Falha na inicialização do Firebase", e);
        }
    }

    private static String getEnvOrDotenv(String key) {
        String value = System.getenv(key);
        String environment = System.getenv("ENVIRONMENT");

        if (environment != null && environment.equalsIgnoreCase("production")) {
            return value;
        }

        return value != null ? value : (dotenv != null ? dotenv.get(key) : null);
    }

    private static Dotenv loadDotenv() {
        try {
            return Dotenv.load();
        } catch (Exception e) {
            return null;
        }
    }
}
