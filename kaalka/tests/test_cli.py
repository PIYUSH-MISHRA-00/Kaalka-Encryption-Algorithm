"""
Automated tests for Kaalka Time-First Protocol CLI
"""
import subprocess
import sys
import tempfile
import os

def run_cli(args, input_data=None):
    env = os.environ.copy()
    env['PYTHONPATH'] = os.getcwd()
    cmd = [sys.executable, 'cli/kaalka_time_cli.py'] + args
    result = subprocess.run(cmd, input=input_data, capture_output=True, env=env, text=True)
    return result

def test_cli_help():
    result = run_cli(['--help'])
    assert 'Kaalka Time-First Protocol CLI' in result.stdout
    assert 'encrypt-message' in result.stdout
    assert result.returncode == 0

def test_cli_encrypt_decrypt_message():
    # Generate secrets
    from kaalka.time_protocol import generate_sundial
    sender_secret, _ = generate_sundial()
    recipient_secret, _ = generate_sundial()
    msg = 'Test message'
    # Encrypt
    result_enc = run_cli([
        'encrypt-message',
        '--message', msg,
        '--recipient-secret', recipient_secret.hex(),
        '--sender-secret', sender_secret.hex()
    ])
    assert result_enc.returncode == 0
    envelope_b64 = result_enc.stdout.strip()
    # Decrypt
    result_dec = run_cli([
        'decrypt-message',
        '--envelope', envelope_b64,
        '--recipient-secret', recipient_secret.hex()
    ])
    assert result_dec.returncode == 0
    assert msg in result_dec.stdout

def test_cli_encrypt_decrypt_file():
    from kaalka.time_protocol import generate_sundial
    sender_secret, _ = generate_sundial()
    recipient_secret, _ = generate_sundial()
    with tempfile.TemporaryDirectory() as tmpdir:
        in_path = os.path.join(tmpdir, 'test.txt')
        out_path = os.path.join(tmpdir, 'enc.json')
        dec_path = os.path.join(tmpdir, 'dec.txt')
        msg = 'File test data'
        with open(in_path, 'w') as f:
            f.write(msg)
        # Encrypt file
        result_enc = run_cli([
            'encrypt-file',
            '--input', in_path,
            '--output', out_path,
            '--recipient-secret', recipient_secret.hex(),
            '--sender-secret', sender_secret.hex()
        ])
        assert result_enc.returncode == 0
        # Decrypt file
        result_dec = run_cli([
            'decrypt-file',
            '--input', out_path,
            '--output', dec_path,
            '--recipient-secret', recipient_secret.hex()
        ])
        assert result_dec.returncode == 0
        with open(dec_path, 'r') as f:
            dec_msg = f.read()
        assert msg == dec_msg
