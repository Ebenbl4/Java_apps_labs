/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minesweeperkurs;

/**
 *
 * @author tolyan
 */
public interface GameInterface {
	public short [] getFieldSize();
	public CellState getState(short row, short col);
	public byte getNearbyMinesCount(short row, short col);
	public void checkWinCondition();
	public boolean getWinCondition();
	public boolean getGameOver();
	public void updateCounter();
	public OpenCellInterface getFlaggedCellsList();
	OpenCellInterface openCell(short row, short col);
	public OpenCellInterface toggleFlag(short row, short col);
	public OpenCellInterface openNearbyCells(short row, short col);
}
