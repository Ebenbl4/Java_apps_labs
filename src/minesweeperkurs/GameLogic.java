/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minesweeperkurs;
import java.util.Random;
/**
 *
 * @author tolyan
 */

public class GameLogic {
    final private Random random = new Random();
    private int[][] GameField;
    private final int RowsCount = 18;
    private final int ColsCount = 30;
    private final int MinesCount = 36;
    private int MineCounter;
	private boolean GameOver;
	private boolean WinCondition;

	public GameLogic(){
		this.GameField = new int[RowsCount][ColsCount];
		InitField();
	}

    public void InitField() {
	int MinesToFill = MinesCount;
	for (int i = 0; i < RowsCount; i++) {
		for(int j = 0; j < ColsCount; j++) {
			this.GameField[i][j] = CellState.Empty.ordinal();
		}
	}
	while (MinesToFill != 0) {
		int rand_row = random.nextInt(RowsCount);
		int rand_col = random.nextInt(ColsCount);
		if (random.nextInt(2) == 1 && (GameField[rand_row][rand_col] != CellState.ContainesMine.ordinal())){
			MinesToFill--;
			this.GameField[rand_row][rand_col] = CellState.ContainesMine.ordinal();
		}
	}
	MineCounter = MinesCount;
	GameOver = false;
	WinCondition = false;
    }

	public int[] GetFieldSize() {
		int[] FieldSize = {RowsCount, ColsCount};
		return FieldSize;
	}

    public int SetValue(int Value, int Row, int Col) {
    	if ((Row > RowsCount || Row < 0) || (Col > ColsCount || Col < 0)) {
		    return 1;
	    }
		else {
			this.GameField[Row][Col] = Value;
			return 0;
		}
    }

    public int GetValue(int Row, int Col) {
	if ((Row > RowsCount || Row < 0) || (Col > ColsCount || Col < 0)) {
	    return 1;
        }
		else {
			return this.GameField[Row][Col];
		}
    }

	private int CountFlaggedCells() {
		int Counter = 0;
		for (int i = 0; i < RowsCount; i++) {
			for(int j = 0; j < ColsCount; j++) {
				if (this.GameField[i][j] == CellState.Flagged.ordinal() || this.GameField[i][j] == CellState.FlaggedMine.ordinal()) {
					Counter++;
				}
			}
		}
		return Counter;
	}
	
    public void UpdateCounter() {
		int CountedFlags = CountFlaggedCells();
		if (CountedFlags <= MineCounter) {
			MineCounter = MinesCount - CountedFlags;
		}
	}
            
	public int CountNearbyMines(int Row, int Col) {
		int Counter = 0;
		int[][] AdjacentObjectsCoordinates = new int[][] {
			{-1, -1},
			{-1, 0},
			{-1, 1},
			{0, -1},
			{0, 1},
			{1, -1},
			{1, 0},
			{1, 1}
		};
		for	(int i = 0; i < 8; i++) {
			if ((Row + AdjacentObjectsCoordinates[i][0] < 0 || Row + AdjacentObjectsCoordinates[i][0] >= RowsCount) ||
    (Col + AdjacentObjectsCoordinates[i][1] < 0 || Col + AdjacentObjectsCoordinates[i][1] >= ColsCount)){
			}
			else {
				if (this.GameField[AdjacentObjectsCoordinates[i][0] + Row][AdjacentObjectsCoordinates[i][1] + Col] == CellState.ContainesMine.ordinal() || this.GameField[AdjacentObjectsCoordinates[i][0] + Row][AdjacentObjectsCoordinates[i][1] + Col] == CellState.Blown.ordinal()) {
				Counter++;
				}
			} 
		}
		return Counter;
	}

