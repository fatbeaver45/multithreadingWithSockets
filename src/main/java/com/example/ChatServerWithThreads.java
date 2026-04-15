package com.example;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * This program is a server that takes connection requests on
 * the port specified by the constant LISTENING_PORT.  When a
 * connection is opened, the program should allow the client to send it messages. The messages should then 
 * become visible to all other clients.  The program will continue to receive
 * and process connections until it is killed (by a CONTROL-C,
 * for example). 
 * 
 * This version of the program creates a new thread for
 * every connection request.
 */
public class ChatServerWithThreads {

    public static final int LISTENING_PORT = 9876;

    public static void main(String[] args) {

        //Mench: put this in a constructor

        ServerSocket listener;  // Listens for incoming connections.
        Socket connection;      // For communication with the connecting program.

        /* Accept and process connections forever, or until some error occurs. */

        try {
            listener = new ServerSocket(LISTENING_PORT);
            System.out.println("Listening on port " + LISTENING_PORT);
            while (true) {
                connection = listener.accept();
                ConnectionHandler h = new ConnectionHandler(connection);
                h.start();
                  // Accept next connection request and handle it.
            }
        }
        catch (Exception e) {
            System.out.println("Sorry, the server has shut down.");
            System.out.println("Error:  " + e);
            return;
        }

    }  // end main()


    /**
     *  Defines a thread that handles the connection with one
     *  client.
     */

    //open up all input and outputs
    // listen 
    
    private static class ConnectionHandler extends Thread {
        private volatile static ArrayList<ConnectionHandler> handlers = new ArrayList<ConnectionHandler>();
        private static int nextID=0;
        Socket client;
        ObjectOutputStream oos;
        ObjectInputStream ois;
        String name;
        ConnectionHandler(Socket socket) {
            client = socket;
            name = nextID+"";
            nextID++;
            try {
                oos = new ObjectOutputStream(client.getOutputStream());
                ois = new ObjectInputStream(client.getInputStream());
                synchronized (handlers) {
                    handlers.add(this);
                }
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // try {
            //     ois = new ObjectInputStream(client.getInputStream());
            // } catch (IOException e) {
            //     // TODO Auto-generated catch block
            //     e.printStackTrace();
            // }
        }
        // public void sendMessage(){

        // }
        public void run() {
            String clientAddress = client.getInetAddress().toString();
            while(true) {
	            try {
                    Object incoming = ois.readObject();
                    if (incoming.equals("!disconnect")) {
                        System.out.println("Connection Closed");
                        break;
                    }
                    if (incoming instanceof Info) {
                        Info info = (Info) incoming;
                        String message = info.getMessage();
                        System.out.println(message);
                        synchronized (handlers) {
                            for (ConnectionHandler hand : handlers){
                                try{
                                    hand.oos.writeObject(message);
                                    hand.oos.flush();
                                }
                                catch (IOException e){
                                }
                            }
                        }
                    }
                    else if (incoming.equals("!disconnect")) {
                        System.out.println("Connection Closed");
                        break;
                    }
                    
                    
                    
                    
                    
	            }
	            catch (Exception e){
	                System.out.println("Error on connection with: " 
	                        + clientAddress + ": " + e);
                            synchronized (handlers) {
                    handlers.remove(this);
                    }
                    break;
	            }
            }
        }
    }


}
