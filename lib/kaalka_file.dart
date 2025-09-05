import 'dart:typed_data';
import 'kaalka_protocol.dart';
import 'dart:convert';

/// File chunking and encryption (Kaalka-style)
class KaalkaFile {
  static const int chunkSize = 1024 * 1024; // 1MB

  /// Encrypt file as chunks
  static List<Map<String, dynamic>> encryptFileChunks(
    Uint8List fileBytes,
    String senderId,
    String receiverId, {
    String? timestamp,
  }) {
    final chunks = <Map<String, dynamic>>[];
    final totalChunks = (fileBytes.length / chunkSize).ceil();
    for (var i = 0; i < totalChunks; i++) {
      final start = i * chunkSize;
      final end = ((i + 1) * chunkSize).clamp(0, fileBytes.length);
      final chunkBytes = fileBytes.sublist(start, end);
      final chunkTime = timestamp ?? DateTime.now().toUtc().toIso8601String();
      final chunkEnvelope = KaalkaProtocol.encryptEnvelope(
        base64.encode(chunkBytes),
        senderId,
        receiverId,
        timestamp: chunkTime,
        seq: i + 1,
      );
      chunkEnvelope['chunkIndex'] = i;
      chunks.add(chunkEnvelope);
    }
    return chunks;
  }

  /// Decrypt file chunks
  static Uint8List decryptFileChunks(
    List<Map<String, dynamic>> encryptedChunks,
    String receiverId, {
    String? timestamp,
  }) {
    final sortedChunks = List<Map<String, dynamic>>.from(encryptedChunks)
      ..sort((a, b) => a['chunkIndex'].compareTo(b['chunkIndex']));
    final fileBytes = <int>[];
    for (final chunk in sortedChunks) {
      final chunkData = KaalkaProtocol().decryptEnvelope(chunk, receiverId, timestamp: timestamp);
      fileBytes.addAll(base64.decode(chunkData));
    }
    return Uint8List.fromList(fileBytes);
  }
}
