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

    public void OK_headers(String resource)
    {
        //Allow, Content-Encoding, Content-Length, Content-Type, Expires, Last-Modified
        try
        {
            File f = new File(resource);
            Date d = new Date(f.lastModified());
            outToClient.writeBytes("Allow: GET, POST, HEAD" + "\n"); //no sure if this is right
            outToClient.writeBytes("Content-Type: " + Files.probeContentType(f.toPath()) + "\n");
            outToClient.writeBytes("Content-Length: "+ f.length() + "\n");
            outToClient.writeBytes("Last-Modified: " + d + "\n");
            outToClient.writeBytes("Content-Encoding: identity" + "\n"); 
            d.setTime(1626865200000L);
            outToClient.writeBytes("Expires: " + d + "\n");
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
        String client_request[] = client_sentence.split(" ");;
        if(client_request.length != 3)
            outToClient.writeBytes("HTTP/1.0 400 Bad Request" + "\n");
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
                        outToClient.writeBytes("HTTP/1.0 505 HTTP Version Not Supported" + "\n");
                    else
                        outToClient.writeBytes("HTTP/1.0 400 Bad Request" + "\n");
                }else    
                    outToClient.writeBytes("HTTP/1.0 400 Bad Request" + "\n");
            }else if(command.compareTo("GET") != 0 && command.compareTo("POST") != 0 && command.compareTo("HEAD") != 0){
                //command is valid for 1.0 but not supported
                if(command.compareTo("DELETE") == 0 || command.compareTo("PUT") == 0 || command.compareTo("LINK") == 0 || command.compareTo("UNLINK") == 0)
                    outToClient.writeBytes("HTTP/1.0 501 Not Implemented" + "\n");
                else 
                    outToClient.writeBytes("HTTP/1.0 400 Bad Request" + "\n");
            }else if(command.compareTo("GET") == 0){
                Charset charset = Charset.forName("ISO-8859-1");
                try { //works for textfiles but idk about nontext files (MIME)
                    List<String> lines = Files.readAllLines(Paths.get(resource), charset);
                    outToClient.writeBytes("HTTP/1.0 200 OK" + "\n");
                    OK_headers(resource);
                    for (String line : lines) {
                        System.out.println(line);
                        outToClient.writeBytes(line + "\n");
                    }
                } catch (IOException e) {
                    System.out.println(e);
                    outToClient.writeBytes("404 Not Found" + "\n");
                }
            }
            else if(command.compareTo("POST") == 0){
                outToClient.writeBytes("HTTP/1.0 200 OK" + "\n");
            }
            else if(command.compareTo("HEAD") == 0){
               outToClient.writeBytes("HTTP/1.0 200 OK" + "\n");
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
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            Thread t = new client_handler(connectionSocket, inFromClient, outToClient);
            t.start();
        }
        
    }
}
