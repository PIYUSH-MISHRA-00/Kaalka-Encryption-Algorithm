# Kaalka Encryption Algorithm (Kotlin)

Robust, timestamp-based encryption for Kotlin/JVM, compatible with Python, Dart, and JavaScript implementations. Uses angles and trigonometric functions derived from timestamps for strong, time-dependent encryption.

**Version:** 3.0

**Features:**
- Text encryption/decryption (timestamp-based, trigonometric logic)
- File encryption/decryption (text and binary, with extension handling)
- Binary/media encryption/decryption
- Extension handling (preserves file extension during encryption/decryption)
- Error handling (returns success/failure for file operations)
- Packet demo (UDP send/receive with encryption)
- KaalkaNTP (NTP-based timestamp logic)
- Thorough test suite for all features

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

### File/Media Encryption & Decryption

```kotlin
val kaalka = Kaalka()
val inputFile = "myphoto.jpg" // or any file/media
val encryptedFile = "myphoto_encrypted.bin"
val decryptedFile = "myphoto_decrypted.jpg"
val timestamp = "12:34:56" // or null for current time

// Encrypt file/media
val encSuccess = kaalka.encryptFile(inputFile, encryptedFile, timestamp)

// Decrypt file/media
val decSuccess = kaalka.decryptFile(encryptedFile, decryptedFile, timestamp)

println("Encrypt Success: $encSuccess, Decrypt Success: $decSuccess")
```
### Packet Example

```kotlin
val packet = Packet("Hello, Kaalka!")
packet.sendAndReceiveData()
```

## Cross-Platform Compatibility

- Compatible with Python, Dart, and JavaScript Kaalka implementations.
- Uses the same timestamp-based, angle/trigonometric encryption logic for full interoperability.

## Tests

Run all tests (including text, file, binary, extension, error handling, and UDP packet demo) with:

```
cd C:\Projects\Kaalka-Encryption-Algorithm
.\gradlew.bat test
```

Test output and reports will be available in `kaalka-library/build/reports/tests/`.

## Build JAR

```
cd C:\Projects\Kaalka-Encryption-Algorithm
.\gradlew.bat clean build jar
```

The JAR will be in `build/libs/Kaalka-Encryption-Algorithm-3.0.jar`.


