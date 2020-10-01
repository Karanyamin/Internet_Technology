import java.io.*;
import java.net.*;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.lang.String;
import java.lang.*;
import java.io.IOException;
import java.io.PrintWriter;
  
// Client class 
public class Client  
{ 
    public static void main(String[] args) throws IOException  
    { 
        String sentence;
        String modifiedSentence;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = new Socket("hostname", 3456);
        //DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        System.out.println("connected to server!");
        sentence = inFromUser.readLine();
        sentence = sentence + "\r";
        outToServer.println(sentence);
        while(true)
        {
        modifiedSentence = inFromServer.readLine();
        System.out.println(modifiedSentence);
        }
        //clientSocket.close(); 
    } 

} 
