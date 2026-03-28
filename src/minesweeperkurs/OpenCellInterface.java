/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minesweeperkurs;

import java.util.List;

/**
 *
 * @author tolyan
 */
public interface OpenCellInterface {
	record SingleCell(short row, short col) implements OpenCellInterface {};
	record MultipleCells(List<short[]> cellsList) implements OpenCellInterface {};
	record OpenAllMines(List<Short[]> minesList) implements OpenCellInterface {};
	record NoAction() implements OpenCellInterface {};
}
