package biz.burse;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.*;
import java.io.*;
import java.util.Scanner;
import javax.net.*;
import java.net.ServerSocket;
import javax.net.ssl.SSLServerSocketFactory;


//import org.apache.activemq.broker.*;

public class Server {
    public static void main(final String[] args) throws Exception {

        // Initiate source connection - shelter
        //int port = 0;
        final InetAddress shelterAddress;
        int port =0; 
        try (BufferedReader reader = new BufferedReader(new FileReader("sharedInfo.cfg"))) {
            final String[] hostPort = reader.readLine().split(" ");
            byte[] IP = new byte[4];
            final String[] input = hostPort[0].split(".");
            for(int i=0;i<IP.length;i++){
                IP[i]=Integer.valueOf(input[i]);
            }
            shelterAddress = new InetSocketAddress(input);
            port = Integer.valueOf(hostPort[1]);

        } catch (final FileNotFoundException e) {
            System.out.println("no file" + e);
        } catch (final IOException e) {
            System.out.println("system issue" + e);
        }

        if(port==0){
            throw new IllegalArgumentException();
        }

        try (Socket connection = new Socket(shelterAddress, port) ){

        } catch (final IOException e) {

        }


        //Copy data from shelter to internal Queue

        //Await incoming connections

        //connect incoming connection to Queue

        
    }
}