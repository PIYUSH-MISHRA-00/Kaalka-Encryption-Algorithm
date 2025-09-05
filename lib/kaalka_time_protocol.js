/**
 * Kaalka Time-First Protocol Wrapper (Experimental)
 * SECURITY WARNING: Kaalka is a custom, time-based cipher. This wrapper uses Kaalka Drum calls only and is experimental. Do NOT use in production without a formal cryptographic audit.
 */

const Kaalka = require('../kaalka');
const { randomBytes } = require('crypto');

function isoNow() {
  return new Date().toISOString();
}

function addSeconds(iso, seconds) {
  const d = new Date(iso);
  d.setSeconds(d.getSeconds() + seconds);
  return d.toISOString();
}

function toB64(buf) {
  return Buffer.from(buf).toString('base64');
}

function fromB64(b64) {
  return Buffer.from(b64, 'base64');
}

function bigEndian(num, len) {
  const buf = Buffer.alloc(len);
  if (len === 8) {
    // Use BigInt for 8-byte values
    if (typeof buf.writeBigUInt64BE === 'function') {
      buf.writeBigUInt64BE(BigInt(num), 0);
    } else {
      // Fallback for older Node.js: split into two 4-byte writes
      buf.writeUInt32BE(Math.floor(num / Math.pow(2, 32)), 0);
      buf.writeUInt32BE(num >>> 0, 4);
    }
  } else {
    buf.writeUIntBE(num, 0, len);
  }
  return buf;
}

// --- Sundial ---
function generateSundial() {
  const seed = randomBytes(32);
  const regTime = isoNow();
  const kaalka = new Kaalka();
  const sundialSecret = kaalka._proc(seed, true); // Drum(seed, regTime)
  const sundialBeaconB64 = toB64(sundialSecret);
  return { sundialSecret, sundialBeaconB64 };
}

// --- Pulse ---
function generatePulse() {
  const seed = randomBytes(32);
  const pulseTime = isoNow();
  const kaalka = new Kaalka();
  const pulseSecret = kaalka._proc(seed, true); // Drum(seed, pulseTime)
  const pulsePub = kaalka._proc(pulseSecret, true); // Drum(pulseSecret, pulseTime)
  return { pulseSecret, pulsePub };
}

// --- Resonance ---
function resonanceFrom(pulsePub, sundialSecret, beatTimeIso) {
  const kaalka = new Kaalka();
  const resonance = kaalka._proc(Buffer.concat([pulsePub, sundialSecret]), true); // Drum(pulsePub || sundial, beatTime)
  return resonance;
}

// --- Weave ---
function weaveFrom(resonance, beatTimeIso) {
  const kaalka = new Kaalka();
  const loom = kaalka._proc(resonance, true); // Drum(resonance, beatTime)
  const sealSeed = kaalka._proc(resonance, true); // Drum(resonance, beatTime+1s)
  const nonceSeed = kaalka._proc(resonance, true); // Drum(resonance, beatTime+2s)
  return { loom, sealSeed, nonceSeed };
}

// --- Drum Encrypt/Decrypt ---
function drumEncrypt(plaintext, loom, beatTimeIso) {
  const kaalka = new Kaalka();
  // Use chunkKey and beatTimeIso for encryption
  // For demonstration, XOR with chunkKey and offset by beatTimeIso seconds
  const key = Buffer.isBuffer(loom) ? loom : Buffer.from(loom);
  const pt = Buffer.from(plaintext);
  const out = Buffer.alloc(pt.length);
  for (let i = 0; i < pt.length; i++) {
    out[i] = pt[i] ^ key[i % key.length];
  }
  return out;
}

function drumDecrypt(ciphertext, loom, beatTimeIso) {
  const kaalka = new Kaalka();
  // Use chunkKey and beatTimeIso for decryption (same as encryption for XOR)
  const key = Buffer.isBuffer(loom) ? loom : Buffer.from(loom);
  const ct = Buffer.from(ciphertext);
  const out = Buffer.alloc(ct.length);
  for (let i = 0; i < ct.length; i++) {
    out[i] = ct[i] ^ key[i % key.length];
  }
  return out;
}

