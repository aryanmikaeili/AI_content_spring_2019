package com.company.players;

import com.company.Block;
import com.company.Board;
import com.company.IntPair;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player95103729 extends Player {

    public Player95103729(int col){super(col);}

    @Override
    public IntPair getMove(Board board) {

//        for (int z = 0 ; z < board.size; z++){
//            for (int x = 0; x < board.size; x++){
//                System.out.print(board.getCell(z,x).getColor());
//            }
//            System.out.println();
//        }
        Board bd = new Board(board);
        k = 0;
        optIntPair = null;
        ArrayList<Integer> mb = new ArrayList<>(bd.getBlocks(this.getCol()));
        ArrayList<Integer> hb = new ArrayList<>(bd.getBlocks(3 - this.getCol()));
        System.out.println("Best Possible: " + mini_max(bd, mb, 0, hb, 0, upper_level, this.getCol(), -1000));
//        System.out.println(optIntPair.x + " " +optIntPair.y);
        if (optIntPair == null)
            return new IntPair(-10,-10);
        return optIntPair;
//
//        Block nextBlock = new Block(board.getBlocks(this.getCol()).get(0), this.getCol());
//        for (int i = 0; i < board.size; i++) {
//            for (int j = 0; j < board.size; j++) {
//                boolean flag = true;
//                nextBlock.setOrigin(new IntPair(j, i));
//                ArrayList<IntPair> blockCoordinate = nextBlock.getCoordinates();
//                for (int k = 0; k < blockCoordinate.size(); k++) {
//                    if (blockCoordinate.get(k).x < 0 || blockCoordinate.get(k).y < 0 || blockCoordinate.get(k).y >= board.size || blockCoordinate.get(k).x >= board.size) {
//                        flag = false;
//                    } else if (board.getCell(blockCoordinate.get(k).x, blockCoordinate.get(k).y).getColor() != 0) {
//                        flag = false;
//                    }
//                }
//                if(flag)
//                    return new IntPair(j, i);
//            }
//        }
//        return new IntPair(0,0);
    }

    int k = 0;
    public IntPair optIntPair = null;
    int upper_level = 3;
    int iteration_limit = 25;

    private int mini_max(Board board, ArrayList<Integer> my_blocks, int my_it, ArrayList<Integer> her_blocks, int her_it,
                         int depth, int player_id, int best_found){
//        System.out.println(k++);
        if(depth == 0)
            return board.getScore(this.getCol()) - board.getScore(3 - this.getCol());

        Block nextBlock;
        int b_type = my_blocks.get(my_it);
        if (player_id == 3 - this.getCol())
            b_type = her_blocks.get(her_it);

        if (player_id == this.getCol())
            nextBlock = new Block(my_blocks.get(my_it), player_id);
        else
            nextBlock = new Block(her_blocks.get(her_it), 3 - player_id);

        int opt_score = 0;
//        IntPair optIntPair = null;
        int num = 0;
        for (int i = 0; i < board.size; i++) {
            for (int j = 0; j < board.size; j++) {
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
//                    System.out.println("LLLLL " + j + " "  + i );
                    Board new_board = new Board(board);
                    new_board.move(new IntPair(j,i), player_id, b_type);
                    int nmit = my_it;
                    int hit = her_it;
                    if(player_id == this.getCol())
                        nmit++;
                    else hit++;

                    int score = mini_max(new_board, my_blocks, nmit , her_blocks, hit, depth-1, 3 - player_id , opt_score);
                    if (player_id == this.getCol() && (score > opt_score || optIntPair == null)){
                        opt_score = score;
                        if (depth == upper_level) optIntPair = new IntPair(j,i);
                        if (opt_score > best_found){
                            num = iteration_limit;
                        }
                    }
                    if (player_id == 3 - this.getCol() && (score < opt_score || optIntPair == null)){
                        opt_score = score;
                        if (depth == upper_level) optIntPair = new IntPair(j,i);
                        if (opt_score < best_found){
                            num = iteration_limit;
                        }
                    }
//                    if (depth == 1 && num == 0){
//                        for (int z = 0 ; z < board.size; z++){
//                            for (int x = 0; x < board.size; x++){
//                                System.out.print(new_board.getCell(board.size - 1 - z,x).getColor());
//                            }
//                            System.out.println();
//                        }
//                        System.out.println(nextBlock.getOrigin().x + " " + nextBlock.getOrigin().y);
//                        System.out.println("==============================score:" + score);
//
//                    }
                    num++;
                }
            }
            if (num >= iteration_limit)break;
        }

        return opt_score;
    }
}
