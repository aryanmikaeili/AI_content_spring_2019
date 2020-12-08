package com.company.players;

import com.company.Board;
import com.company.IntPair;

import java.util.ArrayList;
import java.util.Collections;


class State {
    int col;
    int turn;
    Board board;
    ArrayList<IntPair> nextMoves;
    ArrayList<Integer> futureTypes;

    public int heuristic(Board board) {
        int main = ( board.getScore(col) - board.getScore(3 - col) ) * 1000;
        int other = 0;
        for (int i = 0; i < 8; i++) {
            int count1 = 0, count2 = 0;
            for (int j = 0; j < 8; j++) {
                if (board.getCell(i, j).getColor() == col)
                    count1++;
                if (board.getCell(j, i).getColor() == col)
                    count2++;
            }
            other += count1 * count1 + count2 * count2;
        }
        return main + other;
    }

    public int evalute() {
        if (getMoves().isEmpty())
            return col == turn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        return heuristic(board);
    }

    public State(int col, int turn, ArrayList<Integer> futureTypes, Board board) {
        this.col = col;
        this.turn = turn;
        this.board = new Board(board);
        this.futureTypes = new ArrayList<>();
        this.futureTypes.addAll(futureTypes);
    }

    public ArrayList<IntPair> getMoves() {
        if (this.nextMoves != null)
            return nextMoves;
        ArrayList<IntPair> result = new ArrayList<>();
        ArrayList<MinimaxResult> notOrdered = new ArrayList<>();
        int nextType = futureTypes.get(0);

        for (int x = 0; x < Board.size; x++) {
            for (int y = 0; y < Board.size; y++) {
                Board nextBoard = new Board(this.board);
                IntPair move = new IntPair(x, y);
                int res = nextBoard.move(move, turn, nextType);
                if (res != -1) {
                    MinimaxResult moveResult = new MinimaxResult(heuristic(nextBoard), move);
                    notOrdered.add(moveResult);
                }
            }
        }
        if (col == turn)
            Collections.sort(notOrdered, (o1, o2) -> o2.score - o1.score);
        else
            Collections.sort(notOrdered, (o1, o2) -> o1.score - o2.score);
        for (int i = 0; i < notOrdered.size(); i++) {
            result.add(notOrdered.get(i).bestMove);
        }
        this.nextMoves = result;
        return result;
    }

}

class MinimaxResult {
    int score;
    IntPair bestMove;

    public MinimaxResult(int score, IntPair bestMove) {
        this.score = score;
        this.bestMove = bestMove;
    }
}

public class Player95109315 extends Player {

    public Player95109315(int col) {
        super(col);
    }

    private MinimaxResult minimax(int depth, State root, int alpha, int beta) {
        IntPair bestMove = null;
        ArrayList<IntPair> nextMoves = root.getMoves();

        if (!nextMoves.isEmpty())
            bestMove = nextMoves.get(0);

        if (nextMoves.isEmpty() || depth == 0) {
            return new MinimaxResult(root.evalute(), bestMove);
        }


        for (IntPair move : nextMoves) {
            Board nextBoard = new Board(root.board);
            nextBoard.move(move, root.turn, root.futureTypes.get(0));

            ArrayList<Integer> nextFutureTypes = new ArrayList<>();
            nextFutureTypes.addAll(root.futureTypes);
            nextFutureTypes.remove(0);

            State nextState = new State(root.col, 3 - root.turn, nextFutureTypes, nextBoard);
            MinimaxResult result = minimax(depth - 1, nextState, alpha, beta);
            if (root.turn == root.col) {
                if (result.score > alpha) {
                    alpha = result.score;
                    bestMove = move;
                }
            }
            else {
                if (result.score < beta) {
                    beta = result.score;
                    bestMove = move;
                }
            }

            if (alpha >= beta) break;

//            System.out.println(bestMove.x + " " + bestMove.y);

        }

        return new MinimaxResult(root.turn == root.col ? alpha : beta, bestMove);
    }


    @Override
    public IntPair getMove(Board board) {
        ArrayList<Integer> ownTypes = board.getBlocks(this.getCol());
        ArrayList<Integer> enTypes = board.getBlocks(3 - this.getCol());
        ArrayList<Integer> futureTypes = new ArrayList<>();
        for (int i = 0; i < Math.min(ownTypes.size(), enTypes.size()) * 2; i++) {
            futureTypes.add(i % 2 == 0 ? ownTypes.get(i / 2) : enTypes.get(i / 2));
        }


        int maxDepth = 4;
        State root = new State(this.getCol(), this.getCol(), futureTypes, board);
        MinimaxResult result = minimax(maxDepth, root, Integer.MIN_VALUE, Integer.MAX_VALUE);
        if (result.bestMove == null)
            System.out.println("Player95llll");
//        System.out.println(result.bestMove.x + " " + result.bestMove.y);
        return result.bestMove;
//        return new IntPair(-1, -1);
    }
}

