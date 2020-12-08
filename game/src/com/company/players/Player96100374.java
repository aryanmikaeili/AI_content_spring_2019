package com.company.players;

import com.company.Board;
import com.company.IntPair;

import java.util.*;


public class Player96100374 extends Player {


    public static void main(String[] args) {


        Board board = new Board();

    }

    private int score;

    public Player96100374(int col) {
        super(col);
    }


    @Override
    public IntPair getMove(Board board) {

//        board.getCell()
        score = board.getScore(getCol());
        Node96100374<int[][]> tree = makeDecisionTree(board);

        dfs(tree);
        for (Node96100374 child : tree.getChildren()) {
            alphaBetaDFS(child);
        }
        int max = -1;
        int max_index = -1;

        for (int i = 0; i < tree.getChildren().size(); i++) {
            if (tree.getChildren().get(i).getValue() > max) {
                max = tree.getChildren().get(i).getValue();
                max_index = i;
            }
        }
        if (max_index == -1) {
            return randomMove(tree.getData(), board.getBlocks(getCol()).get(0));
        }
//        System.out.println(7 - tree.getChildren().get(max_index).getAddedPair().x + " " + tree.getChildren().get(max_index).getAddedPair().y);
        return new IntPair(7 - tree.getChildren().get(max_index).getAddedPair().x, tree.getChildren().get(max_index).getAddedPair().y);
    }

    private void alphaBetaDFS(Node96100374 Node96100374) {

        int max = -1000;
        if (Node96100374.getDepth() % 2 == 0) {
            if (Node96100374.getCurrentValue() >= Node96100374.getParent().getCurrentValue() && Node96100374.getCurrentValue() != -1) {
                return;
            }
        } else {
            if (Node96100374.getCurrentValue() <= Node96100374.getParent().getCurrentValue() && Node96100374.getCurrentValue() != -1) {
                return;
            }
        }

        if (Node96100374.getChildren().size() == 0) {
            Node96100374.setValue(evaluate((int[][]) Node96100374.getData()));
            Node96100374.setInitialized(true);
            Node96100374.setCurrentValue(Node96100374.getValue());
            return;
        }
//        dfs(Node96100374);
        for (Object child : Node96100374.getChildren()) {
            alphaBetaDFS((Node96100374) child);
            max = Math.max(max, ((Node96100374) child).getValue());
        }
        Node96100374.setInitialized(true);
        Node96100374.setValue(max);
        Node96100374.getParent().setCurrentValue(Math.max(Node96100374.getParent().getCurrentValue(), Node96100374.getValue()));
    }

    private IntPair randomMove(int[][] map, int numberOfShape) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (shapeCanAddedToCoordinate(map, numberOfShape, i, j)) {
                    return new IntPair(i,j);
                }
            }
        }
        return null;
    }

    private void dfs(Node96100374 root) {

        Node96100374<int[][]> currentNode96100374 = root;
        ArrayList<Node96100374> path = new ArrayList<>();


        while (root.getChildren().size() != 0) {
            if (currentNode96100374.getChildren().size() == 0) {
                break;
            }
            currentNode96100374 = currentNode96100374.getChildren().get(new Random().nextInt(currentNode96100374.getChildren().size()));
        }
        currentNode96100374.setValue(evaluate(currentNode96100374.getData()));
        currentNode96100374.setCurrentValue(currentNode96100374.getValue());
        currentNode96100374.setInitialized(true);
        path.add(currentNode96100374);
        Node96100374 child = currentNode96100374;
        currentNode96100374 = currentNode96100374.getParent();

        while (currentNode96100374 != null && currentNode96100374.getParent() != null) {
            currentNode96100374.setCurrentValue(child.getCurrentValue());
            currentNode96100374 = currentNode96100374.getParent();
        }

    }

    //making tree:
    private Node96100374<int[][]> makeDecisionTree(Board board) {
        score = board.getScore(getCol());

        int[][] board_root = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board_root[i][j] = board.getCell(7 - i, j).getColor();
            }
        }

//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                System.out.print(board_root[i][j] + " ");
//            }
//            System.out.println();
//        }
//        System.out.println();


        int currentPlayer = getCol();
        Node96100374<int[][]> root = new Node96100374<>(board_root, getCol(), 0, new IntPair(-1, -1));
        root.setValue(evaluate(board_root));
        board.getBlocks(currentPlayer).get(0);


        int iterationOfShapes = 0;
        Node96100374<int[][]> currentNode96100374;
        ArrayList<Node96100374<int[][]>> queue = new ArrayList<>();


        makeADepth(queue, root, board.getBlocks(currentPlayer).get(iterationOfShapes / 2), currentPlayer, root.getDepth() + 1);
        iterationOfShapes++;


        currentPlayer = switchPlayer(currentPlayer);

        for (Node96100374 child : root.getChildren()) {
            makeADepth(queue, child, board.getBlocks(currentPlayer).get(iterationOfShapes / 2), currentPlayer, child.getDepth() + 1);
        }

        iterationOfShapes++;

