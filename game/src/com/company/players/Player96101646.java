package com.company;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Player96101646 extends com.company.players.Player {
    static final int LOSING_POINT = -50000;
    static final int MAX_NUM_OF_MOVES = 80;
    static final int BOARD_SIZE = 8;
    static final int OUR_TURN = 0;
    static final int WIN_BONUS = 50000;
    private int finalDepth;

    Player96101646(int col, int maxDepth) {
        super(col);
        this.finalDepth = maxDepth;
    }

    @Override
    public IntPair getMove(Board board) {
        return alphaBetaSearch(new State(board, finalDepth));
    }


    private IntPair alphaBetaSearch(State state) {
        // returning the action with value 'v'
        return maxValue(state, Integer.MIN_VALUE, Integer.MAX_VALUE, 0).getValue();
    }

    private Pair<Integer, IntPair> maxValue(State state, int alpha, int beta, int depth) {

        if (state.isCutOff(depth))
            return new Pair<>(state.eval(depth), new IntPair(1, 1));

        int v = Integer.MIN_VALUE;
        LinkedList<IntPair> action = state.getActions(depth);
        Iterator i = action.iterator();
        IntPair temp;
        IntPair reserve = new IntPair(1, 1);


        while (i.hasNext()) {
            temp = (IntPair) i.next();
            int minValue = minValue(state.result(temp, depth), alpha, beta, depth + 1).getKey();


            if (v <= minValue) {
                reserve = temp;
                v = minValue;
            }

            if (v >= beta) {
                return new Pair<>(v, reserve);
            }
            alpha = Math.max(alpha, v);
        }
        return new Pair<>(v, reserve);
    }

    private Pair<Integer, IntPair> minValue(State state, int alpha, int beta, int depth) {

        if (state.isCutOff(depth)) return new Pair<>(state.eval(depth), null);

        /*888888888888888888888888888
        int v = Integer.MAX_VALUE;
        IntPair temp = new IntPair(0, 0);
        v = maxValue(state.result(new NaivePlayer(2).getMove(state.board), depth), alpha, beta, depth + 1).getKey();
        if (v <= alpha) return new Pair<>(v, temp);
        return new Pair<>(v, temp);
        888888888888888888888888888*/
        int v = Integer.MAX_VALUE;
        LinkedList<IntPair> actions = state.getActions(depth);
        Iterator i = actions.iterator();
        IntPair temp = new IntPair(0, 0);
        while (i.hasNext()) {
            temp = (IntPair) i.next();
            v = Math.min(v, maxValue(state.result(temp, depth), alpha, beta, depth + 1).getKey());
            if (v <= alpha) return new Pair<>(v, temp);
            beta = Math.min(beta, v);
        }
        return new Pair<>(v, temp);
    }
}

class State {
    Board board;
    private int finalDepth;

    State(Board board, int finalDepth) {
        this.board = new Board(board);
        this.finalDepth = finalDepth;
    }

    boolean isCutOff(int depth) {
        if (depth >= finalDepth) return true;
        return this.isTerminal(depth);
    }

    private boolean isTerminal(int depth) {
        // TODO: 3/19/2019 check which one id right
//        if (this.board.getNumberOfMoves() + depth > Player96101646.MAX_NUM_OF_MOVES);
        if (this.board.getNumberOfMoves() > Player96101646.MAX_NUM_OF_MOVES) return true;
        return !hasMove(depth);
    }

