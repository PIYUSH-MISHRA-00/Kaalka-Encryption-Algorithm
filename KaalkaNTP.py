# Kaalka Algorithm for NTP
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
# to form cipher_final = [f'(second) + (header), f'(second) + (cipher)]

# Step - 6 Store the value of request timestamp and encrypt the message with request timestamp's second value at the native system ready for communication
# where the f' will be calculated same as
#  if(second >= 0 && second <= 90)
#  then f' == Cosec
#  elseif(second >= 91 && second <= 180)
#  then f' == Tangent
#  elseif(second >= 181 && second <= 270)
#  then f' == Secant
#  elseif(second >= 271 && second <= 360)
#  then f' == Cotangent

# To form cipher_request = [f'(request_second) + (header), f'(request_second) + (cipher)]

# Step - 7  Extract the request second value from the cipher_request and store the value in a variable

# Step - 8 Store the value of response timestamp, request timestamp and encrypt with request timestamp's second value at the native system ready for communication
# where the f' will be calculated same as
#  if(second >= 0 && second <= 90)
#  then f' == Cosec
#  elseif(second >= 91 && second <= 180)
#  then f' == Tangent
#  elseif(second >= 181 && second <= 270)
#  then f' == Secant
#  elseif(second >= 271 && second <= 360)
#  then f' == Cotangent

# To form cipher_response = [f'[f(response_second) + (header), f(request_timestamp), f(response_second) + ASCII(response)]]

# Validate the header timestamp values at the time of devlievery to the systems for validation purpose.

## Note that all the conversion will take place with help of abs function for the absolute value

### Boilerplate code

"""
import time
import math

# Step 1: First store the timestamp of the message generated in a variable
timestamp = time.time()

# Step 2: Extract the value of second from the timestamp and convert them into radians.
second = int(timestamp % 60)
second_radians = math.radians(second)

# Step 3: Conversion of the individual characters of the message into ASCII value and storing them into an array
message = "Hello, Kaalka!"
ascii_values = [ord(char) for char in message]

# Step 4: Add the value of f(second) to each character of the array based on second's value
def f(value):
    return math.sin(value)  # Placeholder trigonometric function

encrypted_message = [ord(char) + f(second_radians) for char in message]

# Step 5: Encrypt and store the timestamp and cipher in the specified format [header + cipher]
header = [math.sin(time.localtime().tm_hour), math.cos(time.localtime().tm_min), math.tan(second_radians)]
cipher = [f(second_radians) + ascii_value for ascii_value in ascii_values]
cipher_final = header + cipher

# Step 6: Store the value of request timestamp and encrypt the message with request timestamp's second value at the native system ready for communication
request_timestamp = time.time()
request_second = int(request_timestamp % 60)
request_second_radians = math.radians(request_second)

cipher_request = [f(request_second_radians) + header, f(request_second_radians) + cipher]

# Step 7: Extract the request second value from the cipher_request and store the value in a variable
extracted_request_second = int(cipher_request[1][0] - header[2])

# Step 8: Store the value of response timestamp, request timestamp and encrypt with request timestamp's second value at the native system ready for communication
response_timestamp = time.time()
response_second = int(response_timestamp % 60)
response_second_radians = math.radians(response_second)

cipher_response = [f(response_second_radians) + header, f(request_second_radians) + cipher]

# Validate the header timestamp values at the time of delivery to the systems for validation purposes.

"""
# Boilerplate for Kaalka algorithm to encrypt and decrypt packets, we'll create a simple Packet class and implement the encryption and decryption logic with the use of the UDP protocol for simplicity
"""
import time
import typing
import math
import ntplib

class Kaalka:
    def __init__(self) -> None:
        self.second = 0
        self._update_timestamp()

    def _update_timestamp(self):
        ntp_client = ntplib.NTPClient()
        response = ntp_client.request('pool.ntp.org', version=4)
        ntp_time = response.tx_time
        self.second = abs(int(ntp_time % 60))

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

class Packet:
    def __init__(self, data: str) -> None:
        self.data = data
        self.encrypted_data = None

    def encrypt(self, kaalka: Kaalka) -> None:
        if not self.encrypted_data:
            encrypted_message = kaalka.encrypt(self.data)
            self.encrypted_data = encrypted_message

    def decrypt(self, kaalka: Kaalka) -> None:
        if self.encrypted_data:
            decrypted_message = kaalka.decrypt(self.encrypted_data)
            self.data = decrypted_message
            self.encrypted_data = None

if __name__ == "__main__":
    kaalka = Kaalka()

    # Example Usage
    message = "Hello, Kaalka!"
    print("Original Message:", message)

    # Create a packet with the message
    packet = Packet(message)

    # Encrypt the packet using Kaalka algorithm
    packet.encrypt(kaalka)
    print("Encrypted Data:", packet.encrypted_data)

    # Decrypt the packet using Kaalka algorithm
    packet.decrypt(kaalka)
    print("Decrypted Message:", packet.data)

"""
# Structured Kaalka for communication over NTP 
"""
import socket
import time
import typing
import math
import ntplib

class Kaalka:
    def __init__(self) -> None:
        self.second = 0
        self._update_timestamp()

    def _update_timestamp(self):
        ntp_client = ntplib.NTPClient()
        response = ntp_client.request('pool.ntp.org', version=4)
        ntp_time = response.tx_time
        self.second = abs(int(ntp_time % 60))

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

class Packet:
    def __init__(self, data: str) -> None:
        self.data = data
        self.encrypted_data = None

    def encrypt(self, kaalka: Kaalka) -> None:
        if not self.encrypted_data:
            encrypted_message = kaalka.encrypt(self.data)
            self.encrypted_data = encrypted_message

    def decrypt(self, kaalka: Kaalka) -> None:
        if self.encrypted_data:
            decrypted_message = kaalka.decrypt(self.encrypted_data)
            self.data = decrypted_message
            self.encrypted_data = None

def sender():
    # Simulate sender preparing data
    message = "Hello, Kaalka!"
    print("Original Message:", message)

    # Create a packet with the message
    packet = Packet(message)

    # Encrypt the packet using Kaalka algorithm
    kaalka = Kaalka()
    packet.encrypt(kaalka)
    print("Encrypted Data:", packet.encrypted_data)

    # Simulate sending the encrypted data over the network
    send_data_over_network(packet.encrypted_data)

def receiver():
    # Simulate receiving the encrypted data over the network
    received_data = receive_data_over_network()

    # Create a packet with the received data
    packet = Packet(received_data)

    # Decrypt the packet using Kaalka algorithm
    kaalka = Kaalka()
    packet.decrypt(kaalka)
    print("Decrypted Message:", packet.data)

def send_data_over_network(data):
    # Simulate sending data over the network (e.g., using sockets)
    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
        s.sendto(data.encode(), ('127.0.0.1', 12345))

def receive_data_over_network():
    # Simulate receiving data over the network (e.g., using sockets)
    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
        s.bind(('127.0.0.1', 12345))
        data, addr = s.recvfrom(1024)
        return data.decode()

if __name__ == "__main__":
    # Start the receiver in a separate thread (simulating a different machine)
    import threading
    receiver_thread = threading.Thread(target=receiver)
    receiver_thread.start()

    # Give some time for the receiver to be ready
    time.sleep(1)

    # Start the sender (simulating another machine)
    sender()

"""

