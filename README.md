<h1 align="center"> RSA en Java</h1>

<div align="center">

[![Static Badge](https://img.shields.io/badge/Made_with_❤_by-Cakeneka-pink?style=flat)](https://github.com/CakeNeka)

</div>

Un ejemplo básico de uso del algoritmo **RSA** en Java


> ### Índice
>
> - [El algoritmo RSA](#el-algoritmo-rsa)
> - [Cómo ejecutar el ejemplo](#ejecución)
> - [Explicación](#explicación)
>   - [Clases](#clases)
>   - [Flujo de ejecución](#flujo)
> - [Código](#código)
>   - [Clase principal](#main)
>   - [Clase de descifrado](#descifrado)
>   - [Clase de cifrado](#cifrado)
> - [Fuentes](#fuentes)

## El algoritmo RSA

### Algoritmos asimétricos

Los algoritmos asimétricos se caracterizan por utilizar **dos claves** conectadas:

- Clave **pública**: Responsable del **cifrado**. Es comunicada al **emisor** del mensaje.
- Clave **privada**: Responsable del **descifrado**. Solo debe conocerla el **receptor** del mensaje.

Dada la clave privada es posible calcular la clave pública, pero dada
la clave pública es imposible calcular la clave privada.

Este sistema es **más costoso** que los algoritmos simétricos,
pero permite la comunicación segura entre emisor y receptor
sin haberse puesto de acuerdo previamente en una **clave secreta**. 

Los algoritmos asimétricos pueden utilizarse para intercambiar 
una clave **secreta** antes de iniciar la comunicación, por ejemplo:

```mermaid
sequenceDiagram
    actor A 
    actor B
    A->>A: Genera claves<br>pública y privada
    A->>B: Envía clave pública (PK)
    B->>B: Genera clave Secreta (SK)
    B->>B: Cifra SK con PK enviada por A
    B->>A: Envía clave secreta cifrada
    A->>A: Descifra clave secreta <br> con clave privada
```

### RSA

RSA (llamado así por sus creadores, Rivest, Shamir y Adleman) es un algoritmo
de cifrado asimétrico. Fue descrito en 1977 en el MIT.
La seguridad de este algoritmo radica en la dificultad de factorizar números grandes.

## Ejecución

El programa ha sido desarrollado en **IntelliJ IDEA**.

1. Ejecutar la clase **Main**
2. Introducir por teclado el mensaje a encriptar
3. Se mostrará el mensaje **encriptado** y después el mensaje **desencriptado**

## Explicación

### Clases

#### Clases del JDK

- `Base64`: Codificación para transformar una secuencia arbitraria de bytes 
  en una secuencia de caracteres o viceversa.
- `Cypher`
- Clases para **entrada/salida** (`Files`, `FileOutputStream`, `FileInputStream`)

#### Clases escritas para el ejemplo

Para visualizar el funcionamiento del algoritmo RSA, en este ejemplo dos clases
interactúan encriptando y desencriptando ficheros.

- [`Main`](./src/Main.java) contiene el método principal y se encarga de la interacción con el usuario.
- [`KeyManager`](./src/KeyManager.java) genera las claves **pública** y **privada**, solo comparte la clave **pública**
  para que cualquiera pueda enviarle mensajes encriptados. En una situación real, sería el **receptor** del mensaje encriptado.
- [`EncryptedWriter`](./src/EncryptedWriter.java) solo puede cifrar datos porque conoce únicamente la
  clave **pública**. En una situación real, sería el **emisor** del mensaje.


### Flujo

La aplicación sigue el siguiente flujo:

1. `KeyManager` genera la clave **pública** y **privada**
2. `KeyManager` escribe la clave **pública** en un fichero (`public.key`)
3. `EncryptedWriter` recupera la clave **pública** del fichero
4. `EncryptedWriter` **cifra** los datos con la clave pública y los escribe en un nuevo fichero (`encrypted_msg`)
5. `KeyManager` recupera y **descifra** los datos del fichero `encrypted_msg`

> [!important]
> Los datos del fichero **`encrypted_msg`** son binarios y no se pueden leer abriendo directamente el fichero

## Código

### Main

El método principal del programa

```java
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
```

### Cifrado

En [`EncryptedWriter`](./src/EncryptedWriter.java).

Método para cifrar un `String` y almacenar el mensaje cifrado en un fichero.

```java
/**
 * Escribe el mensaje cifrado en el fichero files/encrypted_msg
 *
 * Escribe directamente los bytes del mensaje encriptado para
 * evitar transformaciones innecesarias a String
 * @return Los bytes del mensaje encriptado, para mostrarlo en el método principal
 * @throws Exception
 */
public byte[] writeEncryptedMessage() throws Exception {
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
```

### Descifrado

En [`KeyManager`](./src/KeyManager.java).

Método para desencriptar el fichero encriptado

```java

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
```

## Fuentes

- [Baeldung](https://www.baeldung.com/java-rsa)
- [Wikipedia: RSA](https://en.wikipedia.org/wiki/RSA_(cryptosystem))
- [Wikipedia: Criptografía asimétrica](https://es.wikipedia.org/wiki/Criptograf%C3%ADa_asim%C3%A9trica)
- [Github Gist](https://gist.github.com/Anass-ABEA/e2627b5df9abf23e006bcd5d390de205)
