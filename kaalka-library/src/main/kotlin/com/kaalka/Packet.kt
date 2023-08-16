package com.kaalka

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class Packet(private val data: String) {

    fun sendAndReceiveData() {
        val kaalka = Kaalka()
        val kaalkaNTP = KaalkaNTP()
        val timestamp = kaalkaNTP.getCurrentTime()
        val encryptedData = kaalka.encrypt(data, timestamp)

        val serverAddress = InetAddress.getByName("127.0.0.1")
        val serverPort = 12345
        val sendData = encryptedData.toByteArray()
        val sendSocket = DatagramSocket()
        val sendPacket = DatagramPacket(sendData, sendData.size, serverAddress, serverPort)
        sendSocket.send(sendPacket)
        sendSocket.close()

        println("Packet: Sent encrypted data to server.")

        val receiveSocket = DatagramSocket(serverPort)
        val receiveData = ByteArray(1024)
        val receivePacket = DatagramPacket(receiveData, receiveData.size)
        receiveSocket.receive(receivePacket)
        receiveSocket.close()

        val receivedMessage = String(receivePacket.data).trim()
        println("Packet: Received encrypted data from server: $receivedMessage")

        val decryptedMessage = kaalka.decrypt(receivedMessage, timestamp)
        println("Packet: Decrypted data from server: $decryptedMessage")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val packet = Packet("Hello, Kaalka!")
            packet.sendAndReceiveData()
        }
    }
}