    private boolean hasMove(int depth) {
        Block nextBlock = new Block(board.getBlocks(getTurn()).get(depth / 2), getTurn());
        ArrayList<IntPair> blockCoordinates;

        for (int x = 0; x < Player96101646.BOARD_SIZE; x++) {
            outer:
            for (int y = 0; y < Player96101646.BOARD_SIZE; y++) {
                nextBlock.setOrigin(new IntPair(x, y));
                blockCoordinates = nextBlock.getCoordinates();

                for (int i = 0; i < blockCoordinates.size(); i++) {
                    if (blockCoordinates.get(i).x < 0 || blockCoordinates.get(i).y < 0 || blockCoordinates.get(i).y >= Player96101646.BOARD_SIZE || blockCoordinates.get(i).x >= Player96101646.BOARD_SIZE) {
                        continue outer;
                    } else if (this.board.getCell(blockCoordinates.get(i).x, blockCoordinates.get(i).y).getColor() != 0) {
                        continue outer;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private int getTurn() {
        if (this.board.getNumberOfMoves() % 2 == 1) return 0;
        else return 1;
    }

    private State getClone() {
        return new State(this.board, finalDepth);
    }

    State result(IntPair action, int depth) {
        State temp = this.getClone();
        temp.board.move(action, getTurn(), temp.board.getBlocks(getTurn()).get(depth / 2));
        return temp;
    }

    LinkedList<IntPair> getActions(int depth) {
        LinkedList<IntPair> actions = new LinkedList<>();

        Block nextBlock = new Block(board.getBlocks(getTurn()).get(depth / 2), getTurn());
        ArrayList<IntPair> blockCoordinates;

        IntPair origin;
        for (int x = Player96101646.BOARD_SIZE - 1; x >= 0; x--) {
            outer:
            for (int y = Player96101646.BOARD_SIZE - 1; y >= 0; y--) {
                origin = new IntPair(y, x);
                nextBlock.setOrigin(origin);
                blockCoordinates = nextBlock.getCoordinates();

                for (int i = 0; i < blockCoordinates.size(); i++) {
                    if (blockCoordinates.get(i).x < 0 || blockCoordinates.get(i).y < 0 || blockCoordinates.get(i).y >= Player96101646.BOARD_SIZE || blockCoordinates.get(i).x >= Player96101646.BOARD_SIZE) {
                        continue outer;
                    } else if (this.board.getCell(blockCoordinates.get(i).x, blockCoordinates.get(i).y).getColor() != 0) {
                        continue outer;
                    }
                }
                actions.add(origin);
            }
        }
        return actions;
    }

    int eval(int depth) {
// TODO: 4/23/2019 the problem is i should  have considered a positive point for states that have more places for me to put blocks an negetive point for the opposite!

        boolean hasMove = hasMove(depth);
        int avgSum = getAvgBlockInLine(Player96101646.OUR_TURN) - getAvgBlockInLine(1 - Player96101646.OUR_TURN);
//        avgSum = avgSum < 0 ? 0 : avgSum;
        avgSum = (avgSum + 100) / 2;
        if (!hasMove && getTurn() != Player96101646.OUR_TURN)
            return (this.board.getScore(Player96101646.OUR_TURN + 1) - this.board.getScore(1 - Player96101646.OUR_TURN + 1)) * 100 + avgSum + Player96101646.LOSING_POINT; // our lost!
        else if (!hasMove && getTurn() == Player96101646.OUR_TURN)
            return (this.board.getScore(Player96101646.OUR_TURN + 1) - this.board.getScore(1 - Player96101646.OUR_TURN + 1)) * 100 + avgSum + Player96101646.WIN_BONUS;
//            return (this.board.getScore(Player96101646.OUR_TURN + 1) - this.board.getScore(1 - Player96101646.OUR_TURN + 1)) * 100 + getAvgBlockInLine(Player96101646.OUR_TURN) - getAvgBlockInLine(1 - Player96101646.OUR_TURN) + Player96101646.LOSING_POINT; // our lost!

        else
            return (this.board.getScore(Player96101646.OUR_TURN + 1) - this.board.getScore(1 - Player96101646.OUR_TURN + 1)) * 100 + avgSum;
    }

    private int getAvgBlockInLine(int turn) {
        int[] lineValues = new int[Player96101646.BOARD_SIZE];
        for (int i = 0; i < lineValues.length; i++) {
            lineValues[i] = 0;
        }

        int neededColor = turn == Player96101646.OUR_TURN ? Player96101646.OUR_TURN + 1 : Player96101646.OUR_TURN + 2;
        for (int x = 0; x < Player96101646.BOARD_SIZE; x++) {
            int blocksInLine = 0;
            for (int y = 0; y < Player96101646.BOARD_SIZE; y++) {
                if (this.board.getCell(x, y).getColor() == neededColor) blocksInLine++;
            }
            lineValues[blocksInLine]++;
        }
        for (int x = 0; x < Player96101646.BOARD_SIZE; x++) {
            int blocksInLine = 0;
            for (int y = 0; y < Player96101646.BOARD_SIZE; y++) {
                if (this.board.getCell(y, x).getColor() == neededColor) blocksInLine++;
            }
            lineValues[blocksInLine]++;
        }
        int ans = 0;
        for (int i = 0; i < lineValues.length; i++) {
            ans += lineValues[i] * i;
        }
//        return 0;
        return ans;
    }

}