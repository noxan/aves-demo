package com.github.noxan.aves.demo.chat;

import java.io.IOException;

import com.github.noxan.aves.net.Connection;
import com.github.noxan.aves.server.Server;
import com.github.noxan.aves.server.ServerHandler;
import com.github.noxan.aves.server.SocketServer;

public class ChatServer implements ServerHandler {
    public static void main(String[] args) {
        Server server = new SocketServer(new ChatServer());
        try {
            server.start();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readData(Connection connection, Object data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void clientConnect(Connection connection) {
        // TODO Auto-generated method stub

    }

    @Override
    public void clientDisconnect(Connection connection) {
        // TODO Auto-generated method stub

    }

    @Override
    public void clientLost(Connection connection) {
        // TODO Auto-generated method stub

    }
}
