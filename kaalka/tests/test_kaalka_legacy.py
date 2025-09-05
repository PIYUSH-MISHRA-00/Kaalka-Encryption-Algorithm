"""
Pytest tests for legacy Kaalka encryption/decryption logic (text and file).
"""
import tempfile, os
from kaalka.file_encryptor import Kaalka

def test_text_roundtrip():
    kaalka = Kaalka()
    msg = "Hello, world!"
    enc = kaalka.encrypt(msg)
    dec = kaalka.decrypt(enc)
    assert dec == msg

def test_text_roundtrip_with_time():
    kaalka = Kaalka()
    msg = "Hello, world!"
    time_key = "14:35:22"
    enc = kaalka.encrypt(msg, time_key)
    dec = kaalka.decrypt(enc, time_key)
    assert dec == msg

def test_file_roundtrip():
    kaalka = Kaalka()
    import secrets
    with tempfile.TemporaryDirectory() as tmpdir:
        in_path = os.path.join(tmpdir, 'test.bin')
        data = secrets.token_bytes(2048)
        with open(in_path, 'wb') as f:
            f.write(data)
        enc_path = kaalka.encrypt(in_path, "12:34:56")
        dec_path = kaalka.decrypt(enc_path, "12:34:56")
        with open(dec_path, 'rb') as f:
            dec_data = f.read()
        assert dec_data == data
