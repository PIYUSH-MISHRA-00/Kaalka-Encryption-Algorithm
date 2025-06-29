const dgram = require('dgram');
const KaalkaNTP = require('./kaalkaNTP');

class Packet {
  constructor(data) {
    this.data = data;
    this.encryptedData = null;
  }

  async encrypt(kaalka) {
    if (!this.encryptedData) {
      const encryptedMessage = await kaalka.encrypt(this.data);
      this.encryptedData = encryptedMessage;
    }
  }

  async decrypt(kaalka) {
    if (this.encryptedData) {
      const decryptedMessage = await kaalka.decrypt(this.encryptedData);
      this.data = decryptedMessage;
      this.encryptedData = null;
    }
  }

  static async sender() {
    // Simulate sender preparing data
    const message = "Hello, Kaalka!";
    console.log("Original Message:", message);

    // Create a packet with the message
    const packet = new Packet(message);

    // Encrypt the packet using Kaalka algorithm
    const kaalka = new KaalkaNTP();
    await packet.encrypt(kaalka);
    console.log("Encrypted Data:", packet.encryptedData);

    // Simulate sending the encrypted data over the network
    await Packet.sendDataOverNetwork(packet.encryptedData);
  }

  static async receiver() {
    // Simulate receiving the encrypted data over the network
    const receivedData = await Packet.receiveDataOverNetwork();

    // Create a packet with the received data
    const packet = new Packet(receivedData);

    // Decrypt the packet using Kaalka algorithm
    const kaalka = new KaalkaNTP();
    await packet.decrypt(kaalka);
    console.log("Decrypted Message:", packet.data);
  }

  static async sendDataOverNetwork(data) {
    // Simulate sending data over the network (e.g., using dgram sockets)
    const socket = dgram.createSocket('udp4');
    return new Promise((resolve) => {
      socket.send(data, 0, data.length, 12345, '127.0.0.1', (err) => {
        socket.close();
        resolve();
      });
    });
  }

  static async receiveDataOverNetwork() {
    // Simulate receiving data over the network (e.g., using dgram sockets)
    const socket = dgram.createSocket('udp4');
    return new Promise((resolve) => {
      socket.on('message', (msg) => {
        socket.close();
        resolve(msg.toString());
      });
      socket.bind(12345, '127.0.0.1');
    });
  }
}

module.exports = Packet;
