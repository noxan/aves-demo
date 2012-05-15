package com.github.noxan.aves.demo.chat;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.noxan.aves.auth.AuthException;
import com.github.noxan.aves.auth.User;
import com.github.noxan.aves.auth.accessor.UsernamePassword;
import com.github.noxan.aves.auth.accessor.UsernamePasswordAccessor;
import com.github.noxan.aves.auth.session.SessionManager;
import com.github.noxan.aves.auth.storage.InMemoryUsernamePasswordStorage;
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

    private InMemoryUsernamePasswordStorage storage;
    private SessionManager sessionManager;

    private SocketServer server;

    public ChatServer() {
        storage = new InMemoryUsernamePasswordStorage();
        storage.addUser("noxan", "123");
        storage.addUser("test", "1234");
        sessionManager = new SessionManager(storage);
        server = new SocketServer(this);
    }

    public void start() throws IOException {
        server.start();
    }

    @Override
    public void readData(Connection connection, Object data) {
        logger.log(Level.INFO, data.toString());
        String[] parts = data.toString().split(";");
        try {
            switch(parts[0]) {
                case "login":
                    UsernamePasswordAccessor accessor = new UsernamePassword(parts[1], parts[2]);
                    try {
                        User user = sessionManager.requestSession(accessor, connection);
                        logger.log(Level.INFO, connection + " logged in as " + user.getUsername());
                        connection.write("login;ok");
                        server.broadcast(connection, "chat;Server;" + user.getUsername() + " joined");
                    } catch(AuthException e) {
                        connection.write("login;" + e.getMessage());
                    }
                    break;
                default:
                    logger.log(Level.WARNING, "unknown packet header: " + data.toString());
                    break;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
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
