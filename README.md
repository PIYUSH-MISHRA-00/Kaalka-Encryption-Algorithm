# Kaalka Encryption Algorithm (Kotlin)

Robust, time-first encryption for Kotlin, matching Python, Dart, JavaScript, and Java implementations. Pure Kaalka logic—no external crypto libraries.

## Features
- **Time-first protocol**: Envelope, seal, replay protection, time window
- **Envelope & Seal**: Canonical envelope data class, time-based integrity MIC
- **Replay Protection**: In-memory replay ledger
- **Chunked File Encryption**: File chunking and encryption/decryption
- **CLI Tool**: Envelope, text, and file operations
- **Public API**: Encrypt/decrypt envelope, files, chunks
- **Automated Tests**: Protocol, replay, expiry, chunking
- **Documentation & Metadata**: v5.0.0, KDoc

## File Structure
```
src/main/kotlin/kaalka/
  KaalkaEnvelope.kt
  KaalkaProtocol.kt
  KaalkaFile.kt
  KaalkaUtils.kt
  KaalkaCLI.kt
src/test/kotlin/kaalka/
  KaalkaProtocolTest.kt
  KaalkaFileTest.kt
README.md
CHANGELOG.md
build.gradle
```

## Usage Example
```kotlin
import kaalka.*

fun main() {
    // Envelope encryption/decryption
    val sender = "Alice"
    val receiver = "Bob"
    val timestamp = KaalkaUtils.utcNow()
    val plaintext = "Hello, Bob!"
    val env = KaalkaProtocol.encryptEnvelope(plaintext, sender, receiver, timestamp, 120, 1)
    val decrypted = KaalkaProtocol.decryptEnvelope(env, receiver, timestamp)
    println("Decrypted: $decrypted")

    // File chunk encryption/decryption
    val fileBytes = "This is a test file.".toByteArray()
    val chunks = KaalkaFile.encryptFileChunks(fileBytes, sender, receiver, timestamp)
    val outBytes = KaalkaFile.decryptFileChunks(chunks, receiver, timestamp)
    println("File roundtrip: ${outBytes.toString(Charsets.UTF_8)}")
}
```

## CLI Usage
```
kotlin -cp build/libs/kaalka.jar kaalka.KaalkaCLI encrypt --in input.txt --out out.bin --sender Alice --receiver Bob --timestamp 2025-09-06T12:00:00.000Z
kotlin -cp build/libs/kaalka.jar kaalka.KaalkaCLI decrypt --in out.bin --out output.txt --receiver Bob --timestamp 2025-09-06T12:00:00.000Z
kotlin -cp build/libs/kaalka.jar kaalka.KaalkaCLI envelope --text "Hello" --sender Alice --receiver Bob
```

## Testing
Run all unit tests for complete coverage:
```
./gradlew test
```

## Build Instructions
To build the JAR:
```
./gradlew build
```
JAR will be in `build/libs/`.

## API Reference
- `KaalkaProtocol.encryptEnvelope(plaintext, senderId, receiverId, timestamp?)`
- `KaalkaProtocol.decryptEnvelope(envelope, receiverId, timestamp?)`
- `KaalkaFile.encryptFileChunks(fileBytes, senderId, receiverId, timestamp?)`
- `KaalkaFile.decryptFileChunks(chunks, receiverId, timestamp?)`
- CLI: `KaalkaCLI`

## Changelog
See `CHANGELOG.md` for details.
