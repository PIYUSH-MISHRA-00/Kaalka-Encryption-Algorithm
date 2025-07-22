
# Kaalka Encryption Algorithm for Dart

Robust, timestamp-based encryption for Dart, compatible with Python and JavaScript implementations. Uses angles and trigonometric functions derived from timestamps for strong, time-dependent encryption.

## Features
- **Robust encryption** using timestamp-based keys (angles, trigonometric functions)
- **Cross-platform**: Compatible with Python and Node.js Kaalka implementations
- **Flexible API**: Use system time, NTP, or custom timestamp for encryption/decryption
- **File/media support**: Encrypt/decrypt any file or media type (text, binary, images, etc.)
- **Extension handling**: Encrypted files use `.kaalka`, decrypted files restore original extension
- **Packet support**: Example wrapper for secure message packets

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

## Notes
- Encrypted files use only the `.kaalka` extension (e.g., `photo.kaalka`)
- Decrypted files restore the original name and extension (e.g., `photo.jpg`)
- Encryption and decryption must use the same timestamp
- Supports any file/media type (text, binary, images, etc.)

## License
See LICENSE file for details.

## Contributing
Contributions, bug reports, and feature requests are welcome! Please open an issue or submit a pull request on GitHub.
- Compatible with [Python](https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm) and [Node.js](https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm) Kaalka libraries
