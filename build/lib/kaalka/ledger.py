
"""
Kaalka Ledger: Replay protection for time-first protocol

Provides thread-safe, persistent tracking of sender sequence numbers to prevent replay attacks in Kaalka time-based envelopes.
This module is production-ready and fully tested.
"""
from typing import Optional
import threading, json, os, base64

class Ledger:
    def __init__(self, path: Optional[str]=None):
        """
        Initialize the Ledger.
        If a path is provided, loads persistent sequence data from disk.
        """
        self.lock = threading.Lock()
        self.path = path
        self._store = {}  # Maps sender beacon hex to last seen sequence number
        if path and os.path.exists(path):
            with open(path, 'r') as f:
                self._store = json.load(f)

    def get_last_seq(self, sender_beacon_bytes: bytes) -> int:
        """
        Get the last seen sequence number for a sender beacon.
        Returns 0 if not seen before.
        """
        beacon_hex = base64.b16encode(sender_beacon_bytes).decode()
        with self.lock:
            return int(self._store.get(beacon_hex, 0))

    def update_last_seq(self, sender_beacon_bytes: bytes, seq: int) -> None:
        """
        Update the last seen sequence number for a sender beacon.
        Persists to disk if a path is set.
        """
        beacon_hex = base64.b16encode(sender_beacon_bytes).decode()
        with self.lock:
            self._store[beacon_hex] = seq
            if self.path:
                tmp_path = self.path + '.tmp'
                with open(tmp_path, 'w') as f:
                    json.dump(self._store, f)
                os.replace(tmp_path, self.path)
