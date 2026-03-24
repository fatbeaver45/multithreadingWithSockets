package com.example;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SocketClientExample {

    /*
     * Modify this example so that it opens a dialogue window using java swing,
     * takes in a user message and sends it
     * to the server. The server should output the message back to all connected
     * clients
     * (you should see your own message pop up in your client as well when you send
     * it!).
     * We will build on this project in the future to make a full fledged server
     * based game,
     * so make sure you can read your code later! Use good programming practices.
     * ****HINT**** you may wish to have a thread be in charge of sending
     * information
     * and another thread in charge of receiving information.
     */
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        //get the localhost IP address, if server is running on some other IP, you need to use that
        InetAddress host = InetAddress.getLocalHost();
        ArrayList<String> messages = new ArrayList<String>();
        Socket  socket = new Socket(host.getHostName(), 9876);
        try{
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("your message");
        Scanner line = new Scanner(System.in);
        while(!(line.nextLine()).equals("!disconnect")){
            oos.writeObject(line);
            oos.flush();
        }
        oos.writeObject("!disconnect");
        oos.flush();
        System.out.println("Connection Closed!");
      
        
   
        JFrame f = new JFrame("Chat Room");
		for(int i=0; i<messages.size();i++) {
            f.add(new JLabel(messages.get(i)));
        }
        JButton discon = new JButton("Disconnect")
        discon.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                oos.writeObject("!disconnect");
                oos.flush();
            } 
        } );
        JTextField textField = new JTextField("Your message here");
        JButton send = new JButton("Send");
        discon.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                if(textField.getText() != null || textField.getText() != ""){
                    oos.send(textField.getText);
                    textField.setText("null");
                }
            } 
        } );

    }catch(EOFException e){
        System.out.println("something went wrong");
    }
}
}
