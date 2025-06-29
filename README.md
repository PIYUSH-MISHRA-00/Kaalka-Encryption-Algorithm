# Kaalka Package for Java

Robust, timestamp-based encryption for Java, compatible with Python, Dart, and JavaScript implementations. Uses angles and trigonometric functions derived from timestamps for strong, time-dependent encryption.

## Features
- **Robust encryption** using timestamp-based keys (angles, trigonometric functions)
- **Cross-platform**: Compatible with Python, Dart, and Node.js Kaalka implementations
- **Flexible API**: Use system time, NTP, or custom timestamp for encryption/decryption
- **Packet support**: Example wrapper for secure message packets

## File Structure
```
kaalka
src/
  ├── main/
  │   ├── java/
  │   │   └── com/
  │   │       └── kaalka/
  │   │           ├── Kaalka.java
  │   │           ├── KaalkaNTP.java
  │   │           └── Packet.java
  └── test/
      ├── java/
      │   └── com/
      │       └── kaalka/
      │           ├── KaalkaTest.java
      │           └── KaalkaNTPTest.java
      │
      │── build.gradle            
      ├── pom.xml
      └── README.md
```

## Usage Example
```java
import main.java.com.kaalka.Kaalka;
import main.java.com.kaalka.KaalkaNTP;
import main.java.com.kaalka.Packet;

public class Main {
    public static void main(String[] args) {
        // Example usage of Kaalka
        Kaalka kaalka = new Kaalka();
        String originalMessage = "Hello, Kaalka!";
        String encryptedMessage = kaalka.encrypt(originalMessage, "10:20:30");
        String decryptedMessage = kaalka.decrypt(encryptedMessage, "10:20:30");

        System.out.println("Original Message: " + originalMessage);
        System.out.println("Encrypted Message: " + encryptedMessage);
        System.out.println("Decrypted Message: " + decryptedMessage);

        // Example usage of KaalkaNTP
        KaalkaNTP kaalkaNTP = new KaalkaNTP();
        String encryptedNTPMessage = kaalkaNTP.encrypt(originalMessage, "10:20:30");
        String decryptedNTPMessage = kaalkaNTP.decrypt(encryptedNTPMessage, "10:20:30");

        System.out.println("\nUsing KaalkaNTP:");
        System.out.println("Original Message: " + originalMessage);
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

## API Reference

### Kaalka
- `Kaalka()` — Create instance (uses current system time)
- `String encrypt(String data, Object timeKey)` — Encrypt data (timeKey: `int` seconds or `String` `HH:MM:SS`, `MM:SS`, or `SS`)
- `String decrypt(String encrypted, Object timeKey)` — Decrypt data

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

## Compatibility
- Compatible with [Python](https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm), [Dart](https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm), and [Node.js](https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm) Kaalka libraries
