package com.company.players;

import java.util.ArrayList;

import com.company.Board;
import com.company.Cell;
import com.company.IntPair;

public class Player96109963 extends Player {

    private final static int MAX_DEPTH = 4;
	public Player96109963(int col) {
		super(col);
	}

	@Override
	public IntPair getMove(Board board) {
		
		int player = this.getCol();
		int a = -1000000;
		int b = 1000000;
		int action_i = 0;
		int action_j = 0;
		int max_value = -100000;
		int depth = 0;
		
		for (int i = 0; i < Board.size; i++) {
			for (int j = 0; j < Board.size; j++) {
				int type = board.getBlocks(player).get(0);
				Board boardCopy = new Board(board);
				int possible = 
						boardCopy.move(new IntPair(i, j), player, type);
				if (possible == 0) {
					int cand_value = get_min_value(boardCopy, 3 - player, a, b, depth + 1);
					if (cand_value > max_value) {
						max_value = cand_value;
						action_i = i;
						action_j = j;
						if (max_value >= a)
							a = max_value;
					}
				}
			}
		}	
		return new IntPair(action_i, action_j);
	}
	
	public int utility(Board board) {
		int player = this.getCol();
		return board.getScore(player) - board.getScore(3 - player);
	}
	
	public int get_max_value(Board board, int player, int a, int b, int depth) {

		if (depth  >= MAX_DEPTH || board.getNumberOfMoves() == 80) {
			return utility(board);
		}
		
		int max_value = -100000;
		for (int i = 0; i < Board.size; i++) {
			for (int j = 0; j < Board.size; j++) {
				int type = board.getBlocks(player).get(depth/2);
				Board boardCopy = new Board(board);
				int possible = 
						boardCopy.move(new IntPair(i, j), player, type);
				if (possible == 0) {
					int cand_value = get_min_value(boardCopy, 3 - player, a, b, depth + 1);
					if (cand_value > max_value)
						max_value = cand_value;
					if (max_value >= b)
						return max_value;
					if (max_value >= a)
						a = max_value;
//					System.out.println(a + " max " + b + " " + max_value);
				}
				
			}
		}	
		return max_value;
	}
	
	public int get_min_value(Board board, int player, int a, int b, int depth) {
		if (depth  >= MAX_DEPTH || board.getNumberOfMoves() == 80) {
			return utility(board);
		}
		
		int min_value = 100000;
		for (int i = 0; i < Board.size; i++) {
			for (int j = 0; j < Board.size; j++) {
				int type = board.getBlocks(player).get(depth/2);
				Board boardCopy = new Board(board);
				int possible = 
						boardCopy.move(new IntPair(i, j), player, type);
				if (possible == 0) {
					int cand_value = get_max_value(boardCopy, 3 - player, a, b, depth + 1);
					if (cand_value < min_value)
						min_value = cand_value;
					if (min_value <= a)
						return min_value;
					if (min_value <= b)
						b = min_value;
//					System.out.println(a + " min " + b + " " + min_value);
				}
				
			}
		}	
		return min_value;
	}
	
	

}
