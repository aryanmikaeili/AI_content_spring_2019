package com.company.players;

import com.company.Block;
import com.company.Board;
import com.company.IntPair;

import java.util.ArrayList;

public class Player96109588 extends Player {

    private static final int MAX_DEPTH = 5;

    public Player96109588(int col) {
        super(col);
    }

    @Override
    public IntPair getMove(Board board) {
        Answer answer = max_move(board, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
//        System.err.println("Player " + getCol() + " Score: " + answer.score + " Depth: " + answer.depth_reached);
        return answer.location;
    }

    private boolean isCellFree(Block block, Board board) {
        ArrayList<IntPair> coordinates = block.getCoordinates();
        for (IntPair pair : coordinates) {
            if (pair.x < 0 || pair.x >= Board.size || pair.y < 0 || pair.y >= Board.size ||
                    board.getCell(pair.x, pair.y).getColor() != 0)
                return false;
        }
        return true;
    }

    private Answer max_move(Board board, int depth, int alpha, int beta) {
        if (depth == MAX_DEPTH) {
            int opponent = getCol() == 1 ? 2 : 1;
            return new Answer(null, board.getScore(getCol()) - board.getScore(opponent), MAX_DEPTH);
        }
        int type = board.getBlocks(getCol()).get(depth / 2), color = getCol();
        Block nextBlock = new Block(type, color);
        IntPair answer = new IntPair(0, 0);
        int max = Integer.MIN_VALUE, depth_reached = depth;
        for (int i = 0; i < Board.size; i++) {
            for (int j = 0; j < Board.size; j++) {
                IntPair location = new IntPair(i, j);
                nextBlock.setOrigin(location);
                if (isCellFree(nextBlock, board)) {
                    Board new_board = new Board(board);
                    new_board.move(location, color, type);
                    Answer ans = min_move(new_board, depth + 1, alpha, beta);
                    int score = ans.score;
                    if (score > max) {
                        max = score;
                        answer = location;
                        depth_reached = ans.depth_reached;
                    }
                    if (max >= beta) {
                        return new Answer(answer, max, depth_reached);
                    }
                    alpha = max > alpha ? max : alpha;
                }
            }
        }
        return new Answer(answer, max, depth_reached);
    }

    private Answer min_move(Board board, int depth, int alpha, int beta) {
        int opponent = getCol() == 1 ? 2 : 1;
        if (depth == MAX_DEPTH) {
            return new Answer(null, board.getScore(getCol()) - board.getScore(opponent), MAX_DEPTH);
        }
        int type = board.getBlocks(opponent).get((depth - 1) / 2);
        Block nextBlock = new Block(type, opponent);
        int min = Integer.MAX_VALUE, depth_reached = depth;
        for (int i = 0; i < Board.size; i++) {
            for (int j = 0; j < Board.size; j++) {
                IntPair location = new IntPair(i, j);
                nextBlock.setOrigin(location);
                if (isCellFree(nextBlock, board)) {
                    Board new_board = new Board(board);
                    new_board.move(location, opponent, type);
                    Answer answer = max_move(new_board, depth + 1, alpha, beta);
                    int score = answer.score;
                    if (score < min) {
                        min = score;
                        depth_reached = answer.depth_reached;
                    }
                    if (min <= alpha) {
                        return new Answer(null, score, depth_reached);
                    }
                    beta = min < beta ? min : beta;
                }
            }
        }
        return new Answer(null, min, depth_reached);
    }

    private class Answer {
        IntPair location;
        int score;
        int depth_reached;

        Answer(IntPair location, int score, int depth_reached) {
            this.location = location;
            this.score = score;
            this.depth_reached = depth_reached;
        }
    }
}
