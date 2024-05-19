package zad3.tcp.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;



public class TCPServer {
    public static final int port = Integer.parseInt(System.getenv("TCP_Server"));

    public static void main(String[] args) {
        CounterWrapper counter = new CounterWrapper();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                new Thread(new ContinueCommunication(serverSocket.accept(), counter)).start();
            }
        } catch (IOException ignore) {}
    }
}

class CounterWrapper{
    Integer counter = 0;
    public synchronized void increment(){
        counter++;
        System.out.println(counter);
    }
    public synchronized int read(){return counter;}
}

class ContinueCommunication implements Runnable {
    private final Socket socket;
    private  final CounterWrapper counter;
    
    ContinueCommunication(Socket socket, CounterWrapper counter) {
        this.counter=counter;
        this.socket = socket;
    }

    @Override
    public void run() {
        try(Scanner in = new Scanner(socket.getInputStream())){
            String msg = in.nextLine();

            if(!msg.equals("login")){
                socket.close();
                return;
            }

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("logged in");
            counter.increment();

            while (!socket.isClosed()){
                if(!in.hasNextLine())
                    continue;

                msg = in.nextLine();
                counter.increment();

                if(msg.equals("logout")){
                    writer.println("logged out");
                    socket.close();
                }else{
                    writer.println(msg);
                }
            }

        }catch (IOException ignore){}
    }
}

