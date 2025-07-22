# Kaalka: Time-Based Encryption Library for Python

Kaalka is a robust, time-based encryption library for Python, inspired by the Kaalka Encryption Algorithm. It enables secure, non-trivial encryption and decryption of messages using either the current system time or a user-provided timestamp as the cryptographic key. Kaalka is suitable for secure messaging, cryptographic experiments, and time-sensitive data protection.

## 🚀 Features
- **Time-based encryption:** Use the current system time or any timestamp (HH:MM:SS) as the encryption key.
- **Robust and unpredictable:** Combines trigonometric functions, clock hand angles, and character indices for strong, non-trivial encryption.
- **Easy integration:** Simple Python API for encrypting and decrypting messages.
- **NTP support:** Includes KaalkaNTP for network time protocol-based encryption.

## 🔒 How the Algorithm Works
Kaalka transforms each character in your message using a key derived from the angles between the hour, minute, and second hands of a clock at a given time. The algorithm:
- Calculates angles for the provided or current time.
- Selects trigonometric functions based on the quadrant of each angle.
- Combines these values with the character's index and the time to generate a unique, non-reversible transformation for each character.
- Decryption reverses this process using the same timestamp.

> **Note:** Encryption and decryption must use the same timestamp (or occur at the same second if using system time).

## 📦 Installation

```sh
pip install kaalka
```

## 📝 Usage


### Unified Text, Image, and Media Encryption/Decryption

```python
from kaalka import Kaalka

# Text encryption/decryption (system time)
kaalka = Kaalka()
message = "Hello, world!"
encrypted_message = kaalka.encrypt(message)
decrypted_message = kaalka.decrypt(encrypted_message)

# Text encryption/decryption (explicit time)
timestamp = "14:35:22"
encrypted_message = kaalka.encrypt(message, timestamp)
decrypted_message = kaalka.decrypt(encrypted_message, timestamp)

# File encryption/decryption (any file type, system time)
encrypted_file = kaalka.encrypt("test_image.jpg")        # Produces test_image.kaalka
decrypted_file = kaalka.decrypt(encrypted_file)           # Produces test_image.jpg

# File encryption/decryption (explicit time)
encrypted_file = kaalka.encrypt("test_data.csv", timestamp)
decrypted_file = kaalka.decrypt(encrypted_file, timestamp)

# Folder workflow example
import os, shutil
os.makedirs('encrypted', exist_ok=True)
os.makedirs('decrypted', exist_ok=True)
enc_path = kaalka.encrypt("test_image.jpg", timestamp)
shutil.move(enc_path, f'encrypted/test_image.kaalka')
dec_path = kaalka.decrypt('encrypted/test_image.kaalka', timestamp)
shutil.move(dec_path, f'decrypted/test_image.jpg')
```

> **Note:**
> - Encrypted files use only the `.kaalka` extension (e.g., `test_image.kaalka`, `video.kaalka`, `music.kaalka`).
> - Decrypted files restore the original name and extension (e.g., `test_image.jpg`, `video.mp4`, `music.mp3`).
> - The library supports all file types, including images, videos, music, text, CSV, JSON, XML, and more.
> - Encryption and decryption are lossless and robust for all binary formats.

### KaalkaNTP Usage

```python
from kaalkaNTP import KaalkaNTP

kaalka_ntp = KaalkaNTP()
# Use KaalkaNTP methods for NTP-based encryption
```

## 📂 File Structure

```
Kaalka-Encryption-Algorithm/
|-- kaalka/
|   |-- file_encryptor.py
|   |-- kaalka.py
|   |-- __init__.py
|   |-- __pycache__/
|-- kaalkaNTP/
|   |-- kaalkaNTP.py
|   |-- packet.py
|   |-- __init__.py
|-- build/
|   |-- lib/
|       |-- kaalka/
|       |-- kaalkaNTP/
|-- dist/
|-- kaalka.egg-info/
|-- LICENSE
|-- README.md
|-- requirements.txt
|-- setup.py
|-- .github/
|   |-- workflows/
|       |-- python-package.yml
|-- .git/
```

## ⚠️ Security Notes
- If you do not provide a timestamp, the current system time is used for encryption and decryption. Encryption and decryption must occur at the same second for the result to be correct.
- For reproducibility and secure communication, always provide an explicit timestamp string (format: 'HH:MM:SS').
- The strength of encryption depends on the secrecy and unpredictability of the timestamp used.

## 📄 License
See the LICENSE file for details.

## 🤝 Contributing
Contributions, bug reports, and feature requests are welcome! Please open an issue or submit a pull request on GitHub.
