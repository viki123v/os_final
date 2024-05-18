package zad1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {

    public static void main(String[] args) {
        String[] msgs = {
                "login",
                "my name is",
                "logout"
        };


        try (final DatagramSocket socket = new DatagramSocket(2020)) {
            for (String msg : msgs) {

                byte[] msgInBytes = msg.getBytes();

                DatagramPacket packetForSending = new DatagramPacket(msgInBytes, msgInBytes.length,
                        InetAddress.getLocalHost(), 2000);

                socket.send(packetForSending);
                new Thread(new RecieveWorker(socket)).start();
            }

            while (Thread.activeCount()>2);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

class RecieveWorker implements Runnable {
    private final DatagramSocket socket;
    private final DatagramPacket packet;

    public RecieveWorker(DatagramSocket socket) {
        this.socket = socket;
        byte[] buf = new byte[100];
        packet = new DatagramPacket(buf, buf.length);
    }

    @Override
    public void run() {
        synchronized (socket) {
            try {
                socket.receive(packet);
            } catch (IOException ignore) {
            }
        }
        System.out.println(new String(packet.getData()));
    }
}
