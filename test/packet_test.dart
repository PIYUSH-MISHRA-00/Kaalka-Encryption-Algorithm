import 'package:test/test.dart';
import 'package:Kaalka/kaalka_ntp.dart';
import 'package:Kaalka/packet.dart';

void main() {
  group('Packet Tests', () {
    test('Encrypt and Decrypt Data', () {
      var kaalkaNTP = KaalkaNTP(); // Create an instance of KaalkaNTP
      var packet = Packet('Hello, Kaalka!');

      packet.encrypt(kaalkaNTP); // Use KaalkaNTP instance
      expect(packet.encryptedData, isNotNull);

      packet.decrypt(kaalkaNTP); // Use KaalkaNTP instance
      expect(packet.data, equals('Hello, Kaalka!'));
    });

    test('Send and Receive Data', () async {
      // Run sender and receiver simulations
      Packet.sender();
      await Future.delayed(Duration(seconds: 2));
      Packet.receiver();
    });
  });
}
