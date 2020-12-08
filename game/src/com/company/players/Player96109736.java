package com.company.players;

import com.company.Block;
import com.company.Board;
import com.company.IntPair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

class AlphaBeta {
    private int alpha = Integer.MAX_VALUE;
    private int beta = Integer.MIN_VALUE;
    private boolean max;

    boolean isMax() {
        return max;
    }

    int getAlpha() {
        return alpha;
    }

    void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    int getBeta() {
        return beta;
    }

    void setBeta(int beta) {
        this.beta = beta;
    }

    AlphaBeta(boolean max) {
        this.max = max;
    }
}

public class Player96109736 extends Player {
    private int enemyCol = this.getCol() % 2 + 1;

    public Player96109736(int col) {
        super(col);
    }

    @Override
    public IntPair getMove(Board board) {
        Block nextBlock = new Block(board.getBlocks(this.getCol()).get(0), this.getCol());

        AlphaBeta alphaBeta = new AlphaBeta(true);

        ArrayList<IntPair> validPairs = getValidPairs(nextBlock, board);

        if (validPairs.isEmpty()) {
            return new IntPair(0, 0);
        }

        if (validPairs.size() > 15) {
            return validPairs.get(0);
        }

        int level = getLevel(validPairs.size());

        ArrayList<Thread> threads = new ArrayList<>();
        HashMap<IntPair, Integer> scores = new HashMap<>();
        for (IntPair validPair : validPairs) {
            threads.add(new Thread(() -> scores.put(validPair, getScore(board, validPair, level, this.getCol(), alphaBeta))));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int score = Integer.MIN_VALUE;
        IntPair intPair = validPairs.get(0);
        for (IntPair key : scores.keySet()) {
            if (!scores.containsKey(key)) {
                continue;
            }
            if (score < scores.get(key)) {
                score = scores.get(key);
                intPair = key;
            }
        }

        return intPair;
    }

    private int getScore(Board board, IntPair validPair, int level, int turn, AlphaBeta father) {
        Board newBoard = new Board(board);

        int res = newBoard.move(validPair, turn, newBoard.getBlocks(turn).get(0));

        if (res == -1) {
            if (turn == this.getCol()) {
                int output = Integer.MIN_VALUE / 2 + newBoard.getScore(this.getCol()) - newBoard.getScore(enemyCol);
                updateAlphaBeta(output, father);
                return output;
            } else {
                int output = Integer.MAX_VALUE / 2 + newBoard.getScore(this.getCol()) - newBoard.getScore(enemyCol);
                updateAlphaBeta(output, father);
                return output;
            }
        }

        if (res == -2) {
            if (newBoard.getScore(this.getCol()) > newBoard.getScore(enemyCol)) {
                int output = Integer.MAX_VALUE / 2 + newBoard.getScore(this.getCol()) - newBoard.getScore(enemyCol);
                updateAlphaBeta(output, father);
                return output;
            } else if (newBoard.getScore(this.getCol()) < newBoard.getScore(enemyCol)) {
                int output = Integer.MIN_VALUE / 2 + newBoard.getScore(this.getCol()) - newBoard.getScore(enemyCol);
                updateAlphaBeta(output, father);
                return output;
            } else {
                updateAlphaBeta(0, father);
                return 0;
            }
        }

        if (level == 0) {
            int output = newBoard.getScore(this.getCol()) - newBoard.getScore(enemyCol);
            updateAlphaBeta(output, father);
            return output;
        }

        AlphaBeta alphaBeta = new AlphaBeta(!father.isMax());

        newBoard.getBlocks(turn).remove(0);
        newBoard.getBlocks(turn).add(ThreadLocalRandom.current().nextInt(0, 8));

        ArrayList<IntPair> validPairs = getValidPairs(new Block(newBoard.getBlocks(turn % 2 + 1).get(0), turn % 2 + 1), newBoard);
        int level2 = getLevel(validPairs.size());
        if (level2 < level - 1) {
            level = level2 + 1;
        }
        HashMap<IntPair, Integer> scores = new HashMap<>();
        for (IntPair validPairOtherPlayer : validPairs) {
            if (!((alphaBeta.getAlpha() < father.getBeta()) || (alphaBeta.getBeta() > father.getAlpha()))) {
                scores.put(validPairOtherPlayer, getScore(newBoard, validPairOtherPlayer, level - 1, turn % 2 + 1, alphaBeta));
            } else {
                break;
            }
        }

        int score;
        if (turn == this.getCol()) {
            score = Integer.MAX_VALUE;
            for (Integer s : scores.values()) {
                if (score > s) {
                    score = s;
                }
            }
            alphaBeta.setAlpha(score);
            updateAlphaBeta(score, father);
        } else {
            score = Integer.MIN_VALUE;
            for (Integer s : scores.values()) {
                if (score < s) {
                    score = s;
                }
            }
            alphaBeta.setBeta(score);
            updateAlphaBeta(score, father);
        }
        return score;
    }

    private ArrayList<IntPair> getValidPairs(Block nextBlock, Board board) {
        ArrayList<IntPair> validPairs = new ArrayList<>();
        for (int i = 0; i < Board.size; i++) {
            loop:
            for (int j = 0; j < Board.size; j++) {
                nextBlock.setOrigin(new IntPair(j, i));
                ArrayList<IntPair> blockCoordinate = nextBlock.getCoordinates();
                for (IntPair intPair : blockCoordinate) {
                    if (intPair.x < 0 || intPair.y < 0 || intPair.y >= Board.size || intPair.x >= Board.size) {
                        continue loop;
                    } else if (board.getCell(intPair.x, intPair.y).getColor() != 0) {
                        continue loop;
                    }
                }
                validPairs.add(new IntPair(j, i));
            }
        }
        if (validPairs.isEmpty()) {
            validPairs.add(new IntPair(0, 0));
        }
        return validPairs;
    }

    private int getLevel(int size) {
//        return 2;
        int level;
        if (size > 15) {
            level = 2;
        } else if (size == 15) {
            level = 3;
        } else if (size == 14) {
            level = 3;
        } else if (size == 13) {
            level = 4;
        } else if (size == 12) {
            level = 4;
        } else if (size == 11) {
            level = 5;
        } else if (size == 10) {
            level = 5;
        } else if (size == 9) {
            level = 5;
        } else if (size == 8) {
            level = 5;
        } else if (size == 7) {
            level = 5;
        } else if (size == 6) {
            level = 5;
        } else if (size == 5) {
            level = 8;
        } else if (size == 4) {
            level = 8;
        } else if (size == 3) {
            level = 9;
        } else if (size == 2) {
            level = 18;
        } else {
            level = 18;
        }
        return level;
    }

    private void updateAlphaBeta(int output, AlphaBeta alphaBeta) {
        if (alphaBeta.isMax()) {
            if (output > alphaBeta.getBeta()) {
                alphaBeta.setBeta(output);
            }
        } else {
            if (output < alphaBeta.getAlpha()) {
                alphaBeta.setAlpha(output);
            }
        }
    }
}
