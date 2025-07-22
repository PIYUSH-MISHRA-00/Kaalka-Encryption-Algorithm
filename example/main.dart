import 'dart:io';
import 'package:kaalka/kaalka.dart';
import 'package:kaalka/kaalka_ntp.dart';
import 'package:kaalka/packet.dart';

Future<void> main() async {
  // Basic Kaalka usage with current system time
  final kaalka = Kaalka();
  final message = 'Hello, Kaalka!';
  final encrypted = await kaalka.encrypt(message);
  final decrypted = await kaalka.decrypt(encrypted);
  print('Encrypted: $encrypted');
  print('Decrypted: $decrypted');

  // Kaalka with custom timestamp
  final customKaalka = Kaalka(time: DateTime(1970, 1, 1, 10, 15, 30));
  final encryptedCustom = await customKaalka.encrypt(message, timeKey: '10:15:30');
  final decryptedCustom = await customKaalka.decrypt(encryptedCustom, timeKey: '10:15:30');
  print('Encrypted (custom): $encryptedCustom');
  print('Decrypted (custom): $decryptedCustom');

  // KaalkaNTP async usage
  final encryptedNtp = await KaalkaNTP.encryptWithNtp(message);
  final decryptedNtp = await KaalkaNTP.decryptWithNtp(encryptedNtp);
  print('Encrypted (NTP): $encryptedNtp');
  print('Decrypted (NTP): $decryptedNtp');

  // Packet example
  final packet = Packet(message, timeKey: '12:34:56');
  await packet.encrypt();
  final decryptedPacket = await packet.decrypt();
  print('Packet decrypted: $decryptedPacket');

  // File/media encryption/decryption test for lossless restoration
  final imagePath = 'test_image.jpg';
  if (await File(imagePath).exists()) {
    print('Testing file encryption/decryption for $imagePath...');
    final encryptedFile = await kaalka.encrypt(imagePath);
    print('Encrypted file: $encryptedFile');
    final decryptedFile = await kaalka.decrypt(encryptedFile);
    print('Decrypted file: $decryptedFile');
    final origBytes = await File(imagePath).readAsBytes();
    final decBytes = await File(decryptedFile).readAsBytes();
    final lossless = origBytes.length == decBytes.length &&
        List.generate(origBytes.length, (i) => origBytes[i] == decBytes[i]).every((x) => x);
    print('Lossless restoration: ${lossless ? 'PASS' : 'FAIL'}');
  } else {
    print('test_image.jpg not found in the directory.');
  }
}
