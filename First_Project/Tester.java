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
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.*;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

class Tester{
    public static void main(String args[]) throws Exception 
    {
        //try{
            try{
                File f = new File("test1.txt");
                if(!f.exists()){ //FILE DNE
                    //outToClient.writeBytes("404 Not Found" + crlf + crlf);
                    System.out.println("404 Not Found");
                }else{
                    DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
                    Date result =  df.parse("Wed, 02 Sep 2020 12:00:00 GMT");
                    long lastModified = result.getTime();
                        if(lastModified > f.lastModified()){ //FILE NOT MODIFIED SINCE
                          //outToClient.writeBytes("304 Not Modified" + crlf + crlf);
                          System.out.println("304 Not Modified");
                        }
                }
            }catch(ParseException pe){
                pe.printStackTrace();
                //outToClient.writeBytes("400 Bad Request" + crlf + crlf); //invalid date = Bad request?
                System.out.println("400 Bad Request : date");
            }
        //}catch(IOException e) {
          //  e.printStackTrace();
        //}

        System.out.println("SATISFIES CONDITION");
    }

}