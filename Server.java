//package biz.burse;  

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.*;
import java.nio.file.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.util.ArrayDeque;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//import org.apache.activemq.broker.*;


public class Server {
    private static final String CONFIG_FILE = "SharedInfo.cfg";

    public static void main(final String[] args) throws Exception {
        
        final Map<Integer, Socket> connections = new HashMap<>();

        // check for configuration file
        try {
            if (Files.notExists(FileSystems.getDefault().getPath(CONFIG_FILE).toAbsolutePath())) {
                Files.createFile(FileSystems.getDefault().getPath(CONFIG_FILE).toAbsolutePath());
                System.out.println("Created Example Configuration File");
            }
        } catch (final FileNotFoundException e) {
            System.out.println("no file" + e);
        }
        
         // Initiate source connection - shelter 
         try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
            while(reader.readLine()!=null){
                if(reader.readLine()==null){
                    break;
                }
                final String[] hostPort = reader.readLine().split(" ");
                //final byte[] IP = new byte[4];

                final String IP = hostPort[0];        
         
                //final InetAddress shelterAddress = InetAddress.getByAddress(IP);
                System.out.println("ip format") ;
                final int port = Integer.valueOf(hostPort[1]);

                try{ 
                    Socket connection =new Socket(IP, port);
                    connections.put(port, connection);
                    System.out.println("out");
                    
                } catch (final IOException e){ 
                    System.out.println("Connection Not Estiblished, Bad Destination IP or Destination Port"); 
                }
            }
        } catch (IOException e){
            System.out.println(e);
        }
         
         System.out.println("Successfull Setup");

        try{
            for(Map.Entry<Integer, Socket> connection : connections.entrySet()){
                final NetworkService site = new NetworkService(connection.getKey(), 20, connection.getValue()); // port 6000 with 20 threads
                site.run();
            }

            // try (Socket connection = new Socket(shelterAddress, port)) {

            // Copy data from shelter to internal Queue
            final Deque<byte[]> queue = new ArrayDeque<>();

            // connect incoming connection to Topic
            // BrokerService broker = BrokerFactory.createBroker("Test");

            // Await incoming connections
            //final NetworkService site = new NetworkService(6000, 20, connection); // port 6000 with 20 threads
            //site.run();

        } catch (final IOException e) {
            System.out.println("Port down");
        }

    }

    private static class NetworkService implements Runnable {
        private final ServerSocket serverSocket;
        private final ExecutorService pool;
        private final Socket connection;

        public NetworkService(final int port, final int poolSize, final Socket connection) throws IOException {
            serverSocket = new ServerSocket(port);
            pool = Executors.newFixedThreadPool(poolSize);
            this.connection = connection;
        }

        public void run() { // run the service
            try {
                while (true) {
                    System.out.println("Opening Connection "+ serverSocket.getLocalPort());
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
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    while (true) {
                        final String temp = in.readLine();
                        out.println(temp);
                        out.write(temp);
                        System.out.println(temp);
                    }

                } catch (final Exception e) {

                    }
                    
                    System.out.println(Thread.currentThread().getId());
                }
            }
        }
}