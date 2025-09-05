"""
Kaalka Time-First Protocol CLI

Encrypt/decrypt files, chunked files, and messages using the Kaalka time-first protocol.
"""

import argparse
import sys
import os
import base64
from kaalka.time_protocol import encrypt_envelope, decrypt_envelope, generate_sundial
from kaalka.file_weave import encrypt_file, decrypt_file

def main():
    parser = argparse.ArgumentParser(
        description="Kaalka Time-First Protocol CLI: Encrypt/decrypt files, chunks, and messages using Drum+time primitives."
    )
    subparsers = parser.add_subparsers(dest="command", required=True)

    # Encrypt message
    enc_msg = subparsers.add_parser("encrypt-message", help="Encrypt a message")
    enc_msg.add_argument("--message", required=True, help="Message to encrypt")
    enc_msg.add_argument("--recipient-secret", required=True, help="Recipient Sundial secret (hex)")
    enc_msg.add_argument("--sender-secret", required=True, help="Sender Sundial secret (hex)")
    enc_msg.add_argument("--timestamp", help="Timestamp (ISO8601 or HH:MM:SS)")

    # Decrypt message
    dec_msg = subparsers.add_parser("decrypt-message", help="Decrypt a message envelope")
    dec_msg.add_argument("--envelope", required=True, help="Envelope JSON (base64 or file)")
    dec_msg.add_argument("--recipient-secret", required=True, help="Recipient Sundial secret (hex)")

    # Encrypt file (single)
    enc_file = subparsers.add_parser("encrypt-file", help="Encrypt a file (single envelope)")
    enc_file.add_argument("--input", required=True, help="Input file path")
    enc_file.add_argument("--output", required=True, help="Output file path")
    enc_file.add_argument("--recipient-secret", required=True, help="Recipient Sundial secret (hex)")
    enc_file.add_argument("--sender-secret", required=True, help="Sender Sundial secret (hex)")
    enc_file.add_argument("--timestamp", help="Timestamp (ISO8601 or HH:MM:SS)")

    # Decrypt file (single)
    dec_file = subparsers.add_parser("decrypt-file", help="Decrypt a file (single envelope)")
    dec_file.add_argument("--input", required=True, help="Input file path")
    dec_file.add_argument("--output", required=True, help="Output file path")
    dec_file.add_argument("--recipient-secret", required=True, help="Recipient Sundial secret (hex)")

    # Encrypt chunked file
    enc_chunks = subparsers.add_parser("encrypt-chunks", help="Encrypt a file in chunks")
    enc_chunks.add_argument("--input", required=True, help="Input file path")
    enc_chunks.add_argument("--output-dir", required=True, help="Output directory for chunks and manifest")
    enc_chunks.add_argument("--recipient-secret", required=True, help="Recipient Sundial secret (hex)")
    enc_chunks.add_argument("--sender-secret", required=True, help="Sender Sundial secret (hex)")
    enc_chunks.add_argument("--chunk-size", type=int, default=1048576, help="Chunk size in bytes (default 1MB)")

    # Decrypt chunked file
    dec_chunks = subparsers.add_parser("decrypt-chunks", help="Decrypt a chunked file from manifest")
    dec_chunks.add_argument("--manifest", required=True, help="Manifest JSON file path")
    dec_chunks.add_argument("--output", required=True, help="Output file path")
    dec_chunks.add_argument("--recipient-secret", required=True, help="Recipient Sundial secret (hex)")

    args = parser.parse_args()

    try:
        if args.command == "encrypt-message":
            import json
            message = args.message.encode()
            recipient_secret = bytes.fromhex(args.recipient_secret)
            sender_secret = bytes.fromhex(args.sender_secret)
            envelope = encrypt_envelope(message, recipient_secret, sender_secret, beat_time_iso=args.timestamp)
            envelope_json = json.dumps(envelope)
            print(base64.b64encode(envelope_json.encode()).decode())

        elif args.command == "decrypt-message":
            recipient_secret = bytes.fromhex(args.recipient_secret)
            # Envelope can be base64 string or file
            try:
                with open(args.envelope, 'r') as f:
                    envelope_json = f.read()
            except Exception:
                envelope_json = base64.b64decode(args.envelope).decode()
            import json
            envelope = json.loads(envelope_json)
            plaintext = decrypt_envelope(envelope, recipient_secret)
            print(plaintext.decode(errors='replace'))

        elif args.command == "encrypt-file":
            with open(args.input, 'rb') as f:
                data = f.read()
            recipient_secret = bytes.fromhex(args.recipient_secret)
            sender_secret = bytes.fromhex(args.sender_secret)
            envelope = encrypt_envelope(data, recipient_secret, sender_secret, beat_time_iso=args.timestamp)
            import json
            with open(args.output, 'w') as outf:
                json.dump(envelope, outf, indent=2)
            print(f"File encrypted and envelope written to {args.output}")

        elif args.command == "decrypt-file":
            import json
            with open(args.input, 'r') as f:
                envelope = json.load(f)
            recipient_secret = bytes.fromhex(args.recipient_secret)
            plaintext = decrypt_envelope(envelope, recipient_secret)
            with open(args.output, 'wb') as outf:
                outf.write(plaintext)
            print(f"File decrypted and written to {args.output}")

        elif args.command == "encrypt-chunks":
            recipient_secret = bytes.fromhex(args.recipient_secret)
            sender_secret = bytes.fromhex(args.sender_secret)
            manifest = encrypt_file(args.input, args.output_dir, recipient_secret, sender_secret, chunk_size=args.chunk_size)
            import json
            manifest_path = os.path.join(args.output_dir, 'manifest.json')
            with open(manifest_path, 'w') as mf:
                json.dump(manifest, mf, indent=2)
            print(f"Chunked file encrypted. Manifest written to {manifest_path}")

        elif args.command == "decrypt-chunks":
            recipient_secret = bytes.fromhex(args.recipient_secret)
            decrypt_file(args.manifest, recipient_secret, args.output)
            print(f"Chunked file decrypted and written to {args.output}")

        else:
            parser.print_help()
            sys.exit(1)
    except Exception as e:
        print(f"Error: {e}", file=sys.stderr)
        sys.exit(2)

if __name__ == "__main__":
    main()
