# kaalka/packet.py
import socket
from kaalkaNTP import KaalkaNTP

class Packet:
    def __init__(self, data: str) -> None:
        self.data = data
        self.encrypted_data = None

    def encrypt(self, kaalka: KaalkaNTP) -> None:
        if not self.encrypted_data:
            encrypted_message = kaalka.encrypt(self.data)
            self.encrypted_data = encrypted_message

    def decrypt(self, kaalka: KaalkaNTP) -> None:
        if self.encrypted_data:
            decrypted_message = kaalka.decrypt(self.encrypted_data)
            self.data = decrypted_message
            self.encrypted_data = None

    @staticmethod
    def sender():
        # Simulate sender preparing data
        message = "Hello, Kaalka!"
        print("Original Message:", message)

        # Create a packet with the message
        packet = Packet(message)

        # Encrypt the packet using Kaalka algorithm
        kaalka = KaalkaNTP()
        packet.encrypt(kaalka)
        print("Encrypted Data:", packet.encrypted_data)

        # Simulate sending the encrypted data over the network
        Packet.send_data_over_network(packet.encrypted_data)

    @staticmethod
    def receiver():
        # Simulate receiving the encrypted data over the network
        received_data = Packet.receive_data_over_network()

        # Create a packet with the received data
        packet = Packet(received_data)

        # Decrypt the packet using Kaalka algorithm
        kaalka = KaalkaNTP()
        packet.decrypt(kaalka)
        print("Decrypted Message:", packet.data)

    @staticmethod
    def send_data_over_network(data):
        # Simulate sending data over the network (e.g., using sockets)
        with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
            s.sendto(data.encode(), ('127.0.0.1', 12345))

    @staticmethod
    def receive_data_over_network():
        # Simulate receiving data over the network (e.g., using sockets)
        with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
            s.bind(('127.0.0.1', 12345))
            data, addr = s.recvfrom(1024)
            return data.decode()
