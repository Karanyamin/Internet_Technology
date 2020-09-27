import java.io.*;
import java.net.*;


public class HelloThread extends Thread {

    public void run() {
        System.out.println("Hello from a thread!");
    }

    public static void main(String args[]) {
        (new HelloThread()).start();
    }

}
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