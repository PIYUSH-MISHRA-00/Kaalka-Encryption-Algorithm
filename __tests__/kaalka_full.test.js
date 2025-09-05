const Kaalka = require('../kaalka');
const KaalkaNTP = require('../kaalkaNTP');
const {
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
  decryptEnvelope
} = require('../lib/kaalka_time_protocol');
const Ledger = require('../lib/ledger');
const { encryptFile, decryptFile } = require('../lib/file_weave');
const fs = require('fs').promises;
const path = require('path');

jest.setTimeout(120000);

describe('Kaalka Full Project Test Suite', () => {
  const testText = 'Hello, Kaalka!';
  const testTime = '14:35:22';
  const testFile = path.join(__dirname, 'test.txt');
  const testBinFile = path.join(__dirname, 'test.bin');
  const testLargeFile = path.join(__dirname, 'large.txt');

  beforeAll(async () => {
    await fs.writeFile(testFile, testText);
    await fs.writeFile(testBinFile, Buffer.from([1,2,3,4,5,6,7,8,9,10]));
    await fs.writeFile(testLargeFile, 'A'.repeat(2 * 1024 * 1024)); // 2MB
  });

  afterAll(async () => {
    const files = [testFile, testBinFile, testLargeFile,
      testFile.replace('.txt', '.kaalka'), testBinFile.replace('.bin', '.kaalka'),
      testLargeFile.replace('.txt', '.kaalka'),
      testFile, testBinFile, testLargeFile,
      testFile.replace('.kaalka', '.txt'), testBinFile.replace('.kaalka', '.bin'),
      testLargeFile.replace('.kaalka', '.txt')];
    for (const f of files) {
      try { await fs.unlink(f); } catch {}
    }
  });

  it('Kaalka core: encrypts and decrypts text (system time)', async () => {
    const k = new Kaalka();
    const enc = await k.encrypt(testText);
    const dec = await k.decrypt(enc);
    expect(typeof enc).toBe('string');
    expect(typeof dec).toBe('string');
    expect(dec.length).toBe(testText.length);
  });

  it('Kaalka core: encrypts and decrypts text (explicit time)', async () => {
    const k = new Kaalka();
    const enc = await k.encrypt(testText, testTime);
    const dec = await k.decrypt(enc, testTime);
    expect(dec).toBe(testText);
  });

  it('Kaalka core: encrypts and decrypts a file (explicit time)', async () => {
    const k = new Kaalka();
    const encFile = await k.encrypt(testFile, testTime);
    expect(encFile.endsWith('.kaalka')).toBe(true);
    const decFile = await k.decrypt(encFile, testTime);
    const txt = await fs.readFile(decFile, 'utf8');
    expect(txt.trim()).toBe(testText);
  });

  it('KaalkaNTP: encrypts and decrypts text', async () => {
    const kntp = new KaalkaNTP();
    const enc = await kntp.encrypt(testText, testTime);
    const dec = await kntp.decrypt(enc, testTime);
    expect(dec).toBe(testText);
  });

  it('Time protocol: envelope roundtrip', () => {
    const { sundialSecret, sundialBeaconB64 } = generateSundial();
    const envelope = encryptEnvelope(testText, sundialBeaconB64, sundialSecret);
    const plaintext = decryptEnvelope(envelope, sundialSecret);
    expect(plaintext).toBe(testText);
  });

  it('Time protocol: tamper detection', () => {
    const { sundialSecret, sundialBeaconB64 } = generateSundial();
    const envelope = encryptEnvelope(testText, sundialBeaconB64, sundialSecret);
    envelope.ct = Buffer.from('tampered').toString('base64');
    expect(() => decryptEnvelope(envelope, sundialSecret)).toThrow();
  });

  it('Ledger: replay protection', async () => {
    const ledger = new Ledger('test_ledger.json');
    await ledger.load();
    await ledger.checkReplay('beacon', 1, '2025-09-05T12:00:00.000Z');
    await expect(ledger.checkReplay('beacon', 1, '2025-09-05T12:00:00.000Z')).rejects.toThrow('Replay detected');
    await fs.unlink('test_ledger.json');
  });

  it('FileWeave: chunked file encryption/decryption', async () => {
    // Simulate resonance and beatTimeIso
    const { sundialSecret, sundialBeaconB64 } = generateSundial();
    const { pulsePub } = generatePulse();
    const resonance = resonanceFrom(pulsePub, sundialSecret, '2025-09-05T12:00:00.000Z');
    await encryptFile(testLargeFile, resonance, '2025-09-05T12:00:00.000Z', 'manifest.json');
    await decryptFile('large_out.txt', resonance, '2025-09-05T12:00:00.000Z', 'manifest.json');
    const orig = await fs.readFile(testLargeFile);
    const out = await fs.readFile('large_out.txt');
    expect(orig.equals(out)).toBe(true);
    await fs.unlink('manifest.json');
    await fs.unlink('large_out.txt');
    for (let i = 0; i < 2; i++) {
      try { await fs.unlink(`${testLargeFile}.chunk${i}`); } catch {}
    }
  });
});
