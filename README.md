<h1 align="center"> RSA en Java</h1>

<div align="center">

[![Static Badge](https://img.shields.io/badge/Made_with_❤_by-Cakeneka-pink?style=flat)](https://github.com/CakeNeka)

</div>

Un ejemplo básico de uso del algoritmo **RSA** en Java

---

> ### Índice
>
> - [El algoritmo RSA](#el-algoritmo-rsa)
> - [Cómo ejecutar el ejemplo](#ejecución)
> - [Explicación](#explicación)
> - [Código](#código)
>   - Clase de descifrado
>   - Clase de cifrado
> - [Fuentes](#fuentes)

## El algoritmo RSA

### Algoritmos asimétricos

Los algoritmos asimétricos se caracterizan por utilizar **dos claves** conectadas:

- Clave **pública**: Responsable del **cifrado**. Es comunicada al **emisor** del mensaje.
- Clave **privada**: Responsable del **descifrado**. Solo debe conocerla el **receptor** del mensaje.

Dada la clave privada es posible calcular la clave pública, pero dada
la clave pública es imposible calcular la clave privada.

Este sistema es más costoso que los algoritmos simétricos,
pero permite la comunicación segura entre emisor y receptor
sin haberse puesto de acuerdo previamente en una clave secreta. 

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
La seguridad de este algoritmo radica en la dificultad de factorizar números grandes

## Ejecución



## Explicación


### Clases

Para visualizar el funcionamiento del algoritmo RSA, en este ejemplo dos clases
interactúan encriptando y desencriptando ficheros.

- `Main` contiene el método principal y se encarga de la interacción con el usuario.
- `KeyManager` genera las claves **pública** y **privada**, solo comparte la clave **pública**
  para que cualquiera pueda enviarle mensajes encriptados. En una situación real, sería el **receptor** del mensaje encriptado
- `EncryptedWriter` solo puede cifrar datos porque conoce únicamente la
  clave **pública**. En una situación real, sería el **emisor** del mensaje.


### Flujo

La aplicación sigue el siguiente flujo:

1. `KeyManager` genera la clave **pública** y **privada**
2. `KeyManager` escribe la clave **pública** en un fichero (`public_key.txt`)
3. `EncryptedWriter` recupera la clave **pública** del fichero
4. `EncryptedWriter` **cifra** los datos con la clave pública y los escribe en un nuevo fichero (`encrypted_text.txt`)
5. `KeyManager` recupera y **descifra** los datos del fichero `encrypted_text.txt`

## Código

### Main

```java

```

### Cifrado

```java

```

### Descifrado

```java

```

## Fuentes

- [Baeldung](https://www.baeldung.com/java-rsa)
- [Wikipedia: RSA](https://en.wikipedia.org/wiki/RSA_(cryptosystem))
- [Wikipedia: Criptografía asimétrica](https://es.wikipedia.org/wiki/Criptograf%C3%ADa_asim%C3%A9trica)
- [Github Gist](https://gist.github.com/Anass-ABEA/e2627b5df9abf23e006bcd5d390de205)
