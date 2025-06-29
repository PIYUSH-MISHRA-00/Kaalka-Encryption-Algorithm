import time
import math
import ntplib

class KaalkaNTP:
    def __init__(self):
        self.second = 0
        self._update_timestamp()

    def _update_timestamp(self):
        try:
            ntp_client = ntplib.NTPClient()
            response = ntp_client.request("pool.ntp.org", version=4)
            if response:
                timestamp = response.tx_time
                self.second = int(timestamp % 60)
            else:
                raise Exception("Failed to get NTP response.")
        except Exception as e:
            print("Error fetching NTP time:", e)
            # Fallback to system time if NTP fails
            timestamp = time.time()
            self.second = int(timestamp % 60)

    def encrypt(self, data):
        encrypted_message = self._encrypt_message(data)
        return encrypted_message

    def decrypt(self, encrypted_message):
        decrypted_message = self._decrypt_message(encrypted_message)
        return decrypted_message

    def _get_angles(self):
        h = self.second // 3600
        m = (self.second % 3600) // 60
        s = self.second % 60
        hour_angle = (30 * h) + (0.5 * m) + (0.5/60 * s)
        minute_angle = (6 * m) + (0.1 * s)
        second_angle = 6 * s
        angle_hm = min(abs(hour_angle - minute_angle), 360 - abs(hour_angle - minute_angle))
        angle_ms = min(abs(minute_angle - second_angle), 360 - abs(minute_angle - second_angle))
        angle_hs = min(abs(hour_angle - second_angle), 360 - abs(hour_angle - second_angle))
        return angle_hm, angle_ms, angle_hs

    def _select_trig(self, angle):
        quadrant = int(angle // 90) + 1
        if quadrant == 1:
            return math.sin(math.radians(angle))
        elif quadrant == 2:
            return math.cos(math.radians(angle))
        elif quadrant == 3:
            return math.tan(math.radians(angle))
        else:
            tan_val = math.tan(math.radians(angle))
            return 1 / tan_val if tan_val != 0 else 0

    def _encrypt_message(self, data: str) -> str:
        angle_hm, angle_ms, angle_hs = self._get_angles()
        encrypted = ""
        for idx, c in enumerate(data):
            factor = (self.second + idx + 1) or 1
            offset = (
                self._select_trig(angle_hm) +
                self._select_trig(angle_ms) +
                self._select_trig(angle_hs)
            ) * factor + (idx + 1)
            encrypted += chr((ord(c) + int(round(offset))) % 256)
        return encrypted

    def _decrypt_message(self, encrypted_message: str) -> str:
        angle_hm, angle_ms, angle_hs = self._get_angles()
        decrypted = ""
        for idx, c in enumerate(encrypted_message):
            factor = (self.second + idx + 1) or 1
            offset = (
                self._select_trig(angle_hm) +
                self._select_trig(angle_ms) +
                self._select_trig(angle_hs)
            ) * factor + (idx + 1)
            decrypted += chr((ord(c) - int(round(offset))) % 256)
        return decrypted

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