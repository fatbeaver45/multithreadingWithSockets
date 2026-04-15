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

// import javax.swing.JButton;
// import javax.swing.JFrame;
// import javax.swing.JLabel;
// import javax.swing.JTextArea;
// import javax.swing.JTextField;
// import javax.swing.JPanel;
// import javax.swing.JScrollPane;
// import javax.swing.SwingUtilities;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
        System.out.println("What's your username?");
       
        InetAddress host = InetAddress.getLocalHost();
        ArrayList<String> messages = new ArrayList<String>();
        Socket  socket = new Socket(host.getHostName(), 9876);
        try{
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        // System.out.println(messages.get(messages.size()));
        Scanner line = new Scanner(System.in);
        
      
        JFrame f = new JFrame("Chat Room");
        f.setLayout(new BorderLayout());
        JTextArea chatArea = new JTextArea(10, 30);
        
        class MyThread extends Thread{
        
            public void run(){
                while(true){
                    try {
                        String msg = (String)ois.readObject();
                        SwingUtilities.invokeLater(() -> chatArea.append(msg + "\n"));
                    } catch (ClassNotFoundException | IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        MyThread myThread = new MyThread();
        myThread.start();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        f.add(scrollPane, BorderLayout.CENTER);
        
        JPanel panel = new JPanel();
        JButton discon = new JButton("Disconnect");
        discon.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                try {
                    oos.writeObject("!disconnect");
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                try {
                    oos.flush();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } 
        });
        JTextField textField = new JTextField(20);
        JButton send = new JButton("Send");
        send.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                if(textField.getText() != null && !textField.getText().isEmpty()){
                    try {
                        oos.writeObject(new Info(textField.getText()));
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    try {
                        oos.flush();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    textField.setText("");
                }
            } 
        });
        panel.add(textField);
        panel.add(send);
        panel.add(discon);
        f.add(panel, "South");
        f.pack();
        f.setVisible(true);

        // while(!(line.nextLine()).equals("!disconnect")){
        //     oos.writeObject(new Info((String)(Object)line));
        //     oos.flush();
        // }
        // oos.writeObject("!disconnect");
        // oos.flush();
        // System.out.println("Connection Closed!");

    }catch(EOFException e){
        System.out.println("something went wrong");
    }
}
}
