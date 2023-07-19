# Kaalka Algorithm
# Step - 1 First store the time stamp of the message generated in a variable

# Step - 2 Extract the value of second from timestamp and conver them into radians.

# Step - 3 Conversion of the individual characters of the message into ASCII value and storing them into an array

# Step - 4 Add the value of f(second) to each character of the array in the range ASCII set following the ASCII rule such that the addition extends the ASCII set to start again where the value of the f will be decided by:

#  if(second >= 0 && second <= 90)
#  then f == Sine
#  elseif(second >= 91 && second <= 180)
#  then f == Cotangent
#  elseif(second >= 181 && second <= 270)
#  then f == Cosine
#  elseif(second >= 271 && second <= 360)
#  then f == Tangent

# Step-5 Now we will encrypt and store the timestamp and cipher in the format 
# [header + cipher]  ---- form 1
# header = f(hour), f(minute), f(second)
# cipher = [f(second) + ASCII(0),f(second) + ASCII(1),f(second) + ASCII(2),...........f(second) + ASCII(n)]
# encrypt the form 1 with inverse of f i.e.
#  if(second >= 0 && second <= 90)
#  then f' == Cosec
#  elseif(second >= 91 && second <= 180)
#  then f' == Tangent
#  elseif(second >= 181 && second <= 270)
#  then f' == Secant
#  elseif(second >= 271 && second <= 360)
#  then f' == Cotangent
# to form cipher_final = [f'(second)+(header),f'(second)+(cipher)]

## Note that all the conversion will take place with help of abs function for the absolute value

