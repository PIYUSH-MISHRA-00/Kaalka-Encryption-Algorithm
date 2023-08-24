import 'dart:async';
import 'dart:io';
import 'kaalka_ntp.dart';

class Packet {
  String data;
  String? encryptedData;

  Packet(this.data) {
    encryptedData = null;
  }

  void encrypt(KaalkaNTP kaalka) {
    if (encryptedData == null) {
      String encryptedMessage = kaalka.encrypt(data);
      encryptedData = encryptedMessage;
    }
  }

  void decrypt(KaalkaNTP kaalka) {
    if (encryptedData != null) {
      String decryptedMessage = kaalka.decrypt(encryptedData!);
      data = decryptedMessage;
      encryptedData = null;
    }
  }

  static void sender() {
    // Simulate sender preparing data
    String message = "Hello, Kaalka!";
    print("Original Message: $message");

    // Create a packet with the message
    var packet = Packet(message);

    // Encrypt the packet using Kaalka algorithm
    var kaalka = KaalkaNTP();
    packet.encrypt(kaalka);
    print("Encrypted Data: ${packet.encryptedData}");

    // Simulate sending the encrypted data over the network
    send_data_over_network(packet.encryptedData!);
  }

  static void receiver() {
    // Simulate receiving the encrypted data over the network
    receive_data_over_network().then((receivedData) {
      // Create a packet with the received data
      var packet = Packet(receivedData);

      // Decrypt the packet using Kaalka algorithm
      var kaalka = KaalkaNTP();
      packet.decrypt(kaalka);
      print("Decrypted Message: ${packet.data}");
    });
  }

  static void send_data_over_network(String data) {
    // Simulate sending data over the network (e.g., using sockets)
    var address = InternetAddress('127.0.0.1');
    var port = 12345;
    RawDatagramSocket.bind(address, port).then((socket) {
      socket.send(data.codeUnits, address, port);
      socket.close();
    });
  }

static Future<String> receive_data_over_network() async {
  var address = InternetAddress('127.0.0.1');
  var port = 12345;
  var socket = await RawDatagramSocket.bind(address, port);

  Completer<String> completer = Completer<String>();

  // Listen for incoming data
  socket.listen((event) {
    if (event == RawSocketEvent.read) {
      Datagram datagram = socket.receive()!;
      String receivedData = String.fromCharCodes(datagram.data).trim();
      completer.complete(receivedData);
    }
  });

  return completer.future;
}
}