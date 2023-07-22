# Kaalka Package

* Based upon the Kaalka Encryption Algorithm

* File Structure

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
# Exemplar Usage

```
import com.kaalka.Kaalka;
import com.kaalka.KaalkaNTP;
import org.apache.commons.net.ntp.TimeStamp;

public class Main {
    public static void main(String[] args) {
        // Example usage of Kaalka
        Kaalka kaalka = new Kaalka();
        String originalMessage = "Hello, Kaalka!";
        String encryptedMessage = kaalka.encrypt(originalMessage);
        String decryptedMessage = kaalka.decrypt(encryptedMessage);

        System.out.println("Original Message: " + originalMessage);
        System.out.println("Encrypted Message: " + encryptedMessage);
        System.out.println("Decrypted Message: " + decryptedMessage);

        // Example usage of KaalkaNTP
        KaalkaNTP kaalkaNTP = new KaalkaNTP();
        String encryptedNTPMessage = kaalkaNTP.encrypt(originalMessage);
        String decryptedNTPMessage = kaalkaNTP.decrypt(encryptedNTPMessage);

        System.out.println("\nUsing KaalkaNTP:");
        System.out.println("Original Message: " + originalMessage);
        System.out.println("Encrypted NTP Message: " + encryptedNTPMessage);
        System.out.println("Decrypted NTP Message: " + decryptedNTPMessage);

        // Example usage of Apache Commons Net
        TimeStamp ntpTime = TimeStamp.getCurrentTime();
        long secondsSince1900 = ntpTime.getSeconds();
        System.out.println("\nCurrent NTP Time (seconds since 1900): " + secondsSince1900);
    }
}


```
