package com.github.noxan.aves.demo.chat;

import java.io.IOException;

import com.github.noxan.aves.client.Client;
import com.github.noxan.aves.client.ClientAdapter;
import com.github.noxan.aves.client.SocketClient;

public class ChatClient extends ClientAdapter {
    private SocketClient client;
    private ChatClientGUI gui;

    public static void main(String[] args) {
        new ChatClient();
    }

    public ChatClient() {
        client = new SocketClient(this);
        gui = new ChatClientGUI(this);
    }

    public void login(String username, String password) {
        try {
            client.write("login;" + username + ";" + password + "\n");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            client.connect();
            gui.setStatus(ChatClientStatus.CONNECT);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void chat(String text) {
        try {
            client.write("chat;" + text);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readData(Client client, Object data) {
        String parts[] = ((String)data).trim().split(";");
        String message = parts[0].toLowerCase();
        String[] args = new String[parts.length - 1];
        System.arraycopy(parts, 1, args, 0, args.length);

        switch(message) {
            case "login":
                if(args[0].equals("ok")) {
                    gui.setStatus(ChatClientStatus.LOGIN);
                }
                break;
            case "chat":
                if(args.length > 1) {
                    gui.chat(args[0], args[1]);
                } else {
                    System.err.println("invalid message: " + data);
                }
                break;
            default:
                System.err.println("unknown message: " + data);
                break;
        }
    }
}
