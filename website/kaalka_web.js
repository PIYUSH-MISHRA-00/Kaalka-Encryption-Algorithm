class Kaalka {
  constructor() {
    this.h = 0;
    this.m = 0;
    this.s = 0;
  }

  setTime(h, m, s) {
    this.h = h % 12;
    this.m = m;
    this.s = s;
  }

  // --- Basic Encryption ---
  encrypt(data) {
    const [angle_hm, angle_ms, angle_hs] = this._getAngles();
    let encrypted = "";
    for (let idx = 0; idx < data.length; idx++) {
      const c = data[idx];
      const factor = (this.h + this.m + this.s + idx + 1) || 1;
      const offset = (this._selectTrig(angle_hm) + this._selectTrig(angle_ms) + this._selectTrig(angle_hs)) * factor + (idx + 1);
      encrypted += String.fromCharCode((c.charCodeAt(0) + Math.round(offset)) % 256);
    }
    return btoa(encrypted);
  }

  decrypt(data) {
    try {
      const decoded = atob(data);
      const [angle_hm, angle_ms, angle_hs] = this._getAngles();
      let decrypted = "";
      for (let idx = 0; idx < decoded.length; idx++) {
        const c = decoded[idx];
        const factor = (this.h + this.m + this.s + idx + 1) || 1;
        const offset = (this._selectTrig(angle_hm) + this._selectTrig(angle_ms) + this._selectTrig(angle_hs)) * factor + (idx + 1);
        decrypted += String.fromCharCode((c.charCodeAt(0) - Math.round(offset) + 256) % 256);
      }
      return decrypted;
    } catch (e) { return "Error: Invalid Ciphertext"; }
  }

  // --- Advanced Protocol (Envelopes) ---
  _proc(data, encrypt) {
    const key = (this.h * 3600 + this.m * 60 + this.s) || 1;
    const result = new Uint8Array(data.length);
    for (let idx = 0; idx < data.length; idx++) {
      const b = data[idx];
      const offset = (key + idx) % 256;
      result[idx] = encrypt ? (b + offset) % 256 : (b - offset + 256) % 256;
    }
    return result;
  }

  static generateSundial() {
    const seed = crypto.getRandomValues(new Uint8Array(32));
    const k = new Kaalka();
    const now = new Date();
    k.setTime(now.getHours(), now.getMinutes(), now.getSeconds());
    const secret = k._proc(seed, true);
    return { secret, beacon: btoa(String.fromCharCode(...secret)) };
  }

  static encryptEnvelope(plaintext, recipientBeaconB64, senderSecret) {
    const now = new Date();
    const k = new Kaalka();
    k.setTime(now.getHours(), now.getMinutes(), now.getSeconds());
    
    // Simplified Pulse/Resonance/Loom for demo, following similar patterns
    const pulseSeed = crypto.getRandomValues(new Uint8Array(32));
    const pp = k._proc(pulseSeed, true);
    const bt = now.toISOString();
    
    // Drum Encrypt
    const ptBytes = new TextEncoder().encode(plaintext);
    const ct = k._proc(ptBytes, true);
    
    // Canonical Seal (Simplified)
    const seal = k._proc(new Uint8Array([...pp, ...ct]), true);

    return {
      ver: 1,
      sb: recipientBeaconB64,
      pp: btoa(String.fromCharCode(...pp)),
      bt: bt,
      seq: 1,
      ct: btoa(String.fromCharCode(...ct)),
      seal: btoa(String.fromCharCode(...seal))
    };
  }

  // Helper Methods
  _getAngles() {
    const hA = 30 * this.h + 0.5 * this.m + (0.5 / 60) * this.s;
    const mA = 6 * this.m + 0.1 * this.s;
    const sA = 6 * this.s;
    const diff = (a, b) => Math.min(Math.abs(a - b), 360 - Math.abs(a - b));
    return [diff(hA, mA), diff(mA, sA), diff(hA, sA)];
  }

  _selectTrig(angle) {
    const rad = (angle * Math.PI) / 180;
    const q = Math.floor(angle / 90) + 1;
    if (q === 1) return Math.sin(rad);
    if (q === 2) return Math.cos(rad);
    if (q === 3) return Math.tan(rad);
    return Math.tan(rad) !== 0 ? 1 / Math.tan(rad) : 0;
  }
}
