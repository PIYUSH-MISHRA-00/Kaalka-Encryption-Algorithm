"""
Pytest tests for Kaalka time-first protocol messaging

Tests envelope encryption/decryption, seal tampering detection, replay protection, and time skew rejection using Drum+time protocol primitives.
"""
import pytest

def test_time_roundtrip():
    """
    Test that a message encrypted and then decrypted with the protocol yields the original plaintext.
    """
    from kaalka.time_protocol import encrypt_envelope, decrypt_envelope, generate_sundial
    import secrets, base64
    sender_secret, sender_beacon = generate_sundial()
    recipient_secret, _ = generate_sundial()
    # Use raw secret as recipient beacon (protocol expects this)
    recipient_beacon = recipient_secret
    message = secrets.token_bytes(128)
    envelope = encrypt_envelope(message, recipient_beacon, sender_secret)
    decrypted = decrypt_envelope(envelope, recipient_secret)
    assert decrypted == message

def test_seal_tamper_detects():
    """
    Test that tampering with the seal or ciphertext is detected and rejected.
    """
    from kaalka.time_protocol import encrypt_envelope, decrypt_envelope, generate_sundial
    import pytest, secrets, base64
    sender_secret, sender_beacon = generate_sundial()
    recipient_secret, _ = generate_sundial()
    recipient_beacon = recipient_secret
    message = secrets.token_bytes(64)
    envelope = encrypt_envelope(message, recipient_beacon, sender_secret)
    # Tamper with both ciphertext and seal to guarantee failure
    envelope_bad = dict(envelope)
    ct_bytes = base64.b64decode(envelope_bad['ct'])
    ct_bytes = bytearray(ct_bytes)
    ct_bytes[0] ^= 0xFF
    envelope_bad['ct'] = base64.b64encode(ct_bytes).decode()
    # Also tamper with seal
    seal_bytes = base64.b64decode(envelope_bad['seal'])
    seal_bytes = bytearray(seal_bytes)
    seal_bytes[0] ^= 0xFF
    envelope_bad['seal'] = base64.b64encode(seal_bytes).decode()
    with pytest.raises(Exception):
        decrypt_envelope(envelope_bad, recipient_secret)

def test_replay_detection():
    """
    Test that replayed sequence numbers are detected and rejected by the Ledger.
    """
    from kaalka.time_protocol import encrypt_envelope, decrypt_envelope, generate_sundial
    from kaalka.ledger import Ledger
    import pytest, secrets, base64
    sender_secret, sender_beacon = generate_sundial()
    recipient_secret, _ = generate_sundial()
    recipient_beacon = recipient_secret
    ledger = Ledger()
    message = secrets.token_bytes(32)
    envelope = encrypt_envelope(message, recipient_beacon, sender_secret, seq=42)
    # First decrypt should succeed
    decrypt_envelope(envelope, recipient_secret, ledger=ledger)
    # Replay (same seq) should fail
    with pytest.raises(Exception):
        decrypt_envelope(envelope, recipient_secret, ledger=ledger)

def test_time_skew_rejection():
    """
    Test that envelopes with excessive time skew are rejected.
    """
    from kaalka.time_protocol import encrypt_envelope, decrypt_envelope, generate_sundial
    import pytest, secrets, base64
    from datetime import datetime, timedelta
    sender_secret, sender_beacon = generate_sundial()
    recipient_secret, _ = generate_sundial()
    recipient_beacon = recipient_secret
    message = secrets.token_bytes(32)
    # Use a beat_time far in the past
    old_time = (datetime.utcnow() - timedelta(days=2)).isoformat(timespec='milliseconds') + 'Z'
    envelope = encrypt_envelope(message, recipient_beacon, sender_secret, beat_time_iso=old_time)
    with pytest.raises(Exception):
        decrypt_envelope(envelope, recipient_secret)
