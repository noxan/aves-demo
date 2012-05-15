package com.github.noxan.aves.demo;

import java.io.IOException;

import com.github.noxan.aves.client.Client;
import com.github.noxan.aves.client.ClientHandler;
import com.github.noxan.aves.client.SocketClient;

public class DemoClient implements ClientHandler {
    public static void main(String[] args) {
        SocketClient client = new SocketClient(new DemoClient());
        try {
            client.connect();
            client.write("Hello, this is client!\n");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clientConnect(Client client) {
        System.out.println("Client connect");
    }

    @Override
    public void clientDisconnect(Client client) {
        System.out.println("Client disconnect");
    }

    @Override
    public void readData(Client client, Object data) {
        System.out.println("Client readData: " + data);
    }

    @Override
    public void serverDisconnect(Client client) {
        System.out.println("Server disconnect");
    }

    @Override
    public void serverLost(Client client) {
        System.out.println("Server lost");
    }
}
