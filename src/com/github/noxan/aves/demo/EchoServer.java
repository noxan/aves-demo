package com.github.noxan.aves.demo;

import java.io.IOException;

import com.github.noxan.aves.net.Connection;
import com.github.noxan.aves.server.Server;
import com.github.noxan.aves.server.ServerAdapter;
import com.github.noxan.aves.server.SocketServer;


public class EchoServer extends ServerAdapter {
    public static void main(String[] args) {
        Server server = new SocketServer(new EchoServer());
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readData(Connection connection, Object data) {
        try {
            connection.write(data);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
