package biz.burse;

import java.io.IOException;

import org.apache.activemq.broker.*;

public class Server {
    public static void main(String[] args) throws Exception{

        //Initiate source connection - shelter
        int port=0;
        try( BufferedReader reader = new BufferedReader(new FileReader("sharedInfo.cfg"))){
             port = Integer.valueOf(reader.readLine().split(" ")[0]);
            

        } 
        catch (FileNotFoundException e){
            System.out.println("no file" +e);
        }catch (IOException e){
            System.out.println("system issue" +e);
        }
        

        try( Socket connection = new Socket(InetAddress.getByName("localhost"),port)){
 

        } catch (IOException e){

        }


        //Copy data from shelter to internal Queue

        //Await incoming connections

        //connect incoming connection to Queue

        
    }
}