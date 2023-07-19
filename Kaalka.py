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
