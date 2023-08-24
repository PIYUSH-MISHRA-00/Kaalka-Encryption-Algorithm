import 'package:Kaalka/kaalka.dart';
import 'package:Kaalka/kaalka_ntp.dart';
import 'package:Kaalka/packet.dart';

Future<void> main() async {
  // Example usage of Kaalka
  final kaalka = Kaalka();
  final originalMessage = "Hello, Kaalka!";
  final timestamp = DateTime.now(); // Get the current timestamp
  final encryptedMessage = kaalka.encrypt(originalMessage, timestamp);
  final decryptedMessage = kaalka.decrypt(encryptedMessage, timestamp);

  print("Original Message: $originalMessage");
  print("Encrypted Message: $encryptedMessage");
  print("Decrypted Message: $decryptedMessage");

  // Example usage of KaalkaNTP
  final kaalkaNTP = KaalkaNTP();
  final encryptedNTPMessage = kaalkaNTP.encrypt(originalMessage);
  final decryptedNTPMessage = kaalkaNTP.decrypt(encryptedNTPMessage);

  print("\nUsing KaalkaNTP:");
  print("Original Message: $originalMessage");
  print("Encrypted NTP Message: $encryptedNTPMessage");
  print("Decrypted NTP Message: $decryptedNTPMessage");

  // Example usage of Packet sending and receiving
  Packet("Hello, Kaalka!");

  // Simulate sending and receiving of encrypted data using Packet
  Packet.sender();

  // Wait for sender to complete before receiving
  await Future.delayed(Duration(seconds: 2));
  
  Packet.receiver();
}
