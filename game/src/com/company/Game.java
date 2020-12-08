package com.company;

import com.company.graphic.GamePanel;
import com.company.players.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

class Game {

    private final static int timeLimit = 5000;
    private Player[] players = new Player[2];


    private Board board;
    private int turn = 0;
    private IntPair newOrigin;

    Game(Player p1, Player p2) {
        players[0] = p1;
        players[1] = p2;


    }

    int start() {
        board = new Board();
        GamePanel gamePanel = new GamePanel(board);
        gamePanel.setBounds(0, 0, GamePanel.size * (Board.size + 1), GamePanel.size * (Board.size + 1));
        JFrame frame = new JFrame("Cup");
        frame.setLayout(null);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(GamePanel.size * (Board.size + 1), GamePanel.size * (Board.size + 1) + 22));
        frame.pack();
        frame.setVisible(true);
        frame.add(gamePanel);
        gamePanel.repaint();
        while (board.win() == 0) {
            newOrigin = new IntPair(-10, -10);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread t = new Thread(() -> {
                try {
                    newOrigin = players[turn].getMove(new Board(board));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            t.start();
            try {
                t.join(timeLimit);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return players[1 - turn].getCol();
            }
            if ( newOrigin == null || newOrigin.x == -10) {
                System.out.println(board.getBlocks(turn + 1));
                System.out.println(board.getBlocks(turn + 1).get(0));
                System.out.println("Player " + players[turn].getCol() + " has exceeded the time limit\n" +
                        "Player " + players[1 - turn].getCol() + " has won\n");
                return players[1 - turn].getCol();
            }
            System.out.println(newOrigin.x + " " +newOrigin.y);
            System.out.println(board.getBlocks(turn + 1).get(0));
            int res = board.move(newOrigin, turn + 1,  board.getBlocks(turn + 1).get(0));
            if (res == -1) {
                System.out.println(board.getBlocks(turn + 1));
                System.out.println("Player " + players[turn].getCol() + " has made an invalid move\n" +
                        "Player " + players[1 - turn].getCol() + " has won\n");
                return players[1 - turn].getCol();
            }
            if (res == -2) {
                System.out.println("No more moves!");
                if (board.getScore(1) > board.getScore(2)) {
                    System.out.println("Player 1 has won");
                    System.out.println("score player 1: " + board.getScore(1));
                    System.out.println("score player 2: " + board.getScore(2));
                    return 1;
                } else if (board.getScore(1) < board.getScore(2)) {
                    System.out.println("Player 2 has won");
                    System.out.println("score player 1: " + board.getScore(1));
                    System.out.println("score player 2: " + board.getScore(2));
                    return 2;
                } else {
                    System.out.println("Draw!");
                    System.out.println("score player 1: " + board.getScore(1));
                    System.out.println("score player 2: " + board.getScore(2));
                    return 0;
                }

            }
            gamePanel.repaint();

            board.getBlocks(turn + 1).remove(0);
            board.getBlocks(turn + 1).add(ThreadLocalRandom.current().nextInt(0, 8));

            turn = 1 - turn;
        }
            System.out.println("Player " + board.win() + " has won\n");
            return board.win();
    }

}
