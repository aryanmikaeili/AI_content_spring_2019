package com.company.players;

import com.company.Board;
import com.company.IntPair;
import com.company.players.Player;
import com.company.Block;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;


public class Player94105355 extends Player {

    public Player94105355(int col) {
        super(col);
    }
    private int x, y, final_x, final_y;
    private int depth_limit = 4;
    private int timeLimit = 3000;
    private boolean flag;

    public IntPair getMove(Board board) {

        Max_value(board, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return new IntPair(this.x, this.y);
    }
    public int Max_value(Board board, int depth, int alpha, int beta) {

        int max_val = Integer.MIN_VALUE;
        int ret_val;
//        System.out.println("the depth is " + depth + " alpha is " + alpha + " beta is " + beta);
        ArrayList<Integer> blocks = board.getBlocks(this.getCol());
        if(board.getNumberOfMoves() == 80)
        {
            return (board.getScore(this.getCol()) > board.getScore(3 - (this.getCol()))) ?
                    Integer.MAX_VALUE:Integer.MIN_VALUE;
        }
        if(depth == depth_limit)
        {
            return board.getScore(this.getCol()) - board.getScore(3 - (this.getCol()));
        }
        Block new_block = new Block(blocks.get(0), this.getCol());
        for (int i = 0; i < board.size; i++) {
            for (int j = 0; j < board.size; j++) {
                boolean flag = true;
                Board new_board = new Board(board);
                new_block.setOrigin(new IntPair(i, j));
                ArrayList<IntPair> blockCoordinate = new_block.getCoordinates();
                for (int m = 0; m < blockCoordinate.size(); m++) {
                    if (blockCoordinate.get(m).x < 0 || blockCoordinate.get(m).y < 0 || blockCoordinate.get(m).y >= board.size || blockCoordinate.get(m).x >= board.size) {
                        flag = false;
                    }
                    else if (new_board.getCell(blockCoordinate.get(m).x, blockCoordinate.get(m).y).getColor() != 0) {
                        flag = false;
                    }
                }
                if (flag) {
//                    System.out.println("here");
                    new_board.move(new IntPair(i, j), this.getCol(), blocks.get(0));
//                    System.out.println("here");
                    new_board.getBlocks(this.getCol()).remove(0);
                    new_board.getBlocks(this.getCol()).add(1);
//                    System.out.println("here");
                    ret_val = Min_value(new_board, depth + 1, alpha, beta);
                    if(max_val < ret_val) {
                        max_val = ret_val;
                        if(depth == 0)
                        {
                            this.x = i;
                            this.y = j;
//                            System.out.println("the x is " + i + " " + j);
                        }
                    }
                    max_val = Math.max(max_val, ret_val);
                    if(max_val >= beta)
                        return max_val;
                    alpha = Math.max(max_val, alpha);


                }

            }
        }
        return max_val;
    }
    public int Min_value(Board board, int depth, int alpha, int beta) {
        int min_val = Integer.MAX_VALUE;
        int ret_val;
        ArrayList<Integer> blocks = board.getBlocks(3 - this.getCol());
        if(board.getNumberOfMoves() == 80)
        {
            return (board.getScore(this.getCol()) > board.getScore(3 - (this.getCol()))) ?
                    Integer.MAX_VALUE:Integer.MIN_VALUE;
        }
        if(depth == depth_limit)
        {
            return board.getScore(this.getCol()) - board.getScore(3 - (this.getCol()));
        }
        Block new_block = new Block(blocks.get(0), 3 - this.getCol());
        for (int i = 0; i < board.size; i++) {
            for (int j = 0; j < board.size; j++) {

                boolean flag = true;
                Board new_board = new Board(board);
                new_block.setOrigin(new IntPair(i, j));
                ArrayList<IntPair> blockCoordinate = new_block.getCoordinates();
                for (int m = 0; m < blockCoordinate.size(); m++) {
                    if (blockCoordinate.get(m).x < 0 || blockCoordinate.get(m).y < 0 || blockCoordinate.get(m).y >= board.size || blockCoordinate.get(m).x >= board.size) {
                        flag = false;
                    } else if (board.getCell(blockCoordinate.get(m).x, blockCoordinate.get(m).y).getColor() != 0) {
                        flag = false;
                    }

                }
                if(flag) {
                    new_board.move(new IntPair(i, j), 3 - this.getCol(), blocks.get(0));
                    new_board.getBlocks(3 - this.getCol()).remove(0);
                    new_board.getBlocks(3 - this.getCol()).add(1);
                    ret_val = Max_value(new_board, depth + 1, alpha, beta);
                    min_val = Math.min(min_val, ret_val);
                    if(min_val <= alpha)
                        return min_val;
                    beta = Math.min(min_val, beta);
                }

            }
        }

        return min_val;
    }

}


