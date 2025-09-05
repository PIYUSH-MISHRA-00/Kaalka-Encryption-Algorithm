
# Kaalka: Time-Based Encryption Library for Node.js

Kaalka is a robust, time-based encryption library for Node.js, supporting secure encryption and decryption of text, files, and large files using system time, NTP time, or a user-provided timestamp. It includes chunked file support, a CLI, protocol wrappers, and comprehensive tests. Kaalka is suitable for secure messaging, cryptographic experiments, and time-sensitive data protection.


## 🚀 Features
- **Time-based encryption:** Use system time, NTP time, or any timestamp (ISO or HH:MM:SS) as the encryption key.
- **Chunked file encryption:** Encrypt/decrypt large files in chunks with manifest and per-chunk key/beat.
- **Robust and unpredictable:** Combines trigonometric functions, clock hand angles, and character indices for strong, non-trivial encryption.
- **Easy integration:** Simple Node.js API for text, file, and chunked file encryption/decryption.
- **CLI support:** Command-line interface for file and chunked file operations.
- **NTP support:** Includes KaalkaNTP for network time protocol-based encryption.
- **Full test suite:** Jest tests for all core, protocol, file, and chunked file features.
## 📦 Installation

```sh
npm install kaalka
```

## � Usage

### Text Encryption/Decryption
```js
const Kaalka = require('./kaalka');
const kaalka = new Kaalka();
let encrypted = kaalka.encrypt("Hello, world!");
let decrypted = kaalka.decrypt(encrypted);
encrypted = kaalka.encrypt("Hello, world!", "14:35:22");
decrypted = kaalka.decrypt(encrypted, "14:35:22");
```

### File Encryption/Decryption
```js
let encryptedFile = await kaalka.encrypt("test_image.jpg"); // Produces test_image.kaalka
let decryptedFile = await kaalka.decrypt(encryptedFile); // Produces test_image.jpg
encryptedFile = await kaalka.encrypt("test_data.csv", "14:35:22");
decryptedFile = await kaalka.decrypt(encryptedFile, "14:35:22");
```

### Chunked File Encryption/Decryption
```js
const { encryptFile, decryptFile } = require('./lib/file_weave');
// Encrypt large file in chunks
await encryptFile('large.txt', resonance, '2025-09-05T12:00:00.000Z', 'manifest.json');
// Decrypt large file from chunks
await decryptFile('large_out.txt', resonance, '2025-09-05T12:00:00.000Z', 'manifest.json');
```

### CLI Usage
```sh
node cli/kaalka_time_cli.js encrypt <inputFile> <timestamp>
node cli/kaalka_time_cli.js decrypt <encryptedFile> <timestamp>
```

## 🧩 Protocol & NTP Support
- Time-first protocol wrappers for advanced use cases.
- NTP time synchronization via KaalkaNTP.
## 🧪 Testing
- All features are covered by Jest tests: core, protocol, file, chunked file, and edge cases.
- Run tests with:
```sh
npm test
```
## ✅ Deployment
- All features are implemented and tested.
- All tests pass.
- The codebase is robust and ready for npm deployment.

## �🔒 How the Algorithm Works
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


### Unified Text, Image, and Media Encryption/Decryption

```js
const Kaalka = require('./kaalka');
const kaalka = new Kaalka();

// Text encryption/decryption (system time)
let encrypted = kaalka.encrypt("Hello, world!");
let decrypted = kaalka.decrypt(encrypted);

// Text encryption/decryption (explicit time)
encrypted = kaalka.encrypt("Hello, world!", "14:35:22");
decrypted = kaalka.decrypt(encrypted, "14:35:22");

// File encryption/decryption (any file type, system time)
let encryptedFile = await kaalka.encrypt("test_image.jpg"); // Produces test_image.kaalka
let decryptedFile = await kaalka.decrypt(encryptedFile); // Produces test_image.jpg

// File encryption/decryption (explicit time)
encryptedFile = await kaalka.encrypt("test_data.csv", "14:35:22");
decryptedFile = await kaalka.decrypt(encryptedFile, "14:35:22");

// Folder workflow example
const fs = require('fs').promises;
const path = require('path');
const encPath = await kaalka.encrypt("test_image.jpg", "14:35:22");
await fs.mkdir('encrypted', { recursive: true });
await fs.mkdir('decrypted', { recursive: true });
await fs.rename(encPath, path.join('encrypted', 'test_image.kaalka'));
const decPath = await kaalka.decrypt(path.join('encrypted', 'test_image.kaalka'), "14:35:22");
await fs.rename(decPath, path.join('decrypted', 'test_image.jpg'));
```

> **Note:**
> - Encrypted files use only the `.kaalka` extension (e.g., `test_image.kaalka`, `video.kaalka`, `music.kaalka`).
> - Decrypted files restore the original name and extension (e.g., `test_image.jpg`, `video.mp4`, `music.mp3`).
> - The library supports all file types, including images, videos, music, text, CSV, JSON, XML, and more.
> - Encryption and decryption are lossless and robust for all binary formats.

## ⚠️ Security Notes
- If you do not provide a timestamp, the current system time is used for encryption and decryption. Encryption and decryption must occur at the same second for the result to be correct.
- For reproducibility and secure communication, always provide an explicit timestamp string (format: 'HH:MM:SS').
- The strength of encryption depends on the secrecy and unpredictability of the timestamp used.


## 📄 License
See the LICENSE file for details.

## 🤝 Contributing
Contributions, bug reports, and feature requests are welcome! Please open an issue or submit a pull request on GitHub.
