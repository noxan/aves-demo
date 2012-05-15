package com.github.noxan.aves.demo.chat;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class ChatClientGUI {
    private JFrame frame;

    public ChatClientGUI(ChatClient client) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
        }

        frame = new JFrame();
        frame.setTitle("aves - chatclient");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(320, 680);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
