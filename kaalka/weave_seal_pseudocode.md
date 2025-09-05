# Kaalka Time-First Protocol: Weave + Seal Derivation (Pseudocode)

**All steps use only the Kaalka Drum primitive and time. No external crypto.**

---

## Weave Derivation

Given: `resonance` (bytes), `beat_time_iso` (str)

```
loom      = Drum(resonance, beat_time=beat_time_iso)
seal_seed = Drum(resonance, beat_time=beat_time_iso_plus_1s)
nonce_seed= Drum(resonance, beat_time=beat_time_iso_plus_2s)
```

- `beat_time_iso_plus_1s` means increment the time by 1 second (ISO8601 format).
- All outputs are raw bytes from Drum.

## Seal Derivation

Given: `ciphertext` (bytes), `aad` (bytes), `seq` (int), `seal_seed` (bytes), `beat_time_iso` (str)

```
seal_input = concat(ciphertext, aad, seq_bytes)
seal = Drum(seal_input, key=seal_seed, beat_time=beat_time_iso_plus_3s)
```

- `seq_bytes` is the big-endian encoding of the sequence number.
- `beat_time_iso_plus_3s` means increment the time by 3 seconds.
- Seal is the raw output bytes from Drum (truncate/pad as needed).

## Example Usage in Envelope

- Generate Pulse: `pulse = Drum(random_seed, beat_time=current_time)`
- Resonance: `resonance = Drum(pulse || sundial, beat_time=current_time)`
- Weave: as above
- Encrypt: `ciphertext = Drum(plaintext, key=loom, beat_time=current_time)`
- Seal: as above

---

**All protocol steps are deterministic and time-bound. No external crypto is used.**