import socket
import time
import typing
import math
import ntplib

class Kaalka:
    def __init__(self) -> None:
        self.second = 0
        self._update_timestamp()

    def _update_timestamp(self):
        ntp_client = ntplib.NTPClient()
        response = ntp_client.request('pool.ntp.org', version=4)
        ntp_time = response.tx_time
        self.second = abs(int(ntp_time % 60))

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

class Packet:
    def __init__(self, data: str) -> None:
        self.data = data
        self.encrypted_data = None

    def encrypt(self, kaalka: Kaalka) -> None:
        if not self.encrypted_data:
            encrypted_message = kaalka.encrypt(self.data)
            self.encrypted_data = encrypted_message

    def decrypt(self, kaalka: Kaalka) -> None:
        if self.encrypted_data:
            decrypted_message = kaalka.decrypt(self.encrypted_data)
            self.data = decrypted_message
            self.encrypted_data = None

def sender():
    # Simulate sender preparing data
    message = "Hello, Kaalka!"
    print("Original Message:", message)

    # Create a packet with the message
    packet = Packet(message)

    # Encrypt the packet using Kaalka algorithm
    kaalka = Kaalka()
    packet.encrypt(kaalka)
    print("Encrypted Data:", packet.encrypted_data)

    # Simulate sending the encrypted data over the network
    send_data_over_network(packet.encrypted_data)

def receiver():
    # Simulate receiving the encrypted data over the network
    received_data = receive_data_over_network()

    # Create a packet with the received data
    packet = Packet(received_data)

    # Decrypt the packet using Kaalka algorithm
    kaalka = Kaalka()
    packet.decrypt(kaalka)
    print("Decrypted Message:", packet.data)

def send_data_over_network(data):
    # Simulate sending data over the network (e.g., using sockets)
    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
        s.sendto(data.encode(), ('127.0.0.1', 12345))

def receive_data_over_network():
    # Simulate receiving data over the network (e.g., using sockets)
    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
        s.bind(('127.0.0.1', 12345))
        data, addr = s.recvfrom(1024)
        return data.decode()

if __name__ == "__main__":
    # Start the receiver in a separate thread (simulating a different machine)
    import threading
    receiver_thread = threading.Thread(target=receiver)
    receiver_thread.start()

    # Give some time for the receiver to be ready
    time.sleep(1)

    # Start the sender (simulating another machine)
    sender()
