import javax.crypto.Cipher;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

public class KeyManager {
    /**
     * Contiene claves pública y privada
     */
    private KeyPair keyPair;

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    /**
     * Genera clave pública y privada
     */
    public void generateKeyPair() throws NoSuchAlgorithmException {
        System.out.println(Arrays.toString(Security.getProviders()));
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA"); // Generador de claves RSA
        generator.initialize(1024);
        keyPair = generator.generateKeyPair();
    }

    public void writePublicKey() throws IOException {
        Files.createDirectories(Path.of("files"));
        try (FileOutputStream fos = new FileOutputStream("files/public.key")) {
            fos.write(keyPair.getPublic().getEncoded());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String decryptFile() throws Exception {
        File encryptedFile = new File("files/encrypted_msg");
        byte[] encryptedBytes = Files.readAllBytes(encryptedFile.toPath());
        return decrypt(encryptedBytes);
    }

    private String decrypt(byte[] encryptedBytes) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate()); // Clave privada para desencriptar
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

}
