package com.company.players;

import com.company.Board;
import com.company.IntPair;

public class Player96109599 extends Player {

    int maxDepth = 3;
    private static final int INFINITY = 1000000;

    int totalStatesChecked = 0;
    int invalidMovesChecked = 0;

    public Player96109599(int col) {
        super(col);
    }

    @Override
    public IntPair getMove(Board board) {
        totalStatesChecked = 0;
        invalidMovesChecked = 0;
        Supplementary result = maxValue(board, 0, -INFINITY, INFINITY);
        if (result.origin != null) {
            System.out.println("Im here");
        } else {
            System.out.println("Im here");
            System.out.println("My block is: " + board.getBlocks(getCol()).get(0));
            System.out.println("My color is" + getCol());
            System.out.println("I, Player " + getCol() + " surrender");
            result.origin = new IntPair(0,0);
        }

        System.out.println("Percent of invalid moves checked:" + (1.0 * invalidMovesChecked / totalStatesChecked * 100));

        return result.origin;
    }


    private Supplementary maxValue(Board board, int depth, int alpha, int beta) {

        if (depth == maxDepth) {
            return new Supplementary(board.getScore(getCol()), null);
        }

        int v = -INFINITY;
        IntPair best = null;

        for (int i = 0; i < Board.size; i++) {
            for (int j = 0; j < Board.size; j++) {
                Board newBoard = new Board(board);
                int result = newBoard.move(new IntPair(i, j), getCol(), board.getBlocks(getCol()).get(depth / 2));
                totalStatesChecked++;
                if (result > -1) {
                    int value = minValue(newBoard, depth + 1, alpha, beta);

                    if (value >= v) {
                        v = value;
                        best = new IntPair(i, j);
                    }
                    if (v >= beta) {
                        return new Supplementary(v, new IntPair(i, j));
                    }
                    alpha = (alpha < v) ? v : alpha;
                } else {
                    invalidMovesChecked++;
                    if (best == null && result == -2) {
                        best = new IntPair(i, j);
                    }
                }
            }
        }

        return new Supplementary(v, best);
    }

    private int minValue(Board board, int depth, int alpha, int beta) {

        if (depth == maxDepth) {
            return board.getScore(getCol());
        }

        int v = INFINITY;

        for (int i = 0; i < Board.size; i++) {
            for (int j = 0; j < Board.size; j++) {
                Board newBoard = new Board(board);
                int result = newBoard.move(new IntPair(i, j), 3 - getCol(), board.getBlocks(3 - getCol()).get(depth / 2));
                totalStatesChecked++;
                if (result > -1) {
                    int value = maxValue(newBoard, depth + 1, alpha, beta).value;
                    v = (v > value) ? value : v;
                    if (v <= alpha) {
                        return v;
                    }
                    beta = (beta > v) ? v : beta;
                } else {
                    invalidMovesChecked++;
                }
            }
        }

        return v;
    }
}


class Supplementary {
    int value;
    IntPair origin;

    public Supplementary(int value, IntPair origin) {
        this.value = value;
        this.origin = origin;
    }
}