//        Node96100374.printWholeTree(root);


        int lastDepth = root.getDepth();
        while (queue.size() != 0) {
            currentNode96100374 = queue.remove(0);
            if (queue.size() > 10000) {
                return root;
            }
            int newDepth = currentNode96100374.getDepth();


            if (iterationOfShapes < 20) {
                for (Node96100374 child : currentNode96100374.getChildren()) {
                    makeADepth(queue, child, board.getBlocks(currentPlayer).get(iterationOfShapes / 2), currentPlayer, child.getDepth() + 1);

                }
                if (newDepth > lastDepth) {

                    currentPlayer = switchPlayer(currentPlayer);
                    iterationOfShapes++;
                }

            } else {
                break;
            }
            lastDepth = currentNode96100374.getDepth();
        }

        return root;
    }

    private void makeADepth(ArrayList<Node96100374<int[][]>> queue, Node96100374<int[][]> parent, int block, int currentPlayer, int depth) {
        int[][] board = arrayCopy(parent.getData());
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 0) {
                    if (shapeCanAddedToCoordinate(board, block, i, j)) {
                        Node96100374<int[][]> child = new Node96100374<>(addShapeToMap(board, currentPlayer, block, i, j), currentPlayer, depth, new IntPair(i, j));
                        parent.addChild(child);
                        if (depth < 20) {
                            queue.add(child);
                        }
                        board = arrayCopy(parent.getData());
                        if (parent.getChildren().size() > 4) {
                            return;
                        }
                    }
                }
            }
        }
    }


    //map staffs:
    private int evaluate(int[][] board) {
        int functionForThisPlayer = 0;

        for (int i = 0; i < 8; i++) {
            if (isRowComplete(board, i)) {
                for (int j = 0; j < 8; j++) {
                    if (board[7 - i][j] == getCol()) {
                        score++;
                    }
                    board[i][j] = 0;
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            if (isColumnComplete(board, i)) {
                for (int j = 0; j < 8; j++) {
                    if (board[7 - i][j] == getCol()) {
                        score++;
                    }
                    board[i][j] = 0;
                }

            }
        }

        return functionForThisPlayer;
    }

    private boolean isRowComplete(int[][] board, int rowNum) {
        for (int i = 0; i < 8; i++) {
            if (board[rowNum][i] == 0) {
                return false;
            }

        }
        return true;
    }

    private boolean isColumnComplete(int[][] board, int colNum) {
        for (int i = 0; i < 8; i++) {
            if (board[i][colNum] == 0) {
                return false;
            }
        }
        return true;
    }

    private int[][] addShapeToMap(int[][] map, int numberOfPlayer, int numberOfShape, int coordinate_row, int coordinate_column) {
        switch (numberOfShape + 1) {
            case 1: {
                map[coordinate_row][coordinate_column] = numberOfPlayer;
                break;
            }
            case 2: {
                map[coordinate_row][coordinate_column] = numberOfPlayer;
                map[coordinate_row - 1][coordinate_column] = numberOfPlayer;
                map[coordinate_row - 2][coordinate_column] = numberOfPlayer;
                break;
            }
            case 3: {
                map[coordinate_row][coordinate_column] = numberOfPlayer;
                map[coordinate_row][coordinate_column + 1] = numberOfPlayer;
                map[coordinate_row][coordinate_column + 2] = numberOfPlayer;
                break;
            }
            case 4: {
                map[coordinate_row][coordinate_column] = numberOfPlayer;
                map[coordinate_row - 1][coordinate_column] = numberOfPlayer;
                map[coordinate_row][coordinate_column + 1] = numberOfPlayer;
                break;
            }
            case 5: {
                map[coordinate_row][coordinate_column] = numberOfPlayer;
                map[coordinate_row - 1][coordinate_column] = numberOfPlayer;
                map[coordinate_row][coordinate_column + 1] = numberOfPlayer;
                map[coordinate_row - 1][coordinate_column + 1] = numberOfPlayer;
                break;
            }
            case 6: {
                map[coordinate_row][coordinate_column] = numberOfPlayer;
                map[coordinate_row - 1][coordinate_column] = numberOfPlayer;
                break;
            }
            case 7: {
                map[coordinate_row][coordinate_column] = numberOfPlayer;
                map[coordinate_row][coordinate_column + 1] = numberOfPlayer;
                break;
            }
            case 8: {
                map[coordinate_row][coordinate_column] = numberOfPlayer;
                map[coordinate_row][coordinate_column + 1] = numberOfPlayer;
                map[coordinate_row - 1][coordinate_column + 1] = numberOfPlayer;
                break;
            }
        }


        return map;
    }

    private boolean shapeCanAddedToCoordinate(int[][] map, int numberOfShape, int coordinate_row, int coordinate_column) {


        try {

            switch (numberOfShape + 1) {
                case 1: {
                    if (map[coordinate_row][coordinate_column] != 0) {
                        return false;
                    }
                    break;
                }
                case 2: {
                    if (map[coordinate_row][coordinate_column] != 0 || map[coordinate_row - 1][coordinate_column] != 0 || map[coordinate_row - 2][coordinate_column] != 0) {
                        return false;
                    }
                    break;
                }
                case 3: {
                    if (map[coordinate_row][coordinate_column] != 0 || map[coordinate_row][coordinate_column - 1] != 0 || map[coordinate_row][coordinate_column + 2] != 0) {
                        return false;
                    }
                    break;
                }
                case 4: {
                    if (map[coordinate_row][coordinate_column] != 0 || map[coordinate_row - 1][coordinate_column] != 0 || map[coordinate_row][coordinate_column + 1] != 0) {
                        return false;
                    }
                    break;
                }
                case 5: {
                    if (map[coordinate_row][coordinate_column] != 0 || map[coordinate_row - 1][coordinate_column] != 0 || map[coordinate_row][coordinate_column + 1] != 0 || map[coordinate_row - 1][coordinate_column + 1] != 0) {
                        return false;
                    }

                    break;
                }
                case 6: {
                    if (map[coordinate_row][coordinate_column] != 0 || map[coordinate_row - 1][coordinate_column] != 0) {
                        return false;
                    }
                    break;
                }
                case 7: {
                    if (map[coordinate_row][coordinate_column] != 0 || map[coordinate_row][coordinate_column + 1] != 0) {
                        return false;
                    }
                    break;
                }
                case 8: {
                    if (map[coordinate_row][coordinate_column] != 0 || map[coordinate_row][coordinate_column + 1] != 0 || map[coordinate_row - 1][coordinate_column + 1] != 0) {
                        return false;
                    }
                    break;
                }
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    //utility:
    private int[][] arrayCopy(int[][] copy) {
        int[][] copied = new int[copy.length][copy[0].length];

        for (int i = 0; i < 8; i++) {
            System.arraycopy(copy[i], 0, copied[i], 0, 8);
        }
        return copied;
    }

    private int switchPlayer(int currentPlayer) {
        if (currentPlayer == 1) {
            return 2;
        }
        return 1;
    }

}


class Node96100374<T> {

    private T data;
    private List<Node96100374<T>> children;
    private Node96100374<T> parent = null;

    //additional
    private final int player;
    private final int depth;
    private final IntPair addedPair;
    //initialize value
    private int value;
    private int currentValue;
    private boolean isInitialized;


    public Node96100374(T data, int player, int depth, IntPair addedPair) {
        this.data = data;
        this.player = player;
        this.depth = depth;
        children = new ArrayList<>();
        this.addedPair = addedPair;

        isInitialized = false;
        value = -1;
        currentValue = -1;
    }


    public IntPair getAddedPair() {
        return addedPair;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public void setInitialized(boolean initialized) {
        isInitialized = initialized;
    }

    public void setChildren(List<Node96100374<T>> children) {
        this.children = children;
    }

    public int getValue() {
        return value;
    }


    public int getPlayer() {
        return player;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getDepth() {
        return depth;
    }

    public Node96100374<T> addChild(Node96100374<T> child) {
        child.setParent(this);
        this.children.add(child);
        return child;
    }

    public void addChildren(List<Node96100374<T>> children) {
        children.forEach(each -> each.setParent(this));
        this.children.addAll(children);
    }

    public List<Node96100374<T>> getChildren() {
        return children;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private void setParent(Node96100374<T> parent) {
        this.parent = parent;
    }

    public Node96100374<T> getParent() {
        return parent;
    }


    private void printDataOfThisNode96100374() {
        int[][] print = (int[][]) data;
        System.out.println("Depth: " + depth);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(print[i][j] + " ");
            }
            System.out.println();
        }

    }

    private void printDataOfChild(ArrayList<Node96100374> q) {
        for (int i = 0; i < this.getChildren().size(); i++) {
            q.add(getChildren().get(i));
        }
    }

    static void printWholeTree(Node96100374<int[][]> Node96100374) {
        ArrayList<Node96100374> queue = new ArrayList<>();
        queue.add(Node96100374);

        while (queue.size() != 0) {
            Node96100374 current = queue.remove(0);
            current.printDataOfThisNode96100374();
            current.printDataOfChild(queue);
        }

    }
}