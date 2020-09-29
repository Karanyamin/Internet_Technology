import java.io.*;
import java.net.*;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.lang.String;
import java.lang.*;
import java.io.IOException;

//client_handler class
class client_handler extends Thread
{
    final BufferedReader inFromClient;
    final DataOutputStream outToClient;
    final Socket s;

    //constructor
    public client_handler(Socket s, BufferedReader inFromClient, DataOutputStream outToClient)  
    { 
        this.s = s; 
        this.inFromClient = inFromClient; 
        this.outToClient = outToClient;  
    }

    public void run()  
    {  
        //get request in form: <command> <resource> HTTP/1.0 (NOTE: THERE WILL BE A 5 SECOND TIMEOUT ERROR)
        System.out.println("client connected!");
        try{
        String clientSentence = inFromClient.readLine();
        String capitalizedSentence = clientSentence.toUpperCase() + '\n';
        outToClient.writeBytes(capitalizedSentence);
        }catch (IOException e) { 
                e.printStackTrace();
        }

        /*
        String[] client_request = str.split(" ");
        if(client_request.length != 3)
            //error  "400 Bad Request" 
        if(client_request[2].compareTo("HTTP/1.0") != 0)
            // error "505 HTTP Version Not Supported"
        
        /*
            for (int i = 10; i < 13; i++) 
            {
                System.out.println(Thread.currentThread().getName() + "  " + i);
                 try {
                // thread to sleep for 1000 milliseconds
                     Thread.sleep(1000);
                 } catch (Exception e) {
                System.out.println(e);
                }
            }
        */
            //this.inFromClient.close(); 
            //this.outToClient.close();
        
    } 
}

// PartialHTTP1 Server Class
class PartialHTTP1Server 
{
    public static void main(String args[]) throws Exception 
    {
        //check if there is one argument (port number)
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
            //when client connects to server, obtain input and out streams, and create thread
            Socket connectionSocket = serverSocket.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            Thread t = new client_handler(connectionSocket, inFromClient, outToClient);
            t.start();
        }
        
    }
}
