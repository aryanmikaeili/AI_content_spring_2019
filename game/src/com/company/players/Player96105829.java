package com.company.players;

import com.company.Block;
import com.company.Board;
import com.company.IntPair;

import java.util.ArrayList;

public class Player96105829 extends Player {

    public Player96105829(int col) {
        super(col);

    }

    public Pair maxValue(Board board, int alpha, int beta, int depth) {

        if (depth == 4)
            return new Pair(board.getScore(this.getCol()) - board.getScore((this.getCol() + 1) % 2), null);

        ArrayList<IntPair> children = new ArrayList<>();

        Block nextBlock = new Block(board.getBlocks(this.getCol()).get(0), this.getCol());
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
                if (flag)
                    children.add(new IntPair(j, i));

            }
        }

        int maxScore = -100000;
        IntPair bestIntPair = null;
        int childScore;

        for (IntPair intPair : children) {
            Board localBoard = new Board(board);
            localBoard.move(intPair, this.getCol(), localBoard.getBlocks(this.getCol()).get(0));
            childScore = minValue(localBoard, alpha, beta, depth + 1).score;
            if (childScore > maxScore) {
                maxScore = childScore;
                bestIntPair = intPair;
            }
            if (maxScore >= beta)
                return new Pair(maxScore, bestIntPair);
            alpha = Math.max(alpha, maxScore);
        }

        return new Pair(maxScore, bestIntPair);
    }

    public Pair minValue(Board board, int alpha, int beta, int depth) {

        if (depth == 4)
            return new Pair(board.getScore(this.getCol()) - board.getScore((this.getCol() + 1) % 2), null);

        ArrayList<IntPair> children = new ArrayList<>();

        Block nextBlock = new Block(board.getBlocks(this.getCol()).get(0), this.getCol());
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
                if (flag)
                    children.add(new IntPair(j, i));

            }
        }

        int minScore = 100000;
        IntPair bestIntPair = null;
        int childScore;

        for (IntPair intPair : children) {
            Board localBoard = new Board(board);
            localBoard.move(intPair, (this.getCol() + 1) % 2, localBoard.getBlocks((this.getCol() + 1) % 2).get(0));
            childScore = maxValue(localBoard, alpha, beta, depth + 1).score;
            if (childScore < minScore) {
                minScore = childScore;
                bestIntPair = intPair;
            }
            if (minScore <= alpha)
                return new Pair(minScore, bestIntPair);
            beta = Math.min(beta, minScore);
        }

        return new Pair(minScore, bestIntPair);
    }

    @Override
    public IntPair getMove(Board board) {

        IntPair intPair = maxValue(board, Integer.MIN_VALUE, Integer.MAX_VALUE, 0).intPair;

        if (intPair == null)
            return new IntPair(0, 0);
        else
            return intPair;
    }
}

class Pair {
    public int score;
    public IntPair intPair;

    public Pair(int score, IntPair intPair) {
        this.score = score;
        this.intPair = intPair;
    }
}