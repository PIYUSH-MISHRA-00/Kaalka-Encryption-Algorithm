import 'package:kaalka/kaalka.dart';
import 'package:kaalka/kaalka_ntp.dart';
import 'package:kaalka/packet.dart';

Future<void> main() async {
  // Basic Kaalka usage with current system time
  final kaalka = Kaalka();
  final message = 'Hello, Kaalka!';
  final encrypted = kaalka.encrypt(message);
  final decrypted = kaalka.decrypt(encrypted);
  print('Encrypted: $encrypted');
  print('Decrypted: $decrypted');

  // Kaalka with custom timestamp
  final customKaalka = Kaalka('10:15:30');
  final encryptedCustom = customKaalka.encrypt(message, '10:15:30');
  final decryptedCustom = customKaalka.decrypt(encryptedCustom, '10:15:30');
  print('Encrypted (custom): $encryptedCustom');
  print('Decrypted (custom): $decryptedCustom');

  // KaalkaNTP async usage
  final encryptedNtp = await KaalkaNTP.encryptWithNtp(message);
  final decryptedNtp = await KaalkaNTP.decryptWithNtp(encryptedNtp);
  print('Encrypted (NTP): $encryptedNtp');
  print('Decrypted (NTP): $decryptedNtp');

  // Packet example
  final packet = Packet(message, timeKey: '12:34:56');
  packet.encrypt();
  final decryptedPacket = packet.decrypt();
  print('Packet decrypted: $decryptedPacket');
}
