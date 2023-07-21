# Kaalka Package

* Based upon the Kaalka Encryption Algorithm

* File Structure

```
kaalka/
  ├── kaalka.js
  ├── kaalkaNTP.js
  ├── test/
  │   ├── kaalka.test.js
  │   ├── kaalkaNTP.test.js
  ├── package.json

```
# Exemplar Usage

```
const KaalkaNTP = require('your-library-name/kaalkaNTP');
const Packet = require('your-library-name/packet');

const kaalkaInstance = new KaalkaNTP();
const packet = new Packet("Hello, Kaalka!");
packet.encrypt(kaalkaInstance);
console.log("Encrypted Data:", packet.encryptedData);


```