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
                try(BufferedWriter writer =new BufferedWriter(new FileWriter(CONFIG_FILE))){
                    writer.append("Input Configuration in Shelter IP Shelter Port and External Port \nExample X.X.X.X 40 30000");
                }
                System.out.println("Created Example Configuration File");
                System.exit(0);
            }
        } catch (final FileNotFoundException e) {
            System.out.println("no file" + e);
        }
        
         // Initiate source connection - shelter 
         try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
             String line = "Start";
            while(line!=null){
                System.out.println(line);
                line = reader.readLine();
                if(line==null){
                    break;
                }

                if(!Character.isDigit(line.charAt(0))){ 
                    continue;
                }
                final String[] hostPort = line.split(" ");

                final String IP = hostPort[0]; 
                
                final int port = Integer.valueOf(hostPort[1]);

                try{ 
                    Socket connection =new Socket(IP, port);
                    connections.put(Integer.valueOf(hostPort[2]), connection);
                    System.out.println("out");
                    
                } catch (final IOException e){ 
                    System.out.println("Connection Not Established, Bad Destination IP or Destination Port"); 
                }
            }
        } catch (IOException e){
            System.out.println(e);
        }
         
        if(connections.size()==0){
            System.out.println("0 Shelter Connections Established");
            System.exit(0);
        }
         System.out.println("Successfull Setup");

        try{
            //create thread pool for running each new server port
            final ExecutorService pool;
            pool = Executors.newFixedThreadPool(connections.size());
            
            //create new port for each connection estiblished
            for(Map.Entry<Integer, Socket> connection : connections.entrySet()){
                System.out.println("thread start "+  connections.size());
                pool.execute(new NetworkService(connection.getKey()+1, 20, connection.getValue())); // 20 threads (20 incoming connections to server)
            }


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
                    //System.out.println("Opening Connection "+ serverSocket.getLocalPort());
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