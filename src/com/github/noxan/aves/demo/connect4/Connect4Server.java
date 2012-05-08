/* 
 * Copyright (c) 2012, noxan
 * See LICENSE for details.
 */

package com.github.noxan.aves.demo.connect4;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.noxan.aves.demo.connect4.board.Board;
import com.github.noxan.aves.net.Connection;
import com.github.noxan.aves.server.Server;
import com.github.noxan.aves.server.ServerHandler;
import com.github.noxan.aves.server.SocketServer;

public class Connect4Server implements ServerHandler {
    public static void main(String[] args) {
        Server server = new SocketServer(new Connect4Server());
        try {
            server.start();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private Logger logger = Logger.getLogger(getClass().getName());

    private Connection[] playerConnections;
    private Character[] playerTokens;
    private int playerActive;

    private SocketServer server;

    private boolean playable;

    private Board board;
    private Status status;

    public Connect4Server() {
        board = new Board();
        status = Status.LOBBY;
        playable = false;
        playerConnections = new Connection[2];
        playerTokens = new Character[2];
        playerTokens[0] = 'x';
        playerTokens[1] = 'o';
        resetPlayer();
    }

    private void nextPlayer() {
        playerActive++;
        playerActive %= 2;
    }

    private void resetPlayer() {
        playerActive = (int)Math.round(Math.random());
    }

    @Override
    public void readData(Connection connection, Object data) {
        String messageString = ((String)data).trim();
        String parts[] = messageString.split(" ");
        String message = parts[0].toUpperCase();
        int argc = parts.length - 1;
        String[] args = new String[argc];
        System.arraycopy(parts, 1, args, 0, argc);

        int playerid = 0;
        if(playerConnections[1] == connection) {
            playerid = 1;
        }

        logger.log(Level.INFO, messageString);

        try {
            switch(message) {
            case "CHAT":
                if(argc > 0) {
                    try {
                        server.broadcast(connection, "CHAT " + args[0]);
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                switch(status) {
                case PLAY:
                    switch(message) {
                    case "SURRENDER":
                        connection.write("LOSE");
                        server.broadcast(connection, "WIN");
                        board.reset();
                        try {
                            server.broadcast("LOBBY");
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                        status = Status.LOBBY;
                        break;
                    case "ADD":
                        if(playerConnections[playerActive] == connection) {
                            if(argc > 0) {
                                int col = Integer.parseInt(args[0]);
                                switch(board.insert(col, playerTokens[playerid])) {
                                case SUCCESS:
                                    connection.write("ADD OK");
                                    nextPlayer();
                                    server.broadcast(board.toString());
                                    server.broadcast("-------");
                                    break;
                                case DRAW:
                                    server.broadcast("DRAW");
                                    board.reset();
                                    resetPlayer();
                                    try {
                                        server.broadcast("LOBBY");
                                    } catch(IOException e) {
                                        e.printStackTrace();
                                    }
                                    status = Status.LOBBY;
                                    break;
                                case WIN:
                                    connection.write("WIN");
                                    server.broadcast(connection, "LOSE");
                                    board.reset();
                                    resetPlayer();
                                    try {
                                        server.broadcast("LOBBY");
                                    } catch(IOException e) {
                                        e.printStackTrace();
                                    }
                                    status = Status.LOBBY;
                                    break;
                                default:
                                    connection.write("ADD ERROR");
                                    break;
                                }
                            }
                        } else {
                            connection.write("ADD ERROR: IT IS NOT YOUR TURN");
                        }
                        break;
                    }
                    break;
                case LOBBY:
                    switch(message) {
                    case "START":
                        if(playable) {
                            server.broadcast(playerConnections[playerActive], "PLAY");
                            status = Status.PLAY;
                            playerConnections[playerActive].write("YOUR TURN");
                        } else {
                            connection.write("GAME NOT READY TO PLAY");
                        }
                    }
                    break;
                }
                break;
            }
        } catch(IOException e) {

        }
    }

    @Override
    public void clientConnect(Connection connection) {
        try {
            connection.write("HELLO");
            int count = server.getConnections().size();
            if(count < 2) {
                playerConnections[0] = connection;
            } else if(count > 2) {
                connection.write("BYE GAME IS FULL");
                connection.stop();
            } else {
                try {
                    server.broadcast("GAME READY TO PLAY");
                } catch(IOException e) {
                    e.printStackTrace();
                }
                playable = true;
                playerConnections[1] = connection;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clientDisconnect(Connection connection) {
        if(playerConnections[0] == connection) {
            playerConnections[0] = playerConnections[1];
        }
        playable = false;
        playerActive = 0;
        try {
            server.broadcast("GAME NOT READY TO PLAY");
            if(status == Status.PLAY) {
                server.broadcast("LOBBY");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clientLost(Connection connection) {
        if(playerConnections[0] == connection) {
            playerConnections[0] = playerConnections[1];
        }
        playable = false;
        playerActive = 0;
        try {
            server.broadcast("GAME NOT READY TO PLAY");
            if(status == Status.PLAY) {
                server.broadcast("LOBBY");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
