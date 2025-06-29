import 'package:test/test.dart';
import 'package:kaalka/kaalka.dart';

void main() {
  group('Kaalka Encryption', () {
    test('Encrypt/Decrypt with system time', () {
      final k = Kaalka();
      final msg = 'Hello, Dart!';
      final encrypted = k.encrypt(msg);
      final decrypted = k.decrypt(encrypted);
      expect(decrypted, equals(msg));
    });
    test('Encrypt/Decrypt with fixed timestamp', () {
      final k = Kaalka('10:20:30');
      final msg = 'Timestamp test';
      final encrypted = k.encrypt(msg, '10:20:30');
      final decrypted = k.decrypt(encrypted, '10:20:30');
      expect(decrypted, equals(msg));
    });
  });
}
