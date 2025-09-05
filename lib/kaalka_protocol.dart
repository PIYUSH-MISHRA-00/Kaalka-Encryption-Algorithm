import 'dart:typed_data';
import 'dart:convert';

/// Kaalka Time-First Protocol (Dart)
/// SECURITY WARNING: Kaalka is a custom, time-based cipher. This wrapper uses only Kaalka Drum calls and is experimental. Do NOT use in production without a formal cryptographic audit.

class KaalkaProtocol {
  final Map<String, Set<int>> _replayLedger = {};

  /// Derive a per-device/session secret from UTC time (ms precision)
  static Uint8List keyResonance(String deviceId, {String? timestamp}) {
    final time = timestamp ?? DateTime.now().toUtc().toIso8601String();
    final seed = utf8.encode(deviceId + time);
    return _drum(seed, time);
  }

  /// Drum logic (Kaalka-style, time-based transformation)
  static Uint8List _drum(List<int> bytes, String time) {
    final timeHash = utf8.encode(time);
    final out = List<int>.filled(bytes.length, 0);
    for (var i = 0; i < bytes.length; i++) {
      out[i] = bytes[i] ^ timeHash[i % timeHash.length];
    }
    return Uint8List.fromList(out);
  }

  /// Create a canonical envelope
  static Map<String, dynamic> encryptEnvelope(
    String plaintext,
    String senderId,
    String receiverId, {
    String? timestamp,
    int windowSeconds = 120,
    int seq = 1,
  }) {
    final time = timestamp ?? DateTime.now().toUtc().toIso8601String();
    final key = keyResonance(senderId, timestamp: time);
    final ct = _drum(utf8.encode(plaintext), time);
    final envelope = {
      'senderId': senderId,
      'receiverId': receiverId,
      'timestamp': time,
      'window': windowSeconds,
      'seq': seq,
      'ciphertext': base64.encode(ct),
    };
    envelope['seal'] = _seal(envelope, key, time);
    return envelope;
  }

  /// Decrypt and verify envelope
  String decryptEnvelope(
    Map<String, dynamic> envelope,
    String receiverId, {
    String? timestamp,
  }) {
    final time = timestamp ?? envelope['timestamp'];
    final key = keyResonance(envelope['senderId'], timestamp: time);
    final now = DateTime.now().toUtc();
    final msgTime = DateTime.parse(envelope['timestamp']);
    final window = envelope['window'] ?? 120;
    final seq = envelope['seq'];
    // Time window check
    if (now.difference(msgTime).inSeconds.abs() > window) {
      throw Exception('Message expired');
    }
    // Replay protection
    final ledgerKey = '${envelope['senderId']}:$receiverId';
    _replayLedger.putIfAbsent(ledgerKey, () => {});
    if (_replayLedger[ledgerKey]!.contains(seq)) {
      throw Exception('Replay detected');
    }
    _replayLedger[ledgerKey]!.add(seq);
    // Seal check
    final expectedSeal = _seal(envelope, key, time);
    if (envelope['seal'] != expectedSeal) {
      throw Exception('Seal verification failed');
    }
    // Decrypt
    final ct = base64.decode(envelope['ciphertext']);
    final pt = _drum(ct, time);
    return utf8.decode(pt);
  }

  /// Compute time-based seal (integrity MIC)
  static String _seal(Map<String, dynamic> envelope, Uint8List key, String time) {
    // Canonical order: senderId, receiverId, timestamp, window, seq, ciphertext
    final bytes = <int>[];
    bytes.addAll(utf8.encode(envelope['senderId']));
    bytes.addAll(utf8.encode(envelope['receiverId']));
    bytes.addAll(utf8.encode(envelope['timestamp']));
    bytes.addAll(utf8.encode(envelope['window'].toString()));
    bytes.addAll(utf8.encode(envelope['seq'].toString()));
    bytes.addAll(base64.decode(envelope['ciphertext']));
    bytes.addAll(key);
    return base64.encode(_drum(bytes, time));
  }
}
