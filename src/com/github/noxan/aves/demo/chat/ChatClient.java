package com.github.noxan.aves.demo.chat;

import java.io.IOException;

import com.github.noxan.aves.client.Client;
import com.github.noxan.aves.client.ClientAdapter;
import com.github.noxan.aves.client.SocketClient;

public class ChatClient extends ClientAdapter {
    private SocketClient client;

    public static void main(String[] args) {
        new ChatClient();
    }

    public ChatClient() {
        client = new SocketClient(this);
        try {
            client.connect();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
