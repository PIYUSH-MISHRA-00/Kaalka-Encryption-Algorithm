import 'package:test/test.dart';
import 'package:kaalka/packet.dart';

void main() {
  group('Packet', () {
    test('Encrypt/Decrypt with Packet', () {
      final packet = Packet('Packet payload', timeKey: '03:21:09');
      packet.encrypt();
      final decrypted = packet.decrypt();
      expect(decrypted, equals('Packet payload'));
      expect(packet.encrypted, isNotNull);
    });
  });
}
