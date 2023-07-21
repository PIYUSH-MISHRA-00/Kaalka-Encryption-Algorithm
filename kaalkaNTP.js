const ntpClient = require("ntplib");
const Kaalka = require("./kaalka");

class KaalkaNTP extends Kaalka {
  constructor() {
    super(); // Call the constructor of the base class (Kaalka)
    this.second = 0;
    this._updateTimestamp();
  }

  _updateTimestamp() {
    try {
      ntpClient.getNetworkTime("pool.ntp.org", 123).then((time) => {
        const timestamp = time.tx.raw();
        this.second = Math.floor(timestamp % 60);
      }).catch((err) => {
        console.error("Error fetching NTP time:", err);
        // Fallback to system time if NTP fails
        const timestamp = Date.now();
        this.second = Math.floor(timestamp / 1000) % 60;
      });
    } catch (error) {
      console.error("Error fetching NTP time:", error);
      // Fallback to system time if NTP fails
      const timestamp = Date.now();
      this.second = Math.floor(timestamp / 1000) % 60;
    }
  }

  encrypt(data) {
    const encryptedMessage = this._encryptMessage(data);
    return encryptedMessage;
  }

  decrypt(encryptedMessage) {
    const decryptedMessage = this._decryptMessage(encryptedMessage);
    return decryptedMessage;
  }

  _encryptMessage(data) {
    const asciiValues = [...data].map((char) => char.charCodeAt(0));
    const encryptedValues = asciiValues.map((val) =>
      this._applyTrigonometricFunction(val)
    );
    const encryptedMessage = encryptedValues.map((val) => String.fromCharCode(val)).join("");
    return encryptedMessage;
  }

  _decryptMessage(encryptedMessage) {
    const revAscii = [...encryptedMessage].map((char) => char.charCodeAt(0));
    const decryptedValues = revAscii.map((val) =>
      this._applyInverseFunction(val)
    );
    const decryptedMessage = decryptedValues.map((val) => String.fromCharCode(val)).join("");
    return decryptedMessage;
  }

  _applyTrigonometricFunction(value) {
    const quadrant = this._determineQuadrant(this.second);
    switch (quadrant) {
      case 1:
        return Math.round(value + Math.sin(this.second));
      case 2:
        return Math.round(value + 1 / Math.tan(this.second));
      case 3:
        return Math.round(value + Math.cos(this.second));
      case 4:
        return Math.round(value + Math.tan(this.second));
      default:
        return value; // In case of an invalid quadrant, do not modify the value.
    }
  }

  _applyInverseFunction(value) {
    const quadrant = this._determineQuadrant(this.second);
    switch (quadrant) {
      case 1:
        return Math.round(value - Math.sin(this.second));
      case 2:
        return Math.round(value - 1 / Math.tan(this.second));
      case 3:
        return Math.round(value - Math.cos(this.second));
      case 4:
        return Math.round(value - Math.tan(this.second));
      default:
        return value; // In case of an invalid quadrant, do not modify the value.
    }
  }

  _determineQuadrant(second) {
    if (0 <= second && second <= 15) {
      return 1;
    } else if (16 <= second && second <= 30) {
      return 2;
    } else if (31 <= second && second <= 45) {
      return 3;
    } else {
      return 4;
    }
  }
}

module.exports = KaalkaNTP;
