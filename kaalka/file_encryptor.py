import os

class Kaalka:
    """
    Kaalka encryption/decryption for text and files.
    Usage:
        kaalka = Kaalka()
        encrypted = kaalka.encrypt('hello world')
        decrypted = kaalka.decrypt(encrypted)
        encrypted_file = kaalka.encrypt('image.jpg')
        decrypted_file = kaalka.decrypt('image.jpg.kaalka')
        encrypted_with_time = kaalka.encrypt('hello world', '14:35:22')
        decrypted_with_time = kaalka.decrypt(encrypted_with_time, '14:35:22')
    """

    def __init__(self):
        import time as _t
        t = _t.localtime()
        self.h = t.tm_hour % 12
        self.m = t.tm_min
        self.s = t.tm_sec

    def _set_time(self, time_key):
        if time_key:
            parts = str(time_key).split(':')
            h, m, s = 0, 0, 0
            if len(parts) == 3:
                h, m, s = map(int, parts)
            elif len(parts) == 2:
                m, s = map(int, parts)
            elif len(parts) == 1:
                s = int(parts[0])
            self.h = h % 12
            self.m = m
            self.s = s

    def encrypt(self, data, time_key=None):
        if isinstance(data, str) and os.path.isfile(data):
            ext = os.path.splitext(data)[1]
            with open(data, 'rb') as f:
                raw = f.read()
            self._set_time(time_key)
            enc_bytes = self._proc(raw, True)
            ext_bytes = ext.encode('utf-8')
            ext_len = len(ext_bytes)
            final = bytes([ext_len]) + ext_bytes + enc_bytes
            base = os.path.splitext(data)[0]
            outfile = base + '.kaalka'
            with open(outfile, 'wb') as f:
                f.write(final)
            return outfile
        else:
            self._set_time(time_key)
            return self._encrypt_message(data)

    def decrypt(self, data, time_key=None):
        if isinstance(data, str) and os.path.isfile(data):
            with open(data, 'rb') as f:
                ext_len = f.read(1)[0]
                ext = f.read(ext_len).decode('utf-8')
                enc_data = f.read()
            self._set_time(time_key)
            dec_bytes = self._proc(enc_data, False)
            base = os.path.splitext(data)[0]
            outfile = base + ext if not base.endswith(ext) else base
            with open(outfile, 'wb') as f:
                f.write(dec_bytes)
            return outfile
        else:
            self._set_time(time_key)
            return self._decrypt_message(data)

    def _get_angles(self):
        hour_angle = (30 * self.h) + (0.5 * self.m) + (0.5/60 * self.s)
        minute_angle = (6 * self.m) + (0.1 * self.s)
        second_angle = 6 * self.s
        angle_hm = min(abs(hour_angle - minute_angle), 360 - abs(hour_angle - minute_angle))
        angle_ms = min(abs(minute_angle - second_angle), 360 - abs(minute_angle - second_angle))
        angle_hs = min(abs(hour_angle - second_angle), 360 - abs(hour_angle - second_angle))
        return angle_hm, angle_ms, angle_hs

    def _select_trig(self, angle):
        import math
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

    def _proc(self, data, encrypt):
        angle_hm, angle_ms, angle_hs = self._get_angles()
        result = bytearray()
        h, m, s = self.h, self.m, self.s
        for idx, b in enumerate(data):
            factor = (h + m + s + idx + 1) or 1
            offset = (
                self._select_trig(angle_hm) +
                self._select_trig(angle_ms) +
                self._select_trig(angle_hs)
            ) * factor + (idx + 1)
            if encrypt:
                val = (b + int(round(offset))) % 256
            else:
                val = (b - int(round(offset))) % 256
            result.append(val)
        return bytes(result)

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
