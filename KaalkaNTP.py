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
