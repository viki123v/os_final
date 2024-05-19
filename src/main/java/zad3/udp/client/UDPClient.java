package zad3.udp.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    public static final int sendingPort = Integer.parseInt(System.getenv("UDP_Server"));
    public static final int recievingPort = Integer.parseInt(System.getenv("UDP_Client"));

    public static void main(String[] args) {
        String[] msgs = {
                "login",
                "my name is",
                "logout"
        };


        try (final DatagramSocket socket = new DatagramSocket(recievingPort)) {
            InetAddress addr = InetAddress.getByName("udp_server");

            for (String msg : msgs) {

                byte[] msgInBytes = msg.getBytes();

                DatagramPacket packetForSending = new DatagramPacket(
                        msgInBytes, msgInBytes.length, addr, sendingPort
                );

                System.out.println("Send");

                socket.send(packetForSending);
                new Thread(new RecieveWorker(socket)).start();
            }

            while (Thread.activeCount() > 2) ;

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
        try {
            socket.receive(packet);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
