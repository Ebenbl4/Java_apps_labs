/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minesweeperkurs;

/**
 *
 * @author tolyan
 */

public enum CellState {
	EMPTY,
	CONTAINS_MINE,
	FLAGGED,
	FLAGGED_MINE,
	OPENED,
	BLOWN,
}