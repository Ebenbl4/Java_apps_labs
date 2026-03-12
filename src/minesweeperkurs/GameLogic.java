/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minesweeperkurs;
import java.awt.desktop.OpenFilesHandler;
import javax.swing.border.EmptyBorder;
import java.util.*;
/**
 *
 * @author tolyan
 */

public class GameLogic {
	public static enum CellState {
		EMPTY,
		CONTAINS_MINE,
		FLAGGED,
		FLAGGED_MINE,
		OPENED,
		BLOWN,
	}
	
    private final Random random = new Random();
	private final List<List<CellState>> field;
	private final List<List<Byte>> nearbyMineCounts;
	private final int ROWS = 18;
    private final int COLS = 30;
    private final int MINES_COUNT = 99;
    private int minesRemain = MINES_COUNT;
	private boolean gameOver;
	private boolean winCondition;

	public GameLogic(){
		this.field = new ArrayList<>();
		this.nearbyMineCounts = new ArrayList<>();
		initField();
	}
	
	public void initField() {
		minesRemain = MINES_COUNT;
		gameOver = false;
		winCondition = false;
		for(int i = 0; i < ROWS; i++) {
			List<CellState> row = new ArrayList<>();
			for(int j = 0; j < COLS; j++) {
				row.add(CellState.EMPTY);
			}
			field.add(row);
		}

		List<int[]> positionsList = new ArrayList<>();
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) { 
				positionsList.add(new int[]{i, j});
			}
		}
		
		Collections.shuffle(positionsList);
		positionsList.stream().limit(MINES_COUNT).forEach(pos -> field.get(pos[0]).set(pos[1], CellState.CONTAINS_MINE));
		
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				nearbyMineCounts.get(i).set(j, countNearbyMines(i, j));
			}
		}
	}

	public int[] getFieldSize() {
		int[] fieldSize = {ROWS, COLS};
		return fieldSize;
	}

    public int setValue(CellState value, int row, int col) {
    	if ((row > ROWS || row < 0) || (col > COLS || col < 0)) {
		    return 1;
	    }
		else {
			field.get(row).set(col, value);
			return 0;
		}
    }

    public CellState getValue(int row, int col) {
		if ((row > ROWS || row < 0) || (col > COLS || col < 0)) {
				//write custom exception here 
				return field.get(row).get(col);
			}
			else {
				return field.get(row).get(col);
			}
	}


	private int countFlaggedCells() {
		int count = (int) field.stream().flatMap(List::stream).filter(state -> state == CellState.FLAGGED || state == CellState.FLAGGED_MINE).count();
		return count;
	}

	public boolean checkWinCondition() {
	winCondition = (ROWS * COLS - MINES_COUNT == (int) field.stream().flatMap(List::stream).filter(state -> state == CellState.OPENED).count());
		return winCondition;
	}

	public boolean getGameStatus() {
		return gameOver;
	}

	public void toggleFlag(int row, int col) {
		CellState state = field.get(col).get(row);
		if (null != state) switch (state) {
			case EMPTY -> field.get(row).set(col, CellState.FLAGGED);
			case CONTAINS_MINE -> field.get(row).set(col, CellState.FLAGGED_MINE);
			case FLAGGED -> field.get(row).set(col, CellState.EMPTY);
			case FLAGGED_MINE -> field.get(row).set(col, CellState.CONTAINS_MINE);
			default -> {
			}
		}
		updateCounter();
	}
	
	private byte countNearbyMines(int row, int col) {
		byte count = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (i == 0 && j == 0) {
					continue;
				}
				if (i + row < 0 && i + row > ROWS && j + col < 0 && j + col > COLS) {
					if (field.get(row + i).get(col + j) == CellState.CONTAINS_MINE || field.get(row + i).get(col + j) == CellState.FLAGGED_MINE) {
						count++;
					}
				}
			}
		}
		return count;
	}

	private byte countNearbyFlags(int row, int col) {
		byte count = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (i == 0 && j == 0) {
					continue;
				}
				if (i + row < 0 && i + row > ROWS && j + col < 0 && j + col > COLS) {
					if (field.get(row + i).get(col + j) == CellState.FLAGGED || field.get(row + i).get(col + j) == CellState.FLAGGED_MINE) {
						count++;
					}
				}
			}
		}
		return count;
	}
	
    public void updateCounter() {
		int countedFlags = countFlaggedCells();
		if (countedFlags <= minesRemain) {
			minesRemain = MINES_COUNT - countedFlags;
		}
	}

	public void openCell(int row, int col) {
		if (gameOver) return;
		CellState state = field.get(row).get(col);
		if (null != state) switch (state) {
			case EMPTY -> {
				if (nearbyMineCounts.get(row).get(col) == 0){
					//Open nearby cells placeholder
					return;
				}
				field.get(row).set(col, CellState.OPENED);
			}
			case CONTAINS_MINE -> {
				gameOver = true;
				//Change all states to blown for cells that contain mine placeholder
			}
		}
	}

