# Kaalka Encryption Algorithm for Dart

Robust, timestamp-based encryption for Dart, compatible with Python and JavaScript implementations. Uses angles and trigonometric functions derived from timestamps for strong, time-dependent encryption.

## Features
- **Robust encryption** using timestamp-based keys (angles, trigonometric functions)
- **Cross-platform**: Compatible with Python and Node.js Kaalka implementations
- **Flexible API**: Use system time, NTP, or custom timestamp for encryption/decryption
- **Packet support**: Example wrapper for secure message packets

## Installation
Add to your `pubspec.yaml`:

```yaml
dependencies:
  kaalka:
    git:
      url: https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm.git
```

## Usage

### Basic Encryption/Decryption
```dart
import 'package:kaalka/kaalka.dart';

void main() {
  final kaalka = Kaalka(); // Uses current system time
  final message = 'Hello, Kaalka!';
  final encrypted = kaalka.encrypt(message); // Encrypt with current time
  final decrypted = kaalka.decrypt(encrypted); // Decrypt with same time
  print('Encrypted: $encrypted');
  print('Decrypted: $decrypted');
}
```

### Using a Custom Timestamp
```dart
import 'package:kaalka/kaalka.dart';

final kaalka = Kaalka('10:15:30'); // HH:MM:SS, MM:SS, or SS
final encrypted = kaalka.encrypt('Secret', '10:15:30');
final decrypted = kaalka.decrypt(encrypted, '10:15:30');
```

### Using KaalkaNTP (NTP time, async)
```dart
import 'package:kaalka/kaalka_ntp.dart';

void main() async {
  final encrypted = await KaalkaNTP.encryptWithNtp('Hello!');
  final decrypted = await KaalkaNTP.decryptWithNtp(encrypted);
  print('Encrypted: $encrypted');
  print('Decrypted: $decrypted');
}
```

### Packet Example
```dart
import 'package:kaalka/packet.dart';

final packet = Packet('Payload', timeKey: '12:34:56');
packet.encrypt();
final decrypted = packet.decrypt();
```

## API Reference

### Kaalka
- `Kaalka([dynamic timeKey])` — Create instance with optional timestamp
- `String encrypt(String data, [dynamic timeKey])` — Encrypt data
- `String decrypt(String encrypted, [dynamic timeKey])` — Decrypt data

### KaalkaNTP
- `static Future<String> encryptWithNtp(String data, {dynamic timeKey})`
- `static Future<String> decryptWithNtp(String data, {dynamic timeKey})`

### Packet
- `Packet(String data, {dynamic timeKey})`
- `void encrypt()`
- `String decrypt()`

## Timestamp Format
- Accepts `int` (seconds), `String` (`HH:MM:SS`, `MM:SS`, or `SS`)
- If omitted, uses current system time

## Compatibility
- Compatible with [Python](https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm) and [Node.js](https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm) Kaalka libraries
