

import 'dart:io';
import 'package:test/test.dart';
import '../lib/kaalka.dart';

void main() {
  group('Kaalka Encryption', () {
    final testText = 'Hello, world!';
    final testTime = '14:35:22';
    final testFile = 'test_file.txt';
    final testBinFile = 'test_bin.dat';
    final testUtf8File = 'test_utf8.txt';
    final testLargeFile = 'test_large.txt';

    setUp(() async {
      await File(testFile).writeAsString(testText);
      await File(testBinFile).writeAsBytes([1,2,3,4,5,6,7,8,9,10]);
      await File(testUtf8File).writeAsString('हैलो');
      await File(testLargeFile).writeAsString('A' * 10000);
    });

    tearDown(() async {
      for (final f in [testFile, testBinFile, testUtf8File, testLargeFile,
        testFile.replaceAll('.txt', '.kaalka'), testBinFile.replaceAll('.dat', '.kaalka'),
        testUtf8File.replaceAll('.txt', '.kaalka'), testLargeFile.replaceAll('.txt', '.kaalka'),
        testFile.replaceAll('.kaalka', '.txt'), testBinFile.replaceAll('.kaalka', '.dat'),
        testUtf8File.replaceAll('.kaalka', '.txt'), testLargeFile.replaceAll('.kaalka', '.txt')]) {
        try { await File(f).delete(); } catch (_) {}
      }
    });

    test('encrypts and decrypts text (system time)', () async {
      final k = Kaalka();
      final enc = await k.encrypt(testText);
      final dec = await k.decrypt(enc);
      expect(dec.length, testText.length);
    });

    test('encrypts and decrypts text (explicit time)', () async {
      final k = Kaalka();
      final enc = await k.encrypt(testText, timeKey: testTime);
      final dec = await k.decrypt(enc, timeKey: testTime);
      expect(dec, testText);
    });

    test('encrypts and decrypts a file (system time)', () async {
      final k = Kaalka();
      final encFile = await k.encrypt(testFile);
      expect(encFile.endsWith('.kaalka'), true);
      final decFile = await k.decrypt(encFile);
      final txt = await File(decFile).readAsString();
      expect(txt.trim(), testText);
    });

    test('encrypts and decrypts a file (explicit time)', () async {
      final k = Kaalka();
      final encFile = await k.encrypt(testFile, timeKey: testTime);
      expect(encFile.endsWith('.kaalka'), true);
      final decFile = await k.decrypt(encFile, timeKey: testTime);
      final txt = await File(decFile).readAsString();
      expect(txt.trim(), testText);
    });

    test('encrypts and decrypts a binary file', () async {
      final k = Kaalka();
      final buf = [1,2,3,4,5,6,7,8,9,10];
      final encFile = await k.encrypt(testBinFile, timeKey: '01:02:03');
      final decFile = await k.decrypt(encFile, timeKey: '01:02:03');
      final out = await File(decFile).readAsBytes();
      expect(out, buf);
    });

    test('encrypts and decrypts a UTF-8 file', () async {
      final k = Kaalka();
      final encFile = await k.encrypt(testUtf8File, timeKey: '10:20:30');
      final decFile = await k.decrypt(encFile, timeKey: '10:20:30');
      final txt = await File(decFile).readAsString();
      expect(txt.trim(), 'हैलो');
    });

    test('encrypts and decrypts a large file', () async {
      final k = Kaalka();
      final encFile = await k.encrypt(testLargeFile, timeKey: '05:06:07');
      final decFile = await k.decrypt(encFile, timeKey: '05:06:07');
      final txt = await File(decFile).readAsString();
      expect(txt, 'A' * 10000);
    });

    test('handles empty string and file', () async {
      final k = Kaalka();
      final enc = await k.encrypt('');
      final dec = await k.decrypt(enc);
      expect(dec, '');
      await File('empty.txt').writeAsString('');
      final encFile = await k.encrypt('empty.txt');
      final decFile = await k.decrypt(encFile);
      final txt = await File(decFile).readAsString();
      expect(txt, '');
      try {
        if (await File('empty.txt').exists()) {
          await File('empty.txt').delete();
        }
        if (await File('empty.kaalka').exists()) {
          await File('empty.kaalka').delete();
        }
        if (await File('empty.txt').exists()) {
          await File('empty.txt').delete();
        }
      } catch (_) {}
    });
  });
}
