import java.util.Base64;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        KeyManager keyManager = new KeyManager();
        EncryptedWriter encryptedwriter = new EncryptedWriter();
        Scanner scanner = new Scanner(System.in);

        try {
            keyManager.generateKeyPair();       // Genera claves pública y privada
            keyManager.writePublicKey();        // Almacena clave pública en fichero
            encryptedwriter.readPublicKey();    // Lee clave pública almacenada

            System.out.println("Clave pública generada: " +
                               Base64.getEncoder().encodeToString(keyManager.getPublicKey().getEncoded()));

            // Obtener entrada del usuario
            System.out.println("Introduce el mensaje secreto");
            System.out.print("> ");
            String userInput = scanner.nextLine();

            encryptedwriter.setSecretMessage(userInput);                // Establece mensaje a encriptar
            byte[] encrypted = encryptedwriter.writeEncryptedMessage(); // Encripta y escribe el mensaje
            String decrypted = keyManager.decryptFile();                // Lee el fichero y desencripta el mensaje

            System.out.println("Mensaje encriptado: " + Base64.getEncoder().encodeToString(encrypted));
            System.out.println("Mensaje desencriptado: " + decrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
