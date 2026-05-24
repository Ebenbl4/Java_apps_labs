/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minesweeperkurs;
import java.util.*;
import minesweeperkurs.OpenCellInterface.*;
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
	private boolean firstClick;

	public GameLogic(){
		this.field = new ArrayList<>();
		this.nearbyMineCounts = new ArrayList<>();
		initField();
	}
	
	public final void initField() {
		minesRemain = MINES_COUNT;
		gameOver = false;
		winCondition = false;
		firstClick = true;
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
	public int getTotalMinesCount() {
		return this.MINES_COUNT;
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
	@Override
	public boolean getWinCondition() { return winCondition; }
	@Override
	public boolean getGameOver() { return gameOver; }
	@Override
	public short getFlaggedCellsCount() {
		short count = (short) field.stream().flatMap(List::stream).filter(state -> state == CellState.FLAGGED || state == CellState.FLAGGED_MINE).count();
		return count;
	}

	@Override
	public void checkWinCondition() {
		if (winCondition = (ROWS * COLS - MINES_COUNT == (int) field.stream().flatMap(List::stream).filter(state -> state == CellState.OPENED).count())) {
			gameOver = winCondition;
			flagAllMines();
		}
	}

	@Override
	public OpenCellInterface toggleFlag(short row, short col) {
		if (gameOver) return new NoAction();
		CellState state = field.get(row).get(col);
		if (null != state) switch (state) {
			case EMPTY -> {
				field.get(row).set(col, CellState.FLAGGED);
			}
			case CONTAINS_MINE -> {
				field.get(row).set(col, CellState.FLAGGED_MINE);
			}
			case FLAGGED -> {
				field.get(row).set(col, CellState.EMPTY);
			}
			case FLAGGED_MINE -> {
				field.get(row).set(col, CellState.CONTAINS_MINE);
			}
			default -> {
			}
		}
		return new SingleCell(row, col);
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
		int countedFlags = getFlaggedCellsCount();
		if (countedFlags <= minesRemain) {
			minesRemain = MINES_COUNT - countedFlags;
		}
	}

	@Override
	public OpenCellInterface openCell(short row, short col) {
		if (gameOver) return new NoAction();	
		CellState state = field.get(row).get(col);
		if (null != state) switch (state) {
			case EMPTY -> {
				if (firstClick) {
					firstClick = false;
				}
				if (nearbyMineCounts.get(row).get(col) == 0){
					List<short[]> list = getCellsToOpenList(row, col);
					openCellsFromList(list);
					return new MultipleCells(list);
				}
				else {
					field.get(row).set(col, CellState.OPENED);
					return new SingleCell(row, col);
				}
			}
			case CONTAINS_MINE -> {
				if (firstClick) {
					moveMineToCorner(row, col);
					firstClick = false;
					return openCell(row, col);
				}
				else {
					gameOver = true;
					blowAllMines();
					return new MultipleCells(getBlownCellsList());
				}
			}
		}
		return new NoAction();
	}

	@Override
	public OpenCellInterface openNearbyCells(short row, short col) {
		if (getState(row, col) != CellState.OPENED || gameOver) {
			return new NoAction();
		}
		List<short[]> cellsToOpen = new ArrayList<>();
		if (countNearbyFlags(row, col) != getNearbyMinesCount(row, col)) {
			return new NoAction();
		}
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				short newRow = (short) (row + i);
				short newCol = (short) (col + j);
				if ((i == 0 && j == 0) || newRow < 0 || newRow >= ROWS || newCol < 0 || newCol >= COLS) {
					continue;
				}
				CellState state = getState(newRow, newCol);
				if (state == CellState.CONTAINS_MINE) {
					blowAllMines();
					return new MultipleCells(getBlownCellsList());
				}
				if (state == CellState.EMPTY) {
					OpenCellInterface result = openCell(newRow, newCol);
					if (result instanceof OpenCellInterface.SingleCell single) {
						cellsToOpen.add(new short[]{single.row(), single.col()});
					} 
					else if (result instanceof OpenCellInterface.MultipleCells multiple) {
						cellsToOpen.addAll(multiple.cellsList());
					}
				}

			}
		}
			
		if (cellsToOpen.isEmpty()) {
			return new NoAction();
		}
		else {
			openCellsFromList(cellsToOpen);
			return new MultipleCells(cellsToOpen);
		}
	}

	private void moveMineToCorner(short row, short col) {
		field.get(row).set(col, CellState.EMPTY);
		boolean movedMine = false;
		short[] movedMineCoords = null;
		out:
		for(short i = (short) (ROWS - 1); i > -1; i--) {
			for(short j = (short) (COLS - 1); j > -1; j-- ) {
				switch (field.get(i).get(j)) {
					case EMPTY -> {
						field.get(i).set(j, CellState.CONTAINS_MINE);
						movedMine = true;
						movedMineCoords = new short[] {i, j};
					}
					case FLAGGED -> {
						field.get(i).set(j, CellState.FLAGGED_MINE);
						movedMine = true;
						movedMineCoords = new short[] {i, j};
					}
					default -> {
						break;
					}
				}
				if (movedMine) {
					break out;
				}
			}
		}
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				short newRow = (short) (i + row);
				short newCol = (short) (j + col);
				if ((i == 0 && j == 0) || newRow < 0 || newRow >= ROWS || newCol < 0 || newCol >= COLS) {
					continue;
				}
				nearbyMineCounts.get(newRow).set(newCol, countNearbyMines(newRow, newCol));
			}
		}
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				short newRow = (short) (i + movedMineCoords[0]);
				short newCol = (short) (j + movedMineCoords[1]);
				if ((i == 0 && j == 0) || newRow < 0 || newRow >= ROWS || newCol < 0 || newCol >= COLS) {
					continue;
				}
				nearbyMineCounts.get(newRow).set(newCol, countNearbyMines(newRow, newCol));
			}
		}
	}

	private List<short[]> getBlownCellsList(){
		List<short[]> blownCells = new ArrayList<>();
		for (short i = 0; i < ROWS; i++) {
			List<CellState> currentRow = field.get(i);
			for (short j = 0; j < COLS; j++) {
				if (currentRow.get(j) == CellState.BLOWN) {
					blownCells.add(new short[]{i, j});
				}
			}
		}
		return blownCells;
	}

	public OpenCellInterface getFlaggedCellsList(){
		List<short[]> flaggedCells = new ArrayList<>();
		for (short i = 0; i < ROWS; i++) {
			List<CellState> currentRow = field.get(i);
			for (short j = 0; j < COLS; j++) {
				if (currentRow.get(j) == CellState.FLAGGED_MINE) {
					flaggedCells.add(new short[]{i, j});
				}
			}
		}
		return new MultipleCells(flaggedCells);
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

	private void blowAllMines() {
		field.replaceAll(row -> {
			row.replaceAll(cell -> {
			if (cell == CellState.CONTAINS_MINE || cell == CellState.FLAGGED_MINE) {
				return CellState.BLOWN;
			}
			return cell;
		});
		return row;
		});
	}

	private void flagAllMines() {
		field.replaceAll(row -> {
			row.replaceAll(cell -> {
			if (cell == CellState.CONTAINS_MINE) {
				return CellState.FLAGGED_MINE;
			}
			return cell;
		});
		return row;
		});
	}
}