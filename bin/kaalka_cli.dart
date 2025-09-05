import 'dart:io';
import 'dart:convert';
// import 'dart:typed_data';
import '../lib/kaalka_protocol.dart';
import '../lib/kaalka_file.dart';

void main(List<String> args) async {
  if (args.isEmpty || args.contains('--help')) {
    print('''
Kaalka CLI (Dart)
Usage:
  encrypt-msg --sender <id> --receiver <id> --in <plain.txt> --out <env.json> [--timestamp <ts>]
  decrypt-msg --in <env.json> --receiver <id> --out <plain.txt> [--timestamp <ts>]
  encrypt-file --sender <id> --receiver <id> --in <file> --out <chunks.json> [--timestamp <ts>]
  decrypt-file --in <chunks.json> --receiver <id> --out <file> [--timestamp <ts>]
''');
    exit(0);
  }

  if (args[0] == 'encrypt-msg') {
    final sender = _getArg(args, '--sender');
    final receiver = _getArg(args, '--receiver');
    final inFile = _getArg(args, '--in');
    final outFile = _getArg(args, '--out');
    final ts = _getArg(args, '--timestamp');
    if ([sender, receiver, inFile, outFile].contains(null)) {
      print('Missing required argument for encrypt-msg');
      exit(1);
    }
    final plain = await File(inFile!).readAsString();
    final env = KaalkaProtocol.encryptEnvelope(plain, sender!, receiver!, timestamp: ts);
    await File(outFile!).writeAsString(jsonEncode(env));
    print('Envelope written to $outFile');
    exit(0);
  }

  if (args[0] == 'decrypt-msg') {
    final receiver = _getArg(args, '--receiver');
    final inFile = _getArg(args, '--in');
    final outFile = _getArg(args, '--out');
    final ts = _getArg(args, '--timestamp');
    if ([receiver, inFile, outFile].contains(null)) {
      print('Missing required argument for decrypt-msg');
      exit(1);
    }
    final env = jsonDecode(await File(inFile!).readAsString());
    final pt = KaalkaProtocol().decryptEnvelope(env, receiver!, timestamp: ts);
    await File(outFile!).writeAsString(pt);
    print('Plaintext written to $outFile');
    exit(0);
  }

  if (args[0] == 'encrypt-file') {
    final sender = _getArg(args, '--sender');
    final receiver = _getArg(args, '--receiver');
    final inFile = _getArg(args, '--in');
    final outFile = _getArg(args, '--out');
    final ts = _getArg(args, '--timestamp');
    if ([sender, receiver, inFile, outFile].contains(null)) {
      print('Missing required argument for encrypt-file');
      exit(1);
    }
    final fileBytes = await File(inFile!).readAsBytes();
    final chunks = KaalkaFile.encryptFileChunks(fileBytes, sender!, receiver!, timestamp: ts);
    await File(outFile!).writeAsString(jsonEncode(chunks));
    print('Chunks written to $outFile');
    exit(0);
  }

  if (args[0] == 'decrypt-file') {
    final receiver = _getArg(args, '--receiver');
    final inFile = _getArg(args, '--in');
    final outFile = _getArg(args, '--out');
    final ts = _getArg(args, '--timestamp');
    if ([receiver, inFile, outFile].contains(null)) {
      print('Missing required argument for decrypt-file');
      exit(1);
    }
    final chunks = jsonDecode(await File(inFile!).readAsString()) as List;
    final outBytes = KaalkaFile.decryptFileChunks(List<Map<String, dynamic>>.from(chunks), receiver!, timestamp: ts);
    await File(outFile!).writeAsBytes(outBytes);
    print('File written to $outFile');
    exit(0);
  }

  print('Unknown command. Use --help for usage.');
  exit(1);
}

String? _getArg(List<String> args, String name) {
  final idx = args.indexOf(name);
  if (idx != -1 && idx + 1 < args.length) return args[idx + 1];
  return null;
}
