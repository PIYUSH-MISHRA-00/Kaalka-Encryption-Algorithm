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


### Unified Text and File Encryption/Decryption

```js
const Kaalka = require('./kaalka');
const kaalka = new Kaalka();

// Text encryption/decryption (system time)
let encrypted = kaalka.encrypt("Hello, world!");
let decrypted = kaalka.decrypt(encrypted);

// Text encryption/decryption (explicit time)
encrypted = kaalka.encrypt("Hello, world!", "14:35:22");
decrypted = kaalka.decrypt(encrypted, "14:35:22");

// File encryption/decryption (system time)
let encryptedFile = await kaalka.encrypt("data.txt"); // Produces data.kaalka
let decryptedFile = await kaalka.decrypt(encryptedFile); // Produces data.txt

// File encryption/decryption (explicit time)
encryptedFile = await kaalka.encrypt("data.txt", "14:35:22");
decryptedFile = await kaalka.decrypt(encryptedFile, "14:35:22");
```

> **Note:**
> - Encrypted files use only the `.kaalka` extension (e.g., `data.kaalka`, not `data.txt.kaalka`).
> - Decrypted files restore the original name and extension (e.g., `data.txt`).

## ⚠️ Security Notes
- If you do not provide a timestamp, the current system time is used for encryption and decryption. Encryption and decryption must occur at the same second for the result to be correct.
- For reproducibility and secure communication, always provide an explicit timestamp string (format: 'HH:MM:SS').
- The strength of encryption depends on the secrecy and unpredictability of the timestamp used.

## 📄 License
See the LICENSE file for details.

## 🤝 Contributing
Contributions, bug reports, and feature requests are welcome! Please open an issue or submit a pull request on GitHub.
