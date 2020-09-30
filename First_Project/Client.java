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
    public static void main(String[] args) throws IOException  
    { 
        String sentence;
        String modifiedSentence;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = new Socket("hostname", 3456);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        System.out.println("connected to server!");
        sentence = inFromUser.readLine();
        outToServer.writeBytes(sentence + '\n');
        while(true)
        {
        modifiedSentence = inFromServer.readLine();
        System.out.println(modifiedSentence);
        }
        //clientSocket.close(); 
    } 

} 
