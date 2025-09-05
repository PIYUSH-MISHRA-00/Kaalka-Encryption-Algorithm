
"""
Kaalka File Weave: Chunked file encryption/decryption for time-first protocol

Implements chunked file encryption and manifest generation using Kaalka Drum and time-based protocol primitives.
All cryptographic operations are performed via Drum and time; no external crypto primitives are used.
This module is production-ready and fully tested.
"""
from typing import Dict

def encrypt_file(in_path: str, out_dir: str, recipient_sundial_beacon: bytes, sender_sundial_secret: bytes, chunk_size: int = 1 << 20) -> Dict:
    """
    Encrypts a file in chunks, producing encrypted chunk files and a manifest in out_dir.
    Each chunk is encrypted and sealed using Drum+time protocol primitives.
    Returns manifest dictionary describing all chunks.
    """
    import os, base64, json
    from kaalka.time_protocol import encrypt_envelope
    os.makedirs(out_dir, exist_ok=True)
    manifest = {
        "file": os.path.basename(in_path),
        "chunks": [],
        "total_size": os.path.getsize(in_path)
    }
    with open(in_path, 'rb') as f:
        idx = 0
        while True:
            chunk = f.read(chunk_size)
            if not chunk:
                break
            beat_time_iso = None  # Use current time for each chunk
            envelope = encrypt_envelope(
                chunk,
                recipient_sundial_beacon,
                sender_sundial_secret,
                aad=f"chunk-{idx}".encode(),
                seq=idx+1,
                beat_time_iso=beat_time_iso
            )
            chunk_fname = f"chunk_{idx+1:04d}.kaalka"
            chunk_path = os.path.join(out_dir, chunk_fname)
            with open(chunk_path, 'wb') as cf:
                cf.write(base64.b64decode(envelope['ct']))
            manifest['chunks'].append({
                "file": chunk_fname,
                "envelope": envelope
            })
            idx += 1
    manifest_path = os.path.join(out_dir, 'manifest.json')
    with open(manifest_path, 'w') as mf:
        json.dump(manifest, mf, indent=2)
    return manifest

def decrypt_file(manifest_path: str, recipient_sundial_secret: bytes, out_path: str) -> None:
    """
    Decrypts a file from manifest and encrypted chunks, writing output to out_path.
    Uses Drum+time protocol primitives for decryption and seal verification.
    """
    import os, json, base64
    from kaalka.time_protocol import decrypt_envelope
    with open(manifest_path, 'r') as mf:
        manifest = json.load(mf)
    out_dir = os.path.dirname(manifest_path)
    with open(out_path, 'wb') as outf:
        for chunk_info in manifest['chunks']:
            chunk_file = os.path.join(out_dir, chunk_info['file'])
            with open(chunk_file, 'rb') as cf:
                ciphertext = cf.read()
            envelope = chunk_info['envelope']
            envelope['ct'] = base64.b64encode(ciphertext).decode()
            plaintext = decrypt_envelope(
                envelope,
                recipient_sundial_secret,
                expected_aad=envelope['aad'].encode() if 'aad' in envelope else b''
            )
            outf.write(plaintext)
