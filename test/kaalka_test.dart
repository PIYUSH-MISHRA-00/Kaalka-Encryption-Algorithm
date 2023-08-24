import 'package:test/test.dart';
import 'package:Kaalka/kaalka.dart';

void main() {
  group('Kaalka Tests', () {
    test('Encrypt and Decrypt', () {
      var kaalka = Kaalka();

      var originalMessage = 'Hello, Kaalka!';
      var timestamp = kaalka.getCurrentTime();
      var encryptedMessage = kaalka.encrypt(originalMessage, timestamp);
      var decryptedMessage = kaalka.decrypt(encryptedMessage, timestamp);

      expect(decryptedMessage, equals(originalMessage));
    });
  });
}
