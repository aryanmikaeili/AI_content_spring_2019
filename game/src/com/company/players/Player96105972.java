package com.company.players;

import com.company.Block;
import com.company.Board;
import com.company.IntPair;

import java.util.ArrayList;

public class Player96105972 extends Player {

    private static final int MAX_DEPTH = 4;
    private static final int MAX_UTILITY = 200;
    private static final int MIN_UTILITY = -200;

    public Player96105972(int col) {
        super(col);
    }

    @Override
    public IntPair getMove(Board board) {
        ReturnValue res = maxValue(board, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        if (res.getAction() == null)
            return new IntPair(0, 0);
        return res.getAction();
    }

    private ReturnValue maxValue(Board board, int d, int alpha, int beta) {
        ArrayList<IntPair> successors = getSuccessors(board, this.getCol(), d);
        if (isCutoff(successors, board, d))
            return new ReturnValue(getUtility(board, successors.size(), d, this.getCol()), null);
        int v = Integer.MIN_VALUE;
        IntPair action = null;
        for (IntPair successor : successors) {
            Board copy = new Board(board);
            copy.move(successor, this.getCol(), copy.getBlocks(this.getCol()).get(d / 2));
            int temp = minValue(copy, d + 1, alpha, beta).getUtility();
            if (temp > v) {
                v = temp;
                action = successor;
            }
            if (v >= beta)
                return new ReturnValue(v, action);
            alpha = Integer.max(v, alpha);
        }
        return new ReturnValue(v, action);
    }

    private ReturnValue minValue(Board board, int d, int alpha, int beta) {
        ArrayList<IntPair> successors = getSuccessors(board, 3 - this.getCol(), d);
        if (isCutoff(successors, board, d))
            return new ReturnValue(getUtility(board, successors.size(), d, 3 - this.getCol()), null);
        int v = Integer.MAX_VALUE;
        IntPair action = null;
        for (IntPair successor : successors) {
            Board copy = new Board(board);
            copy.move(successor, 3 - this.getCol(), copy.getBlocks(3 - this.getCol()).get(d / 2));
            int temp = maxValue(copy, d + 1, alpha, beta).getUtility();
            if (temp < v) {
                v = temp;
                action = successor;
            }
            if (v <= alpha)
                return new ReturnValue(v, action);
            beta = Integer.min(v, beta);
        }
        return new ReturnValue(v, action);
    }

    private int getUtility(Board board, int successorsSize, int d, int color) {
        if (d > MAX_DEPTH)
            return 2 * (board.getScore(this.getCol()) - board.getScore(3 - this.getCol())) + 1 * halfFull(board);
        if (board.getNumberOfMoves() > 80) {
            if (board.getScore(this.getCol()) > board.getScore(3 - this.getCol()))
                return MAX_UTILITY;
            else
                return MIN_UTILITY;
        }
        if (successorsSize == 0 && color == this.getCol())
            return MIN_UTILITY;
        if (successorsSize == 0 && color == 3 - this.getCol())
            return MAX_UTILITY;
        return 0;
    }

    private int halfFull(Board board) {
        int hf = 0;
        for (int i = 0; i < Board.size; i++) {
            int countR = 0, countC = 0;
            for (int j = 0; j < Board.size; j++) {
                if (board.getCell(i, j).getColor() == this.getCol())
                    countR++;
                if (board.getCell(j, i).getColor() == this.getCol())
                    countC++;
            }
            if (countR >= Board.size / 2)
                hf++;
            if (countC >= Board.size / 2)
                hf++;
        }
        return hf;
    }

    private ArrayList<IntPair> getSuccessors(Board board, int color, int d) {
        ArrayList<IntPair> successors = new ArrayList<>();
        Block nextBlock = new Block(board.getBlocks(color).get(d / 2), color);
        for (int i = 0; i < Board.size; i++) {
            for (int j = 0; j < Board.size; j++) {
                boolean flag = true;
                nextBlock.setOrigin(new IntPair(j, i));
                ArrayList<IntPair> blockCoordinate = nextBlock.getCoordinates();
                for (IntPair intPair : blockCoordinate) {
                    if (intPair.x < 0 || intPair.y < 0 || intPair.y >= Board.size || intPair.x >= Board.size)
                        flag = false;
                    else if (board.getCell(intPair.x, intPair.y).getColor() != 0)
                        flag = false;
                }
                if(flag)
                    successors.add(new IntPair(j, i));
            }
        }
        return successors;
    }

    private boolean isCutoff(ArrayList<IntPair> successors, Board board, int d) {
        return d > MAX_DEPTH || successors.size() == 0 || board.getNumberOfMoves() > 80;
    }
}

class ReturnValue {

    private int utility;
    private IntPair action;

    ReturnValue(int utility, IntPair action) {
        this.utility = utility;
        this.action = action;
    }

    int getUtility() {
        return utility;
    }

    IntPair getAction() {
        return action;
    }
}