import java.io.*;
import java.net.*;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.lang.String;
import java.lang.*;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.*;
import java.util.ArrayList;

//client_handler class
class client_handler extends Thread
{
    final BufferedReader inFromClient;
    final DataOutputStream outToClient;
    final Socket s;
    private ArrayList<client_handler> clients;

    //constructor
    public client_handler(Socket s, ArrayList<client_handler> clients, BufferedReader inFromClient, DataOutputStream outToClient)  
    { 
        this.s = s; 
        this.clients = clients;
        this.inFromClient = inFromClient; 
        this.outToClient = outToClient;  
    }

    public void run()  
    {  
        //get request in form: <command> <resource> HTTP/1.0 (NOTE: THERE WILL BE A 5 SECOND TIMEOUT ERROR)
        System.out.println("client connected!");
        String client_sentence;
        String server_response = "idk what happened";
        try{
        client_sentence = inFromClient.readLine();
        String client_request[] = client_sentence.split(" ");;
        if(client_request.length != 3)
            server_response = "HTTP/1.0 400 Bad Request" + "\n";
        else
        {
            String command = client_request[0];
            String resource = client_request[1];
            String version = client_request[2];
            //check if the version of HTTP is valid ("HTTP/1.0")
            if(version.compareTo("HTTP/1.0") != 0)
            {
                if(version.length() > 5)
                {
                    if(version.substring(0,5).compareTo("HTTP/") == 0 && Double.valueOf(version.substring(5)) != 1.0) 
                        server_response = "HTTP/1.0 505 HTTP Version Not Supported" + "\n";
                    else
                        server_response = "HTTP/1.0 400 Bad Request" + "\n";
                }else    
                    server_response = "HTTP/1.0 400 Bad Request" + "\n";
            }else if(command.compareTo("GET") != 0 && command.compareTo("POST") != 0 && command.compareTo("HEAD") != 0){
                //command is valid for 1.0 but not supported
                if(command.compareTo("DELETE") == 0 || command.compareTo("PUT") == 0 || command.compareTo("LINK") == 0 || command.compareTo("UNLINK") == 0)
                    server_response = "HTTP/1.0 501 Not Implemented" + "\n";
                else 
                    server_response = "HTTP/1.0 400 Bad Request" + "\n";
                
            }else if(command.compareTo("GET") == 0){
                server_response = "HTTP/1.0 200 OK" + "\n";
                // Allow, Content-Encoding, Content-Length, Content-Type, Expires, Last-Modified for 200 OK
    
            }
            else if(command.compareTo("POST") == 0){
                server_response = "HTTP/1.0 200 OK" + "\n";
            }
            else if(command.compareTo("HEAD") == 0){
                server_response = "HTTP/1.0 200 OK" + "\n";
            }

        }
        outToClient.writeBytes(server_response);
        }catch (IOException e) { 
                e.printStackTrace();
        }
    } 
}

// PartialHTTP1 Server Class
class PartialHTTP1Server 
{
    //initializing the thread pool - starting with 5 
    private static ExecutorService pool = Executors.newFixedThreadPool(5);
    //arraylist to keep track of client threads
    private static ArrayList<client_handler> clients = new ArrayList<>();

    /*public thread_pool(int numThreads, int maxThreads){     
        for(int i = 0; i < numThreads; i++){      
            clients.add(new client_handler(clientThread));    
        }    
        
        for(client_handler thread : clientThread) {      
            thread.start();    
        }  
    }  
                
    public void stop() {     
        for(client_handler thread : clientThreads){      
            thread.stop();    
        }  
    }*/
    
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
            client_handler clientThread  = new client_handler(connectionSocket, clients, inFromClient, outToClient);
            
            clients.add(clientThread);
            pool.execute(clientThread);
            //pool.shutdown();
        }
    }
}