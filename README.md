# Kaalka Encryption Algorithm (Java)

Robust, time-first encryption for Java, matching Python, Dart, and JavaScript implementations. Pure Kaalka logic—no external crypto libraries.

## Features
- **Time-first protocol**: Envelope, seal, replay protection, time window
- **Envelope & Seal**: Canonical envelope structure, time-based integrity MIC
- **Replay Protection**: In-memory replay ledger
- **Chunked File Encryption**: File chunking and encryption/decryption
- **CLI Tool**: Envelope, text, and file operations
- **Public API**: Encrypt/decrypt envelope, files, chunks
- **Automated Tests**: Protocol, replay, expiry, chunking
- **Documentation & Metadata**: v5.0.0, Javadoc

## File Structure
```
src/main/java/kaalka/
  KaalkaEnvelope.java
  KaalkaProtocol.java
  KaalkaFile.java
  KaalkaUtils.java
  KaalkaCLI.java
src/test/java/kaalka/
  KaalkaProtocolTest.java
  KaalkaFileTest.java
README.md
CHANGELOG.md
build.gradle / pom.xml
```

## Usage Example
```java
import kaalka.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // Envelope encryption/decryption
        String sender = "Alice";
        String receiver = "Bob";
        String timestamp = KaalkaUtils.utcNow();
        String plaintext = "Hello, Bob!";
        KaalkaEnvelope env = KaalkaProtocol.encryptEnvelope(plaintext, sender, receiver, timestamp, 120, 1);
        String decrypted = new KaalkaProtocol().decryptEnvelope(env, receiver, timestamp);
        System.out.println("Decrypted: " + decrypted);

        // File chunk encryption/decryption
        byte[] fileBytes = "This is a test file.".getBytes();
        var chunks = KaalkaFile.encryptFileChunks(fileBytes, sender, receiver, timestamp);
        byte[] outBytes = KaalkaFile.decryptFileChunks(chunks, receiver, timestamp);
        System.out.println("File roundtrip: " + new String(outBytes));
    }
}
```

## CLI Usage
```
java -cp build/libs/kaalka.jar kaalka.KaalkaCLI encrypt --in input.txt --out out.bin --sender Alice --receiver Bob --timestamp 2025-09-05T12:00:00.000Z
java -cp build/libs/kaalka.jar kaalka.KaalkaCLI decrypt --in out.bin --out output.txt --receiver Bob --timestamp 2025-09-05T12:00:00.000Z
java -cp build/libs/kaalka.jar kaalka.KaalkaCLI envelope --text "Hello" --sender Alice --receiver Bob
```

## Testing
Run all JUnit tests for complete coverage:
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
- `KaalkaProtocol.encryptEnvelope(String, String, String, String, int, int)`
- `KaalkaProtocol.decryptEnvelope(KaalkaEnvelope, String, String)`
- `KaalkaFile.encryptFileChunks(byte[], String, String, String)`
- `KaalkaFile.decryptFileChunks(List<KaalkaEnvelope>, String, String)`
- CLI: `KaalkaCLI`

## Changelog
See `CHANGELOG.md` for details.
