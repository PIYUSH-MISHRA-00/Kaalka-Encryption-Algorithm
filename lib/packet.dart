import 'dart:async';
import 'dart:io';
import 'kaalka.dart';

/// Packet: Example wrapper for encrypting/decrypting packets with Kaalka.
class Packet {
  String data;
  String? encrypted;
  dynamic timeKey;

  Packet(this.data, {this.timeKey});

  /// Encrypt the packet data.
  Future<void> encrypt() async {
    final k = Kaalka(time: _parseTimeKey(timeKey));
    encrypted = await k.encrypt(data);
  }

  /// Decrypt the packet data.
  Future<String> decrypt() async {
    if (encrypted == null) throw Exception('No encrypted data');
    final k = Kaalka(time: _parseTimeKey(timeKey));
    return await k.decrypt(encrypted!);
  }

  DateTime? _parseTimeKey(dynamic timeKey) {
    if (timeKey == null) return null;
    if (timeKey is DateTime) return timeKey;
    if (timeKey is String) {
      final parts = timeKey.split(':');
      if (parts.length == 3) {
        final now = DateTime.now();
        return DateTime(now.year, now.month, now.day, int.parse(parts[0]), int.parse(parts[1]), int.parse(parts[2]));
      }
    }
    return null;
  }

  static void sender() {
    // Simulate sender preparing data
    String message = "Hello, Kaalka!";
    print("Original Message: $message");

    // Create a packet with the message
    var packet = Packet(message);

    // Encrypt the packet using Kaalka algorithm
    packet.encrypt();
    print("Encrypted Data: ${packet.encrypted}");

    // Simulate sending the encrypted data over the network
    send_data_over_network(packet.encrypted!);
  }

  static void receiver() {
    // Simulate receiving the encrypted data over the network
    receive_data_over_network().then((receivedData) {
      // Create a packet with the received data
      var packet = Packet(receivedData);

      // Decrypt the packet using Kaalka algorithm
      packet.decrypt();
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
