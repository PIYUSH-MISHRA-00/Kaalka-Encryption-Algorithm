/**
 * Kaalka Ledger: JSON-backed replay protection store
 * SECURITY WARNING: Kaalka is a custom, time-based cipher. This wrapper uses Kaalka Drum calls only and is experimental. Do NOT use in production without a formal cryptographic audit.
 */

const fs = require('fs').promises;
const path = require('path');

class Ledger {
  constructor(file = 'kaalka_ledger.json') {
    this.file = path.resolve(file);
    this.store = {};
  }

  async load() {
    try {
      const data = await fs.readFile(this.file, 'utf8');
      this.store = JSON.parse(data);
    } catch {
      this.store = {};
    }
  }

  async save() {
    await fs.writeFile(this.file, JSON.stringify(this.store, null, 2));
  }

  async checkReplay(senderBeacon, seq, beatTimeIso) {
    await this.load();
    const key = `${senderBeacon}:${seq}`;
    if (this.store[key] && this.store[key] === beatTimeIso) {
      throw new Error('Replay detected');
    }
    this.store[key] = beatTimeIso;
    await this.save();
  }
}

module.exports = Ledger;
