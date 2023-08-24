import 'package:test/test.dart';
import 'package:Kaalka/kaalka_ntp.dart';

void main() {
  group('KaalkaNTP Tests', () {
    test('Get Current Time', () {
      var kaalkaNTP = KaalkaNTP();
      var currentTime = kaalkaNTP.getCurrentTime();
      expect(currentTime, isNotNull);
    });
  });
}
