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

### Basic Encryption/Decryption (System Time)

```python
from kaalka import Kaalka

kaalka = Kaalka()
message = "Hello, world!"
encrypted_message = kaalka.encrypt(message)  # Uses current system time as key
decrypted_message = kaalka.decrypt(encrypted_message)  # Uses current system time

print("Encrypted Message:", encrypted_message)
print("Decrypted Message:", decrypted_message)
```

### Encryption/Decryption with Explicit Timestamp

```python
from kaalka import Kaalka

kaalka = Kaalka()
message = "Hello, world!"
timestamp = "14:35:22"  # Use a specific time as key

encrypted_message = kaalka.encrypt(message, timestamp)
decrypted_message = kaalka.decrypt(encrypted_message, timestamp)

print("Encrypted Message:", encrypted_message)
print("Decrypted Message:", decrypted_message)
```

### KaalkaNTP Usage

```python
from kaalkaNTP import KaalkaNTP

kaalka_ntp = KaalkaNTP()
# Use KaalkaNTP methods for NTP-based encryption
```

## 📂 File Structure

```
kaalka_package/
|-- kaalkaNTP/
|   |-- __init__.py
|   |-- kaalkaNTP.py
|   |-- packet.py
|-- kaalka/
|   |-- __init__.py
|   |-- kaalka.py
|-- setup.py
```

## ⚠️ Security Notes
- If you do not provide a timestamp, the current system time is used for encryption and decryption. Encryption and decryption must occur at the same second for the result to be correct.
- For reproducibility and secure communication, always provide an explicit timestamp string (format: 'HH:MM:SS').
- The strength of encryption depends on the secrecy and unpredictability of the timestamp used.

## 📄 License
See the LICENSE file for details.

## 🤝 Contributing
Contributions, bug reports, and feature requests are welcome! Please open an issue or submit a pull request on GitHub.
