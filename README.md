
# Kaalka Package for Java

Robust, timestamp-based encryption for Java, compatible with Python, Dart, and JavaScript implementations. Uses angles and trigonometric functions for text, and integer arithmetic for file/media encryption, ensuring lossless, reversible results for all file types (images, binary, etc.).

## Features
- **Robust encryption** using timestamp-based keys (angles, trigonometric functions for text; integer arithmetic for files/media)
- **Cross-platform**: Compatible with Python, Dart, and Node.js Kaalka implementations
- **Flexible API**: Use system time, NTP, or custom timestamp for encryption/decryption
- **File/media support**: Encrypt/decrypt any file or media type (text, binary, images, etc.) with lossless, reversible results
- **Extension handling**: Encrypted files use `.kaalka`, decrypted files restore original extension
- **Packet support**: Example wrapper for secure message packets

## File Structure
```
Kaalka-Encryption-Algorithm/
├── .classpath
├── .git/
├── .gitignore
├── .gradle/
├── .project
├── build/
│   ├── libs/                # Compiled JARs
│   └── tmp/
├── build.gradle             # Gradle build config
├── effective-pom.xml
├── gradle/
├── gradlew
├── gradlew.bat
├── kaalka/
│   └── src/
│       ├── main/
│       │   └── java/
│       │       └── com/
│       │           └── kaalka/
│       │               ├── Kaalka.java
│       │               ├── KaalkaNTP.java
│       │               └── Packet.java
│       └── test/
│           └── java/
│               └── com/
│                   └── kaalka/
│                       ├── KaalkaFileDemo.java
│                       ├── KaalkaNTPTest.java
│                       ├── KaalkaTest.java
│                       ├── KaalkaTestExtended.java
│                       ├── KaalkaThoroughTest.java
│                       ├── PacketTest.java
│                       └── PerformanceTest.java
├── kaalka-2.0.0.jar
├── kaalka-3.0.0.jar
├── kaalka-lib.jar
├── pom.xml                 # Maven build config
├── README.md
├── settings.xml
└── workflows/
```

## Usage Example
```java
import com.kaalka.Kaalka;
import com.kaalka.KaalkaNTP;
import com.kaalka.Packet;

public class Main {
    public static void main(String[] args) throws Exception {
        // Text encryption/decryption
        Kaalka kaalka = new Kaalka();
        String originalMessage = "Hello, Kaalka!";
        String encryptedMessage = kaalka.encrypt(originalMessage, "10:20:30");
        String decryptedMessage = kaalka.decrypt(encryptedMessage, "10:20:30");
        System.out.println("Original Message: " + originalMessage);
        System.out.println("Encrypted Message: " + encryptedMessage);
        System.out.println("Decrypted Message: " + decryptedMessage);

        // File/media encryption/decryption
        String encryptedFile = kaalka.encryptFile("test_image.jpg", "10:20:30"); // Produces test_image.kaalka
        String decryptedFile = kaalka.decryptFile(encryptedFile, "10:20:30"); // Produces test_image.jpg
        System.out.println("Encrypted File: " + encryptedFile);
        System.out.println("Decrypted File: " + decryptedFile);

        // Example usage of KaalkaNTP
        KaalkaNTP kaalkaNTP = new KaalkaNTP();
        String encryptedNTPMessage = kaalkaNTP.encrypt(originalMessage, "10:20:30");
        String decryptedNTPMessage = kaalkaNTP.decrypt(encryptedNTPMessage, "10:20:30");
        System.out.println("\nUsing KaalkaNTP:");
        System.out.println("Encrypted NTP Message: " + encryptedNTPMessage);
        System.out.println("Decrypted NTP Message: " + decryptedNTPMessage);

        // Example usage of Packet
        Packet packet = new Packet("Payload", "12:34:56");
        packet.encrypt();
        String decryptedPacket = packet.decrypt();
        System.out.println("\nPacket decrypted: " + decryptedPacket);
    }
}
```
## Thorough Testing
Run all JUnit tests and the demo script for complete coverage:
- Text, file, binary, UTF-8, large files, extension handling, error cases
- Performance test for large files

## Build Instructions
To build the JAR for version 3.0:
```sh
./gradlew build
```
The JAR will be in `build/libs/`.

## API Reference

### Kaalka
- `Kaalka()` — Create instance (uses current system time)
- `String encrypt(String data, Object timeKey)` — Encrypt text
- `String decrypt(String encrypted, Object timeKey)` — Decrypt text
- `String encryptFile(String filePath, Object timeKey)` — Encrypt any file/media (extension handled)
- `String decryptFile(String filePath, Object timeKey)` — Decrypt any file/media (extension restored)

### KaalkaNTP
- `KaalkaNTP()` — Create instance (uses system time, can be extended for NTP)
- `String encrypt(String data, Object timeKey)`
- `String decrypt(String encrypted, Object timeKey)`

### Packet
- `Packet(String data, Object timeKey)`
- `void encrypt()`
- `String decrypt()`

## Timestamp Format
- Accepts `int` (seconds), `String` (`HH:MM:SS`, `MM:SS`, or `SS`)
- If omitted, uses current system time

- Compatible with [Python](https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm), [Dart](https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm), and [Node.js](https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm) Kaalka libraries
  - Java v4.0.0 uses integer arithmetic for file/media encryption, matching Python/JavaScript/Dart for robust, lossless results.
