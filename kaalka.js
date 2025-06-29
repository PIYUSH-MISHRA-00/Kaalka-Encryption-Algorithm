const math = require("mathjs");

class Kaalka {
  constructor() {
    this.h = 0;
    this.m = 0;
    this.s = 0;
    this._updateTimestamp();
  }

  _updateTimestamp() {
    const now = new Date();
    this.h = now.getHours() % 12;
    this.m = now.getMinutes();
    this.s = now.getSeconds();
  }

  _parseTime(timeStr) {
    if (typeof timeStr === "number") {
      return { h: 0, m: 0, s: timeStr };
    } else if (typeof timeStr === "string") {
      const parts = timeStr.split(":").map(Number);
      if (parts.length === 3) {
        return { h: parts[0] % 12, m: parts[1], s: parts[2] };
      } else if (parts.length === 2) {
        return { h: 0, m: parts[0], s: parts[1] };
      } else if (parts.length === 1) {
        return { h: 0, m: 0, s: parts[0] };
      } else {
        throw new Error("Invalid time format. Use HH:MM:SS, MM:SS, or SS.");
      }
    } else {
      throw new Error("Invalid time format. Use HH:MM:SS, MM:SS, or SS.");
    }
  }

  encrypt(data, timeKey = null) {
    if (timeKey !== null && typeof timeKey !== "undefined") {
      const { h, m, s } = this._parseTime(timeKey);
      this.h = h;
      this.m = m;
      this.s = s;
    } else {
      this._updateTimestamp();
    }
    return this._encryptMessage(data);
  }

  decrypt(encryptedMessage, timeKey = null) {
    if (timeKey !== null && typeof timeKey !== "undefined") {
      const { h, m, s } = this._parseTime(timeKey);
      this.h = h;
      this.m = m;
      this.s = s;
    } else {
      this._updateTimestamp();
    }
    return this._decryptMessage(encryptedMessage);
  }

  _getAngles() {
    const hour_angle =
      30 * this.h + 0.5 * this.m + (0.5 / 60) * this.s;
    const minute_angle = 6 * this.m + 0.1 * this.s;
    const second_angle = 6 * this.s;
    const angle_hm = Math.min(
      Math.abs(hour_angle - minute_angle),
      360 - Math.abs(hour_angle - minute_angle)
    );
    const angle_ms = Math.min(
      Math.abs(minute_angle - second_angle),
      360 - Math.abs(minute_angle - second_angle)
    );
    const angle_hs = Math.min(
      Math.abs(hour_angle - second_angle),
      360 - Math.abs(hour_angle - second_angle)
    );
    return [angle_hm, angle_ms, angle_hs];
  }

  _selectTrig(angle) {
    const quadrant = Math.floor(angle / 90) + 1;
    const rad = math.unit(angle, "deg").toNumber("rad");
    if (quadrant === 1) {
      return math.sin(rad);
    } else if (quadrant === 2) {
      return math.cos(rad);
    } else if (quadrant === 3) {
      return math.tan(rad);
    } else {
      const tanVal = math.tan(rad);
      return tanVal !== 0 ? 1 / tanVal : 0;
    }
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

  _decryptMessage(encryptedMessage) {
    const [angle_hm, angle_ms, angle_hs] = this._getAngles();
    let decrypted = "";
    for (let idx = 0; idx < encryptedMessage.length; idx++) {
      const c = encryptedMessage[idx];
      const factor = (this.h + this.m + this.s + idx + 1) || 1;
      const offset =
        (this._selectTrig(angle_hm) +
          this._selectTrig(angle_ms) +
          this._selectTrig(angle_hs)) *
          factor +
        (idx + 1);
      decrypted += String.fromCharCode(
        (c.charCodeAt(0) - Math.round(offset) + 256) % 256
      );
    }
    return decrypted;
  }
}

module.exports = Kaalka;
