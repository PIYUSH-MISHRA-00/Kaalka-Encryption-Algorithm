import 'package:test/test.dart';
import 'package:kaalka/kaalka_ntp.dart';

void main() {
  group('KaalkaNTP', () {
    test('Encrypt/Decrypt with NTP (stub)', () async {
      final msg = 'NTP test';
      final encrypted = await KaalkaNTP.encryptWithNtp(msg);
      final decrypted = await KaalkaNTP.decryptWithNtp(encrypted);
      expect(decrypted, equals(msg));
    });
    test('Encrypt/Decrypt with custom time', () async {
      final msg = 'Custom NTP';
      final encrypted = await KaalkaNTP.encryptWithNtp(msg, timeKey: '01:02:03');
      final decrypted = await KaalkaNTP.decryptWithNtp(encrypted, timeKey: '01:02:03');
      expect(decrypted, equals(msg));
    });
  });
}
