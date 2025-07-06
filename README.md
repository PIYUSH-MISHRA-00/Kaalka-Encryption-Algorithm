![Python](https://img.shields.io/badge/python-3670A0?style=for-the-badge&logo=python&logoColor=ffdd54) ![NodeJS](https://img.shields.io/badge/node.js-6DA55F?style=for-the-badge&logo=node.js&logoColor=white) ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E) ![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white) ![Dart](https://img.shields.io/badge/dart-%230175C2.svg?style=for-the-badge&logo=dart&logoColor=white)

[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.8170382.svg)](https://doi.org/10.5281/zenodo.8170382)

# Kaalka Encryption Algorithm

**Time is the Key 🔑**

Kaalka is a robust, timestamp-based encryption algorithm designed for cross-platform security. It leverages the mathematics of clock angles and trigonometric functions, making encryption and decryption dependent on time—either the current system time or a user-supplied timestamp. This approach ensures that the same message encrypted at different times yields different ciphertexts, providing strong, time-variant security.

---

## 🌐 Supported Languages

Kaalka is implemented and maintained in the following languages, ensuring seamless interoperability and consistent security across platforms:

- **Python**: For scripting, automation, and backend services.
- **Node.js (JavaScript)**: For web, serverless, and cross-platform apps.
- **Java**: For enterprise, Android, and backend systems.
- **Kotlin**: For modern JVM and Android development.
- **Dart**: For Flutter and cross-platform mobile/web apps.

---

## 🚀 Quick Start

### Python

Install:
```bash
pip install kaalka
```
Usage:
```python
from kaalka import Kaalka
kaalka = Kaalka()
message = "Hello, world!"
encrypted = kaalka.encrypt(message)
decrypted = kaalka.decrypt(encrypted)
print("Encrypted:", encrypted)
print("Decrypted:", decrypted)
```

---

### Node.js (JavaScript)

Install:
```bash
npm i kaalka
```
Usage:
```js
const Kaalka = require('kaalka');
const kaalka = new Kaalka();
const message = "Hello, world!";
const encrypted = kaalka.encrypt(message);
const decrypted = kaalka.decrypt(encrypted);
console.log("Encrypted:", encrypted);
console.log("Decrypted:", decrypted);
```

---

### Java

Add the JAR to your project (see [Build & Release](#build--release-java--kotlin)):
```java
import com.kaalka.Kaalka;
public class Example {
    public static void main(String[] args) {
        Kaalka kaalka = new Kaalka();
        String message = "Hello, Kaalka!";
        String encrypted = kaalka.encrypt(message);
        String decrypted = kaalka.decrypt(encrypted);
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);
    }
}
```

---

### Kotlin

Add the JAR to your project:
```kotlin
dependencies {
    implementation(files("path/to/Kaalka-Encryption-Algorithm.jar"))
}
```
Usage:
```kotlin
import com.kaalka.Kaalka
fun main() {
    val kaalka = Kaalka()
    val message = "Hello, Kaalka!"
    val encrypted = kaalka.encrypt(message)
    val decrypted = kaalka.decrypt(encrypted)
    println("Encrypted: $encrypted")
    println("Decrypted: $decrypted")
}
```

---

### Dart

Add to your `pubspec.yaml`:
```yaml
dependencies:
  kaalka: ^2.0.0
```
Usage:
```dart
import 'package:kaalka/kaalka.dart';

void main() {
  final kaalka = Kaalka();
  final message = 'Hello, world!';
  final encrypted = kaalka.encrypt(message);
  final decrypted = kaalka.decrypt(encrypted);
  print('Encrypted: $encrypted');
  print('Decrypted: $decrypted');
}
```

---

## 🔑 How Kaalka Works

- **Timestamp-based Security:**  
  Encryption and decryption depend on the time (hour, minute, second) or a provided timestamp, making brute-force attacks significantly harder.
- **Mathematical Obfuscation:**  
  Uses the angles between clock hands and trigonometric functions (sin, cos, tan, cot) to generate complex, time-dependent offsets for each character.
- **Flexible Timestamp Input:**  
  Accepts timestamps in `HH:MM:SS`, `MM:SS`, or `SS` formats, or uses the current system time by default.
- **Cross-Platform Consistency:**  
  The algorithm is mathematically identical across all supported languages, ensuring encrypted data can be decrypted on any platform.

---

## 📦 Project Structure (Kotlin Example)

```
kaalka-library/
├─ src/
│   ├─ main/
│   │   └─ kotlin/
│   │       └─ com/kaalka/
│   │           ├─ Kaalka.kt
│   │           ├─ KaalkaNTP.kt
│   │           ├─ Packet.kt
│   │           └─ Main.kt
│   └─ test/
│       └─ kotlin/
│           └─ com/kaalka/
│               └─ test.kt
├─ build.gradle.kts
├─ settings.gradle.kts
└─ README.md
```

---

## 🧪 Example Usage (Kotlin)

```kotlin
val kaalka = Kaalka()
val message = "Hello, Kaalka!"
val timestamp = "10:20:30" // or null for current time
val encrypted = kaalka.encrypt(message, timestamp)
val decrypted = kaalka.decrypt(encrypted, timestamp)
```

---

## 🔄 Cross-Platform Compatibility

- **Python:** [PyPI](https://pypi.org/project/kaalka/)
- **Node.js:** [npm](https://www.npmjs.com/package/kaalka)
- **Dart:** [pub.dev](https://pub.dev/packages/kaalka)
- **Java/Kotlin:** [GitHub Releases](https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm/releases)

All implementations use the same timestamp-based, angle/trigonometric encryption logic, ensuring you can encrypt in one language and decrypt in another.

---

## 🛠️ Build & Release (Java / Kotlin)

**Build the JAR:**
```bash
cd kaalka-library
# If you have gradlew.bat:
./gradlew.bat clean build
# Or use IntelliJ IDEA to build and run tests
```
The JAR will be in `build/libs/`.

**Testing:**
- Run tests using `./gradlew.bat test` or from your IDE.

---

## 📖 License

Kaalka is open-source under the MIT License. See [LICENSE](LICENSE) for details.

---

## 🤝 Contributing

Contributions, bug reports, and feature requests are welcome!  
Please open an issue or submit a pull request on [GitHub](https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm).

---

## 💡 Author & Credits

Developed and maintained by [Piyush Mishra](https://github.com/PIYUSH-MISHRA-00).  
Special thanks for all across Python, Java, Kotlin, Dart, and JavaScript implementations.

---

**Time is the Key.**
