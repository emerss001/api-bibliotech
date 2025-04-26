package util;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import javax.servlet.http.Part;
import java.io.InputStream;
import java.util.UUID;

public class FirebaseUpload {
    public static String upload(Part filePart) {
        try {

            String fileName = filePart.getSubmittedFileName().trim().replace(" ", "_");
            String contentType = filePart.getContentType();
            InputStream fileStream = filePart.getInputStream();

            return uploadArquivo(fileStream, fileName, contentType);
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao fazer upload: " + e.getMessage();
        }
    }

    public static String uploadArquivo(InputStream inputStream, String nomeOriginal, String contentType) throws Exception {
        String nomeArquivo = UUID.randomUUID() + "-" + nomeOriginal;

        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob = bucket.create(nomeArquivo, inputStream, contentType);

        // Tornar o arquivo público
        blob.createAcl(com.google.cloud.storage.Acl.of(com.google.cloud.storage.Acl.User.ofAllUsers(), com.google.cloud.storage.Acl.Role.READER));

        return String.format("https://storage.googleapis.com/%s/%s", bucket.getName(), nomeArquivo);
    }
}
