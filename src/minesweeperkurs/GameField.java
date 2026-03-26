/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minesweeperkurs;

/**
 *
 * @author tolyan
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class GameField extends JPanel{
	private JButton[][] Buttons;
//	private final GameLogic Game;
	private int rnd_rows;
	private int rnd_cols;

//	public GameField(GameLogic Game) {
//		this.Game = Game;
////		int[] FieldSize = Game.GetFieldSize();
//		rnd_rows = FieldSize[0];
//		rnd_cols = FieldSize[1];
//		setLayout(new GridLayout(FieldSize[0], FieldSize[1], 1, 1));
//		setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
//
//		Buttons = new JButton[FieldSize[0]][FieldSize[1]];
//
//		CreateButtons(FieldSize[0], FieldSize[1]);
//	}

	private void CreateButtons(int Rows, int Cols) {
		for (int i = 0; i < Rows; i++) {
			for(int j = 0; j < Cols; j++) {
				JButton Button = new JButton();
				Button.setPreferredSize(new Dimension(40,40));
				Button.setFont(new Font("Segoe UI", Font.BOLD, 16));
				Button.setFocusPainted(false);
				Button.setMargin(new Insets(0, 0, 0, 0));
				Button.putClientProperty("row", i);
				Button.putClientProperty("col", j);
				Button.addActionListener(e -> clickedOnCell(Button));
				Button.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if (SwingUtilities.isRightMouseButton(e)) {
							clickedOnCellRight(Button);
						}
						else if (SwingUtilities.isMiddleMouseButton(e)) {
							clickedOnCellMiddle(Button);
						}
					}
				});
				Buttons[i][j] = Button;
				add(Button);
			}
		}
	}

	private void clickedOnCell(JButton Button) {
		int i = (int) Button.getClientProperty("row");
		int j = (int) Button.getClientProperty("col");
		
//		Game.OpenCell(i, j);
//		if (Game.GetGameStatus() && Game.CheckWinCondition() == false) {
//			JOptionPane.showMessageDialog(this, "ЛМАО, ПОСОСИ!");
//		}
//		else if (Game.CheckWinCondition() == true) {
//			JOptionPane.showMessageDialog(this, "ГОЙДА!!");
//		}
//		updateAllCellsRender();
	}

	private void clickedOnCellRight(JButton Button){
		int i = (int) Button.getClientProperty("row");
		int j = (int) Button.getClientProperty("col");
//		Game.ToggleFlag(i, j);
//		updateAllCellsRender();
	}

	private void clickedOnCellMiddle(JButton Button){
		int i = (int) Button.getClientProperty("row");
		int j = (int) Button.getClientProperty("col");
//		Game.OpenAllNearbyCells(i, j);
//		updateAllCellsRender();
	}

//	private void updateCellRender(int Row, int Col) {
//		JButton Button = Buttons[Row][Col];
//		int State = Game.GetValue(Row, Col);
//		if (State == CellState.Empty.ordinal() || State == CellState.ContainesMine.ordinal()){
//			Button.setText("");
//			Button.setEnabled(true);
//		} else if (State == CellState.Flagged.ordinal() || State == CellState.FlaggedMine.ordinal()) {
//			Button.setText("F");
//			Button.setEnabled(true);
//		} else if (State == CellState.Blown.ordinal()) {
//			Button.setText("B");
//			Button.setEnabled(true);
//		} else {
//			if (Game.CountNearbyMines(Row, Col) != 0){
//				Button.setText(String.valueOf(Game.CountNearbyMines(Row, Col)));
//				Button.setEnabled(true);
//			}
//			else {
//				Button.setText("");
//				Button.setEnabled(true);
//			}
//	}
	}

//	public void updateAllCellsRender() {
//		for (int i = 0; i < rnd_rows; i++) {
//			for(int j = 0; j < rnd_cols; j++) {
//				updateCellRender(i, j);
//			}
//		}
//		
//	}
//}
