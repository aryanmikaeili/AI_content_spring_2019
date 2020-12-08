package com.company;


import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Board {

    public final static int size = 8;
    private final static int maxNumberOfMoves = 80;
    private Cell[][] cells = new Cell[size][size];
    private int score[] = new int[2];
    private int numberOfMoves = 0;
    private ArrayList<Integer> blocks1 = new ArrayList<>();
    private ArrayList<Integer> blocks2 = new ArrayList<>();

    public Board() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                this.cells[i][j] = new Cell(i, j, 0);
        for(int i = 0; i < 10; i++){
            blocks1.add(ThreadLocalRandom.current().nextInt(0, 8));
            blocks2.add(ThreadLocalRandom.current().nextInt(0, 8));


        }
        score[0] = 0;
        score[1] = 0;

    }

    public Board(Board board) {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                this.cells[i][j] = new Cell(board.cells[i][j]);

        this.numberOfMoves = board.getNumberOfMoves();
        this.score[0] = board.getScore(1);
        this.score[1] = board.getScore(2);
        for(int i = 0; i < 10; i++){
            this.blocks1.add(board.blocks1.get(i));
            this.blocks2.add(board.blocks2.get(i));
        }

    }

    public Cell getCell(int i, int j) {
        return cells[i][j];
    }


    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    int win() {
        return 0;
    }

    public int move(IntPair origin, int playerColor, int type) {
        numberOfMoves++;
        Block newBlock = new Block(type, playerColor);
        newBlock.setOrigin(origin);

        ArrayList<IntPair> blockCoordinate = newBlock.getCoordinates();
        for(int i = 0; i < blockCoordinate.size(); i++)
        {
            if(blockCoordinate.get(i).x < 0 || blockCoordinate.get(i).y < 0 || blockCoordinate.get(i).y >= size || blockCoordinate.get(i).x >= size){
                return -1;
            }
            else if(getCell(blockCoordinate.get(i).x,blockCoordinate.get(i).y).getColor() != 0){
                return -1;
            }
        }
        if (numberOfMoves > maxNumberOfMoves) {
            return -2;
        }
        for(int i = 0; i < blockCoordinate.size(); i++)
        {
            getCell(blockCoordinate.get(i).x,blockCoordinate.get(i).y).setColor(playerColor);
        }
        for(int i = 0; i < size; i++){
            if(isRowComplete(i)){
                for (int j = 0; j < size; j++){
                    if (getCell(i,j).getColor() == 1){
                        score[0] += 1;
                    }
                    else{
                        score[1] += 1;
                    }
                    getCell(i,j).setColor(0);
                }
            }
            if(isColumnComplete(i)){
                for (int j = 0; j < size; j++){
                    if (getCell(j,i).getColor() == 1){
                        score[0] += 1;
                    }
                    else{
                        score[1] += 1;
                    }
                    getCell(j,i).setColor(0);
                }
            }
        }

        return 0;
    }
    public ArrayList<Integer> getBlocks(int player){
        if(player == 1){
            return blocks1;
        }
        return blocks2;
    }
    public boolean isRowComplete(int rowNum){
        for(int i = 0; i < size; i++){
            if(getCell(rowNum, i).getColor() == 0) {
                return false;
            }

        }
        return true;
    }

    public boolean isColumnComplete(int colNum){
        for(int i = 0; i < size; i++){
            if(getCell(i, colNum).getColor() == 0) {
                return false;
            }
        }
        return true;
    }
    public int getScore(int player){
        return score[player - 1];
    }

}