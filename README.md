


# Kaalka Encryption Algorithm for Dart (v5.0.0)

Application-ready time protocol: envelope, seal, replay protection, file chunking, public API. Pure Kaalka time-based logic, no external crypto. Cross-language compatible with Python and JavaScript.


## Features
- **Time-based protocol**: Envelope, seal, replay protection, time window, sequence, sender/receiver IDs
- **Robust encryption**: Timestamp-based keys, Kaalka drum logic, no external crypto
- **File/media support**: Chunked file encryption/decryption for large files
- **Flexible API**: Use system UTC or custom timestamp for all operations
- **Cross-platform**: Compatible with Python and Node.js Kaalka implementations
## Public API

```dart
// Envelope encryption
Map<String, dynamic> KaalkaProtocol.encryptEnvelope(String plaintext, String senderId, String receiverId, {String? timestamp});

// Envelope decryption
String KaalkaProtocol().decryptEnvelope(Map<String, dynamic> envelope, String receiverId, {String? timestamp});

// File chunk encryption
List<Map<String, dynamic>> KaalkaFile.encryptFileChunks(Uint8List fileBytes, String senderId, String receiverId, {String? timestamp});

// File chunk decryption
Uint8List KaalkaFile.decryptFileChunks(List<Map<String, dynamic>> encryptedChunks, String receiverId, {String? timestamp});
```
## Protocol Details

- **Envelope**: Canonical format with senderId, receiverId, timestamp, window, seq, ciphertext, seal
- **Seal**: Time-based integrity check (MIC) using Kaalka logic
- **Replay protection**: Sequence numbers tracked per sender/receiver
- **Time window**: Messages auto-expire outside allowed window
- **Chunking**: Large files split and encrypted per chunk with chunk index + timestamp
## Example Usage

```dart
import 'package:kaalka/kaalka_protocol.dart';
import 'package:kaalka/kaalka_file.dart';
import 'dart:typed_data';

void main() {
  // Envelope roundtrip
  final envelope = KaalkaProtocol.encryptEnvelope('hello', 'sender', 'receiver');
  final pt = KaalkaProtocol().decryptEnvelope(envelope, 'receiver');

  // File chunk roundtrip
  final fileBytes = Uint8List.fromList(List.generate(2 * 1024 * 1024, (i) => i % 256));
  final chunks = KaalkaFile.encryptFileChunks(fileBytes, 'sender', 'receiver');
  final outBytes = KaalkaFile.decryptFileChunks(chunks, 'receiver');
}
```
## Testing
- Unit tests for envelope, replay, file chunking, and time logic in `test/kaalka_protocol_test.dart`
## Changelog
- See CHANGELOG.md for v5.0.0: Application-ready protocol, envelope, seal, replay, chunking, public API

## Installation
Add to your `pubspec.yaml`:

```yaml
dependencies:
  kaalka:
    git:
      url: https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm.git
      path: lib
```

## Usage

### Text Encryption/Decryption
```dart
import 'package:kaalka/kaalka.dart';

void main() async {
  final kaalka = Kaalka(); // Uses current system time
  final message = 'Hello, Kaalka!';
  final encrypted = await kaalka.encrypt(message); // Encrypt with current time
  final decrypted = await kaalka.decrypt(encrypted); // Decrypt with same time

  // Explicit time
  final encrypted2 = await kaalka.encrypt(message, timeKey: '14:35:22');
  final decrypted2 = await kaalka.decrypt(encrypted2, timeKey: '14:35:22');
}
```

### File/Media Encryption/Decryption
```dart
import 'package:kaalka/kaalka.dart';

void main() async {
  final kaalka = Kaalka();
  // Encrypt a file (any type)
  final encryptedFile = await kaalka.encrypt('photo.jpg'); // Produces photo.kaalka
  // Decrypt the file
  final decryptedFile = await kaalka.decrypt(encryptedFile); // Produces photo.jpg
}
```

### NTP Time Support
```dart
import 'package:kaalka/kaalka_ntp.dart';

void main() async {
  final encrypted = await KaalkaNTP.encryptWithNtp('NTP message');
  final decrypted = await KaalkaNTP.decryptWithNtp(encrypted);
}
```

### Packet Wrapper
```dart
import 'package:kaalka/packet.dart';

void main() {
  final packet = Packet('Packet payload', timeKey: '03:21:09');
  packet.encrypt();
  final decrypted = packet.decrypt();
}
```

- Encrypted files use only the `.kaalka` extension (e.g., `photo.kaalka`)
- Decrypted files restore the original name and extension (e.g., `photo.jpg`)
- Encryption and decryption must use the same timestamp
- Supports any file/media type (text, binary, images, etc.)
- Lossless, reversible encryption for all file/media types (images, video, music, text, CSV, JSON, XML, etc.)

## License
See LICENSE file for details.

## Contributing
Contributions, bug reports, and feature requests are welcome! Please open an issue or submit a pull request on GitHub.
- Compatible with [Python](https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm) and [Node.js](https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm) Kaalka libraries
  - Dart v4.0.0 uses integer arithmetic for file/media encryption, matching Python/JavaScript for robust, lossless results.
