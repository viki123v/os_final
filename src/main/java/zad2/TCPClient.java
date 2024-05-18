package zad2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class TCPClient {

    public static void main(String[] args) {
        final int threadCount = 3;
        String[][] msgs = {
                {"asdf"},
                {"login","logout"},
                {"login","zxcvzxcv","logout"}
        };

        for (int i = 0; i < threadCount; i++) {
            new Thread(new SenderThreads(msgs[i])).start();
        }

        while (Thread.activeCount()>2);
    }
}


class SenderThreads implements Runnable {
    String[] msgs;
    public SenderThreads(String[] msg){
        this.msgs=msg;
    }

    @Override
    public void run() {
        try(final Socket socket = new Socket(InetAddress.getLocalHost(),2000)){
            PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
            Scanner in = new Scanner(socket.getInputStream());

            for(String msg: msgs){
                writer.println(msg);

                if(socket.isClosed())
                    break;

                if(in.hasNextLine()){
                    System.out.println(in.nextLine());
                }
            }

        } catch (IOException e){
            System.out.println("This here");
        }
    }
}
