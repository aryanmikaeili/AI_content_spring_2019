package com.company.players;

import com.company.Block;
import com.company.Board;
import com.company.IntPair;

import java.util.ArrayList;

public class Player95105549 extends Player {
    public Player95105549(int col) {
        super(col);
    }

    @Override
    public IntPair getMove(Board board) {
        Block nextBlock = new Block(board.getBlocks(this.getCol()).get(0), this.getCol());
        IntPair bestMove = new IntPair(0,0);
        int bestScore = Integer.MIN_VALUE;
        for (int i = 0; i < board.size - 2; i++) {
            for (int j = 0; j < board.size - 2; j++) {
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
                    IntPair pair = new IntPair(j, i);
                    Board tmpBoard = new Board(board);
                    tmpBoard.move(pair, this.getCol(), tmpBoard.getBlocks(this.getCol()).get(0));
                    int moveScore = minimax(tmpBoard, 0, this.getCol(), Integer.MIN_VALUE, Integer.MAX_VALUE, true);
                    if(moveScore > bestScore){
                        bestScore = moveScore;
                        bestMove = pair;
                    }
                }
            }
        }
        return bestMove;
    }

    public int minimax(Board board, int depth, int player, int alpha, int beta, boolean isMax){
        if(depth == 4){
            int tmpScore = board.getScore(this.getCol());
            tmpScore -= board.getScore(3 - this.getCol());
            return tmpScore;
        }
        Block nextBlock = new Block(board.getBlocks(this.getCol()).get(0), this.getCol());
        IntPair pair;
        int best;
        if(isMax) {
            best = Integer.MIN_VALUE;
            for (int i = 0; i < board.size - 2; i++) {
                for (int j = 0; j < board.size - 2; j++) {
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
                    if (flag) {
                        pair = new IntPair(j, i);
                        Board tmpBoard = new Board(board);
                        tmpBoard.move(pair, player, tmpBoard.getBlocks(player).get(0));
                        int val = minimax(tmpBoard, depth + 1, 3 - player, alpha, beta, false);
                        best = Math.max(best, val);
                        alpha = Math.max(alpha, best);
                        if (beta <= alpha)
                            return best;
                    }
                }
            }
        }
        else {
            best = Integer.MAX_VALUE;
            for (int i = 0; i < board.size - 2; i++) {
                for (int j = 0; j < board.size - 2; j++) {
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
                    if (flag) {
                        pair = new IntPair(j, i);
                        Board tmpBoard = new Board(board);
                        tmpBoard.move(pair, player, tmpBoard.getBlocks(player).get(0));
                        int val = minimax(tmpBoard, depth + 1, 3 - player, alpha, beta, true);
                        best = Math.min(best, val);
                        beta = Math.min(beta, best);
                        if (beta <= alpha)
                            return best;
                    }
                }
            }
        }
        return best;
    }
}
