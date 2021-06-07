package com.example;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Client {
    String nick;
    DataInputStream in;
    DataOutputStream out;
    Server server;
    int id;

    public Client(String nick, DataOutputStream out, DataInputStream in, Server server, int id) {
        this.nick = nick;
        this.out = out;
        this.in = in;
        this.server = server;
        this.id = id;
        this.startListen();
    }

    public void startListen() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String str = in.readUTF();
                        if (str.equals("/end")) {
                            break;
                        }
                        server.clientSendMessage(id, str, nick);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    void receiveMessgae(int id, String msg,String nick) {
        try {
            Message message = new Message((id==this.id),msg,nick);
            String messagejson = new Gson().toJson(message);
            System.out.println(messagejson);
            out.writeUTF(messagejson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
