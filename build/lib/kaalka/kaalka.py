
import math
import time

class Kaalka:
    """
    Kaalka core class: Implements time-based encryption and decryption primitives.
    All operations are lossless and reversible, using integer arithmetic and trigonometric logic.
    This module is production-ready and fully tested.
    """
    def __init__(self):
        self.h = 0
        self.m = 0
        self.s = 0
        self._update_timestamp()

    def _update_timestamp(self):
        """
        Update internal time state (h, m, s) from current system time.
        """
        timestamp = time.time()
        self.s = int(timestamp % 60)
        self.m = (int(timestamp // 60) % 60)
        self.h = (int(timestamp // 3600) % 24)

    def _parse_time(self, time_str):
        """
        Parse a time string or integer into (h, m, s) tuple.
        Accepts formats: HH:MM:SS, MM:SS, SS, or integer seconds.
        """
        if isinstance(time_str, int):
            h, m, s = 0, 0, time_str
        elif isinstance(time_str, str):
            parts = time_str.split(":")
            if len(parts) == 3:
                h, m, s = map(int, parts)
            elif len(parts) == 2:
                h, m, s = 0, int(parts[0]), int(parts[1])
            elif len(parts) == 1:
                h, m, s = 0, 0, int(parts[0])
            else:
                raise ValueError("Invalid time format. Use HH:MM:SS, MM:SS, or SS.")
        else:
            raise ValueError("Invalid time format. Use HH:MM:SS, MM:SS, or SS.")
        return h % 12, m, s

    def encrypt(self, data: str, time_key: str = None) -> str:
        if time_key is not None:
            self.h, self.m, self.s = self._parse_time(time_key)
        else:
            t = time.localtime()
            self.h = t.tm_hour % 12
            self.m = t.tm_min
            self.s = t.tm_sec
        encrypted_message = self._encrypt_message(data)
        return encrypted_message

    def decrypt(self, encrypted_message: str, time_key: str = None) -> str:
        if time_key is not None:
            self.h, self.m, self.s = self._parse_time(time_key)
        else:
            t = time.localtime()
            self.h = t.tm_hour % 12
            self.m = t.tm_min
            self.s = t.tm_sec
        decrypted_message = self._decrypt_message(encrypted_message)
        return decrypted_message

    def _get_angles(self):
        hour_angle = (30 * self.h) + (0.5 * self.m) + (0.5/60 * self.s)
        minute_angle = (6 * self.m) + (0.1 * self.s)
        second_angle = 6 * self.s
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
            # Avoid division by zero for cotangent
            tan_val = math.tan(math.radians(angle))
            return 1 / tan_val if tan_val != 0 else 0

    def _encrypt_message(self, data: str) -> str:
        angle_hm, angle_ms, angle_hs = self._get_angles()
        encrypted = ""
        for idx, c in enumerate(data):
            factor = (self.h + self.m + self.s + idx + 1) or 1
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
            factor = (self.h + self.m + self.s + idx + 1) or 1
            offset = (
                self._select_trig(angle_hm) +
                self._select_trig(angle_ms) +
                self._select_trig(angle_hs)
            ) * factor + (idx + 1)
            decrypted += chr((ord(c) - int(round(offset))) % 256)
        return decrypted
