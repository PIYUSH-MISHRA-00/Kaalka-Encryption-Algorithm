import 'dart:math';
import 'dart:core';

class Kaalka {
  int second = 0;

  Kaalka() {
    _updateTimestamp();
  }

  void _updateTimestamp() {
    var timestamp = DateTime.now().millisecondsSinceEpoch / 1000;
    second = (timestamp % 60).toInt();
  }

  DateTime getCurrentTime() {
    return DateTime.now();
  }

  String encrypt(String data, DateTime timestamp) {
    var encryptedMessage = _encryptMessage(data);
    return encryptedMessage;
  }

  String decrypt(String encryptedMessage, DateTime timestamp) {
    var decryptedMessage = _decryptMessage(encryptedMessage);
    return decryptedMessage;
  }

  String _encryptMessage(String data) {
    var asciiValues = data.runes.toList();
    var encryptedValues = asciiValues.map((val) => _applyTrigonometricFunction(val)).toList();
    var encryptedMessage = String.fromCharCodes(encryptedValues);
    return encryptedMessage;
  }

  String _decryptMessage(String encryptedMessage) {
    var revAscii = encryptedMessage.runes.toList();
    var decryptedValues = revAscii.map((val) => _applyInverseFunction(val)).toList();
    var decryptedMessage = String.fromCharCodes(decryptedValues);
    return decryptedMessage;
  }

  int _applyTrigonometricFunction(int value) {
    var quadrant = _determineQuadrant(second);
    if (quadrant == 1) {
      return (value + sin(second)).round();
    } else if (quadrant == 2) {
      return (value + 1 / tan(second)).round();
    } else if (quadrant == 3) {
      return (value + cos(second)).round();
    } else if (quadrant == 4) {
      return (value + tan(second)).round();
    } else {
      return value;
    }
  }

  int _applyInverseFunction(int value) {
    var quadrant = _determineQuadrant(second);
    if (quadrant == 1) {
      return (value - sin(second)).round();
    } else if (quadrant == 2) {
      return (value - 1 / tan(second)).round();
    } else if (quadrant == 3) {
      return (value - cos(second)).round();
    } else if (quadrant == 4) {
      return (value - tan(second)).round();
    } else {
      return value;
    }
  }

  int _determineQuadrant(int second) {
    if (0 <= second && second <= 15) {
      return 1;
    } else if (16 <= second && second <= 30) {
      return 2;
    } else if (31 <= second && second <= 45) {
      return 3;
    } else {
      return 4;
    }
  }
}