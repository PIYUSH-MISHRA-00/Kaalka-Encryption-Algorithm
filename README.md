# Kaalka Package - Python

* Based upon the Kaalka Encryption Algorithm

* To use into your python project first run the following into the terminal

```
pip install kaalka
```
# Exemplar Usage

```
from kaalka import Kaalka
from kaalkaNTP import KaalkaNTP

# Create an instance of Kaalka
kaalka = Kaalka()
message = "Hello, world!"
encrypted_message = kaalka.encrypt(message)
decrypted_message = kaalka.decrypt(encrypted_message)

print("Encrypted Message:", encrypted_message)
print("Decrypted Message:", decrypted_message)

# Create an instance of KaalkaNTP
kaalka_ntp = KaalkaNTP()
# Now you can use the methods of KaalkaNTP as well

```

# npm

* For using the node package for Kaalka just run the following into the terminal
```
npm i kaalka
```
# Exemplar Usage

```
const KaalkaNTP = require('your-library-name/kaalkaNTP');
const Packet = require('your-library-name/packet');

const kaalkaInstance = new KaalkaNTP();
const packet = new Packet("Hello, Kaalka!");
packet.encrypt(kaalkaInstance);
console.log("Encrypted Data:", packet.encryptedData);

```

# Java

* For using the jar into your java project

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

