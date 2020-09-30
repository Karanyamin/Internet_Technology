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


class ThreadRunnable implements Runnable {
    private int thread_id;
    public ThreadRunnable(int thread_id) {
        this.thread_id = thread_id;
    }
    //keeping track of when thread begins and ends
    public void run() {
        //need to figure out how to only use one thread for each server/client connection
        System.out.println("Start Thread: " + thread_id);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread Completed: " + thread_id);
    }
}

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
        ExecutorService executor = Executors.newFixedThreadPool(50);
        
        //wait for clients to connect
        while (true) 
        {
            //when client connects to server, obtain input and out streams, and create thread
            Socket connectionSocket = serverSocket.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            Thread t = new client_handler(connectionSocket, inFromClient, outToClient);
            t.start();

            //our code is now limited to 50 threads that can be used
            for (int i = 0; i < 50; i++) {
                executor.submit(new ThreadRunnable(i));
            }
            executor.shutdown();

            while (!executor.isTerminated()) {}
                System.out.println("All Threads Completed.");
            /*try {
            executor.awaitTermination(1, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }    
    }
}
