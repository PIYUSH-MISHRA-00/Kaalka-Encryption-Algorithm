jest.setTimeout(60000);

const KaalkaNTP = require('../kaalkaNTP');

describe('KaalkaNTP', () => {
  let kaalkaNTP;
  const timestamp = "10:15:30"; // Use a fixed, valid timestamp

  beforeEach(() => {
    kaalkaNTP = new KaalkaNTP();
  });

  test('encrypt and decrypt should work correctly', async () => {
    const originalData = 'Hello, Kaalka NTP!';
    const encryptedData = await kaalkaNTP.encrypt(originalData, timestamp);
    const decryptedData = await kaalkaNTP.decrypt(encryptedData, timestamp);

    expect(encryptedData).not.toBe(originalData);
    expect(decryptedData).toBe(originalData);
  });

  test('encrypt and decrypt should handle empty data', async () => {
    const originalData = '';
    const encryptedData = await kaalkaNTP.encrypt(originalData, timestamp);
    const decryptedData = await kaalkaNTP.decrypt(encryptedData, timestamp);

    expect(encryptedData).toBe(originalData);
    expect(decryptedData).toBe(originalData);
  });
});
