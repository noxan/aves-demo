/* 
 * Copyright (c) 2012, noxan
 * See LICENSE for details.
 */

package com.github.noxan.aves.demo;

import java.io.IOException;

import com.github.noxan.aves.auth.AuthException;
import com.github.noxan.aves.auth.User;
import com.github.noxan.aves.auth.accessor.UsernamePassword;
import com.github.noxan.aves.auth.accessor.UsernamePasswordAccessor;
import com.github.noxan.aves.auth.session.SessionManager;
import com.github.noxan.aves.auth.storage.InMemoryUsernamePasswordStorage;
import com.github.noxan.aves.net.Connection;
import com.github.noxan.aves.server.Server;
import com.github.noxan.aves.server.ServerAdapter;
import com.github.noxan.aves.server.SocketServer;

public class AuthServer extends ServerAdapter {
    public static void main(String[] args) {
        Server server = new SocketServer(new AuthServer());
        try {
            server.start();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private InMemoryUsernamePasswordStorage userStorage;
    private SessionManager manager;

    public AuthServer() {
        userStorage = new InMemoryUsernamePasswordStorage();
        userStorage.addUser("noxan", "123");
        manager = new SessionManager(userStorage);
    }

    @Override
    public void readData(Connection connection, Object data) {
        String parts[] = ((String)data).trim().split(" ");
        String message = parts[0].toUpperCase();
        String[] args = new String[parts.length - 1];
        System.arraycopy(parts, 1, args, 0, args.length);
        try {
            switch(message) {
                case "LOGIN":
                    if(args.length > 1) {
                        UsernamePasswordAccessor accessor = new UsernamePassword(args[0], args[1]);
                        try {
                            User user = manager.requestSession(accessor, connection);
                            connection.write("Welcome " + user.getUsername() + "!");
                        } catch(AuthException e) {
                            connection.write("LOGIN ERROR: " + e.getMessage());
                        }
                    } else {
                        connection.write("LOGIN ERROR: Invalid parameter(s)");
                    }
                    break;
                case "LOGOUT":
                    connection.write("Not implemented yet!");
                    break;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
