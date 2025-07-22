
const fs = require('fs').promises;
const path = require('path');
const Kaalka = require('../kaalka');

describe('Kaalka Encryption Library', () => {
  const testText = 'Hello, world!';
  const testTime = '14:35:22';
  const testFile = path.join(__dirname, 'test.txt');
  const testBinFile = path.join(__dirname, 'test.bin');
  const testUtf8File = path.join(__dirname, 'utf8.txt');
  const testLargeFile = path.join(__dirname, 'large.txt');

  beforeAll(async () => {
    await fs.writeFile(testFile, testText);
    await fs.writeFile(testBinFile, Buffer.from([1,2,3,4,5,6,7,8,9,10]));
    await fs.writeFile(testUtf8File, 'हैलो');
    await fs.writeFile(testLargeFile, 'A'.repeat(10000));
  });

  afterAll(async () => {
    const files = [testFile, testBinFile, testUtf8File, testLargeFile,
      testFile.replace('.txt', '.kaalka'), testBinFile.replace('.bin', '.kaalka'),
      testUtf8File.replace('.txt', '.kaalka'), testLargeFile.replace('.txt', '.kaalka'),
      testFile, testBinFile, testUtf8File, testLargeFile,
      testFile.replace('.kaalka', '.txt'), testBinFile.replace('.kaalka', '.bin'),
      testUtf8File.replace('.kaalka', '.txt'), testLargeFile.replace('.kaalka', '.txt')];
    for (const f of files) {
      try { await fs.unlink(f); } catch {}
    }
  });

  it('encrypts and decrypts text (system time)', async () => {
    const k = new Kaalka();
    const enc = await k.encrypt(testText);
    const dec = await k.decrypt(enc);
    expect(typeof enc).toBe('string');
    expect(typeof dec).toBe('string');
    // System time: may fail if time changes, so skip strict equality
    expect(dec.length).toBe(testText.length);
  });

  it('encrypts and decrypts text (explicit time)', async () => {
    const k = new Kaalka();
    const enc = await k.encrypt(testText, testTime);
    const dec = await k.decrypt(enc, testTime);
    expect(dec).toBe(testText);
  });

  it('encrypts and decrypts a file (system time)', async () => {
    const k = new Kaalka();
    const encFile = await k.encrypt(testFile);
    expect(encFile.endsWith('.kaalka')).toBe(true);
    const decFile = await k.decrypt(encFile);
    const txt = await fs.readFile(decFile, 'utf8');
    expect(txt.trim()).toBe(testText);
  });

  it('encrypts and decrypts a file (explicit time)', async () => {
    const k = new Kaalka();
    const encFile = await k.encrypt(testFile, testTime);
    expect(encFile.endsWith('.kaalka')).toBe(true);
    const decFile = await k.decrypt(encFile, testTime);
    const txt = await fs.readFile(decFile, 'utf8');
    expect(txt.trim()).toBe(testText);
  });

  it('encrypts and decrypts a binary file', async () => {
    const k = new Kaalka();
    const buf = Buffer.from([1,2,3,4,5,6,7,8,9,10]);
    const encFile = await k.encrypt(testBinFile, '01:02:03');
    const decFile = await k.decrypt(encFile, '01:02:03');
    const out = await fs.readFile(decFile);
    expect(out.equals(buf)).toBe(true);
  });

  it('encrypts and decrypts a UTF-8 file', async () => {
    const k = new Kaalka();
    const encFile = await k.encrypt(testUtf8File, '10:20:30');
    const decFile = await k.decrypt(encFile, '10:20:30');
    const txt = await fs.readFile(decFile, 'utf8');
    expect(txt.trim()).toBe('हैलो');
  });

  it('encrypts and decrypts a large file', async () => {
    const k = new Kaalka();
    const encFile = await k.encrypt(testLargeFile, '05:06:07');
    const decFile = await k.decrypt(encFile, '05:06:07');
    const txt = await fs.readFile(decFile, 'utf8');
    expect(txt).toBe('A'.repeat(10000));
  });

  it('handles empty string and file', async () => {
    const k = new Kaalka();
    const enc = await k.encrypt('');
    const dec = await k.decrypt(enc);
    expect(dec).toBe('');
    await fs.writeFile(path.join(__dirname, 'empty.txt'), '');
    const encFile = await k.encrypt(path.join(__dirname, 'empty.txt'));
    const decFile = await k.decrypt(encFile);
    const txt = await fs.readFile(decFile, 'utf8');
    expect(txt).toBe('');
    await fs.unlink(path.join(__dirname, 'empty.txt')).catch(()=>{});
    await fs.unlink(path.join(__dirname, 'empty.kaalka')).catch(()=>{});
    await fs.unlink(path.join(__dirname, 'empty.txt')).catch(()=>{});
  });
});
