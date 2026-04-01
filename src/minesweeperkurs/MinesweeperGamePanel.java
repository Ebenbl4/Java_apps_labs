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
import java.util.ArrayList;
import java.util.List;

public class MinesweeperGamePanel extends JPanel {
	private static final Color[] Colors = {
		Color.black, // Mine color
		Color.blue,
		Color.green,
		Color.red,
		new Color(0, 0, 139), // Dark blue
		new Color(139, 69, 19), // Brown
		Color.cyan,
		Color.black,
		Color.gray
	};

	private final Runnable retFunc;
	private GameInterface gameInterface;
	private JButton[][] buttons;
	private short rows;
	private short cols;

	public MinesweeperGamePanel(Runnable retFunc) {
		this.retFunc = retFunc;
		setLayout(new GridBagLayout());
		initGameField();
	}

	private void initGameField() {
			this.gameInterface = new GameLogic();
			short[] fieldSize = gameInterface.getFieldSize();
			this.rows = fieldSize[0];
			this.cols = fieldSize[1];

			GridBagConstraints c = new GridBagConstraints();
			JButton btnMenu = new JButton("To menu");
			c.weightx = 0.5;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = 0;
			add(btnMenu);

			JPanel gameField = new JPanel(new GridLayout(fieldSize[0], fieldSize[1], 3, 3));
			gameField.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
			createButtons(gameField);
			c.gridy = 1;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.weighty = 1.0;
			c.fill = GridBagConstraints.BOTH;
			c.insets = new Insets(10, 5, 0, 5);
			add(gameField, c);

			btnMenu.addActionListener(e -> retFunc.run());
		}

		private void createButtons(JPanel grid) {
			buttons = new JButton[rows][cols];
			for (short i = 0; i < rows; i++) {
				for (short j = 0; j < cols; j++) {
					JButton button = new JButton();
					button.setPreferredSize(new Dimension(40, 40));
					button.setFont(new Font("Arial", Font.BOLD, 16));
					button.setFocusPainted(false);
					button.setBackground(Color.gray);
					button.setMargin(new Insets(0, 0, 0, 0));
					button.putClientProperty("row", i);
					button.putClientProperty("col", j);
					button.addActionListener(e -> {
						clickedOnCell(button);
					});
					button.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent e) {
							if (SwingUtilities.isRightMouseButton(e)) {
							} else if (SwingUtilities.isMiddleMouseButton(e)) {
							}
						}
					});
					buttons[i][j] = button;
					grid.add(button);
				}
			}
		}

		private void clickedOnCell(JButton button) {
			short i = (short) button.getClientProperty("row");
			short j = (short) button.getClientProperty("col");
			OpenCellInterface record = gameInterface.openCell(i, j);
			render(record);
			boolean[] status = gameInterface.getGameStatus();
			if (status[0] && !status[1]) {
				JOptionPane.showMessageDialog(this, "ЛМАО, ПОСОСИ!");
			} else if (status[0] && status[1]) {
				JOptionPane.showMessageDialog(this, "ГОЙДА!");
			}
		}

		private void render(OpenCellInterface record) {
			if (record instanceof OpenCellInterface.SingleCell single) {
				renderSingleCell(single.row(), single.col());
			}
			else if (record instanceof OpenCellInterface.MultipleCells multiple) {
				renderCellsFromList(multiple);
			}
			else if (record instanceof OpenCellInterface.OpenAllMines mines) {
				renderShowAllMines(mines);
			}
		}

		private void renderSingleCell(short row, short col) {
			JButton button = buttons[row][col];
			CellState state = gameInterface.getState(row, col);
			switch (state) {
				case OPENED -> {
					byte nearbyMines = gameInterface.getNearbyMinesCount(row, col);
					if (nearbyMines == 0) {
						button.setText("");
					} else {
						button.setText(String.valueOf(nearbyMines));
						button.setForeground(Colors[nearbyMines]);
					}
					button.setBackground(Color.white);
				}
				case EMPTY -> {
					button.setText("");
				}
				case CONTAINS_MINE -> {
					button.setText("");
				}
				case FLAGGED -> {
					button.setText("F");
				}
				case FLAGGED_MINE -> {
					button.setText("F");

				}
				default -> {
				}
			}
			button.setEnabled(true);
		}

		private void renderCellsFromList(OpenCellInterface.MultipleCells record) {
			List<short[]> cellsToOpen = record.cellsList();
			for (short[] cell : cellsToOpen) {
				renderSingleCell(cell[0], cell[1]);
			}
		}

		private void renderShowAllMines(OpenCellInterface.OpenAllMines record) {
			List<short[]> cellsToOpen = record.cellsList();
			for (short[] cell : cellsToOpen) {
				JButton button = buttons[cell[0]][cell[1]];
				button.setText("*");
				button.setForeground(Colors[0]);
				button.setBackground(Color.white);
			}
		}
}
