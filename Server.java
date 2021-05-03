import java.net.*;
import java.io.*;

import static java.lang.System.*;

public class Server{

    ServerSocket server;
    Socket socket;

    BufferedReader reader;
    PrintWriter writer;

    // constructor
    public Server(){
        try{
            server = new ServerSocket(7740); //cleint request .. creating a server
            System.out.println("Server is accepting connection:");
            System.out.println("waiting....:");
            socket = server.accept();// accepting connection of client-socket, returning object of the client

            // reading data 
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // writing data
            writer = new PrintWriter(socket.getOutputStream());

            //calling methods
            Reading();
            Writing();

        }catch(Exception e){
            e.printStackTrace();
        }


    }

    //reading method to read data from client
    public void Reading(){

        // thread - keeps reading the data

        Runnable runnable = () ->{ 
            System.out.println("reading started...");

            
            // infinite loop to read data whenever recieved from client
            try{
                while(true){

                    String msg = reader.readLine();

                //in case client wants to exit the chat
                    if(msg.equalsIgnoreCase("exit")){
                        System.out.println("Client exited the chat");
                        break; // closing the reader
                    }
                    System.out.println("Client: "+ msg);
                }             
            }catch(Exception e ){
                    //e.printStackTrace();
                    System.out.println("Connection closed");
                }     
            
            
        };
    
        // total running 4 threads
        new Thread(runnable).start();
                 
    }

    public void Writing(){

        // thread - taking the data from user and sending to client 
        Runnable r2 = () -> {

            System.out.println("Writer Started..");


            try{
                while(true){
                
                    // data from console 
                    BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in)) ;
                    String content = reader1.readLine();
                    out.println(content);
                    out.flush();
                    
            }

            }catch (Exception e){
                e.printStackTrace();
            }
        };

        // starging thread
        new Thread(r2).start();
    
}
    
    public static void main(String[]args){
        System.out.println("This is server ");
        new Server(); // server object

    }
} 