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

/**
 * Genera la clave pública y privada, comparte la clave pública mediante un fichero
 * y desencripta un mensaje
 */
public class KeyManager {

    /**
     * KeyPair Contiene claves pública y privada
     */
    private KeyPair keyPair;

    /**
     * Para mostrar la clave pública en el programa principal ({@code Main})
     * @return la clave pública
     */
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

    /**
     * Escribe los bytes de la clave pública
     */
    public void writePublicKey() throws IOException {
        Files.createDirectories(Path.of("files"));
        try (FileOutputStream fos = new FileOutputStream("files/public.key")) {
            fos.write(keyPair.getPublic().getEncoded());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Lee el fichero cifrado y devuelve el mensaje descifrado
     *
     * @return el mensaje descifrado
     */
    public String decryptFile() throws Exception {
        File encryptedFile = new File("files/encrypted_msg");
        byte[] encryptedBytes = Files.readAllBytes(encryptedFile.toPath());
        return decrypt(encryptedBytes);
    }

    /**
     * Descifra un mensaje cifrado
     *
     * @param encryptedBytes Bytes del mensaje encriptado
     * @return El mensaje descifrado
     */
    private String decrypt(byte[] encryptedBytes) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate()); // Clave privada para desencriptar
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

}
