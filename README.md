<div align="center>
  <img src="https://img.shields.io/badge/python-3670A0?style=for-the-badge&logo=python&logoColor=ffdd54">
  <img src="https://img.shields.io/badge/node.js-6DA55F?style=for-the-badge&logo=node.js&logoColor=white">
  <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white">
  <img src="https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E">
  <img src="https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white">
  <img src="https://img.shields.io/badge/dart-%230175C2.svg?style=for-the-badge&logo=dart&logoColor=white">
</div>

<a href="https://doi.org/10.5281/zenodo.8170382"><img src="https://zenodo.org/badge/DOI/10.5281/zenodo.8170382.svg" alt="DOI"></a>


# Kaalka Encryption Algorithm

<div align="center">
  <b>Time is the Key 🔑</b>
</div>

Kaalka is a robust, timestamp-based encryption algorithm for cross-platform security. It leverages clock angles and trigonometric functions, making encryption and decryption dependent on time—either the current system time or a user-supplied timestamp. This ensures that the same message encrypted at different times yields different ciphertexts, providing strong, time-variant security.

---

## 🌐 Supported Languages

Kaalka is implemented and maintained in:

- **Python**
- **Node.js (JavaScript)**
- **Java**
- **Kotlin**
- **Dart**

---

## 🚀 Quick Start

### Python
```python
from kaalka import Kaalka
kaalka = Kaalka()

# Text
encrypted = kaalka.encrypt("Hello")                      # system time
decrypted = kaalka.decrypt(encrypted)
encrypted_exp = kaalka.encrypt("Hello", time_key="12:34:56")
decrypted_exp = kaalka.decrypt(encrypted_exp, time_key="12:34:56")

# File / Media

# File / Media
# NOTE: You do NOT need to specify the original file extension when encrypting or decrypting.
# Kaalka automatically stores the original extension inside the encrypted file and restores it on decryption.
kaalka.encrypt_file("input.jpg", "output.kaalka")              # system time
kaalka.decrypt_file("output.kaalka")                           # restores original extension
kaalka.encrypt_file("input.jpg", "output.kaalka", time_key="12:34:56")
kaalka.decrypt_file("output.kaalka", time_key="12:34:56")
```

### Node.js (JavaScript)
```js
const Kaalka = require('kaalka');
const kaalka = new Kaalka();

// Text
const encrypted = kaalka.encrypt("Hello");               // system time
const decrypted = kaalka.decrypt(encrypted);
const encryptedExp = kaalka.encrypt("Hello", "12:34:56");
const decryptedExp = kaalka.decrypt(encryptedExp, "12:34:56");

// File / Media

// File / Media
// NOTE: You do NOT need to specify the original file extension when encrypting or decrypting.
// Kaalka automatically stores the original extension inside the encrypted file and restores it on decryption.
kaalka.encryptFile("input.jpg", "output.kaalka");              // system time
kaalka.decryptFile("output.kaalka");                           // restores original extension
kaalka.encryptFile("input.jpg", "output.kaalka", "12:34:56");
kaalka.decryptFile("output.kaalka", "12:34:56");
```

### Java
```java
Kaalka kaalka = new Kaalka();

// Text
String encrypted = kaalka.encrypt("Hello");              // system time
String decrypted = kaalka.decrypt(encrypted);
String encryptedExp = kaalka.encrypt("Hello", "12:34:56");
String decryptedExp = kaalka.decrypt(encryptedExp, "12:34:56");

// File / Media

// File / Media
// NOTE: You do NOT need to specify the original file extension when encrypting or decrypting.
// Kaalka automatically stores the original extension inside the encrypted file and restores it on decryption.
kaalka.encryptFile("input.jpg", "output.kaalka");              // system time
kaalka.decryptFile("output.kaalka");                           // restores original extension
kaalka.encryptFile("input.jpg", "output.kaalka", "12:34:56");
kaalka.decryptFile("output.kaalka", "12:34:56");
```

### Kotlin
```kotlin
val kaalka = Kaalka()

// Text
val encrypted = kaalka.encrypt("Hello")                  // system time
val decrypted = kaalka.decrypt(encrypted)
val encryptedExp = kaalka.encrypt("Hello", "12:34:56")
val decryptedExp = kaalka.decrypt(encryptedExp, "12:34:56")

// File / Media
kaalka.encryptFile("input.jpg", "enc.bin")               // system time
kaalka.decryptFile("enc.bin", "output.jpg")
kaalka.encryptFile("input.jpg", "enc.bin", "12:34:56")
kaalka.decryptFile("enc.bin", "output.jpg", "12:34:56")
```

### Dart
```dart
final kaalka = Kaalka();

// Text
final encrypted = kaalka.encrypt('Hello');               // system time
final decrypted = kaalka.decrypt(encrypted);
final encryptedExp = kaalka.encrypt('Hello', timeKey: '12:34:56');
final decryptedExp = kaalka.decrypt(encryptedExp, timeKey: '12:34:56');

// File / Media

// File / Media
// NOTE: You do NOT need to specify the original file extension when encrypting or decrypting.
// Kaalka automatically stores the original extension inside the encrypted file and restores it on decryption.
kaalka.encryptFile('input.jpg', 'output.kaalka');              // system time
kaalka.decryptFile('output.kaalka');                           // restores original extension
kaalka.encryptFile('input.jpg', 'output.kaalka', timeKey: '12:34:56');
kaalka.decryptFile('output.kaalka', timeKey: '12:34:56');
```

---

## 🔑 How Kaalka Works

- <b>Timestamp-based Security:</b> Encryption and decryption rely on the timestamp (hour, minute, second).
- <b>Mathematical Obfuscation:</b> Uses clock hand angles and trigonometric operations (sin, cos, tan, cot) to compute dynamic character-level transformations.
- <b>Flexible Time Input:</b> Supports time in HH:MM:SS, MM:SS, SS, or defaults to system time.
- <b>Cross-Platform Identicality:</b> Works seamlessly across all supported platforms using the same algorithmic logic.

---

## 🔄 Cross-Platform Compatibility

- <b>Python:</b> <a href="https://pypi.org/project/kaalka/">PyPI</a>
- <b>Node.js:</b> <a href="https://www.npmjs.com/package/kaalka">npm</a>
- <b>Dart:</b> <a href="https://pub.dev/packages/kaalka">pub.dev</a>
- <b>Java/Kotlin:</b> <a href="https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm/releases">GitHub Releases</a>

---

## 🤝 Contributing

Contributions, bug reports, and feature requests are welcome.<br>
Submit issues or pull requests via <a href="https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm">GitHub</a>.

---

## 💡 Author & Credits

Developed and maintained by <a href="https://github.com/PIYUSH-MISHRA-00">Piyush Mishra</a>.<br>
Thanks to contributors across all supported language implementations.

<div align="center"> <b>Time is the Key.</b> </div>
