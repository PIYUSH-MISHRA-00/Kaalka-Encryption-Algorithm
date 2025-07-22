import 'package:test/test.dart';
import 'package:kaalka/packet.dart';

void main() {
  group('Packet', () {
    test('Encrypt/Decrypt with Packet', () async {
      final packet = Packet('Packet payload', timeKey: '03:21:09');
      await packet.encrypt();
      final decrypted = await packet.decrypt();
      expect(decrypted, equals('Packet payload'));
      expect(packet.encrypted, isNotNull);
    });
  });
}
