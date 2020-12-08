package com.company.players;

import com.company.Block;
import com.company.Board;
import com.company.IntPair;
import javafx.util.Pair;

import java.util.ArrayList;

public class Player96105564 extends Player {
    private int maxDepth = 4;

    public Player96105564(int col) {
        super(col);
    }

    private int getUtility(Board board, int stateType, int currentColor) {
        int utility = 0;
        if (stateType == -1 && currentColor == this.getCol())
            utility -= 100;
        if (stateType == -1 && currentColor == 3 - this.getCol())
            utility += 100;
        if (stateType == -2) {
            if (board.getScore(3 - this.getCol()) > board.getScore(this.getCol()))
                utility -= 100;
            else
                utility += 100;
        }
        if (stateType == 0) {
            utility += (board.getScore(this.getCol()) - board.getScore(3 - this.getCol()));
            for (int i = 0; i < Board.size; i++) {
                int playerColor1 = 0, playerColor2 = 0;
                for (int j = 0; j < Board.size; j++) {
                    int color1 = board.getCell(i, j).getColor();
                    int color2 = board.getCell(j, i).getColor();
                    if (color1 != 0) {
                        if (color1 == this.getCol())
                            playerColor1++;
                    }
                    if (color2 != 0) {
                        if (color2 == this.getCol())
                            playerColor2++;
                    }
                }
                if (playerColor1 >= Board.size / 2)
                    utility++;
                if (playerColor2 >= Board.size / 2)
                    utility++;
            }
        }

        return utility;
    }


    private Pair<Integer, IntPair> max_value(Board board, int alpha, int beta, int depth) {
        ArrayList<IntPair> successors = getSuccessor(board, this.getCol(), depth);
        int terminalState = isTerminal(board, successors);
        if (terminalState != 0)
            return new Pair<>(getUtility(board, terminalState, this.getCol()), null);
        if (depth > maxDepth)
            return new Pair<>(getUtility(board, 0, this.getCol()), null);
        int value = Integer.MIN_VALUE;
        IntPair origin = new IntPair(0, 0);
        for (IntPair intPair : successors) {
            Board copyBoard = new Board(board);
            copyBoard.move(intPair, this.getCol(), copyBoard.getBlocks(this.getCol()).get(depth / 2));
            Pair<Integer, IntPair> pair = min_value(copyBoard, alpha, beta, depth + 1);
            if (value < pair.getKey()) {
                value = pair.getKey();
                origin = intPair;
            }
            if (value >= beta)
                return new Pair<>(value, origin);
            alpha = Math.max(alpha, value);
        }

        return new Pair<>(value, origin);
    }

    private Pair<Integer, IntPair> min_value(Board board, int alpha, int beta, int depth) {
        ArrayList<IntPair> successors = getSuccessor(board, 3 - this.getCol(), depth);
        int terminalState = isTerminal(board, successors);
        if (terminalState != 0)
            return new Pair<>(getUtility(board, terminalState, 3 - this.getCol()), null);
        if (depth > maxDepth)
            return new Pair<>(getUtility(board, 0, 3 - this.getCol()), null);
        int value = Integer.MAX_VALUE;
        IntPair origin = new IntPair(0, 0);
        for (IntPair intPair : successors) {
            Board copyBoard = new Board(board);
            copyBoard.move(intPair, 3 - this.getCol(), copyBoard.getBlocks(3 - this.getCol()).get(depth / 2));
            Pair<Integer, IntPair> pair = max_value(copyBoard, alpha, beta, depth + 1);
            if (value > pair.getKey()) {
                value = pair.getKey();
                origin = intPair;
            }
            if (value <= alpha)
                return new Pair<>(value, origin);
            beta = Math.min(beta, value);
        }
        return new Pair<>(value, origin);
    }

    private ArrayList<IntPair> getSuccessor(Board board, int currentColor, int depth) {
        ArrayList<IntPair> successors = new ArrayList<>();
        Block nextBlock = new Block(board.getBlocks(currentColor).get(depth / 2), this.getCol());
        for (int i = 0; i < Board.size; i++) {
            for (int j = 0; j < Board.size; j++) {
                boolean flag = true;
                nextBlock.setOrigin(new IntPair(j, i));
                ArrayList<IntPair> blockCoordinate = nextBlock.getCoordinates();
                for (int k = 0; k < blockCoordinate.size(); k++) {
                    if (blockCoordinate.get(k).x < 0 || blockCoordinate.get(k).y < 0 || blockCoordinate.get(k).y >= Board.size || blockCoordinate.get(k).x >= Board.size) {
                        flag = false;
                    } else if (board.getCell(blockCoordinate.get(k).x, blockCoordinate.get(k).y).getColor() != 0) {
                        flag = false;
                    }

                }
                if (flag)
                    successors.add(new IntPair(j, i));

            }
        }
        return successors;
    }

    private int isTerminal(Board board, ArrayList<IntPair> successors) {
        if (successors.size() == 0)
            return -1;
        if (board.getNumberOfMoves() > 80)
            return -2;
        return 0;
    }


    @Override
    public IntPair getMove(Board board) {
        Pair<Integer, IntPair> pair = max_value(board, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        if (pair.getValue() == null)
            return new IntPair(0, 0);
        return pair.getValue();
    }
}
