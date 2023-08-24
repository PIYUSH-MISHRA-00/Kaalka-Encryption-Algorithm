# Kaalka Package for Python

* Based upon the Kaalka Encryption Algorithm

* File Structure

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
# Exemplar Usage

```
from kaalka import Kaalka
from kaalkaNTP import KaalkaNTP

# Create an instance of Kaalka
kaalka = Kaalka()
message = "Hello, world!"
encrypted_message = kaalka.encrypt(message)
decrypted_message = kaalka.decrypt(encrypted_message)

print("Encrypted Message:", encrypted_message)
print("Decrypted Message:", decrypted_message)

# Create an instance of KaalkaNTP
kaalka_ntp = KaalkaNTP()
# Now you can use the methods of KaalkaNTP as well

```
