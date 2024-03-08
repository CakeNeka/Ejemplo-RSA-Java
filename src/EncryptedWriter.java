import javax.crypto.Cipher;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Lee el fichero que contiene la clave pública y escribe un mensaje cifrado
 * con esta clave.
 *
 * Como esta clase únicamente conoce la clave pública, solo puede
 * encriptar datos.
 */
public class EncryptedWriter {
    private String secretMessage;
    private PublicKey publicKey;

    /**
     * Establece el mensaje secreto a encriptar
     * @param secretMessage String del mensaje secreto
     */
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

    /**
     * Escribe el mensaje cifrado en el fichero files/encrypted_msg
     *
     * Escribe directamente los bytes del mensaje encriptado para
     * evitar transformaciones innecesarias a String
     * @return Los bytes del mensaje encriptado, para mostrarlo en el método principal
     * @throws Exception
     */
    public byte[] writeEncryptedMessage() throws Exception{
        byte[] messageBytes = secretMessage.getBytes();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(messageBytes);


        Files.createDirectories(Path.of("files")); // Crea el directorio si no existe
        try (FileOutputStream fos = new FileOutputStream("files/encrypted_msg")) {
            fos.write(encryptedBytes); // Escribe los bytes en el fichero
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return encryptedBytes;
    }

}