# Boilerplate code using the standard python cryptography library performing symmetric encryption AES
"""
from cryptography.fernet import Fernet
import time

# Step 1: First store the timestamp of the message generated in a variable
timestamp = time.time()

# Step 2: Extract the value of second from the timestamp and convert them into radians.
second = int(timestamp % 60)
second_radians = second * (3.14159265359 / 180)

# Step 3: Conversion of the individual characters of the message into ASCII value and storing them into an array
message = "Hello, world!"
ascii_values = [ord(char) for char in message]

# Step 4: Add the value of f(second) to each character of the array based on second's value where f is decided will be decided first
# For simplicity, let's assume f(second) is a simple function that increments ASCII values

def f(value):
    return value + second

encrypted_message = [f(char) for char in ascii_values]

# Encrypt and decrypt the message using a symmetric encryption algorithm (AES) from the cryptography library
key = Fernet.generate_key()
cipher_suite = Fernet(key)
cipher_text = cipher_suite.encrypt(bytes(encrypted_message))
decrypted_message = cipher_suite.decrypt(cipher_text)

# Print the results
print("Original Message:", message)
print("Encrypted Cipher Text:", cipher_text)
print("Decrypted Message:", decrypted_message.decode())

"""
# Boilerplate for testing
"""
from cryptography.fernet import Fernet
import time
import math

# Step 1: First store the timestamp of the message generated in a variable
timestamp = time.time()

# Step 2: Extract the value of second from the timestamp and convert them into radians.
second = int(timestamp % 60)
second_radians = second * (math.pi / 30)  # Since there are 60 seconds in a minute and 2*pi radians in a circle

# Step 3: Conversion of the individual characters of the message into ASCII value and storing them into an array
message = "Hello, world!"
ascii_values = [ord(char) for char in message]

# Step 4: Add the value of f(second) to each character of the array based on second's value
# For simplicity, let's assume f(second) is a simple function that increments ASCII values

def f(value, second):
    if 0 <= second <= 90:
        return value + math.sin(second_radians)
    elif 91 <= second <= 180:
        return value + 1 / math.tan(second_radians)
    elif 181 <= second <= 270:
        return value + math.cos(second_radians)
    elif 271 <= second <= 360:
        return value + math.tan(second_radians)

# Step 5: Applying the f function to each character of the array and creating the encrypted message
encrypted_values = [f(char, second) for char in ascii_values]

# Encrypt and decrypt the message using a symmetric encryption algorithm (Fernet) from the cryptography library
key = Fernet.generate_key()
cipher_suite = Fernet(key)
cipher_text = cipher_suite.encrypt(bytes(encrypted_values))
decrypted_values = cipher_suite.decrypt(cipher_text)

# Converting decrypted bytes back to characters
decrypted_message = ''.join(chr(int(round(x))) for x in decrypted_values)

# Print the results
print("Original Message:", message)
print("Encrypted Cipher Text:", cipher_text)
print("Decrypted Message:", decrypted_message)
"""
# Structure of Kaalka with above
"""
from __future__ import annotations
import time
import typing
import math

class Kaalka:
    def __init__(self) -> None:
        pass

    def encrypt(self, data: str) -> str:
        timestamp = time.time()
        second = int(timestamp % 60)
        encrypted_message = self._encrypt_message(data, second)
        return encrypted_message

    def _encrypt_message(self, data: str, second: int) -> str:
        ascii_values = [ord(char) for char in data]
        encrypted_values = [self._apply_trigonometric_function(val, second) for val in ascii_values]
        encrypted_message = "".join(chr(val) for val in encrypted_values)
        return encrypted_message

    def _apply_trigonometric_function(self, value: int, second: int) -> int:
        quadrant = self._determine_quadrant(second)
        if quadrant == 1:
            return round(value + math.sin(second))
        elif quadrant == 2:
            return round(value + 1 / math.tan(second))
        elif quadrant == 3:
            return round(value + math.cos(second))
        elif quadrant == 4:
            return round(value + math.tan(second))
        else:
            return value  # In case of an invalid quadrant, do not modify the value.

    def _determine_quadrant(self, second: int) -> int:
        if 0 <= second <= 15:
            return 1
        elif 16 <= second <= 30:
            return 2
        elif 31 <= second <= 45:
            return 3
        else:
            return 4


# Test the Kaalka encryption algorithm
if __name__ == "__main__":
    kaalka = Kaalka()
    message = "Hello, world!"
    encrypted_message = kaalka.encrypt(message)
    print("Original Message:", message)
    print("Encrypted Message:", encrypted_message)

"""
# Structured Kalka with encryption and decryption
"""
from __future__ import annotations
import time
import typing
import math

class Kaalka:
    def __init__(self) -> None:
        pass

    def encrypt(self, data: str) -> str:
        timestamp = time.time()
        second = int(timestamp % 60)
        encrypted_message = self._encrypt_message(data, second)
        return encrypted_message
    
    def decrypt(self, encrypted_message: str) -> str:
        timestamp = time.time()
        second = int(timestamp % 60)
        decrypted_message = self._decrypt_message(encrypted_message, second)
        return decrypted_message

    def _encrypt_message(self, data: str, second: int) -> str:
        ascii_values = [ord(char) for char in data]
        encrypted_values = [self._apply_trigonometric_function(val, second) for val in ascii_values]
        encrypted_message = "".join(chr(val) for val in encrypted_values)
        return encrypted_message
    
    def _decrypt_message(self, encrypted_message: str, second: int) -> str:
        rev_ascii = [ord(char) for char in encrypted_message]
        decrypted_values = [self._apply_inverse_function(val, second) for val in rev_ascii]
        decrypted_message = "".join(chr(val) for val in decrypted_values)
        return decrypted_message

    def _apply_trigonometric_function(self, value: int, second: int) -> int:
        quadrant = self._determine_quadrant(second)
        if quadrant == 1:
            return round(value + math.sin(second))
        elif quadrant == 2:
            return round(value + 1 / math.tan(second))
        elif quadrant == 3:
            return round(value + math.cos(second))
        elif quadrant == 4:
            return round(value + math.tan(second))
        else:
            return value  # In case of an invalid quadrant, do not modify the value.
        
    def _apply_inverse_function(self, value: int, second: int) -> int:
        quadrant = self._determine_quadrant(second)
        if quadrant == 1:
            return round(value - math.sin(second))
        elif quadrant == 2:
            return round(value - 1 / math.tan(second))
        elif quadrant == 3:
            return round(value - math.cos(second))
        elif quadrant == 4:
            return round(value - math.tan(second))
        else:
            return value  # In case of an invalid quadrant, do not modify the value.

    def _determine_quadrant(self, second: int) -> int:
        if 0 <= second <= 15:
            return 1
        elif 16 <= second <= 30:
            return 2
        elif 31 <= second <= 45:
            return 3
        else:
            return 4


# Test the Kaalka encryption algorithm
if __name__ == "__main__":
    kaalka = Kaalka()
    message = "Hello, world!"
    print("Original Message:", message)
    encrypted_message = kaalka.encrypt(message)
    print("Encrypted Message:", encrypted_message)
    decrypted_message = kaalka.decrypt(encrypted_message)
    print("Decrypted Message:", decrypted_message)
"""
# Global timestamp generation and storing of the value of second to be used simultaneously for the encryption and decryption
"""
from __future__ import annotations
import time
import typing
import math

class Kaalka:
    def __init__(self) -> None:
        self.second = 0
        self._update_timestamp()

    def _update_timestamp(self):
        timestamp = time.time()
        self.second = int(timestamp % 60)

    def encrypt(self, data: str) -> str:
        encrypted_message = self._encrypt_message(data)
        return encrypted_message
    
    def decrypt(self, encrypted_message: str) -> str:
        decrypted_message = self._decrypt_message(encrypted_message)
        return decrypted_message

    def _encrypt_message(self, data: str) -> str:
        ascii_values = [ord(char) for char in data]
        encrypted_values = [self._apply_trigonometric_function(val) for val in ascii_values]
        encrypted_message = "".join(chr(val) for val in encrypted_values)
        return encrypted_message
    
    def _decrypt_message(self, encrypted_message: str) -> str:
        rev_ascii = [ord(char) for char in encrypted_message]
        decrypted_values = [self._apply_inverse_function(val) for val in rev_ascii]
        decrypted_message = "".join(chr(val) for val in decrypted_values)
        return decrypted_message

    def _apply_trigonometric_function(self, value: int) -> int:
        quadrant = self._determine_quadrant(self.second)
        if quadrant == 1:
            return round(value + math.sin(self.second))
        elif quadrant == 2:
            return round(value + 1 / math.tan(self.second))
        elif quadrant == 3:
            return round(value + math.cos(self.second))
        elif quadrant == 4:
            return round(value + math.tan(self.second))
        else:
            return value  # In case of an invalid quadrant, do not modify the value.
        
    def _apply_inverse_function(self, value: int) -> int:
        quadrant = self._determine_quadrant(self.second)
        if quadrant == 1:
            return round(value - math.sin(self.second))
        elif quadrant == 2:
            return round(value - 1 / math.tan(self.second))
        elif quadrant == 3:
            return round(value - math.cos(self.second))
        elif quadrant == 4:
            return round(value - math.tan(self.second))
        else:
            return value  # In case of an invalid quadrant, do not modify the value.

    def _determine_quadrant(self, second: int) -> int:
        if 0 <= second <= 15:
            return 1
        elif 16 <= second <= 30:
            return 2
        elif 31 <= second <= 45:
            return 3
        else:
            return 4


# Test the Kaalka encryption algorithm
if __name__ == "__main__":
    kaalka = Kaalka()
    message = "Hello, world!"
    print("Original Message:", message)
    encrypted_message = kaalka.encrypt(message)
    print("Encrypted Message:", encrypted_message)
    decrypted_message = kaalka.decrypt(encrypted_message)
    print("Decrypted Message:", decrypted_message)

"""
