import java.io.*;
import java.net.*;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.lang.String;
import java.lang.*;
import java.io.IOException;
  
// Client class 
public class Client  
{
    final static private String crlf = "\r\n";

    public static void main(String[] args) throws IOException  
    {
        if (args.length != 1) {
            System.out.println("Please enter a port number");
            System.exit(1);
        }

        String sentence;
        String modifiedSentence;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = new Socket("localhost", Integer.parseInt(args[0]));
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        System.out.println("connected to server! Enter command:");
        //sentence = inFromUser.readLine();
        sentence = "GET url HTTP/1.0"; //Edit command here
        System.out.println("Sending command: [" + sentence + "]");
        outToServer.writeBytes(sentence + crlf + crlf);
        modifiedSentence = inFromServer.readLine();
        System.out.println("FROM SERVER: " + modifiedSentence);
        clientSocket.close(); 
    } 

} 
