<div align="center>
  <img src="https://img.shields.io/badge/python-3670A0?style=for-the-badge&logo=python&logoColor=ffdd54">
  <img src="https://img.shields.io/badge/node.js-6DA55F?style=for-the-badge&logo=node.js&logoColor=white">
  <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white">
  <img src="https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white">
  <img src="https://img.shields.io/badge/dart-%230175C2.svg?style=for-the-badge&logo=dart&logoColor=white">
</div>

<a href="https://doi.org/10.5281/zenodo.8170382"><img src="https://zenodo.org/badge/DOI/10.5281/zenodo.8170382.svg" alt="DOI"></a>

# <div align="center">Kaalka Encryption Algorithm</div>

<div align="center">
  <b style="font-size:1.5em;">Time is the Key 🔑</b>
  <br>
  <!-- <img src="https://user-images.githubusercontent.com/25122299/273370073-7e2e2e2e-7e2e-4e2e-8e2e-7e2e2e2e2e2e.png" width="120"/> -->
</div>

---

## 🌟 Overview
Kaalka is a robust, time-driven encryption protocol for cross-platform security. It leverages clock angles, trigonometric functions, and time-based transformations—making every encryption unique to its timestamp. Kaalka supports envelope, seal, replay protection, chunked file encryption, and a CLI tool, all implemented natively in Python, JavaScript, Java, Kotlin, and Dart.

---

## 🚀 Features
- **Time-First Protocol:** Envelope, seal, replay protection, time window
- **Envelope & Seal:** Canonical structure, time-based integrity MIC
- **Replay Protection:** In-memory ledger, auto-expiry
- **Chunked File Encryption:** Large file support, lossless roundtrip
- **CLI Tool:** Encrypt, decrypt, envelope, file operations
- **Cross-Platform:** Identical logic in Python, JS, Java, Kotlin, Dart
- **Flexible API:** System or user-defined timestamp
- **Production-Ready:** Fully tested, documented, and versioned (v5.0.0)

---

## 📦 Supported Languages
<div align="center">
  <b>Python · Node.js · Java · Kotlin · Dart</b>
</div>

---

## 💻 Quick Start

### Python
```python
from kaalka import Kaalka
kaalka = Kaalka()
encrypted = kaalka.encrypt("Hello")
decrypted = kaalka.decrypt(encrypted)
# Explicit timestamp
encrypted_exp = kaalka.encrypt("Hello", time_key="12:34:56")
decrypted_exp = kaalka.decrypt(encrypted_exp, time_key="12:34:56")
```

### Java
```java
Kaalka kaalka = new Kaalka();
String encrypted = kaalka.encrypt("Hello");
String decrypted = kaalka.decrypt(encrypted);
// Explicit timestamp
String encryptedExp = kaalka.encrypt("Hello", "12:34:56");
String decryptedExp = kaalka.decrypt(encryptedExp, "12:34:56");
```

### Kotlin
```kotlin
val kaalka = Kaalka()
val encrypted = kaalka.encrypt("Hello")
val decrypted = kaalka.decrypt(encrypted)
// Explicit timestamp
val encryptedExp = kaalka.encrypt("Hello", "12:34:56")
val decryptedExp = kaalka.decrypt(encryptedExp, "12:34:56")
```

### Dart
```dart
final kaalka = Kaalka();
final encrypted = kaalka.encrypt('Hello');
final decrypted = kaalka.decrypt(encrypted);
// Explicit timestamp
final encryptedExp = kaalka.encrypt('Hello', timeKey: '12:34:56');
final decryptedExp = kaalka.decrypt(encryptedExp, timeKey: '12:34:56');
```

### JavaScript
```js
const Kaalka = require('kaalka');
const kaalka = new Kaalka();
const encrypted = kaalka.encrypt("Hello");
const decrypted = kaalka.decrypt(encrypted);
// Explicit timestamp
const encryptedExp = kaalka.encrypt("Hello", "12:34:56");
const decryptedExp = kaalka.decrypt(encryptedExp, "12:34:56");
```

---

## 🛡️ Protocol & CLI Highlights
- **Envelope:** senderId, receiverId, timestamp, window, seq, ciphertext, seal
- **Seal:** Time-based integrity MIC, auto-expiry, replay protection
- **Chunked File:** Large file support, chunk index + timestamp
- **Explicit Timestamp Support:**
  - All APIs and CLI commands allow you to pass a custom timestamp for encryption and decryption.
  - This enables time-variant security and cross-platform compatibility.
- **CLI Tool:**
  - `encrypt --in <inputFile> --out <outputFile> --sender <id> --receiver <id> --timestamp <time>`
  - `decrypt --in <inputFile> --out <outputFile> --receiver <id> --timestamp <time>`
  - `envelope --text "message" --sender <id> --receiver <id> --timestamp <time>`

---

## 🧪 Testing & Reliability
- **Unit Tests:** Protocol, envelope, seal, replay, chunking, CLI
- **Cross-Platform:** Identical results across all supported languages
- **Production-Ready:** All tests pass, robust error handling

---

## 📚 Documentation & API
- **API Reference:**
  - `encryptEnvelope(plaintext, senderId, receiverId, timestamp?)`
  - `decryptEnvelope(envelope, receiverId, timestamp?)`
  - `encryptFileChunks(fileBytes, senderId, receiverId, timestamp?)`
  - `decryptFileChunks(chunks, receiverId, timestamp?)`
- **CLI Usage:** See above
- **Changelog:** See `CHANGELOG.md`

---

## 🔄 Cross-Platform Compatibility
- **Python:** [PyPI](https://pypi.org/project/kaalka/)
- **Node.js:** [npm](https://www.npmjs.com/package/kaalka)
- **Dart:** [pub.dev](https://pub.dev/packages/kaalka)
- **Java/Kotlin:** [GitHub Releases](https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm/releases)

---

## 🤝 Contributing
Contributions, bug reports, and feature requests are welcome.<br>
Submit issues or pull requests via [GitHub](https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm).

---

## 💡 Author & Credits
Developed and maintained by [Piyush Mishra](https://github.com/PIYUSH-MISHRA-00).<br>
Thanks to contributors across all supported language implementations.

<div align="center"> <b>Time is the Key.</b> </div>
