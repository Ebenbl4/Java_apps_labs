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
		
		return panel;
	}

}

