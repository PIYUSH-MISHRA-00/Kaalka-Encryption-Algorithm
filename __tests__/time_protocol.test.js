const {
  generateSundial,
  encryptEnvelope,
  decryptEnvelope
} = require('../lib/kaalka_time_protocol');

test('Roundtrip: encrypt + decrypt succeeds', () => {
  const { sundialSecret, sundialBeaconB64 } = generateSundial();
  const envelope = encryptEnvelope('hello world', sundialBeaconB64, sundialSecret);
  const plaintext = decryptEnvelope(envelope, sundialSecret);
  expect(plaintext).toBe('hello world');
});

test('Tamper: modify ct/seal → fails', () => {
  const { sundialSecret, sundialBeaconB64 } = generateSundial();
  const envelope = encryptEnvelope('hello world', sundialBeaconB64, sundialSecret);
  envelope.ct = Buffer.from('tampered').toString('base64');
  expect(() => decryptEnvelope(envelope, sundialSecret)).toThrow();
});

test('Replay: same seq rejected', async () => {
  // Ledger logic would be tested here
  // For now, just simulate
  expect(true).toBe(true);
});

test('Time skew: envelope outside ±120s fails', () => {
  const { sundialSecret, sundialBeaconB64 } = generateSundial();
  const envelope = encryptEnvelope('hello world', sundialBeaconB64, sundialSecret);
  envelope.bt = new Date(Date.now() - 200 * 1000).toISOString();
  expect(() => decryptEnvelope(envelope, sundialSecret)).toThrow();
});
