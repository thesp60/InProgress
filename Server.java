package biz.burse;  

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.*;
import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;
import javax.net.*;
import java.net.ServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//import org.apache.activemq.broker.*;

public class Server {
    public static void main(final String[] args) throws Exception {

        // Initiate source connection - shelter
        InetAddress shelterAddress=null;
        int port =0; 
        try (BufferedReader reader = new BufferedReader(new FileReader("sharedInfo.cfg"))) {
            final String[] hostPort = reader.readLine().split(" ");
            final byte[] IP = new byte[4];
            final String[] input = hostPort[0].split(".");
            for (int i = 0; i < IP.length; i++) {
                IP[i] = Byte.valueOf(input[i]);
            }
            shelterAddress = InetAddress.getByAddress(IP);
            port = Integer.valueOf(hostPort[1]);

        } catch (final FileNotFoundException e) {
            System.out.println("no file" + e);
        } catch (final IOException e) {
            System.out.println("system issue" + e);
        }

        if (port == 0 || shelterAddress == null) {
            throw new IllegalArgumentException();
        }

        // try (Socket connection =new Socket("localhost", port) ){
        try (Socket connection = new Socket(shelterAddress, port)) {

        } catch (final IOException e) {

        }

        // Copy data from shelter to internal Queue
        final Deque<byte[]> queue = new ArrayDeque<>();

        //Await incoming connections
         class NetworkService implements Runnable {
            private final ServerSocket serverSocket;
            private final ExecutorService pool;
         
            public NetworkService(final int port, final int poolSize) throws IOException {
                serverSocket = new ServerSocket(port);
                pool = Executors.newFixedThreadPool(poolSize);
            }

            public void run() { // run the service
                try {
                    while(true) {
                        pool.execute(new Handler(serverSocket.accept()));
                    }
                } catch (final IOException ex) {
                    pool.shutdown();
                }
            }

            class Handler implements Runnable {
                private final Socket socket;

                Handler(final Socket socket) {
                    this.socket = socket;
                }

                public void run() {
                    // read and service request on socket
                    System.out.println(Thread.currentThread().getId());
                }
            }
        }

         

        final NetworkService site = new NetworkService(6000, 20);
        site.run();
        


        //connect incoming connection to Queue

        
    }
}