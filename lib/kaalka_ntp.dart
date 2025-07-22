import 'dart:async';
import 'kaalka.dart';

/// KaalkaNTP: Kaalka encryption using NTP or user-provided timestamp.
/// Compatible with Python/JS implementations.
class KaalkaNTP extends Kaalka {
  KaalkaNTP([dynamic timeKey]) : super(time: _parseTimeKey(timeKey));

  static DateTime? _parseTimeKey(dynamic timeKey) {
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

  /// Get current time from NTP server (returns [h, m, s]).
  /// This is a stub; for real NTP, use a Dart NTP package or implement UDP query.
  static Future<List<int>> getNtpTime() async {
    // TODO: Replace with real NTP query for production use.
    final now = DateTime.now().toUtc();
    return [now.hour % 12, now.minute, now.second];
  }

  /// Encrypt [data] using NTP time or provided [timeKey].
  static Future<String> encryptWithNtp(String data, {dynamic timeKey}) async {
    if (timeKey != null) {
      return Kaalka(time: _parseTimeKey(timeKey)).encrypt(data);
    } else {
      final t = await getNtpTime();
      return Kaalka(time: DateTime(DateTime.now().year, DateTime.now().month, DateTime.now().day, t[0], t[1], t[2])).encrypt(data);
    }
  }

  /// Decrypt [data] using NTP time or provided [timeKey].
  static Future<String> decryptWithNtp(String data, {dynamic timeKey}) async {
    if (timeKey != null) {
      return await Kaalka(time: _parseTimeKey(timeKey)).decrypt(data);
    } else {
      final t = await getNtpTime();
      return await Kaalka(time: DateTime(DateTime.now().year, DateTime.now().month, DateTime.now().day, t[0], t[1], t[2])).decrypt(data);
    }
  }
}
