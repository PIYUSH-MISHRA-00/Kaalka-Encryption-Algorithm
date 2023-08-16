[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.8170382.svg)](https://doi.org/10.5281/zenodo.8170382)

# Python

* Based upon the Kaalka Encryption Algorithm

* To use into your python project first run the following into the terminal

```
pip install kaalka
```
# Exemplar Usage

```
from kaalka import Kaalka
from kaalkaNTP import KaalkaNTP
from packet import Packet

# Create an instance of Kaalka
kaalka = Kaalka()

# Create an instance of KaalkaNTP
kaalka_ntp = KaalkaNTP()

# Fetch the current timestamp using KaalkaNTP
timestamp = kaalka_ntp.get_current_time()

# Use the timestamp for encryption and decryption
message = "Hello, world!"
encrypted_message = kaalka.encrypt(message, timestamp)
decrypted_message = kaalka.decrypt(encrypted_message, timestamp)

print("Original Message:", message)
print("Encrypted Message:", encrypted_message)
print("Decrypted Message:", decrypted_message)

# Create a packet with the encrypted message
packet = Packet(encrypted_message)

# Simulate sending the packet over the network
Packet.send_data_over_network(packet.encrypted_data)

# Simulate receiving the packet over the network
received_data = Packet.receive_data_over_network()

# Create a packet with the received data
received_packet = Packet(received_data)

# Decrypt the received packet using Kaalka
received_decrypted_message = kaalka.decrypt(received_packet.encrypted_data, timestamp)

print("\nReceived and Decrypted Message:", received_decrypted_message)

```

# npm

* For using the node package for Kaalka just run the following into the terminal
```
npm i kaalka
```
# Exemplar Usage

* Using kaalka for Encryption and Decryption: 

```
const Kaalka = require('kaalka');

// Create an instance of Kaalka
const kaalkaInstance = new Kaalka();

const message = "Hello, world!";

// Encrypt the message
const encryptedMessage = kaalkaInstance.encrypt(message);

// Decrypt the encrypted message
const decryptedMessage = kaalkaInstance.decrypt(encryptedMessage);

console.log("Original Message:", message);
console.log("Encrypted Message:", encryptedMessage);
console.log("Decrypted Message:", decryptedMessage);

```
* Using kaalka_NTP and Packet for Encryption:

```
const KaalkaNTP = require('kaalka_NTP'); // Replace with the actual import
const Packet = require('packet'); // Replace with the actual import

// Create an instance of KaalkaNTP
const kaalkaNTPInstance = new KaalkaNTP();

// Create a Packet object with some data
const packet = new Packet("Hello, Kaalka!");

// Encrypt the packet using KaalkaNTP instance
packet.encrypt(kaalkaNTPInstance);

console.log("Original Data:", packet.originalData);
console.log("Encrypted Data:", packet.encryptedData);

```

# Java

* For using the jar into your java project

# Exemplar Usage

```
import com.kaalka.Kaalka;
import com.kaalka.KaalkaNTP;
import com.kaalka.Packet;
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
        long secondsSince1999 = ntpTime.getSeconds();
        System.out.println("\nCurrent NTP Time (seconds since 1999): " + secondsSince1999);

        // Example usage of Packet sending and receiving
        Packet packet = new Packet(originalMessage);

        // Encrypt the packet using KaalkaNTP
        packet.encrypt(kaalkaNTP);
        System.out.println("\nEncrypted Packet Data: " + packet.getEncryptedData());

        // Simulate sending the encrypted packet over the network
        Packet.sendDataOverNetwork(packet.getEncryptedData());

        // Simulate receiving the encrypted packet over the network
        String receivedData = Packet.receiveDataOverNetwork();

        // Create a packet with the received data
        Packet receivedPacket = new Packet(receivedData);

        // Decrypt the received packet using KaalkaNTP
        receivedPacket.decrypt(kaalkaNTP);
        System.out.println("Received and Decrypted Packet Data: " + receivedPacket.getData());
    }
}

```

