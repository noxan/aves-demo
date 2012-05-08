/* 
 * Copyright (c) 2012, noxan
 * See LICENSE for details.
 */

package com.github.noxan.aves.demo.connect4.board;

import java.awt.Point;
import java.util.ArrayList;

public class Board {
    private char[][] board;
    private int boardHeight[];

    private int width = 7;
    private int height = 6;

    public Board() {
        board = new char[width][height];
        boardHeight = new int[width];
        reset();
    }

    public BoardInsertResult insert(int col, char value) {
        if(getHeight(col) < getHeight() && col >= 0 && col < getWidth()) {
            board[col][getHeight(col)] = value;
            if(checkWin(col, getHeight(col), value)) {
                return BoardInsertResult.WIN;
            }
            boardHeight[col]++;
            if(checkDraw()) {
                return BoardInsertResult.DRAW;
            }
            return BoardInsertResult.SUCCESS;
        }
        return BoardInsertResult.ERROR;
    }

    public int getHeight(int col) {
        return boardHeight[col];
    }

    public char get(int col, int row) {
        return board[col][row];
    }

    public void reset() {
        for(int col = 0; col < getWidth(); col++) {
            for(int row = 0; row < getHeight(); row++) {
                board[col][row] = '.';
            }
        }
        for(int col = 0; col < getWidth(); col++) {
            boardHeight[col] = 0;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private boolean checkDraw() {
        for(int col = 0; col < getWidth(); col++) {
            if(getHeight(col) < getHeight()) {
                return false;
            }
        }
        return true;
    }

    private boolean checkWin(int column, int row, char player) {
        if(player == '.') { // empty point to check
            return false;
        }
        for(int x = (column <= 0 ? column : column - 1); x <= (column + 1 < getWidth() ? column + 1 : getWidth() - 1); x++) {
            for(int y = (row > 0 ? row - 1 : row); y <= (row + 1 < getHeight() ? row + 1 : getHeight() - 1); y++) {
                if(get(x, y) == player) {
                    ArrayList<Point> points = new ArrayList<Point>();
                    points.add(new Point(column, row));
                    points.add(new Point(x, y));
                    if(checkWin(points, player)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkWin(ArrayList<Point> points, char player) {
        int dx = points.get(points.size() - 1).x - points.get(points.size() - 2).x;
        int dy = points.get(points.size() - 1).y - points.get(points.size() - 2).y;

        int x1 = points.get(points.size() - 1).x + dx;
        int y1 = points.get(points.size() - 1).y + dy;

        if(x1 >= 0 && x1 < getWidth() && y1 >= 0 && y1 < getHeight()) {
            if(get(x1, y1) == player) {
                Point p1 = new Point(x1, y1);
                if(!points.contains(p1)) {
                    points.add(p1);
                    checkWin(points, player);
                }
            }
        }

        int x2 = points.get(points.size() - 1).x - dx;
        int y2 = points.get(points.size() - 1).y - dy;

        if(x2 >= 0 && x2 < getWidth() && y2 >= 0 && y2 < getHeight()) {
            if(get(x2, y2) == player) {
                Point p2 = new Point(x2, y2);
                if(!points.contains(p2)) {
                    points.add(p2);
                    checkWin(points, player);
                }
            }
        }
        return points.size() >= 4;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int row = getHeight() - 1; row >= 0; row--) {
            for(int col = 0; col < getWidth(); col++) {
                sb.append(board[col][row]);
            }
            if(row != 0) { // check to remove last newline
                sb.append(System.getProperty("line.separator"));
            }
        }
        return sb.toString();
    }
}
