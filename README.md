# Kaalka Encryption Algorithm (Kotlin)

Robust, timestamp-based encryption for Kotlin/JVM, compatible with Python, Dart, Java, and JavaScript implementations. Uses angles and trigonometric functions for text, and integer arithmetic for file/media encryption, ensuring lossless, reversible results for all file types (images, binary, etc.).

**Version:** 4.0.0

**Features:**
- Text encryption/decryption (timestamp-based, trigonometric logic)
- File/media encryption/decryption (lossless, integer arithmetic for robust support)
- Binary/media encryption/decryption
- Extension handling (preserves file extension during encryption/decryption)
- Error handling (returns success/failure for file operations)
- Packet demo (UDP send/receive with encryption)
- KaalkaNTP (NTP-based timestamp logic)
- Thorough test suite for all features, including images and binary files

## Project Structure

```
kaalka-library/
│
├─ src/
│   ├─ main/
│   │   └─ kotlin/
│   │       ├─ com/kaalka/
│   │       │   ├─ Kaalka.kt
│   │       │   ├─ KaalkaNTP.kt
│   │       │   ├─ Packet.kt
│   │       └─ Main.kt
│   │
│   └─ test/
│       └─ kotlin/
│           └─ com/kaalka/
│               └─ test.kt
│
├─ build.gradle.kts
├─ settings.gradle.kts
└─ README.md
```

## Usage

### Kaalka (local time-based)

```kotlin
val kaalka = Kaalka()
val message = "Hello, Kaalka!"
val timestamp = "10:20:30" // or null for current time
val encrypted = kaalka.encrypt(message, timestamp)
val decrypted = kaalka.decrypt(encrypted, timestamp)
```

### KaalkaNTP (NTP time-based)

```kotlin
val kaalkaNTP = KaalkaNTP()
val encrypted = kaalkaNTP.encrypt(message) // uses NTP time
val decrypted = kaalkaNTP.decrypt(encrypted) // uses NTP time
```

### File/Media Encryption & Decryption (Lossless)

```kotlin
val kaalka = Kaalka()
val imagePath = "test_image.jpg"
val encryptedFile = "test_image.kaalka"
val decryptedFile = "test_image_restored.jpg"
val timestamp = "12:34:56"
val encSuccess = kaalka.encryptFile(imagePath, encryptedFile, timestamp)
val decSuccess = kaalka.decryptFile(encryptedFile, decryptedFile, timestamp)
val origBytes = java.io.File(imagePath).readBytes()
val decBytes = java.io.File(decryptedFile).readBytes()
val lossless = origBytes.size == decBytes.size && origBytes.indices.all { origBytes[it] == decBytes[it] }
println("Lossless restoration: ${if (lossless) "PASS" else "FAIL"}")
```

### Packet Example

```kotlin
val packet = Packet("Hello, Kaalka!")
packet.sendAndReceiveData()
```

## Cross-Platform Compatibility

- Compatible with Python, Dart, Java, and JavaScript Kaalka implementations.
- Uses the same timestamp-based, angle/trigonometric encryption logic for text, and integer arithmetic for file/media for full interoperability and lossless results.

## Tests

Run all tests (including text, file, binary, extension, error handling, and UDP packet demo) with:

```
cd C:\Projects\Kaalka-Encryption-Algorithm
.\gradlew.bat test
```

Test output and reports will be available in `kaalka-library/build/reports/tests/`.

The tests include automated verification of encryption and decryption, including lossless testing of the provided `test_image.jpg`.

## Build JAR

```
cd C:\Projects\Kaalka-Encryption-Algorithm
.\gradlew.bat clean build jar
```

The JAR will be in `build/libs/Kaalka-Encryption-Algorithm-4.0.jar`.
