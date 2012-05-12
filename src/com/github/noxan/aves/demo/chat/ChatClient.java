package com.github.noxan.aves.demo.chat;

import java.io.IOException;

import com.github.noxan.aves.client.ClientAdapter;
import com.github.noxan.aves.client.SocketClient;

public class ChatClient extends ClientAdapter {
    public static void main(String[] args) {
        SocketClient client = new SocketClient(new ChatClient());
        try {
            client.connect();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
