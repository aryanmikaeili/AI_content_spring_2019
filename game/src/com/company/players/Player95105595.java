package com.company.players;

import com.company.Block;
import com.company.Board;
import com.company.IntPair;
import java.util.ArrayList;
class GraphNode{
    ArrayList<GraphNode> children = new ArrayList<>();
    Board board;
    int value = 0;
    int depth = -1;
    IntPair intPair;
    GraphNode(Board board) {
        this.board = board;
    }
}
public class Player95105595 extends Player{
    private final int depth = 3;
    private static int MAX_VALUE =  1000000;
    private static int MIN_VALUE = -1000000;

    public Player95105595(int col){
        super(col);
    }
    private int getValue(Board board, int blockNumber){ //TODO
        int max = 0;
        int rows;
        int opponent;
        for (int i = 0; i < board.size; i++) {
            int count = 0;
            opponent = 0;
            for (int j = 0; j < board.size; j++) {
                if (board.getCell(j,i).getColor() == this.getCol())
                    count++;
                if (board.getCell(j,i).getColor() == 3 - this.getCol())
                    opponent ++;
            }
            if (count >= opponent)
                max++;
//            max = max > (count - opponent)? max:(count - opponent);
        }
        rows = max;
        max = 0;
        for (int i = 0; i < board.size; i++) {
            int count = 0;
            opponent = 0;
            for (int j = 0; j < board.size; j++) {
                if (board.getCell(i,j).getColor() == this.getCol())
                    count++;
                if (board.getCell(i,j).getColor() == 3 - this.getCol())
                    opponent++;

            }
            if (count >= opponent)
                max++;
//            max = max > (count - opponent)? max: (count - opponent);
        }

        int freeSpace = 0;
        for (int i = 0; i < board.size; i++) {
            for (int j = 0; j < board.size; j++) {
                int blockType = board.getBlocks(this.getCol()).get(blockNumber);
                Block nextBlock = new Block(blockType, this.getCol());
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
                if (!flag){
                    freeSpace --;
                }
            }
        }
//    return 4;
        return (2 * board.getScore(this.getCol()) - 2 * board.getScore(3 - this.getCol()) + freeSpace +  rows + max);
    }
    private int minimax(int depth, GraphNode root,
                       Boolean maximizingPlayer,
                       int alpha,
                       int beta,int blockNumber) {
        if (depth == this.depth){
            int value = getValue(root.board, blockNumber + 1);
            root.value = value;
            return root.value;
        }
        if (maximizingPlayer) {
            int best = MIN_VALUE;
            int whichBlock = depth / 2;
            Board board = root.board;
            int player = this.getCol();
            for (int i = 0; i < board.size; i++) {
                for (int j = 0; j < board.size; j++) {
                    int blockType = board.getBlocks(player).get(whichBlock);
                    Block nextBlock = new Block(blockType, player);
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
                    if(flag){
                        Board newBoard = new Board(board);
                        newBoard.move(new IntPair(j, i), player, blockType);

                        GraphNode child = new GraphNode(newBoard);
                        child.depth = depth + 1;
                        child.intPair = new IntPair(j, i);
                        root.children.add(child);
                        int val = minimax(depth + 1, child,
                                false, alpha, beta, blockNumber);
                        best = Math.max(best, val);
                        alpha = Math.max(alpha, best);
                        if (beta <= alpha)
                            break;

                    }
                }
            }
            root.value = best;
            return best;
        }
        else {
            int best = MAX_VALUE;
            int whichBlock = depth / 2;
            Board board = root.board;
            int player = 3 - this.getCol();
            for (int i = 0; i < board.size; i++) {
                for (int j = 0; j < board.size; j++) {
                    int blockType = board.getBlocks(player).get(whichBlock);
                    Block nextBlock = new Block(blockType, player);
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
                        Board newBoard = new Board(board);
                        newBoard.move(new IntPair(j, i), player, blockType);

                        GraphNode child = new GraphNode(newBoard);
                        child.depth = depth + 1;
                        child.intPair = new IntPair(j, i);
                        root.children.add(child);

                        int val = minimax(depth + 1, child,
                                true, alpha, beta, blockNumber + 1);
                        best = Math.min(best, val);
                        beta = Math.min(beta, best);
                        if (beta <= alpha)
                            break;


                    }
                }
            }
            root.value = best;
            return best;
        }


    }
    @Override
    public IntPair getMove(Board board) {
        GraphNode root = new GraphNode(board);
        root.depth = 0;
        int best = minimax(0, root,  true, MIN_VALUE, MAX_VALUE, 0);
        System.out.println("best: " + best);

        for (int i = 0; i < root.children.size() ; i++) {
            if (root.children.get(i).value == best){
                return new IntPair(root.children.get(i).intPair);
            }
        }
        return new IntPair(0,0);
    }
}
