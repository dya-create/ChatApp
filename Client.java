import java.net.*;
import java.io.*;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JMenu;

import javax.swing.*;
import static java.lang.System.*;

import java.awt.event.*;
import java.awt.Font;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

import javax.swing.JFrame;
import javax.swing.BorderFactory;


public class Client extends JFrame {

    Socket socket;
    BufferedReader reader;
    PrintWriter writer;

    //Declare components swing

    private JLabel heading = new JLabel ("Client Messenger");
    private JTextArea messageArea = new JTextArea() ;
    private JTextField messageInput = new JTextField();

    private Font font = new Font("Courier", Font.PLAIN,12);
    public Client(){
        try{
            //request
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1",7753); // ip addresss port number? server on same computer..creating a socket

            System.out.println("Connection sucessful:"); // success message 

            //reading data obtainer from the client socket
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //writing data
            writer = new PrintWriter(socket.getOutputStream(),true);

            showGUI(); // method to create GUI 
            manageEvents();// event handling 

            Reading();
            //Writing(); //deleted since it is coded for console, GUI threads manages it all
        }catch(Exception e){
            System.out.println("Connection Closed");
            
        }
    }

    //event handling
    private void manageEvents(){

        messageInput.addKeyListener(new KeyListener(){

        
            public void keyReleased(KeyEvent e){
                //System.out.println("KeyReleased" + e.getKeyCode());
                if(e.getKeyCode()==10){

                    System.out.println("You pressed enter button");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me: " +contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");// clear message input
                    messageInput.requestFocus();
                }

            }

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }        
             
        });
    }

    //GUI method
    private void showGUI (){  

        this.setTitle("Client Messenger");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            

        JOptionPane.showMessageDialog(this, "This is a ChatApp between a Client and Server that are under same Network.\n There are two developmement needed to run this app, Client.java, Server.java. \nClients chat is GUI based while server\n is console based.  \nPlease make sure that IP address and port numbers are correct.\n Make sure that port numbers are same and change them to test the app faster.");
    
        // label font
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("clogo.jpeg"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);

        heading.setHorizontalAlignment(SwingConstants.CENTER); // center the heading
        heading.setBorder(BorderFactory.createEmptyBorder(5,5,5,5)); //spacing aroung heading

        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER); //sending messages from center      

        this.setLayout(new BorderLayout());
        this.add(heading,BorderLayout.NORTH);

        //scorlling panel
        JScrollPane scroll = new JScrollPane(messageArea);

        this.add(scroll,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        this.setVisible(true); 
        
    }

    public void actionPerformed(ActionEvent e){
        JOptionPane.showMessageDialog(this,"Diya", "hi",JOptionPane.INFORMATION_MESSAGE);
    }
    
    

    //reading from client side
    public void Reading(){

        // thread - keeps reading the data
        Runnable runnable = () ->{ 
            System.out.println("Reader started...");

            // infinite loop to read data whenever recieved from client
            try{
                while(true){
               
                    String msg = reader.readLine();

                // in case client wants to exit the chat
                    if(msg.equalsIgnoreCase("exit")){
                        System.out.println("Server exited the chat");
                        //not working - dp
                        JOptionPane.showMessageDialog(this,"Server Terminated the chat"); // tell the user about chat
                        
                        messageInput.setEnabled(false); //turn off text edit 
                        socket.close();//close ther serversocket object
                        break;
                    }
                    System.out.println();
                    System.out.println("Server: " +msg);
                    messageArea.append("Server: "+ msg + "\n"); //not working - dp
            }


            }catch(Exception e){
                    //.printStackTrace();
                    System.out.println("Connection Closed");
            }          
        };

        new Thread(runnable).start();
                 
    }

    public void Writing(){

        // thread - taking the data from user and sending to client 
        Runnable r2 = () -> {
            System.out.println("Writer started");

            try{
                while(true){
                
                    // data from console 
                    BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in)) ;
                    
                    String content = reader1.readLine();
                    out.println(content);
                    out.flush();
 

            }
            }catch (Exception e){
                System.out.println("Connection CLosed");
            }
        
        };

        //starting thread
        new Thread(r2).start();
    
}

    public static void main(String[]args){
        System.out.println("This is client: ");
        new Client();
    
    }
}
 
