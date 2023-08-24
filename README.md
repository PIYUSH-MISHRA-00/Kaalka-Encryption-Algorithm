# Kaalka Package for Javascript

* Based upon the Kaalka Encryption Algorithm

* For using the node package for Kaalka just run the following into the terminal
```
npm i kaalka
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
