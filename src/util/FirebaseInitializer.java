package util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FirebaseInitializer {
    public static void initialize() {
        try {
            // Tenta ler das variáveis de ambiente primeiro
            String privateKey = System.getenv("FIREBASE_PRIVATE_KEY");

            // Se não encontrou, tenta ler do .env (modo desenvolvimento)
            if (privateKey == null) {
                privateKey = Dotenv.load().get("FIREBASE_PRIVATE_KEY");
            }

            // Se ainda for nulo, lança erro
            if (privateKey == null) {
                throw new RuntimeException("Variável FIREBASE_PRIVATE_KEY não encontrada");
            }

            privateKey = privateKey
                    .replace("\\n", "\n")
                    .replace("\"", "");

            // Restante do seu código...
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

            FirebaseApp.initializeApp(options);
            System.out.println("Firebase inicializado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao inicializar Firebase: " + e.getMessage());
            throw new RuntimeException("Falha na inicialização do Firebase", e);
        }
    }

    private static String getEnvOrDotenv(String key) {
        String value = System.getenv(key);

        // Se estiver em produção, não tenta usar Dotenv
        String environment = System.getenv("ENVIRONMENT");
        if (environment != null && environment.equalsIgnoreCase("production")) {
            return value; // mesmo que seja null, não tenta ler do .env
        }

        // Se não estiver em produção, tenta fallback no .env
        return value != null ? value : Dotenv.load().get(key);
    }

}