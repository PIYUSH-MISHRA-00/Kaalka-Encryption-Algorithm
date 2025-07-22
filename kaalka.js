const fs = require("fs").promises;
const math = require("mathjs");
const path = require("path");

class Kaalka {
  constructor() {
    const now = new Date();
    this.h = now.getHours() % 12;
    this.m = now.getMinutes();
    this.s = now.getSeconds();
  }

  _setTime(timeKey) {
    if (timeKey) {
      const parts = String(timeKey).split(":");
      let h = 0,
        m = 0,
        s = 0;
      if (parts.length === 3) {
        h = parseInt(parts[0]);
        m = parseInt(parts[1]);
        s = parseInt(parts[2]);
      } else if (parts.length === 2) {
        m = parseInt(parts[0]);
        s = parseInt(parts[1]);
      } else if (parts.length === 1) {
        s = parseInt(parts[0]);
      }
      this.h = h % 12;
      this.m = m;
      this.s = s;
    }
  }

  async encrypt(data, timeKey) {
    if (typeof data === "string" && (await this._isFile(data))) {
      const ext = path.extname(data);
      const raw = await fs.readFile(data);
      this._setTime(timeKey);
      const encBytes = this._proc(raw, true);
      const extBytes = Buffer.from(ext, "utf8");
      const extLen = Buffer.from([extBytes.length]);
      const final = Buffer.concat([extLen, extBytes, encBytes]);
      const base = path.join(path.dirname(data), path.basename(data, ext));
      const outfile = base + ".kaalka";
      await fs.writeFile(outfile, final);
      return outfile;
    } else {
      this._setTime(timeKey);
      return this._encryptMessage(data);
    }
  }

  async decrypt(data, timeKey) {
    if (typeof data === "string" && (await this._isFile(data))) {
      const buf = await fs.readFile(data);
      const extLen = buf[0];
      const ext = buf.slice(1, 1 + extLen).toString("utf8");
      const encData = buf.slice(1 + extLen);
      this._setTime(timeKey);
      const decBytes = this._proc(encData, false);
      const base = path.join(
        path.dirname(data),
        path.basename(data, ".kaalka")
      );
      const outfile = base + ext;
      await fs.writeFile(outfile, decBytes);
      return outfile;
    } else {
      this._setTime(timeKey);
      return this._decryptMessage(data);
    }
  }

  _getAngles() {
    const hourAngle =
      30 * this.h + 0.5 * this.m + (0.5 / 60) * this.s;
    const minuteAngle = 6 * this.m + 0.1 * this.s;
    const secondAngle = 6 * this.s;
    const angle_hm = Math.min(
      Math.abs(hourAngle - minuteAngle),
      360 - Math.abs(hourAngle - minuteAngle)
    );
    const angle_ms = Math.min(
      Math.abs(minuteAngle - secondAngle),
      360 - Math.abs(minuteAngle - secondAngle)
    );
    const angle_hs = Math.min(
      Math.abs(hourAngle - secondAngle),
      360 - Math.abs(hourAngle - secondAngle)
    );
    return [angle_hm, angle_ms, angle_hs];
  }

  _selectTrig(angle) {
    const rad = (angle * Math.PI) / 180;
    const quadrant = Math.floor(angle / 90) + 1;
    if (quadrant === 1) return Math.sin(rad);
    if (quadrant === 2) return Math.cos(rad);
    if (quadrant === 3) return Math.tan(rad);
    const tanVal = Math.tan(rad);
    return tanVal !== 0 ? 1 / tanVal : 0;
  }

  _proc(data, encrypt) {
    // Use integer arithmetic for lossless, reversible byte transformation
    const h = this.h, m = this.m, s = this.s;
    const key = (h * 3600 + m * 60 + s) || 1;
    const result = [];
    for (let idx = 0; idx < data.length; idx++) {
      const b = data[idx];
      const offset = (key + idx) % 256;
      let val;
      if (encrypt) {
        val = (b + offset) % 256;
      } else {
        val = (b - offset) % 256;
      }
      result.push(val);
    }
    return Buffer.from(result);
  }

  _encryptMessage(data) {
    const [angle_hm, angle_ms, angle_hs] = this._getAngles();
    let encrypted = "";
    for (let idx = 0; idx < data.length; idx++) {
      const c = data[idx];
      const factor = (this.h + this.m + this.s + idx + 1) || 1;
      const offset =
        (this._selectTrig(angle_hm) +
          this._selectTrig(angle_ms) +
          this._selectTrig(angle_hs)) *
          factor +
        (idx + 1);
      encrypted += String.fromCharCode(
        (c.charCodeAt(0) + Math.round(offset)) % 256
      );
    }
    return encrypted;
  }

  _decryptMessage(data) {
    const [angle_hm, angle_ms, angle_hs] = this._getAngles();
    let decrypted = "";
    for (let idx = 0; idx < data.length; idx++) {
      const c = data[idx];
      const factor = (this.h + this.m + this.s + idx + 1) || 1;
      const offset =
        (this._selectTrig(angle_hm) +
          this._selectTrig(angle_ms) +
          this._selectTrig(angle_hs)) *
          factor +
        (idx + 1);
      decrypted += String.fromCharCode(
        (c.charCodeAt(0) - Math.round(offset)) % 256
      );
    }
    return decrypted;
  }

  async _isFile(p) {
    try {
      const stat = await fs.stat(p);
      return stat.isFile();
    } catch {
      return false;
    }
  }
}

module.exports = Kaalka;
