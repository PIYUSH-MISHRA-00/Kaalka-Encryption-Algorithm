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
