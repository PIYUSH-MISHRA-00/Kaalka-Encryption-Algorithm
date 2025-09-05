# Kaalka Envelope Schema (Time-First Protocol)

**WARNING: This protocol is experimental and for research/demo only. See README.md.**

## Envelope JSON Fields
- `ver`: Protocol version (int)
- `sb`: Sender beacon (base64, 32 bytes)
- `pp`: Pulse pub (base64, 32 bytes)
- `bt`: Beat time (ISO8601, ms precision)
- `seq`: Sequence number (int)
- `aad`: Additional authenticated data (base64)
- `ct`: Ciphertext (base64)
- `seal`: Integrity tag (base64)

## Canonical Byte Layout for Seal Input
See canonical_bytes_for_mac in time_protocol.py for exact layout.

## Resonance & Weave Derivation
- Resonance: sha256(pulse_pub || sundial_secret), then Drum("resonance", beat_time="0000-00-00T00:00:00.000Z")
- Weave: sha256(resonance || beat_time_iso), then Drum("weave:1", beat_time=beat_time_iso)

## Time Tolerance
- Default: 120 seconds

## Example Envelope
```
{
  "ver": 1,
  "sb": "...base64...",
  "pp": "...base64...",
  "bt": "2025-09-05T12:34:56.789Z",
  "seq": 1,
  "aad": "...base64...",
  "ct": "...base64...",
  "seal": "...base64..."
}
```
