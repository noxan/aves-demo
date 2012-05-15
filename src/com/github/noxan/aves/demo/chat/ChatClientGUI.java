package com.github.noxan.aves.demo.chat;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import layout.TableLayout;

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
        cards.add(new ConnectPanel(client), "connect");

        frame.add(cards, BorderLayout.CENTER);

        frame.setSize(320, 680);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    private class ConnectPanel extends JPanel {
        private static final long serialVersionUID = -8154588851543816456L;

        private ChatClient client;

        private JButton connectButton;

        public ConnectPanel(ChatClient client) {
            this.client = client;
            setLayout(new TableLayout(new double[][] { { TableLayout.FILL, 0.5f, TableLayout.FILL }, { TableLayout.FILL, TableLayout.PREFERRED, 5, TableLayout.PREFERRED, TableLayout.FILL } }));
            initComponents();
        }

        private void initComponents() {
            JLabel offlineLabel = new JLabel("Server currently offline.");
            offlineLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(offlineLabel, "1,1");

            connectButton = new JButton("Try reconnect");
            connectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    client.connect();
                }
            });
            add(connectButton, "1,3");
        }
    }
}
