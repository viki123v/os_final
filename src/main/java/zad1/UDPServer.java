package zad1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {
    public static void main(String[] args) {
        try(DatagramSocket socket = new DatagramSocket(2000)){
            while(true){
                byte[] buf = new byte[100];
                DatagramPacket packet = new DatagramPacket(buf,buf.length);
                socket.receive(packet);

                new Thread(new RespondingWorker(socket, packet))
                        .start();
            }
        }catch (IOException ignore){}

    }
}
class RespondingWorker implements Runnable{
    private final static int sendingPort = 2020;
    private final DatagramSocket server ;
    private final DatagramPacket packet ;
    RespondingWorker(DatagramSocket server, DatagramPacket packet){
        this.server=server;
        this.packet =packet;
    }

    @Override
    public void run() {
        String msg = new String(packet.getData());
        String response = "";

        switch (msg){
            case "login":
                response="logged in";
                break;
            case "logout":
                response="logged out";
            default: response=msg;
        }

        synchronized (server){
            try {
                server.send(new DatagramPacket(
                        response.getBytes(),
                        response.getBytes().length,
                        packet.getAddress(),
                        sendingPort
                ));
            }catch (IOException ignore){}
        }
    }
}

