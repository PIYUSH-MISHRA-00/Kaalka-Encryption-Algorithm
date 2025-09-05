import 'dart:io';
// import 'dart:convert';
import 'package:test/test.dart';

void main() {
  test('CLI envelope roundtrip', () async {
    final plainFile = 'plain.txt';
    final envFile = 'env.json';
    final outFile = 'out.txt';
    await File(plainFile).writeAsString('cli-test');
    final dartBin = Platform.resolvedExecutable;
    final cliPath = File('bin/kaalka_cli.dart').absolute.path;
    final timestamp = DateTime.now().toUtc().toIso8601String();
    final result1 = await Process.run(dartBin, [
      cliPath,
      'encrypt-msg',
      '--sender', 'cliSender',
      '--receiver', 'cliReceiver',
      '--in', plainFile,
      '--out', envFile,
      '--timestamp', timestamp
    ]);
    if (result1.exitCode != 0) {
      print('Encrypt CLI failed: stdout=${result1.stdout}, stderr=${result1.stderr}');
    }
    expect(result1.exitCode, 0);
    final result2 = await Process.run(dartBin, [
      cliPath,
      'decrypt-msg',
      '--receiver', 'cliReceiver',
      '--in', envFile,
      '--out', outFile,
      '--timestamp', timestamp
    ]);
    if (result2.exitCode != 0) {
      print('Decrypt CLI failed: stdout=${result2.stdout}, stderr=${result2.stderr}');
    }
    expect(result2.exitCode, 0);
    final outText = await File(outFile).readAsString();
    expect(outText, 'cli-test');
    await File(plainFile).delete();
    await File(envFile).delete();
    await File(outFile).delete();
  });
}
