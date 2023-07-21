const Kaalka = require('../kaalka');

describe('Kaalka', () => {
  let kaalka;

  beforeEach(() => {
    kaalka = new Kaalka();
  });

  test('encrypt and decrypt should work correctly', () => {
    const originalData = 'Hello, Kaalka!';
    const encryptedData = kaalka.encrypt(originalData);
    const decryptedData = kaalka.decrypt(encryptedData);

    expect(encryptedData).not.toBe(originalData);
    expect(decryptedData).toBe(originalData);
  });

  test('encrypt and decrypt should handle empty data', () => {
    const originalData = '';
    const encryptedData = kaalka.encrypt(originalData);
    const decryptedData = kaalka.decrypt(encryptedData);

    expect(encryptedData).toBe(originalData);
    expect(decryptedData).toBe(originalData);
  });
});
