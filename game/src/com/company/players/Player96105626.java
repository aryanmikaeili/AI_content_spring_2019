package com.company.players;

import com.company.Block;
import com.company.Board;
import com.company.IntPair;

import java.util.ArrayList;

public class Player96105626 extends Player {
    private static int MAX = 1000000;
    private static int MIN = -1000000;
    private IntPair bestMove = null;

    public Player96105626(int col) {
        super(col);
    }

    @Override
    public IntPair getMove(Board board) {
        minimax(0, board, true, MIN, MAX, 4);
        System.out.println("best move  " + bestMove.x + "  " + bestMove.y + "type    " + board.getBlocks(1).get(0));
        if(board.move(bestMove,super.getCol(),board.getBlocks(super.getCol()).get(0)) == -1)
            System.out.println("salam");
        return bestMove;
    }

    public ArrayList<IntPair> moveGenerator(Board board, int player, int turn) {
        ArrayList<IntPair> allPossibleMoves = new ArrayList<>();
        Block nextBlock = new Block(board.getBlocks(player).get(turn), player);
        for (int i = 0; i < Board.size; i++) {
            for (int j = 0; j < Board.size; j++) {
                boolean flag = true;
                nextBlock.setOrigin(new IntPair(j, i));
                ArrayList<IntPair> blockCoordinate = nextBlock.getCoordinates();
                for (int k = 0; k < blockCoordinate.size(); k++) {
                    if (blockCoordinate.get(k).x < 0 || blockCoordinate.get(k).y < 0 || blockCoordinate.get(k).y >= Board.size || blockCoordinate.get(k).x >= Board.size) {
                        flag = false;
                        break;
                    } else if (board.getCell(blockCoordinate.get(k).x, blockCoordinate.get(k).y).getColor() != 0) {
                        flag = false;
                        break;
                    }

                }
                if (flag)
                    allPossibleMoves.add(new IntPair(j, i));

            }
        }

        return allPossibleMoves;
    }

    private int boardScore(Board board) {
        if (super.getCol() == 1)
            return board.getScore(1) - board.getScore(2);
        else
            return board.getScore(2) - board.getScore(1);
    }

    private int minimax(int depth, Board board, Boolean maximizingPlayer, int alpha, int beta, int MAX_DEPTH) {
        if (depth == MAX_DEPTH)
            return boardScore(board);

        if (maximizingPlayer) {
            int best = MIN;

            ArrayList<IntPair> possible_moves = moveGenerator(board, super.getCol(), depth / 2);
            if (possible_moves.size() == 0)
                return MIN/2;


            for (IntPair possible_move : possible_moves) {

                Board new_board = new Board(board);
                new_board.move(possible_move, super.getCol(), board.getBlocks(super.getCol()).get(depth / 2));
                int val = minimax(depth + 1, new_board, false, alpha, beta, MAX_DEPTH);
                if (best < val && depth == 0) {
                    bestMove = possible_move;
                }

                best = Math.max(best, val);
                alpha = Math.max(alpha, best);

                if (beta <= alpha)
                    break;
            }
            return best;
        } else {
            int best = MAX;

            ArrayList<IntPair> possible_moves = moveGenerator(board, otherPlayer(), depth / 2);
            if (possible_moves.size() == 0)
                return MAX/2;

            for (IntPair possible_move : possible_moves) {

                Board new_board = new Board(board);
                new_board.move(possible_move, otherPlayer(), board.getBlocks(otherPlayer()).get(depth / 2));
                int val = minimax(depth + 1, new_board, true, alpha, beta, MAX_DEPTH);
                best = Math.min(best, val);
                beta = Math.min(beta, best);

                if (beta <= alpha)
                    break;
            }
            return best;
        }
    }

    private int otherPlayer() {
        if (super.getCol() == 1)
            return 2;
        else
            return 1;
    }
}
