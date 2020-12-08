package com.company;

import java.util.ArrayList;

/**
 * Created by Aryan on 2/28/2019.
 */
public class Block {
    private int color;
    private ArrayList<IntPair> shape = new ArrayList<>();

    IntPair origin;

    public Block(int type, int color){
        shape.add(new IntPair(0,0));
        this.color = color;
        if (type == 1){
            shape.add(new IntPair(1,0));
            shape.add(new IntPair(2,0));
        }
        else if(type == 2){
            shape.add(new IntPair(0,1));
            shape.add(new IntPair(0,2));
        }
        else if(type == 3){
            shape.add(new IntPair(0,1));
            shape.add(new IntPair(1,0));
        }
        else if(type == 4){
            shape.add(new IntPair(0,1));
            shape.add(new IntPair(1,0));
            shape.add(new IntPair(1,1));
        }
        else if(type == 5){
            shape.add(new IntPair(1,0));

        }
        else if(type == 6){
            shape.add(new IntPair(0,1));

        }
        else if(type == 7){
            shape.add(new IntPair(0,1));
            shape.add(new IntPair(1,1));
        }
    }
    public void setOrigin(IntPair origin){
        this.origin = origin;
    }
    public IntPair getOrigin(){
        return this.origin;
    }
    public ArrayList getCoordinates(){
        ArrayList<IntPair> coordinates = new ArrayList<>();
        for(int i = 0; i < this.shape.size(); i++) {
            int x = this.shape.get(i).x + this.origin.x;
            int y = this.shape.get(i).y + this.origin.y;
            coordinates.add(new IntPair(x, y));
        }
        return coordinates;
    }
    public ArrayList getShape(){
        return this.shape;
    }
}
