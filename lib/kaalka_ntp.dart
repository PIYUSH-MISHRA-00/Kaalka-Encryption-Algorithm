import 'dart:async';
import 'dart:io';
import 'kaalka.dart';

/// KaalkaNTP: Kaalka encryption using NTP or user-provided timestamp.
/// Compatible with Python/JS implementations.
class KaalkaNTP extends Kaalka {
  KaalkaNTP([dynamic timeKey]) : super(timeKey);

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
      return Kaalka(timeKey).encrypt(data);
    } else {
      final t = await getNtpTime();
      return Kaalka('${t[0]}:${t[1]}:${t[2]}').encrypt(data);
    }
  }

  /// Decrypt [data] using NTP time or provided [timeKey].
  static Future<String> decryptWithNtp(String data, {dynamic timeKey}) async {
    if (timeKey != null) {
      return Kaalka(timeKey).decrypt(data);
    } else {
      final t = await getNtpTime();
      return Kaalka('${t[0]}:${t[1]}:${t[2]}').decrypt(data);
    }
  }
}
