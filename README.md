# Kaalka: Time-Based Encryption Library for Node.js

Kaalka is a robust, time-based encryption library for Node.js, inspired by the Kaalka Encryption Algorithm. It enables secure, non-trivial encryption and decryption of messages using either the current system time, NTP time, or a user-provided timestamp as the cryptographic key. Kaalka is suitable for secure messaging, cryptographic experiments, and time-sensitive data protection.

## 🚀 Features
- **Time-based encryption:** Use the current system time, NTP time, or any timestamp (HH:MM:SS) as the encryption key.
- **Robust and unpredictable:** Combines trigonometric functions, clock hand angles, and character indices for strong, non-trivial encryption.
- **Easy integration:** Simple Node.js API for encrypting and decrypting messages.
- **NTP support:** Includes KaalkaNTP for network time protocol-based encryption.

## 🔒 How the Algorithm Works
Kaalka transforms each character in your message using a key derived from the angles between the hour, minute, and second hands of a clock at a given time. The algorithm:
- Calculates angles for the provided or current time.
- Selects trigonometric functions based on the quadrant of each angle.
- Combines these values with the character's index and the time to generate a unique, non-reversible transformation for each character.
- Decryption reverses this process using the same timestamp.

> **Note:** Encryption and decryption must use the same timestamp (or occur at the same second if using system time).

## 📦 Installation

```sh
npm install kaalka
```

## 📝 Usage

### Basic Encryption/Decryption (System Time)

```js
const Kaalka = require('kaalka');
const kaalka = new Kaalka();
const message = "Hello, world!";
const encrypted = kaalka.encrypt(message); // Uses current system time as key
const decrypted = kaalka.decrypt(encrypted); // Uses current system time
console.log("Encrypted Message:", encrypted);
console.log("Decrypted Message:", decrypted);
```

### Encryption/Decryption with Explicit Timestamp

```js
const Kaalka = require('kaalka');
const kaalka = new Kaalka();
const message = "Hello, world!";
const timestamp = "14:35:22"; // Use a specific time as key
const encrypted = kaalka.encrypt(message, timestamp);
const decrypted = kaalka.decrypt(encrypted, timestamp);
console.log("Encrypted Message:", encrypted);
console.log("Decrypted Message:", decrypted);
```

### NTP-Based Encryption/Decryption (Async)

```js
const KaalkaNTP = require('kaalka/kaalkaNTP');
(async () => {
  const kaalkaNtp = new KaalkaNTP();
  const message = "Hello, Kaalka!";
  const encrypted = await kaalkaNtp.encrypt(message); // Uses NTP time
  const decrypted = await kaalkaNtp.decrypt(encrypted); // Uses NTP time
  console.log("Encrypted Message:", encrypted);
  console.log("Decrypted Message:", decrypted);
})();
```

### Packet Example (Async)

```js
const KaalkaNTP = require('kaalka/kaalkaNTP');
const Packet = require('kaalka/packet');
(async () => {
  const kaalkaInstance = new KaalkaNTP();
  const packet = new Packet("Hello, Kaalka!");
  await packet.encrypt(kaalkaInstance);
  console.log("Encrypted Data:", packet.encryptedData);
  await packet.decrypt(kaalkaInstance);
  console.log("Decrypted Data:", packet.data);
})();
```

## ⚠️ Security Notes
- If you do not provide a timestamp, the current system or NTP time is used for encryption and decryption. Encryption and decryption must occur at the same second for the result to be correct.
- For reproducibility and secure communication, always provide an explicit timestamp string (format: 'HH:MM:SS').
- The strength of encryption depends on the secrecy and unpredictability of the timestamp used.

## 📄 License
See the LICENSE file for details.

## 🤝 Contributing
Contributions, bug reports, and feature requests are welcome! Please open an issue or submit a pull request on GitHub.
