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
public class MinesweeperWindow extends JFrame{

	public MinesweeperWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		JPanel appMenu = createMenuPanel();
		setContentPane(appMenu);
	}

	private void switchPanel(JPanel panel) {
		setContentPane(panel);
		revalidate();
		repaint();
	}
	
	private JPanel createMenuPanel() {
		JPanel panel = new JPanel(new GridLayout(4, 1, 0, 40));
		panel.setBorder(BorderFactory.createEmptyBorder(50, 250, 50, 250));

		JLabel title = new JLabel("Main menu", SwingConstants.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 20));
		JButton btnOption1 = new JButton("opt1");
		JButton btnOption2 = new JButton("opt2");
		JButton btnExit = new JButton("Exit");

		btnOption1.setFont(new Font("Arial", Font.PLAIN, 16));
		btnOption2.setFont(new Font("Arial", Font.PLAIN, 16));
		btnExit.setFont(new Font("Arial", Font.PLAIN, 16));
		panel.add(title);
		panel.add(btnOption1);
		panel.add(btnOption2);
		panel.add(btnExit);

		btnExit.addActionListener(e -> System.exit(0));
		btnOption1.addActionListener(e -> switchPanel(createGameFieldPanel()));
		
		
		return panel;
	}
	
	private JPanel createGameFieldPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		JButton btnMenu = new JButton("To menu");
		c.weightx = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(btnMenu);
		GameLogic game = new GameLogic();
		short[] fieldSize = game.getFieldSize();
		JPanel gameField = new JPanel(new GridLayout(fieldSize[0], fieldSize[1], 3, 3));
		gameField.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		JButton[][] fieldButtons = createButtons(fieldSize[0], fieldSize[1], gameField);
		c.gridy = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 5, 0, 5);
		
		panel.add(gameField, c);
		
		return panel;
	}

	JButton[][] createButtons(short rows, short cols, JPanel grid) {
		JButton[][] buttons = new JButton[cols][cols];
		for (short i = 0; i < rows; i++) {
			for (short j = 0; j < cols; j++) {
				JButton button = new JButton();
				button.setPreferredSize(new Dimension(40,40));
				button.setFont(new Font("Arial", Font.BOLD, 16));
				button.setFocusPainted(false);
				button.setMargin(new Insets(0, 0, 0, 0));
				button.putClientProperty("row", i);
				button.putClientProperty("col", j);
//				button.addActionListener(e -> clickedOnCell(button));
//				button.addMouseListener(new MouseAdapter() {
//					@Override
//					public void mousePressed(MouseEvent e) {
//						if (SwingUtilities.isRightMouseButton(e)) {
//						}
//						else if (SwingUtilities.isMiddleMouseButton(e)) {
//						}
//					}
//				});
				buttons[i][j] = button;
				grid.add(button);
			}
		}
		return buttons;
	}
}

