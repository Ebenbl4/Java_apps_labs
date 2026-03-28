/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minesweeperkurs;
import java.util.*;
/**
 *
 * @author tolyan
 */

public class GameLogic implements GameInterface {
	
	private final List<List<CellState>> field;
	private final List<List<Byte>> nearbyMineCounts;
	private final short ROWS = 18;
    private final short COLS = 30;
    private final int MINES_COUNT = 99;
    private int minesRemain = MINES_COUNT;
	private boolean gameOver;
	private boolean winCondition;

	public GameLogic(){
		this.field = new ArrayList<>();
		this.nearbyMineCounts = new ArrayList<>();
		initField();
	}
	
	public final void initField() {
		minesRemain = MINES_COUNT;
		gameOver = false;
		winCondition = false;
		for(short i = 0; i < ROWS; i++) {
			List<CellState> row = new ArrayList<>();
			for(short j = 0; j < COLS; j++) {
				row.add(CellState.EMPTY);
			}
			field.add(row);
		}
		
		List<short[]> positionsList = new ArrayList<>();
		for (short i = 0; i < ROWS; i++) {
			for (short j = 0; j < COLS; j++) { 
				positionsList.add(new short[]{i, j});
			}
		}
		
		Collections.shuffle(positionsList);
		positionsList.stream().limit(MINES_COUNT).forEach(pos -> field.get(pos[0]).set(pos[1], CellState.CONTAINS_MINE));

		for(short i = 0; i < ROWS; i++) {
			List<Byte> row = new ArrayList<>();
			for(short j = 0; j < COLS; j++) {
				row.add(countNearbyMines(i, j));
			}
			nearbyMineCounts.add(row);
		}
		
	}

	@Override
	public short[] getFieldSize() {
		short[] fieldSize = {ROWS, COLS};
		return fieldSize;
	}

	@Override
    public CellState getState(short row, short col) {
		if ((row >= ROWS || row < 0) || (col >= COLS || col < 0)) {
				//write custom exception here 
				return field.get(row).get(col);
			}
			else {
				return field.get(row).get(col);
			}
	}

	@Override
	public byte getNearbyMinesCount(short row, short col) {
		if (row >= ROWS || row < 0 || col < 0 || col >= COLS) {
			return -1; // Write exception here
		}
		return nearbyMineCounts.get(row).get(col);
	}

	private short countFlaggedCells() {
		short count = (short) field.stream().flatMap(List::stream).filter(state -> state == CellState.FLAGGED || state == CellState.FLAGGED_MINE).count();
		return count;
	}

	@Override
	public boolean checkWinCondition() {
	winCondition = (ROWS * COLS - MINES_COUNT == (int) field.stream().flatMap(List::stream).filter(state -> state == CellState.OPENED).count());
		return winCondition;
	}

	@Override
	public boolean[] getGameStatus() {
		return new boolean[]{gameOver, winCondition};
	}

	public void toggleFlag(short row, short col) {
		CellState state = field.get(col).get(row);
		if (null != state) switch (state) {
			case EMPTY -> field.get(row).set(col, CellState.FLAGGED);
			case CONTAINS_MINE -> field.get(row).set(col, CellState.FLAGGED_MINE);
			case FLAGGED -> field.get(row).set(col, CellState.EMPTY);
			case FLAGGED_MINE -> field.get(row).set(col, CellState.CONTAINS_MINE);
			default -> {
			}
		}
	}
	
	private byte countNearbyMines(short row, short col) {
		byte count = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if ((i == 0 && j == 0) || i + row < 0 || i + row >= ROWS || j + col < 0 || j + col >= COLS) {
					continue;
				}
				if (field.get(row + i).get(col + j) == CellState.CONTAINS_MINE || field.get(row + i).get(col + j) == CellState.FLAGGED_MINE) {
						count++;
				}
			}
		}
		return count;
	}

	private byte countNearbyFlags(short row, short col) {
		byte count = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if ((i == 0 && j == 0) || i + row < 0 || i + row >= ROWS || j + col < 0 || j + col >= COLS) {
					continue;
					}
				if (field.get(row + i).get(col + j) == CellState.FLAGGED || field.get(row + i).get(col + j) == CellState.FLAGGED_MINE) {
						count++;
					}
				}
			}
		return count;
	}
	
	@Override
    public void updateCounter() {
		int countedFlags = countFlaggedCells();
		if (countedFlags <= minesRemain) {
			minesRemain = MINES_COUNT - countedFlags;
		}
	}

	@Override
	public void openCell(short row, short col) {
		if (gameOver) return;
		CellState state = field.get(row).get(col);
		if (null != state) switch (state) {
			case EMPTY -> {
				if (nearbyMineCounts.get(row).get(col) == 0){
					List<short[]> list = getCellsToOpenList(row, col);
					openCellsFromList(list);
				}
				else {
					field.get(row).set(col, CellState.OPENED);
				}
			}
			case CONTAINS_MINE -> {
				gameOver = true;
				//Change all states to blown for cells that contain mine placeholder
			}
		}
	}

	private List<short[]> getCellsToOpenList(short row, short col) {
		List<short[]> cellsToOpen = new ArrayList<>();
		boolean[][] visitedCells = new boolean[ROWS][COLS];
		Queue<short[]> queue = new ArrayDeque<>();
		short[] startingPoint = {row, col};
		queue.add(startingPoint);
		visitedCells[row][col] = true;
		cellsToOpen.add(startingPoint);

		while (!queue.isEmpty()) {			
			short[] currentCell = queue.remove();
			short currentRow = currentCell[0];
			short currentCol = currentCell[1];
			for (short i = -1; i < 2; i++){
				for (short j = -1; j < 2; j++) {
					short newRow = (short) (currentRow + i);
					short newCol = (short) (currentCol + j);
					if((i == 0 && j == 0) || newRow < 0 || newRow >= ROWS || newCol < 0 || newCol >= COLS){
						continue;
					}
					if (field.get(newRow).get(newCol) == CellState.EMPTY && visitedCells[newRow][newCol] == false){
						short[] nextCell = {newRow, newCol};
						visitedCells[newRow][newCol] = true;
						if (nearbyMineCounts.get(newRow).get(newCol) == 0) {
							queue.add(nextCell);
						}
						cellsToOpen.add(nextCell);
					}
				}
			}
		}
		return cellsToOpen;
	}

	private void openCellsFromList(List<short[]> list) {
		for (short[] array : list) {
			field.get(array[0]).set(array[1], CellState.OPENED);
		}
	}

	
}