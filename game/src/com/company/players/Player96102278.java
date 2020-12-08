package com.company.players;

import com.company.Block;
import com.company.Board;
import com.company.IntPair;

import java.util.ArrayList;

public class Player96102278 extends Player {

    public Player96102278(int col) {
        super(col);
        Node.setMyCol(col);
    }

    @Override
    public IntPair getMove(Board board) {
        Node node = new Node(0, getCol(), board);
        node.miniMax(Integer.MIN_VALUE, Integer.MAX_VALUE);
        return node.getNextLoc();
    }
}

class Node{
    private int depth;
    private Board board;
    private int col;
    private IntPair nextLoc;
    private static int myCol;
    private int failedMoves = 0;

    public Node(int depth, int col, Board board){
        this.depth = depth;
        this.col = col;
        this.board = board;
    }

    public static void setMyCol(int myCol) {
        Node.myCol = myCol;
    }

    public int miniMax(int alpha, int beta){

        if(depth == 2 && myCol != col)
            return calculateValue();

        ArrayList<IntPair> locations  = getLoc();

        Integer bestValue;
        if(col == myCol)
            bestValue = Integer.MIN_VALUE;
        else
            bestValue = Integer.MAX_VALUE;

        for (IntPair loc: locations) {
            Board newBoard = new Board(board);
            newBoard.move(loc, col, newBoard.getBlocks(col).get(depth));
            Node tempNode;
            if(col == myCol)
                tempNode = new Node(depth + 1, 3-col, newBoard);
            else
                tempNode = new Node(depth, myCol, newBoard);
            int val = tempNode.miniMax(alpha, beta);
            if(col == myCol) {
                bestValue = val > bestValue ? val : bestValue;
                alpha = alpha > bestValue ? alpha : bestValue;
            }else{
                bestValue = val < bestValue ? val : bestValue;
                beta = beta < bestValue ? beta : bestValue;
            }
            nextLoc = loc;
            if(beta <= alpha)
                break;


        }
        return bestValue;

    }

    public IntPair getNextLoc() {
        return nextLoc;
    }

    private ArrayList<IntPair> getLoc(){
        ArrayList<IntPair> locations = new ArrayList<>();
        Block nextBlock = new Block(board.getBlocks(col).get(0), col);
        for (int i = 0; i < board.size; i++) {
            for (int j = 0; j < board.size; j++) {
                boolean flag = true;
                nextBlock.setOrigin(new IntPair(j, i));
                ArrayList<IntPair> blockCoordinate = nextBlock.getCoordinates();
                for (int k = 0; k < blockCoordinate.size(); k++) {
                    if (blockCoordinate.get(k).x < 0 || blockCoordinate.get(k).y < 0 || blockCoordinate.get(k).y >= board.size || blockCoordinate.get(k).x >= board.size) {
                        flag = false;
                        failedMoves++;
                    } else if (board.getCell(blockCoordinate.get(k).x, blockCoordinate.get(k).y).getColor() != 0) {
                        flag = false;
                        failedMoves++;
                    }

                }
                if(flag)
                    locations.add(new IntPair(j, i));

            }
        }
        if(locations.size() == 0)
            locations.add(new IntPair(0,0));
        return locations;
    }

    private int calculateValue(){
        int score = board.getScore(col) - failedMoves*2 - board.getScore(3 - col);
        if(myCol == col)
            return score;
        return -1*score;
    }
}
