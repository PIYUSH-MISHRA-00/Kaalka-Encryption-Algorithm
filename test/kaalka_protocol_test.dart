import 'package:test/test.dart';
import 'dart:typed_data';
import '../lib/kaalka_protocol.dart';
import '../lib/kaalka_file.dart';

void main() {
  test('Envelope roundtrip', () {
    final envelope = KaalkaProtocol.encryptEnvelope('hello', 'sender', 'receiver');
    final pt = KaalkaProtocol().decryptEnvelope(envelope, 'receiver');
    expect(pt, 'hello');
  });

  test('Replay protection', () {
    final kp = KaalkaProtocol();
    final envelope = KaalkaProtocol.encryptEnvelope('msg', 'sender', 'receiver', seq: 42);
    kp.decryptEnvelope(envelope, 'receiver');
    expect(() => kp.decryptEnvelope(envelope, 'receiver'), throwsException);
  });

  test('File chunk roundtrip', () {
    final fileBytes = Uint8List.fromList(List.generate(2 * 1024 * 1024, (i) => i % 256));
    final chunks = KaalkaFile.encryptFileChunks(fileBytes, 'sender', 'receiver');
    final outBytes = KaalkaFile.decryptFileChunks(chunks, 'receiver');
    expect(outBytes.length, fileBytes.length);
    expect(outBytes, fileBytes);
  });

  test('User timestamp', () {
  final now = DateTime.now().toUtc().toIso8601String();
  final envelope = KaalkaProtocol.encryptEnvelope('timed', 'sender', 'receiver', timestamp: now);
  final pt = KaalkaProtocol().decryptEnvelope(envelope, 'receiver', timestamp: now);
  expect(pt, 'timed');
  });
}
