import java.io.*;
import java.net.*;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.lang.String;
//import java.io.IOException;

/*
public class HelloThread extends Thread {

    public void run() {
        System.out.println("Hello from a thread!");
    }

    public static void main(String args[]) {
        (new HelloThread()).start();
    }

}
*/
/*
class client_handler extends Thread
{
    final DataInputStream dis; 
    final DataOutputStream dos; 
    final Socket s;

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)  
    { 
        this.s = s; 
        this.dis = dis; 
        this.dos = dos; 
    }

    public void run()  
    {  
        while (true)  
        { 
            System.out.println("hi!");
        }   

}

*/

class PartialHTTP1Server 
{
    public static void main(String args[]) throws Exception 
    {
        if (args.length != 1) 
        {
            System.err.println("Usage: java PartialHTTP1Server <port number>");
            System.exit(1);
        }
        //create server socket given port number
        int portNumber = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(portNumber);

        //wait for clients to connect
        while (true) 
        {
            Socket connectionSocket = serverSocket.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            //get request in form: <command> <resource> HTTP/1.0 (NOTE: THERE WILL BE A 5 SECOND TIMEOUT ERROR)
            String str = inFromClient.readLine();
            String[] client_request = str.split(" ");
            if(client_request.length != 3)
                //error  "400 Bad Request" 
            if(client_request[2].compareTo("HTTP/1.0") != 0)
                // error "505 HTTP Version Not Supported"

            String command = client_request[0];
            String resource = client_request[1];
        
            //Thread t = new client_handler(serverSocket, inFromClient, outToClient);
            //t.start();

        }
    }
}
