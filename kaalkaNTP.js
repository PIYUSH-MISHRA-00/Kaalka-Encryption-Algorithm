const ntpClient = require("ntp-client");
const Kaalka = require("./kaalka");

class KaalkaNTP extends Kaalka {
  _parseTime(timeKey) {
    const parts = String(timeKey).split(":");
    let h = 0, m = 0, s = 0;
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
    return { h: h % 12, m, s };
  }
  constructor() {
    super();
    this.ntpTime = null;
  }

  _updateNtpTimestamp() {
    return new Promise((resolve, reject) => {
      ntpClient.getNetworkTime("pool.ntp.org", 123, (err, date) => {
        if (err) {
          // Fallback to system time
          super._updateTimestamp();
          resolve();
        } else {
          this.h = date.getHours() % 12;
          this.m = date.getMinutes();
          this.s = date.getSeconds();
          this.ntpTime = date;
          resolve();
        }
      });
    });
  }

  async encrypt(data, timeKey = null) {
    if (timeKey !== null && typeof timeKey !== "undefined") {
      const { h, m, s } = this._parseTime(timeKey);
      this.h = h;
      this.m = m;
      this.s = s;
    } else {
      await this._updateNtpTimestamp();
    }
    return this._encryptMessage(data);
  }

  async decrypt(encryptedMessage, timeKey = null) {
    if (timeKey !== null && typeof timeKey !== "undefined") {
      const { h, m, s } = this._parseTime(timeKey);
      this.h = h;
      this.m = m;
      this.s = s;
    } else {
      await this._updateNtpTimestamp();
    }
    return this._decryptMessage(encryptedMessage);
  }
}

module.exports = KaalkaNTP;
