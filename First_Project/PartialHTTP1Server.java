import java.io.*;
import java.net.*;

class PartialHTTP1Server {

    public static void main(String args[]) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java PartialHTTP1Server <port number>");
            System.exit(1);
        }
    
        int portNumber = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(portNumber);

        while (true) {
            Socket connectionSocket = serverSocket.accept();
            
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

        }
    }
}