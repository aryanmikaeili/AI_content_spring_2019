package com.company.players;

import com.company.Block;
import com.company.Board;
import com.company.IntPair;
import javafx.util.Pair;

import java.sql.Wrapper;
import java.util.ArrayList;

public class Player96106152 extends Player {
    private static IntPair noMovement = new IntPair(0, 0);
    private int decisionDepth = 5;

    public Player96106152(int col) {
        super(col);
    }

    @Override
    public IntPair getMove(Board board)
    {
        MiniMaxResult res = getMaxMove(board, getCol(), decisionDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return res.getMovement();
    }

    public MiniMaxResult getMinMove(Board board, int playerColor, int depth, int a, int b) {
        int newDepth = depth - 1;
        if (depth == 0) {
            return new MiniMaxResult(board.getScore(getCol()) - board.getScore(3 - getCol()), noMovement);
        }
        MiniMaxResult bestMovement = new MiniMaxResult(Integer.MAX_VALUE, noMovement);
        ArrayList<Pair<Board, IntPair>> successors = getBoardSuccessors(board, depth, playerColor);
        if (successors.size() == 0) {
            return bestMovement;
        }
        for (Pair<Board, IntPair> successor : successors) {
            MiniMaxResult res = getMaxMove(successor.getKey(), 3 - playerColor, newDepth, a, b);
            if (res.getScore() < bestMovement.getScore()) {
                bestMovement = new MiniMaxResult(res.getScore(), successor.getValue());
            }
            if (res.getScore() <= a) {
                return bestMovement;
            }
            b = Math.min(res.getScore(), b);

        }
        return bestMovement;
    }

    public MiniMaxResult getMaxMove(Board board, int playerColor, int depth, int a, int b) {
        int newDepth = depth - 1;
        if (depth == 0) {

            return new MiniMaxResult(board.getScore(getCol()) - board.getScore(getCol()), noMovement);
        }
        MiniMaxResult bestMovement = new MiniMaxResult(Integer.MIN_VALUE, noMovement);
        ArrayList<Pair<Board, IntPair>> successors = getBoardSuccessors(board, depth, playerColor);
        if (successors.size() == 0) {
            return bestMovement;
        }
        for (Pair<Board, IntPair> successor : successors) {
            MiniMaxResult res = getMinMove(successor.getKey(), 3 - playerColor, newDepth, a, b);
            if (res.getScore() > bestMovement.getScore()) {
                bestMovement = new MiniMaxResult(res.getScore(), successor.getValue());
            }
            if (res.getScore() >= b) {
                return bestMovement;
            }
            a = Math.max(res.getScore(), a);
        }
        return bestMovement;

    }

    public ArrayList<Pair<Board, IntPair>> getBoardSuccessors(Board board, int depth, int player) {
        ArrayList<Pair<Board, IntPair>> successors = new ArrayList<>();
        Block nextBlock = new Block(board.getBlocks(player).get((decisionDepth - depth)/2), player);
        for (int i = 0; i < board.size; i++) {
            for (int j = 0; j < board.size; j++) {
                boolean flag = true;
                nextBlock.setOrigin(new IntPair(j, i));
                ArrayList<IntPair> blockCoordinate = nextBlock.getCoordinates();
                for (int k = 0; k < blockCoordinate.size(); k++) {
                    if (blockCoordinate.get(k).x < 0 || blockCoordinate.get(k).y < 0 || blockCoordinate.get(k).y >= board.size || blockCoordinate.get(k).x >= board.size) {
                        flag = false;
                    } else if (board.getCell(blockCoordinate.get(k).x, blockCoordinate.get(k).y).getColor() != 0) {
                        flag = false;
                    }

                }
                if(flag) {
                    Board newBoard = new Board(board);
                    IntPair origin = new IntPair(j, i);
                    newBoard.move(origin, player,
                            board.getBlocks(player).get(decisionDepth - depth));
                    successors.add(new Pair<Board, IntPair>(newBoard, origin));
                }
            }
        }
        return successors;
    }
}

class MiniMaxResult {
    private int score;
    private IntPair movement;

    public MiniMaxResult(int score, IntPair movement) {
        this.score = score;
        this.movement = movement;
    }

    public int getScore() {
        return score;
    }

    public IntPair getMovement() {
        return movement;
    }
}

