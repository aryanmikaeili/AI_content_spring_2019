package com.company.players;

import com.company.Block;
import com.company.Board;
import com.company.IntPair;

import java.util.ArrayList;

public class NaivePlayer extends Player {

    public NaivePlayer(int col) {
        super(col);
    }

    @Override
    public IntPair getMove(Board board) {
        Block nextBlock = new Block(board.getBlocks(this.getCol()).get(0), this.getCol());
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
                if(flag)
                    return new IntPair(j, i);

            }
        }
        return new IntPair(0,0);
    }
}
