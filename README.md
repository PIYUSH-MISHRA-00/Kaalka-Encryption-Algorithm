# Kaalka Encryption Algorithm (Kotlin)

Robust, timestamp-based encryption for Kotlin/JVM, compatible with Python, Dart, and JavaScript implementations. Uses angles and trigonometric functions derived from timestamps for strong, time-dependent encryption.

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

### Packet Example

```kotlin
val packet = Packet("Hello, Kaalka!")
packet.sendAndReceiveData()
```

## Cross-Platform Compatibility

- Compatible with Python, Dart, and JavaScript Kaalka implementations.
- Uses the same timestamp-based, angle/trigonometric encryption logic.

## Tests

Run tests with:

```
kotlin src/test/kotlin/com/kaalka/test.kt
```

## Build JAR

```
cd kaalka-library
./gradlew clean build
```

The JAR will be in `build/libs/`.


