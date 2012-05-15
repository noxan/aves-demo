package com.github.noxan.aves.demo.chat;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class ChatClientGUI {
    private JFrame frame;
    private JPanel cards;

    public ChatClientGUI(ChatClient client) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
        }

        frame = new JFrame();
        frame.setTitle("aves - chatclient");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cards = new JPanel(new CardLayout());

        frame.add(cards, BorderLayout.CENTER);

        frame.setSize(320, 680);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
