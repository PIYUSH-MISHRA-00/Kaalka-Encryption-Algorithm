import math
import time

class Kaalka:
    def __init__(self):
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
