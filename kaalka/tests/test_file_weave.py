"""
Pytest tests for Kaalka file chunking

Tests chunked file encryption and decryption using Drum+time protocol primitives.
"""
import pytest

def test_file_weave_roundtrip():
    """
    Test that a file encrypted in chunks and then decrypted yields the original file contents.
    """
    import tempfile, os, secrets, base64
    from kaalka.file_weave import encrypt_file, decrypt_file
    from kaalka.time_protocol import generate_sundial
    from kaalka.file_encryptor import Kaalka
    # Create a temp file
    with tempfile.TemporaryDirectory() as tmpdir:
        in_path = os.path.join(tmpdir, 'test.bin')
        data = secrets.token_bytes(4096)
        with open(in_path, 'wb') as f:
            f.write(data)
        sender_secret, sender_beacon = generate_sundial()
        recipient_secret, _ = generate_sundial()
        recipient_beacon = recipient_secret
        out_dir = os.path.join(tmpdir, 'enc')
        manifest = encrypt_file(in_path, out_dir, recipient_beacon, sender_secret, chunk_size=1024)
        out_path = os.path.join(tmpdir, 'dec.bin')
        manifest_path = os.path.join(out_dir, 'manifest.json')
        decrypt_file(manifest_path, recipient_secret, out_path)
        with open(out_path, 'rb') as f:
            dec_data = f.read()
        assert dec_data == data
