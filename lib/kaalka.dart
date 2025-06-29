import 'dart:math';
import 'dart:core';

/// Kaalka Encryption Algorithm (Dart)
/// Robust, timestamp-based encryption using angles and trigonometric functions.
/// Compatible with Python/JS implementations.
class Kaalka {
  int h = 0;
  int m = 0;
  int s = 0;

  /// Create a Kaalka instance. If [timeKey] is provided, uses it as the timestamp.
  Kaalka([dynamic timeKey]) {
    if (timeKey != null) {
      _parseTime(timeKey);
    } else {
      _updateTimestamp();
    }
  }

  /// Update h, m, s to current system time (local).
  void _updateTimestamp() {
    final now = DateTime.now();
    h = now.hour % 12;
    m = now.minute;
    s = now.second;
  }

  /// Parse a time key (int seconds, or string 'HH:MM:SS', 'MM:SS', 'SS').
  void _parseTime(dynamic timeKey) {
    if (timeKey is int) {
      h = 0;
      m = 0;
      s = timeKey;
    } else if (timeKey is String) {
      final parts = timeKey.split(":").map(int.parse).toList();
      if (parts.length == 3) {
        h = parts[0] % 12;
        m = parts[1];
        s = parts[2];
      } else if (parts.length == 2) {
        h = 0;
        m = parts[0];
        s = parts[1];
      } else if (parts.length == 1) {
        h = 0;
        m = 0;
        s = parts[0];
      } else {
        throw ArgumentError("Invalid time format. Use HH:MM:SS, MM:SS, or SS.");
      }
    } else {
      throw ArgumentError("Invalid time format. Use HH:MM:SS, MM:SS, or SS.");
    }
  }

  /// Encrypt [data] using the current or provided timestamp.
  String encrypt(String data, [dynamic timeKey]) {
    if (timeKey != null) {
      _parseTime(timeKey);
    } else {
      _updateTimestamp();
    }
    return _encryptMessage(data);
  }

  /// Decrypt [encryptedMessage] using the current or provided timestamp.
  String decrypt(String encryptedMessage, [dynamic timeKey]) {
    if (timeKey != null) {
      _parseTime(timeKey);
    } else {
      _updateTimestamp();
    }
    return _decryptMessage(encryptedMessage);
  }

  /// Calculate angles between clock hands for the timestamp.
  List<double> _getAngles() {
    final hourAngle = (30 * h) + (0.5 * m) + (0.5 / 60 * s);
    final minuteAngle = (6 * m) + (0.1 * s);
    final secondAngle = 6 * s;
    final angleHm = min((hourAngle - minuteAngle).abs(), 360 - (hourAngle - minuteAngle).abs());
    final angleMs = min((minuteAngle - secondAngle).abs(), 360 - (minuteAngle - secondAngle).abs());
    final angleHs = min((hourAngle - secondAngle).abs(), 360 - (hourAngle - secondAngle).abs());
    return [angleHm, angleMs, angleHs];
  }

  /// Select a trigonometric function based on the quadrant of [angle].
  double _selectTrig(double angle) {
    final quadrant = (angle ~/ 90) + 1;
    final rad = angle * pi / 180;
    if (quadrant == 1) {
      return sin(rad);
    } else if (quadrant == 2) {
      return cos(rad);
    } else if (quadrant == 3) {
      return tan(rad);
    } else {
      final tanVal = tan(rad);
      return tanVal != 0 ? 1 / tanVal : 0;
    }
  }

  /// Internal: encrypt message string.
  String _encryptMessage(String data) {
    final angles = _getAngles();
    final angleHm = angles[0];
    final angleMs = angles[1];
    final angleHs = angles[2];
    final buffer = StringBuffer();
    for (var idx = 0; idx < data.length; idx++) {
      final c = data.codeUnitAt(idx);
      final factor = (h + m + s + idx + 1) == 0 ? 1 : (h + m + s + idx + 1);
      final offset = (_selectTrig(angleHm) + _selectTrig(angleMs) + _selectTrig(angleHs)) * factor + (idx + 1);
      buffer.writeCharCode((c + offset.round()) % 256);
    }
    return buffer.toString();
  }

  /// Internal: decrypt message string.
  String _decryptMessage(String encryptedMessage) {
    final angles = _getAngles();
    final angleHm = angles[0];
    final angleMs = angles[1];
    final angleHs = angles[2];
    final buffer = StringBuffer();
    for (var idx = 0; idx < encryptedMessage.length; idx++) {
      final c = encryptedMessage.codeUnitAt(idx);
      final factor = (h + m + s + idx + 1) == 0 ? 1 : (h + m + s + idx + 1);
      final offset = (_selectTrig(angleHm) + _selectTrig(angleMs) + _selectTrig(angleHs)) * factor + (idx + 1);
      buffer.writeCharCode((c - offset.round() + 256) % 256);
    }
    return buffer.toString();
  }
}