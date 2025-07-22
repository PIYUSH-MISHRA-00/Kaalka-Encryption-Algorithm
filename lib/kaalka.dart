import 'dart:io';
import 'dart:math';
import 'dart:convert';

class Kaalka {
  late int h, m, s;

  Kaalka({DateTime? time}) {
    final now = time ?? DateTime.now();
    this.h = now.hour % 12;
    this.m = now.minute;
    this.s = now.second;
  }

  void _setTime(String? timeKey) {
    if (timeKey != null && timeKey.isNotEmpty) {
      final parts = timeKey.split(':');
      int hh = 0, mm = 0, ss = 0;
      if (parts.length == 3) {
        hh = int.parse(parts[0]);
        mm = int.parse(parts[1]);
        ss = int.parse(parts[2]);
      } else if (parts.length == 2) {
        mm = int.parse(parts[0]);
        ss = int.parse(parts[1]);
      } else if (parts.length == 1) {
        ss = int.parse(parts[0]);
      }
      this.h = hh % 12;
      this.m = mm;
      this.s = ss;
    }
  }

  Future<String> encrypt(dynamic data, {String? timeKey}) async {
    _setTime(timeKey);
    // File or media encryption
    if (data is String && await File(data).exists()) {
      final file = File(data);
      final ext = '.' + data.split('.').last;
      final raw = await file.readAsBytes();
      final encBytes = _proc(raw, true);
      final extBytes = utf8.encode(ext);
      final extLen = [extBytes.length];
      final finalBytes = <int>[]..addAll(extLen)..addAll(extBytes)..addAll(encBytes);
      final base = data.substring(0, data.length - ext.length);
      final outfile = base + '.kaalka';
      await File(outfile).writeAsBytes(finalBytes);
      return outfile;
    }
    // Text encryption
    if (data is String) {
      return _encryptMessage(data);
    }
    // Binary encryption
    if (data is List<int>) {
      return utf8.decode(_proc(data, true));
    }
    throw ArgumentError('Unsupported data type for encryption.');
  }

  Future<dynamic> decrypt(dynamic data, {String? timeKey}) async {
    _setTime(timeKey);
    // File or media decryption
    if (data is String && await File(data).exists()) {
      final file = File(data);
      final buf = await file.readAsBytes();
      if (buf.isEmpty || buf.length < 2) {
        throw ArgumentError('File is too small or corrupted for decryption.');
      }
      final extLen = buf[0];
      final ext = utf8.decode(buf.sublist(1, 1 + extLen));
      final encData = buf.sublist(1 + extLen);
      final decBytes = _proc(encData, false);
      final base = data.substring(0, data.length - '.kaalka'.length);
      final outfile = base + ext;
      await File(outfile).writeAsBytes(decBytes);
      return outfile;
    }
    // Text decryption
    if (data is String) {
      return _decryptMessage(data);
    }
    // Binary decryption
    if (data is List<int>) {
      return utf8.decode(_proc(data, false));
    }
    throw ArgumentError('Unsupported data type for decryption.');
  }

  List<double> _getAngles() {
    final hourAngle = 30 * h + 0.5 * m + (0.5 / 60) * s;
    final minuteAngle = 6 * m + 0.1 * s;
    final secondAngle = 6 * s;
    final angle_hm = min((hourAngle - minuteAngle).abs(), 360 - (hourAngle - minuteAngle).abs());
    final angle_ms = min((minuteAngle - secondAngle).abs(), 360 - (minuteAngle - secondAngle).abs());
    final angle_hs = min((hourAngle - secondAngle).abs(), 360 - (hourAngle - secondAngle).abs());
    return [angle_hm, angle_ms, angle_hs];
  }

  double _selectTrig(double angle) {
    final rad = angle * pi / 180;
    final quadrant = (angle ~/ 90) + 1;
    if (quadrant == 1) return sin(rad);
    if (quadrant == 2) return cos(rad);
    if (quadrant == 3) return tan(rad);
    final tanVal = tan(rad);
    return tanVal != 0 ? 1 / tanVal : 0;
  }

  List<int> _proc(List<int> data, bool encrypt) {
    // Use integer arithmetic for lossless, reversible byte transformation (for files/media)
    final key = (h * 3600 + m * 60 + s) == 0 ? 1 : (h * 3600 + m * 60 + s);
    final result = <int>[];
    for (var idx = 0; idx < data.length; idx++) {
      final b = data[idx];
      final offset = (key + idx) % 256;
      int val;
      if (encrypt) {
        val = (b + offset) % 256;
      } else {
        val = (b - offset) % 256;
      }
      result.add(val);
    }
    return result;
  }

  String _encryptMessage(String data) {
    final angles = _getAngles();
    var encrypted = '';
    for (var idx = 0; idx < data.length; idx++) {
      final c = data.codeUnitAt(idx);
      final factor = (h + m + s + idx + 1) == 0 ? 1 : (h + m + s + idx + 1);
      final offset = (angles.map(_selectTrig).reduce((a, b) => a + b)) * factor + (idx + 1);
      encrypted += String.fromCharCode((c + offset.round()) % 256);
    }
    return encrypted;
  }

  String _decryptMessage(String data) {
    final angles = _getAngles();
    var decrypted = '';
    for (var idx = 0; idx < data.length; idx++) {
      final c = data.codeUnitAt(idx);
      final factor = (h + m + s + idx + 1) == 0 ? 1 : (h + m + s + idx + 1);
      final offset = (angles.map(_selectTrig).reduce((a, b) => a + b)) * factor + (idx + 1);
      decrypted += String.fromCharCode((c - offset.round()) % 256);
    }
    return decrypted;
  }
}
