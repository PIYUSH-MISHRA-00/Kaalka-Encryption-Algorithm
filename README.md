# Kaalka Package

* Based upon the Kaalka Encryption Algorithm

* To use into your project first run the following into the terminal

```
pip install kaalka
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
