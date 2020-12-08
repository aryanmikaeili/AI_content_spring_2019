package com.company.players;

import com.company.Block;
import com.company.Board;
import com.company.IntPair;

import java.util.ArrayList;

public class Player96109725 extends Player {

    Board[] boards;
    int boardsSize;
    int boardPointer;
    IntPair[] intPairs;
    int[] fathers;
    int boardsDepth = 0;

    int searchTime = 2;
    final static int MAX = Integer.MAX_VALUE;
    final static int MIN = Integer.MIN_VALUE;

    @Override
    public IntPair getMove(Board board) {
        int myCode = this.getCol();
        int oppCode = (myCode == 1) ? 2 : 1;

        ArrayList<Integer> myBlocks = board.getBlocks(this.getCol());
        ArrayList<Integer> oppBlocks = board.getBlocks(oppCode);

        boards = new Board[1000];
        intPairs = new IntPair[1000];
        fathers = new int[1000];
        boardsSize = 0;

        boards[0] = board;

        boardsSize++;

//        Integer blockType = myBlocks.get(0);
//        Block nextBlock = new Block(blockType, this.getCol());
//
//
//        int score = -1;
//        int staticScore = board.getScore(this.getCol()) - 1;
//        IntPair result = null;
//
//        for (int i = 0; i < board.size; i++) {
//            for (int j = 0; j < board.size; j++) {
//                IntPair intPair = new IntPair(i, j);
//                nextBlock.setOrigin(intPair);
//                ArrayList<IntPair> blockCoordinate = nextBlock.getCoordinates();
////                if (canSet(board, blockCoordinate)) {
////                    System.out.println("turn" + board.getNumberOfMoves());
////                    System.out.println("player " + this.getCol() + " score: " + board.getScore(this.getCol()));
////                    return intPair;
////                }
//
//                if (canSet(board, blockCoordinate)) {
//                    Board boardPrime = new Board();
//                    boardPrime.move(intPair, this.getCol(), blockType);
//                    int nextScore = boardPrime.getScore(this.getCol());
//                    if (score < nextScore) {
//                        result = intPair;
//                        score = nextScore;
//                    }
//                }
//            }
//        }
//
//        if (result == null) {
//            return null;
//        }
//
//        System.out.println("turn" + board.getNumberOfMoves());
//        System.out.println("player " + this.getCol() + " score: " + board.getScore(this.getCol()));
//        return result;
        setBoards(searchTime);
        int index = minMax(0, 0, true, MIN, MAX);
        return getIntPair(index);
    }

    private IntPair getIntPair(int index) {
        int result = index;
        while (fathers[result] != 0) {
            result = fathers[result];
        }
        return intPairs[result];
    }

    private int minMax(int depth, int nodeIndex, boolean maximizingPlayer, int alpha, int beta) {
        int myCode = this.getCol();
        int oppCode = (myCode == 1) ? 2 : 1;

        if (depth == boardsDepth) {
            return nodeIndex;
        }
        if (maximizingPlayer) {
            int best = MIN;
            int result = nodeIndex;
            for (int i = 0; i < 2; i++) {
                int val = boards[minMax(depth + 1, nodeIndex * 2 + i, false, alpha, beta)].getScore(myCode);
                best = Math.max(best, val);
                if (val > best) {
                    result = nodeIndex * 2 + i;
                }

                alpha = Math.max(alpha, val);
            }
            return nodeIndex;
        } else {
            int best = MAX;
            int result = nodeIndex;
            for (int i = 0; i < 2; i++) {
                int val = boards[minMax(depth + 1, nodeIndex * 2 + i, true, alpha, beta)].getScore(myCode);
                if (val < best) {
                    result = nodeIndex * 2 + i;
                }

                beta = Math.min(alpha, val);
            }
            return result;
        }
    }

    private void setBoards(int searchTime) {
        boolean colbool = true;
        int myCode = this.getCol();
        int oppCode = (myCode == 1) ? 2 : 1;

        int q = 0;
        int p = 1;

        for (int i = 0; i < 2 * searchTime; i++) {
            int col = colbool ? myCode : oppCode;
            expandBoards(q, p, col, i/2);
            q = p;
            p = boards.length;
            colbool = !colbool;
        }

        Board[] newBoards = new Board[p - q];
        for (int i = q; i < p; i++) {
            newBoards[i - q] = boards[i];
        }
        boards = newBoards;
        boardPointer = q;
        boardsSize = p - q;

    }

    private void expandBoards(int q, int p, int col, int blockNumber) {

        int pointer = q;
        while (pointer < p) {
            Board board = boards[pointer];
            ArrayList<Integer> blocks = board.getBlocks(col);
            Integer blockType = blocks.get(blockNumber);

            for (int i = 0; i < board.size; i++) {
                for (int j = 0; j < board.size; j++) {
                    Block nextBlock = new Block(blockType, col);
                    IntPair intPair = new IntPair(i, j);
                    nextBlock.setOrigin(intPair);
                    if (canSet(board, nextBlock.getCoordinates())) {
                        Board nextBoard = board;
                        nextBoard.move(intPair, col, blockType);
                        boards[boardsSize] = nextBoard;
                        fathers[boardsSize] = pointer;
                        intPairs[boardsSize] = intPair;
                        boardsSize++;
                    }
                }
            }
            pointer++;
        }
        boardsDepth++;
    }

    private boolean canSet(Board board, ArrayList<IntPair> blockCoordinate) {
        boolean flag = true;
        for (int k = 0; k < blockCoordinate.size(); k++) {
            if (blockCoordinate.get(k).x < 0 || blockCoordinate.get(k).y < 0 || blockCoordinate.get(k).y >= board.size || blockCoordinate.get(k).x >= board.size) {
                flag = false;
            } else if (board.getCell(blockCoordinate.get(k).x, blockCoordinate.get(k).y).getColor() != 0) {
                flag = false;
            }

        }
        return flag;
    }

    public Player96109725(int col) {
        super(col);
    }

    @Override
    public int getCol() {
        return super.getCol();
    }
}
