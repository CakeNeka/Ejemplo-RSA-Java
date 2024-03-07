import javax.crypto.Cipher;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class EncryptedWriter {
    private String secretMessage;
    private PublicKey publicKey;

    public void setSecretMessage(String secretMessage) {
        this.secretMessage = secretMessage;
    }

    /**
     * Recupera la clave pública del fichero escrito por {@code KeyManager}
     */
    public void readPublicKey() throws Exception{
        File publicKeyFile = new File("files/public.key");
        byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        publicKey = keyFactory.generatePublic(publicKeySpec);
    }

    public byte[] writeEncryptedMessage() throws Exception{
        byte[] messageBytes = secretMessage.getBytes();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(messageBytes);


        Files.createDirectories(Path.of("files"));
        try (FileOutputStream fos = new FileOutputStream("files/encrypted_msg")) {
            fos.write(encryptedBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return encryptedBytes;
    }

}
