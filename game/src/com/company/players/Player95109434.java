package com.company.players;

import com.company.Block;
import com.company.Board;
import com.company.IntPair;

import java.util.ArrayList;

public class Player95109434 extends Player {

    public Player95109434(int col) {
        super(col);
    }

    private int max_depth = 3;

    private boolean isValid(Board board, Block block, int i, int j) {
        block.setOrigin(new IntPair(i, j));
        ArrayList<IntPair> blockCoordinate = block.getCoordinates();
        for (int k = 0; k < blockCoordinate.size(); k++) {
            if (blockCoordinate.get(k).x < 0 || blockCoordinate.get(k).y < 0 || blockCoordinate.get(k).y >= board.size || blockCoordinate.get(k).x >= board.size) {
                return false;
            } else if (board.getCell(blockCoordinate.get(k).x, blockCoordinate.get(k).y).getColor() != 0) {
                return false;
            }
        }
        return true;
    }

    private int boardHeuristic(Board board, int player, int opponent) {
        int score = (board.getScore(player) - board.getScore(opponent)) * 5;
        for (int i = 0; i < board.size; i++) {
            int row = 0;
            for (int j = 0; j < board.size; j++) {
                if (board.getCell(i, j).getColor() == player)
                    row++;
                else if (board.getCell(i, j).getColor() == opponent)
                    row--;
            }
            if (row >= 6)
                score++;
        }

        for (int i = 0; i < board.size; i++) {
            int col = 0;
            for (int j = 0; j < board.size; j++) {
                if (board.getCell(j, i).getColor() == player)
                    col++;
                else if (board.getCell(j, i).getColor() == opponent)
                    col--;
            }
            if (col >= 6)
                score++;
        }
        return score;
    }

    @Override
    public IntPair getMove(Board board) {
        Block nextBlock = new Block(board.getBlocks(this.getCol()).get(0), this.getCol());
        Node95109434 move = minimax(board, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return move.getOrigin();
    }

    private Node95109434 minimax(Board board, int depth, int alpha, int beta) {
        int player = depth % 2; // if depth == 1 -> opponent
        int col = player == 0 ? this.getCol() : 3 - this.getCol();
        int opponent_col = 3 - col;

        if (depth == max_depth) {
            Node95109434 leaf = new Node95109434(boardHeuristic(board, col, opponent_col), true);
            return leaf;
        }

        int block_no = board.getBlocks(col).get(depth / 2);
        Block current_block = new Block(block_no, col);
        int v = player == 0 ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Node95109434 result = null;
        // for each successor of state
        for (int i = 0; i < Board.size; i++) {
            for (int j = 0; j < Board.size; j++) {
                if (!isValid(board, current_block, i, j))
                    continue;
                // build new board
                Board board1 = new Board(board);
                int move_res = board1.move(new IntPair(i, j), col, block_no);
                if (move_res == -2 && result == null)
                    result = new Node95109434(v, player, new IntPair(i, j));
                if (move_res < 0)
                    continue;
                Node95109434 successor = minimax(board1, depth + 1, alpha, beta);
                if (successor == null)
                    continue;
                int successor_val = successor.getValue();
                if (player == 0) {
                    // v = max(v, value(successor, α, β)
                    if (successor_val > v) {
                        v = successor_val;
                        result = new Node95109434(successor_val, player, new IntPair(i, j));
                    }
                    // if v ≥ β return v
                    if (v >= beta)
                        return result;

                    // α = max(α, v)
                    alpha = Integer.max(alpha, v);
                } else if (player == 1) {
                    // v = min(v, value(successor, α, β)
                    if (successor_val < v) {
                        v = successor_val;
                        result = new Node95109434(successor_val, player, new IntPair(i, j));
                    }
                    // if v ≤ α return v
                    if (v <= alpha)
                        return result;

                    // β = min(β, v)
                    beta = Integer.min(beta, v);
                }
            }
        }
        if (result == null)
            result = new Node95109434(v, true);
        return result;
    }

}

class Node95109434 {
    private int value = 0;
    private int player; // if the current player in Node95109434 is opponent
    private boolean isLeaf;
    private IntPair origin;

    public Node95109434(int value, boolean isLeaf) {
        this.value = value;
        this.isLeaf = isLeaf;
    }

    public Node95109434(int value, int player, IntPair origin) {
        this.value = value;
        this.player = player;
        this.origin = origin;
    }

    public int getValue() {
        return value;
    }

    public IntPair getOrigin() {
        return origin;
    }
}


