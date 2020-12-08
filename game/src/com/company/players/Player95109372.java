package com.company.players;

import com.company.Board;
import com.company.IntPair;

import java.util.ArrayList;


class Node95109372 {
    private IntPair coordinates;
    private boolean isLeaf;
    private int value;

    public IntPair getCoordinates() {
        return coordinates;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public int getValue() {
        return value;
    }

    public Node95109372(boolean isLeaf, int value) {
        this.isLeaf = isLeaf;
        this.value = value;
    }

    public Node95109372(boolean isLeaf, int value, IntPair coordinates) {
        this.isLeaf = isLeaf;
        this.value = value;
        this.coordinates = coordinates;
    }
}

public class Player95109372 extends Player {
    private final int opCol;
    private final static int maxNumberOfMoves = 80;
    private final static int maxDepth = 3;

    public Player95109372(int col) {
        super(col);
        opCol = 3 - col;
    }

    @Override
    public IntPair getMove(Board board) {
        ArrayList<Integer> myBlocks = board.getBlocks(this.getCol());
        ArrayList<Integer> opBlocks = board.getBlocks(this.opCol);
        System.out.println(board.getNumberOfMoves());
        System.out.println(myBlocks.get(0));
        Node95109372 result = minimax(new Board(board), 0, true, Integer.MIN_VALUE, Integer.MAX_VALUE, myBlocks,
                opBlocks, maxNumberOfMoves - board.getNumberOfMoves());
        System.out.println("Best Move:"+result.getValue()+" Coords: " + result.getCoordinates().x + ", " +result.getCoordinates().y);
        return result.getCoordinates();
    }


    private int heuristic(Board board, boolean rowWise) {
        int player = this.getCol();
        int result = 0;
        for (int i = 0; i < board.size; i++) {
            int occupied = 0;
            int sum = 0;
            for (int j = 0; j < board.size; j++) {
                if (rowWise) {
                    if (board.getCell(i, j).getColor() == player)
                        sum++;
                    if (board.getCell(i, j).getColor() != 0)
                        occupied++;
                } else {
                    if (board.getCell(j, i).getColor() == player)
                        sum++;
                    if (board.getCell(j, i).getColor() != 0)
                        occupied++;
                }
            }
            if (occupied >= 6)
                result += (2 * sum - occupied);

        }
        return result;
    }

    private int heuristic(Board board) {
        return heuristic(board, true) + heuristic(board, false);
    }

    private boolean hasNoMoves(Board board, int player, int blockType){
        int dim = board.size;
        for (int i = 0; i <  dim; i++) {
            for (int j = 0; j < dim; j++) {
                Board tempBoard = new Board(board);
                if(tempBoard.move(new IntPair(i, j), player, blockType) >= 0){
                    return false;
                }
            }
        }
        return true;
    }


    private Node95109372 minimax(Board board, int depth, boolean isMaximizingPlayer, int alpha, int beta,
                         ArrayList<Integer> myBlocks, ArrayList<Integer> opBlocks, int remainingMoves) {

        {
            int player = isMaximizingPlayer ? this.getCol() : this.opCol;
            int blockType = isMaximizingPlayer ? myBlocks.get(depth) : opBlocks.get(depth);

            if (hasNoMoves(board, player, blockType)) {
                return new Node95109372(true, isMaximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE);
            }
        }

        if (remainingMoves == 0) {
            return (new Node95109372(true, board.getScore(this.getCol()) - board.getScore(opCol)));
        }

        if (depth == maxDepth) {
            return (new Node95109372(true, heuristic(board) + board.getScore(this.getCol()) - board.getScore(opCol)));
        }

        int dim = board.size;
        if (isMaximizingPlayer) {
            int bestVal = Integer.MIN_VALUE;
            int blocktype = myBlocks.get(depth);
            IntPair coordinates = null;

            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    Board childBoard = new Board(board);
                    if (childBoard.move(new IntPair(i, j), this.getCol(), blocktype) >= 0) {
                        Node95109372 valueNode95109372 = minimax(childBoard, depth + 1, false, alpha, beta,
                                myBlocks, opBlocks, remainingMoves - 1);
                        if(valueNode95109372.getValue() > bestVal){
                            bestVal = valueNode95109372.getValue();
                            coordinates = new IntPair(i, j);
                        }
                        if(depth == 0){
                            System.out.println("POSSIBLE MOVE: "+ valueNode95109372.getValue());
                        }


                        alpha = Math.max(alpha, bestVal);
                        if (beta <= alpha) {
                            return (new Node95109372(false, bestVal, coordinates));
                        }
                    }
                }
            }

            return (new Node95109372(false, bestVal, coordinates));
        } else {
            int bestVal = Integer.MAX_VALUE;
            int blocktype = opBlocks.get(depth);
            IntPair coordinates = null;

            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    Board childBoard = new Board(board);
                    if (childBoard.move(new IntPair(i, j), this.opCol, blocktype) >= 0) {
                        Node95109372 valueNode95109372 = minimax(childBoard, depth + 1, true, alpha, beta,
                                myBlocks, opBlocks, remainingMoves - 1);
                        if(valueNode95109372.getValue() < bestVal){
                            bestVal = valueNode95109372.getValue();
                            coordinates = new IntPair(i, j);
                        }

                        beta = Math.min(beta, bestVal);
                        if(beta <= alpha){
                            return (new Node95109372(false, bestVal, coordinates));
                        }
                    }
                }
            }
            return (new Node95109372(false, bestVal, coordinates));
        }
    }


}
