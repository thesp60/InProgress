package biz.burse;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.*;
import java.io.*;
import java.util.Scanner;
import javax.net.*;
import java.net.ServerSocket;
import javax.net.ssl.SSLServerSocketFactory;


import org.apache.activemq.broker.*;

public class Server {
    public static void main(final String[] args) throws Exception {

        // Initiate source connection - shelter
        int port = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("sharedInfo.cfg"))) {
            port = Integer.valueOf(reader.readLine().split(" ")[0]);

        } catch (final FileNotFoundException e) {
            System.out.println("no file" + e);
        } catch (final IOException e) {
            System.out.println("system issue" + e);
        }

        try (Socket connection = new Socket(InetAddress.getByName("localhost"), port)) {

        } catch (final IOException e) {

        }


        //Copy data from shelter to internal Queue

        //Await incoming connections

        //connect incoming connection to Queue

        
    }
}