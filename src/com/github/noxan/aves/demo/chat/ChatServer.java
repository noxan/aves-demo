package com.github.noxan.aves.demo.chat;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.noxan.aves.net.Connection;
import com.github.noxan.aves.server.ServerHandler;
import com.github.noxan.aves.server.SocketServer;

public class ChatServer implements ServerHandler {
    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        try {
            server.start();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public void readData(Connection connection, Object data) {
        logger.log(Level.INFO, data.toString());
    }

    @Override
    public void clientConnect(Connection connection) {
        logger.log(Level.INFO, connection + " connected");
    }

    @Override
    public void clientDisconnect(Connection connection) {
        logger.log(Level.INFO, connection + " disconnected");
    }

    @Override
    public void clientLost(Connection connection) {
        logger.log(Level.INFO, connection + " lost");
    }
}