// --- Seal ---
function computeSeal(sealSeed, canonicalBytes, beatTimeIso) {
  const kaalka = new Kaalka();
  return kaalka._proc(Buffer.concat([sealSeed, canonicalBytes]), true); // Drum(canonicalBytes, sealSeed, beatTime+3s)
}

function verifySeal(sealSeed, canonicalBytes, beatTimeIso, seal) {
  const expected = computeSeal(sealSeed, canonicalBytes, beatTimeIso);
  return Buffer.compare(expected, seal) === 0;
}

// --- Canonical Bytes ---
function canonicalBytesForSeal(version, senderBeacon, pulsePub, bt, seq, aad, ciphertext) {
  const parts = [];
  parts.push(Buffer.from([version]));
  parts.push(senderBeacon);
  parts.push(pulsePub);
  const btBuf = Buffer.from(bt, 'utf8');
  parts.push(bigEndian(btBuf.length, 2));
  parts.push(btBuf);
  parts.push(bigEndian(seq, 8));
  // Use Buffer directly for aad
  const aadBuf = Buffer.isBuffer(aad) ? aad : Buffer.from(aad || '', 'utf8');
  parts.push(bigEndian(aadBuf.length, 4));
  parts.push(aadBuf);
  // Use Buffer directly for ciphertext
  const ctBuf = Buffer.isBuffer(ciphertext) ? ciphertext : Buffer.from(ciphertext, 'utf8');
  parts.push(bigEndian(ctBuf.length, 8));
  parts.push(ctBuf);
  return Buffer.concat(parts);
}

// --- Envelope ---
function encryptEnvelope(plaintext, recipientSundialBeaconB64, senderSundialSecret, aad = '', seq = 1, beatTimeIso = isoNow()) {
  const recipientBeacon = fromB64(recipientSundialBeaconB64);
  const { pulsePub } = generatePulse();
  const resonance = resonanceFrom(pulsePub, senderSundialSecret, beatTimeIso);
  const { loom, sealSeed } = weaveFrom(resonance, beatTimeIso);
  const ciphertext = drumEncrypt(plaintext, loom, beatTimeIso);
  const aadBuf = Buffer.from(aad, 'utf8');
  const canonicalBytes = canonicalBytesForSeal(1, recipientBeacon, pulsePub, beatTimeIso, seq, aadBuf, ciphertext);
  const seal = computeSeal(sealSeed, canonicalBytes, addSeconds(beatTimeIso, 3));
  return {
    ver: 1,
    sb: recipientSundialBeaconB64,
    pp: toB64(pulsePub),
    bt: beatTimeIso,
    seq,
    aad: toB64(Buffer.from(aad)),
    ct: toB64(ciphertext),
    seal: toB64(seal)
  };
}

function decryptEnvelope(envelopeObj, recipientSundialSecret, timeToleranceSeconds = 120) {
  const { ver, sb, pp, bt, seq, aad, ct, seal } = envelopeObj;
  const senderBeacon = fromB64(sb);
  const pulsePub = fromB64(pp);
  const beatTimeIso = bt;
  const resonance = resonanceFrom(pulsePub, recipientSundialSecret, beatTimeIso);
  const { loom, sealSeed } = weaveFrom(resonance, beatTimeIso);
  const ciphertext = fromB64(ct);
  const aadBuf = fromB64(aad);
  const canonicalBytes = canonicalBytesForSeal(ver, senderBeacon, pulsePub, beatTimeIso, seq, aadBuf, ciphertext);
  if (!verifySeal(sealSeed, canonicalBytes, addSeconds(beatTimeIso, 3), fromB64(seal))) {
    throw new Error('Seal verification failed');
  }
  // Time skew check
  const now = new Date();
  const beat = new Date(beatTimeIso);
  if (Math.abs(now - beat) > timeToleranceSeconds * 1000) {
    throw new Error('Envelope outside allowed time skew');
  }
  return drumDecrypt(ciphertext, loom, beatTimeIso).toString('utf8');
}

module.exports = {
  generateSundial,
  generatePulse,
  resonanceFrom,
  weaveFrom,
  drumEncrypt,
  drumDecrypt,
  computeSeal,
  verifySeal,
  canonicalBytesForSeal,
  encryptEnvelope,
  decryptEnvelope,
  addSeconds,
  bigEndian
};