	private int CountNearbyFlags(int Row, int Col) {
		int Counter = 0;
		int[][] AdjacentObjectsCoordinates = new int[][] {
			{-1, -1},
			{-1, 0},
			{-1, 1},
			{0, -1},
			{0, 1},
			{1, -1},
			{1, 0},
			{1, 1}
		};
		for	(int i = 0; i < 8; i++) {
			if ((Row + AdjacentObjectsCoordinates[i][0] < 0 || Row + AdjacentObjectsCoordinates[i][0] >= RowsCount) ||
    (Col + AdjacentObjectsCoordinates[i][1] < 0 || Col + AdjacentObjectsCoordinates[i][1] >= ColsCount)){
			}
			else {
				if (this.GameField[AdjacentObjectsCoordinates[i][0] + Row][AdjacentObjectsCoordinates[i][1] + Col] == CellState.FlaggedMine.ordinal() || this.GameField[AdjacentObjectsCoordinates[i][0] + Row][AdjacentObjectsCoordinates[i][1] + Col] == CellState.Flagged.ordinal()) {
				Counter++;
				}
			} 
		}
		return Counter;
	}
	
	
	public void OpenCell(int Row, int Col) {
		if (GameOver || GameField[Row][Col] == CellState.Flagged.ordinal() || GameField[Row][Col] == CellState.FlaggedMine.ordinal()) {return;}
		if (this.GameField[Row][Col] == CellState.ContainesMine.ordinal()) {
			this.GameField[Row][Col] = CellState.Blown.ordinal();
			//GameOver = true;
			return;
		}
		if (this.GameField[Row][Col] == CellState.Empty.ordinal()) {
			GameField[Row][Col] = CellState.OpenedEmpty.ordinal();
			if (CountNearbyMines(Row, Col) == 0) {
				OpenAllNearbyCells(Row, Col);
			}
		}
		
	} 

	public void OpenAllNearbyCells(int Row, int Col) {
		int[][] AdjacentObjectsCoordinates = new int[][] {
			{-1, -1},
			{-1, 0},
			{-1, 1},
			{0, -1},
			{0, 1},
			{1, -1},
			{1, 0},
			{1, 1}
		};
		//if (CountNearbyFlags(Row, Col) > CountNearbyMines(Row, Col)) {
		//	return;
		//}
		for	(int i = 0; i < 8; i++) {
			if ((Row + AdjacentObjectsCoordinates[i][0] < 0 || Row + AdjacentObjectsCoordinates[i][0] >= RowsCount) || 
				(Col + AdjacentObjectsCoordinates[i][1] < 0 || Col + AdjacentObjectsCoordinates[i][1] >= ColsCount)){
			}
			else{
				OpenCell(Row + AdjacentObjectsCoordinates[i][0], Col + AdjacentObjectsCoordinates[i][1]);
			}
		} 
	}

	public void ToggleFlag(int Row, int Col) {
		if (this.GameField[Row][Col] == CellState.ContainesMine.ordinal() || this.GameField[Row][Col] == CellState.Empty.ordinal()) {
			this.GameField[Row][Col] = (this.GameField[Row][Col] == CellState.ContainesMine.ordinal()) ? CellState.FlaggedMine.ordinal() : CellState.Flagged.ordinal();
			return;
		}
		if (this.GameField[Row][Col] == CellState.FlaggedMine.ordinal() || this.GameField[Row][Col] == CellState.Flagged.ordinal()) {
			this.GameField[Row][Col] = (this.GameField[Row][Col] == CellState.FlaggedMine.ordinal()) ? CellState.ContainesMine.ordinal() : CellState.Empty.ordinal();
		}
	}

	public boolean CheckWinCondition() {
		for(int i = 0; i < RowsCount; i++) {
			for(int j =0; j < ColsCount; j++) {
				if (this.GameField[i][j] != CellState.Empty.ordinal() || this.GameField[i][j] != CellState.Flagged.ordinal()) {
				}		
				else {
					WinCondition = true;
					GameOver = true;
					return WinCondition;
				}
			}
		}
		return WinCondition;
	}

	public boolean GetGameStatus() {
		return GameOver;
	}

}