//	public List<byte[]> getCellsToOpenList() {
//		List<byte[]> toOpen = new ArrayList<>();
//
//	}
//	public void OpenCell(int Row, int Col) {
//		if (GameOver || GameField[Row][Col] == CellState.Flagged.ordinal() || GameField[Row][Col] == CellState.FlaggedMine.ordinal()) {return;}
//		if (this.GameField[Row][Col] == CellState.ContainesMine.ordinal()) {
//			this.GameField[Row][Col] = CellState.Blown.ordinal();
//			//GameOver = true;
//			return;
//		}
//		if (this.GameField[Row][Col] == CellState.Empty.ordinal()) {
//			GameField[Row][Col] = CellState.OpenedEmpty.ordinal();
//			if (CountNearbyMines(Row, Col) == 0) {
//				OpenAllNearbyCells(Row, Col);
//			}
//		}
//		
//	} 
//
//	public void OpenAllNearbyCells(int Row, int Col) {
//		int[][] AdjacentObjectsCoordinates = new int[][] {
//			{-1, -1},
//			{-1, 0},
//			{-1, 1},
//			{0, -1},
//			{0, 1},
//			{1, -1},
//			{1, 0},
//			{1, 1}
//		};
//		//if (CountNearbyFlags(Row, Col) > CountNearbyMines(Row, Col)) {
//		//	return;
//		//}
//		for	(int i = 0; i < 8; i++) {
//			if ((Row + AdjacentObjectsCoordinates[i][0] < 0 || Row + AdjacentObjectsCoordinates[i][0] >= RowsCount) || 
//				(Col + AdjacentObjectsCoordinates[i][1] < 0 || Col + AdjacentObjectsCoordinates[i][1] >= ColsCount)){
//			}
//			else{
//				OpenCell(Row + AdjacentObjectsCoordinates[i][0], Col + AdjacentObjectsCoordinates[i][1]);
//			}
//		} 
//	}
//
//	public void ToggleFlag(int Row, int Col) {
//		if (this.GameField[Row][Col] == CellState.ContainesMine.ordinal() || this.GameField[Row][Col] == CellState.Empty.ordinal()) {
//			this.GameField[Row][Col] = (this.GameField[Row][Col] == CellState.ContainesMine.ordinal()) ? CellState.FlaggedMine.ordinal() : CellState.Flagged.ordinal();
//			return;
//		}
//		if (this.GameField[Row][Col] == CellState.FlaggedMine.ordinal() || this.GameField[Row][Col] == CellState.Flagged.ordinal()) {
//			this.GameField[Row][Col] = (this.GameField[Row][Col] == CellState.FlaggedMine.ordinal()) ? CellState.ContainesMine.ordinal() : CellState.Empty.ordinal();
//		}
//	}

}