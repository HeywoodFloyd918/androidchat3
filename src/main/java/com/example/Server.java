package com.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private final int PORT = 8189;
    private List<Client> clients;

    public Server() {
        try (ServerSocket server = new ServerSocket(PORT)) {
            clients = new ArrayList<>();
            while (true) {
                System.out.println("Сервер ожидает подключения");
                Socket socket = server.accept();
                try {
                    System.out.println("солнышко");
                    authUser(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка в работе сервера");
        }
    }

    synchronized void authUser(Socket socket) throws IOException {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        String nick = in.readUTF();
        int id = clients.size();
        clients.add(new Client(nick, out, in, this, id));
        System.out.println("Клиент " + nick + " подключился");
    }

    synchronized void clientSendMessage(int id, String msg, String nick) {
        for (Client client : clients) {
            client.receiveMessgae(id, msg, nick);
        }
    }
}

