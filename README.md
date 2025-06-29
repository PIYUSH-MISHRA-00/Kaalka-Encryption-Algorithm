[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.8170382.svg)](https://doi.org/10.5281/zenodo.8170382)

# Kaalka Encryption Algorithm

**Time is the Key 🔑**

A robust, timestamp-based encryption algorithm with cross-platform implementations in Python, Java, Kotlin, Dart, and JavaScript. Uses angles and trigonometric functions derived from timestamps for strong, time-dependent encryption.

---

## Supported Languages & Quick Start

### Python
```bash
pip install kaalka
```
```python
from kaalka import Kaalka
kaalka = Kaalka()
message = "Hello, world!"
encrypted = kaalka.encrypt(message)
decrypted = kaalka.decrypt(encrypted)
```

### Node.js
```bash
npm i kaalka
```
```js
const Kaalka = require('kaalka');
const kaalka = new Kaalka();
const encrypted = kaalka.encrypt('Hello, world!');
const decrypted = kaalka.decrypt(encrypted);
```

### Java
Add the JAR to your project:
```java
import com.kaalka.Kaalka;
Kaalka kaalka = new Kaalka();
String encrypted = kaalka.encrypt("Hello, Kaalka!");
String decrypted = kaalka.decrypt(encrypted);
```

### Kotlin
Add the JAR to your project:
```kotlin
dependencies {
    implementation(files("path/to/your/Kaalka-Encryption-Algorithm.jar"))
}
```
```kotlin
import com.kaalka.Kaalka
val kaalka = Kaalka()
val encrypted = kaalka.encrypt("Hello, Kaalka!")
val decrypted = kaalka.decrypt(encrypted)
```

### Dart
Add to your `pubspec.yaml`:
```yaml
dependencies:
  kaalka: ^1.0.0
```
```dart
import 'package:kaalka/kaalka.dart';
final kaalka = Kaalka();
final encrypted = kaalka.encrypt('Hello, world!');
final decrypted = kaalka.decrypt(encrypted);
```

---

## How Kaalka Works
- **Timestamp-based:** Encryption and decryption depend on the time (hour, minute, second) or a provided timestamp.
- **Angle & Trigonometry:** Uses angles between clock hands and trigonometric functions to generate encryption offsets.
- **Cross-platform:** Compatible logic across Python, Java, Kotlin, Dart, and JavaScript.

---

## Project Structure (Kotlin Example)
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

## Example Usage (Kotlin)
```kotlin
val kaalka = Kaalka()
val message = "Hello, Kaalka!"
val timestamp = "10:20:30" // or null for current time
val encrypted = kaalka.encrypt(message, timestamp)
val decrypted = kaalka.decrypt(encrypted, timestamp)
```

---

## Cross-Platform Compatibility
- Compatible with Python, Dart, JavaScript, Java, and Kotlin implementations.
- Uses the same timestamp-based, angle/trigonometric encryption logic.

---

## Build & Test (Kotlin)
```bash
cd kaalka-library
# If you have gradlew.bat:
./gradlew.bat clean build
# Or use IntelliJ IDEA to build and run tests
```
The JAR will be in `build/libs/`.
