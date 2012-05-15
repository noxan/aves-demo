package com.github.noxan.aves.demo.chat;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import layout.TableLayout;

public class ChatClientGUI {
    private JFrame frame;
    private JPanel cards;
    private ChatPanel chatPanel;

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
        cards.add(new LoginPanel(client), "login");
        chatPanel = new ChatPanel(client);
        cards.add(chatPanel, "chat");

        frame.add(cards, BorderLayout.CENTER);

        frame.setSize(320, 680);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public void setStatus(ChatClientStatus status) {
        switch(status) {
            case LOGIN:
                ((CardLayout)cards.getLayout()).show(cards, "chat");
                break;
            case CONNECT:
                ((CardLayout)cards.getLayout()).show(cards, "login");
                break;
            case DISCONNECT:
            default:
                ((CardLayout)cards.getLayout()).show(cards, "connect");
                break;
        }
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

    private class LoginPanel extends JPanel implements ActionListener {
        private static final long serialVersionUID = 4260715489603841434L;

        private ChatClient client;

        private JTextField usernameField;
        private JPasswordField passwordField;
        private JButton loginButton;

        public LoginPanel(ChatClient client) {
            this.client = client;
            setLayout(new TableLayout(new double[][] { { TableLayout.FILL, 0.5f, TableLayout.FILL },
                    { TableLayout.FILL, TableLayout.PREFERRED, 5, TableLayout.PREFERRED, 5, TableLayout.PREFERRED, 5, TableLayout.PREFERRED, 5, TableLayout.PREFERRED, TableLayout.FILL } }));

            initComponents();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            client.login(usernameField.getText(), new String(passwordField.getPassword()));
        }

        private void initComponents() {
            JLabel usernameLabel = new JLabel("Username");
            usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(usernameLabel, "1,1");

            usernameField = new JTextField();
            add(usernameField, "1,3");

            JLabel passwordLabel = new JLabel("Password");
            passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(passwordLabel, "1,5");

            passwordField = new JPasswordField();
            add(passwordField, "1,7");

            loginButton = new JButton("Login");
            loginButton.addActionListener(this);
            add(loginButton, "1,9");
        }
    }

    private class ChatPanel extends JPanel {
        private static final long serialVersionUID = -848472248464283937L;

        private ChatClient client;

        private JTextArea chatTextArea;
        private JTextField inputField;

        public ChatPanel(ChatClient client) {
            this.client = client;
            setLayout(new TableLayout(new double[][] { { 5, TableLayout.FILL, 5 }, { 5, TableLayout.FILL, 5, TableLayout.PREFERRED, 5 } }));
            initComponents();
        }

        public void addChatText(String text) {
            chatTextArea.append(text + System.getProperty("line.separator"));
        }

        public void initComponents() {
            chatTextArea = new JTextArea("aves - chatclient");
            chatTextArea.setEditable(false);
            add(new JScrollPane(chatTextArea), "1,1");

            inputField = new JTextField();
            inputField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                        client.chat(inputField.getText());
                        inputField.setText("");
                    }
                }
            });
            add(inputField, "1,3");
        }
    }
}
