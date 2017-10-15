/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hirantha.aselachat;

import java.io.Serializable;

/**
 *
 * @author Hirantha
 */
public class ChatMessage implements Serializable {

    protected static final long serialVersionUID = 1112122200L;

    // The different types of message sent by the Client
    // WHOISIN to receive the list of the users connected
    // MESSAGE an ordinary message
    // LOGOUT to disconnect from the Server
    static final int WHOISIN = 0, MESSAGE = 1, LOGOUT = 2;
    private int type;
    private String message;
    private String time;
    private String userName;

    // constructor

    ChatMessage(int type,String userName, String message,String time) {
        this.type = type;
        this.message = message;
        this.time = time;
        this.userName=userName;
    }



    // getters
    int getType() {
        return type;
    }
    String getMessage() {
        return message;
    }

    String getTime(){
        return time;
    }

    String getUserName(){
        return userName;
    }
}