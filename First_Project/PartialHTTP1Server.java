import java.io.*;
import java.net.*;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.lang.String;
import java.lang.*;
import java.io.IOException;
import java.lang.Object;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.util.Date;
import java.io.PrintWriter;

//client_handler class
class client_handler extends Thread
{
    final BufferedReader inFromClient;
    //final DataOutputStream outToClient;
    final PrintWriter outToClient;
    final Socket s;

    //constructor
    public client_handler(Socket s, BufferedReader inFromClient, PrintWriter outToClient)  
    { 
        this.s = s; 
        this.inFromClient = inFromClient; 
        this.outToClient = outToClient;  
    }

    public void OK_headers(File f)
    {
        //Allow, Content-Encoding, Content-Length, Content-Type, Expires, Last-Modified
        try
        {
            Date d = new Date(f.lastModified());
            outToClient.println("Allow: GET, POST, HEAD\r"); //no sure if this is right
            outToClient.println("Content-Type: " + Files.probeContentType(f.toPath()) + "\r");
            outToClient.println("Content-Length: "+ f.length() + "\r");
            outToClient.println("Last-Modified: " + d + "\r");
            outToClient.println("Content-Encoding: identity\r"); 
            d.setTime(1626865200000L);
            outToClient.println("Expires: " + d + "\r");
        }catch (IOException e) { 
                e.printStackTrace();
        }       
    }

    public void run()  
    {  
        //get request in form: <command> <resource> HTTP/1.0 (NOTE: THERE WILL BE A 5 SECOND TIMEOUT ERROR)
        System.out.println("client connected!");
        String client_sentence;
        try{
        client_sentence = inFromClient.readLine();
        String client_request[] = client_sentence.split(" ");
        if(client_request.length != 3){
            outToClient.println("HTTP/1.0 400 Bad Request\r");
        } 
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
                        outToClient.println("HTTP/1.0 505 HTTP Version Not Supported\r");
                    else
                        outToClient.println("HTTP/1.0 400 Bad Request\r");
                }else    
                    outToClient.println("HTTP/1.0 400 Bad Request\r");
            }else if(command.compareTo("GET") != 0 && command.compareTo("POST") != 0 && command.compareTo("HEAD") != 0){
                //command is valid for 1.0 but not supported
                if(command.compareTo("DELETE") == 0 || command.compareTo("PUT") == 0 || command.compareTo("LINK") == 0 || command.compareTo("UNLINK") == 0)
                    outToClient.println("HTTP/1.0 501 Not Implemented\r");
                else 
                    outToClient.println("HTTP/1.0 400 Bad Request\r");
            }else if(command.compareTo("GET") == 0){
                //NEED TO DO CONDITIONAL GET STILL
                Charset charset = Charset.forName("ISO-8859-1");
                try { //works for textfiles but idk about nontext files (MIME)
                    File f = new File(resource);
                    if(f.exists())
                    {
                        List<String> lines = Files.readAllLines(Paths.get(resource), charset);
                        outToClient.println("HTTP/1.0 200 OK\r");
                        outToClient.println("\r");
                        OK_headers(f);
                        for (String line : lines) {
                            outToClient.println(line + "\r");
                        }
                    }
                    else
                        outToClient.println("HTTP/1.0 404 Not Found\r");
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            else if(command.compareTo("POST") == 0){
                //NEED TO DO CONDITIONAL GET STILL
                Charset charset = Charset.forName("ISO-8859-1");
                try { //works for textfiles but idk about nontext files (MIME)
                    File f = new File(resource);
                    if(f.exists())
                    {
                        List<String> lines = Files.readAllLines(Paths.get(resource), charset);
                        outToClient.println("HTTP/1.0 200 OK\r");
                        outToClient.println("\r");
                        OK_headers(f);
                        for (String line : lines) {
                            outToClient.println(line + "\r");
                        }
                    }
                    else
                        outToClient.println("HTTP/1.0 404 Not Found\r");
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            else if(command.compareTo("HEAD") == 0){
              // try {
                    File f = new File(resource);
                    if(f.exists())
                    {
                    outToClient.println("HTTP/1.0 200 OK\r");
                    outToClient.println("\r");
                    OK_headers(f);
                    }
                    else
                        outToClient.println("HTTP/1.0 404 Not Found\r");
                //} catch (IOException e) {
                  //  System.out.println(e);
                //}
            }
        }
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
        
        //wait for clients to connect
        while (true) 
        {
            //when client connects to server, obtain input and out streams, and create thread
            Socket connectionSocket = serverSocket.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            //DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            PrintWriter outToClient = new PrintWriter(connectionSocket.getOutputStream(), true);
            Thread t = new client_handler(connectionSocket, inFromClient, outToClient);
            t.start();
        }
        
    }
}
