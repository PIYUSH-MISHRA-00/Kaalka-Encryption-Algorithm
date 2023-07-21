const KaalkaNTP = require('./kaalkaNTP');

describe('KaalkaNTP', () => {
  let kaalkaNTP;

  beforeEach(() => {
    kaalkaNTP = new KaalkaNTP();
  });

  test('encrypt and decrypt should work correctly', () => {
    const originalData = 'Hello, Kaalka NTP!';
    const encryptedData = kaalkaNTP.encrypt(originalData);
    const decryptedData = kaalkaNTP.decrypt(encryptedData);

    expect(encryptedData).not.toBe(originalData);
    expect(decryptedData).toBe(originalData);
  });

  test('encrypt and decrypt should handle empty data', () => {
    const originalData = '';
    const encryptedData = kaalkaNTP.encrypt(originalData);
    const decryptedData = kaalkaNTP.decrypt(encryptedData);

    expect(encryptedData).toBe(originalData);
    expect(decryptedData).toBe(originalData);
  });
});
