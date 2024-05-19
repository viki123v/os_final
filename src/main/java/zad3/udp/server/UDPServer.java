package zad3.udp.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {
    public static final int port = Integer.parseInt(System.getenv("UDP_Server"));

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            while (true) {
                byte[] buf = new byte[256];
                
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                
                new Thread(new RespondingWorker(socket, packet)).start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}

class RespondingWorker implements Runnable {
    private final DatagramSocket server;
    private final DatagramPacket packet;

    RespondingWorker(DatagramSocket server, DatagramPacket packet) {
        this.server = server;
        this.packet = packet;
    }

    @Override
    public void run() {
        String msg = new String(packet.getData());
        String response;

        switch (msg) {
            case "login":
                response = "logged in";
                break;
            case "logout":
                response = "logged out";
            default:
                response = msg;
        }

        System.out.println("Recieved from:" + packet.getAddress().getHostName());

        try {
            server.send(new DatagramPacket(
                    response.getBytes(),
                    response.getBytes().length,
                    packet.getAddress(),
                    packet.getPort()
            ));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

