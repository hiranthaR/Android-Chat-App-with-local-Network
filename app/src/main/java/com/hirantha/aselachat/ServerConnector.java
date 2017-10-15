package com.hirantha.aselachat;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by User on 10/8/2017.
 */
public class ServerConnector {

    //socket ekak client knk wage kilyala hithahan,serversocket ekak (sever eke) socket allana ekak wage :D
    //man therum arn inna widiha ;D
    // for I/O
    private ObjectInputStream sInput;        // to read from the socket
    private ObjectOutputStream sOutput;        // to write on the socket
    private Socket socket;
    private LoginFragment loginFragment;
    ChatDisplayFragment chatDisplayFragment;

    // the server, the port and the username
    private String server, username;
    private int port;

    /**
     * constructor
     * server: the server address
     * port: the port number
     * username: the username
     */

    public ServerConnector(String server, int port, String username) {
        // which calls the common constructor
        this.server = server;
        this.port = port;
        this.username = username;
        loginFragment = new LoginFragment();
    }


    /*
     * To start the dialog
	 */
    public boolean start() {
        // try to connect to the server
        try {
            socket = new Socket(server, port);
        }
        // if it failed not much I can so
        catch (Exception ec) {
            toasts("Error connectiong to server with: " + server + ":" + port + " " + ec);
            return false;
        }

        chatDisplayFragment = new ChatDisplayFragment();

        //change in to chat view
        MainActivity.context.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, chatDisplayFragment).commit();
        String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
        toasts(msg);
		/* Creating both Data Stream */
        try {
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
            chatDisplayFragment.setRequrements(username,sOutput);

        } catch (IOException eIO) {
            //fuck something went wrong.go back to login
            toasts("Exception creating new Input/output Streams: " + eIO);
            MainActivity.context.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, loginFragment).commit();
            return false;
        }

        // Send our username to the server this is the only message that we
        // will send as a String. All other messages will be ChatMessage objects
        try {
            sOutput.writeObject(username);

        } catch (IOException eIO) {
            //go back to login
            toasts("Exception doing login : " + eIO);
            MainActivity.context.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, loginFragment).commit();
            return false;
        }
        // creates the Thread to listen from the server
        new ListenFromServer().start();
        // success we inform the caller that it worked
        return true;
    }

    /*
     * To send a message to GUI
     */
    private void display(ChatMessage msg) {
        chatDisplayFragment.addMessage(msg);
    }

    private void toasts(final String message) {
        MainActivity.context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
     * When something goes wrong
     * Close the Input/Output streams and disconnect not much to do in the catch clause
     */
    private void disconnect() {
        try {
            if (sInput != null) sInput.close();
        } catch (Exception e) {
        } // not much else I can do
        try {
            if (sOutput != null) sOutput.close();
        } catch (Exception e) {
        } // not much else I can do
        try {
            if (socket != null) socket.close();
        } catch (Exception e) {
        } // not much else I can do

        // change the GUI
        MainActivity.context.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, loginFragment).commit();
    }

    /*
     * a class that waits for the message from the server and append them to the list
     */
    class ListenFromServer extends Thread {

        public void run() {
            while (true) {
                try {
                    ChatMessage msg = (ChatMessage) sInput.readObject();
                    // add to gui
                    display(msg);
                } catch (IOException e) {
                    toasts( "Server has close the connection: " + e);
                    MainActivity.context.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, loginFragment).commit();
                    break;
                }
                catch (ClassNotFoundException e2) {
                    toasts( "Server has close the connection: " + e2);
                    MainActivity.context.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, loginFragment).commit();
                    break;
                }
            }
        }
    }

}
