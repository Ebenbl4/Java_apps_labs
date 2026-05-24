/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minesweeperkurs;

/**
 *
 * @author tolyan
 */
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import javax.imageio.ImageIO;

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
	private JButton iconButton;
	private BufferedImage tiles[];
	JLabel flagCounter;
	private short rows;
	private short cols;
	private Timer timer;
	short seconds = 0;
	short minutes = 0;

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

		JPanel topPanel = new JPanel(new GridBagLayout());
		GridBagConstraints topC = new GridBagConstraints();

		JButton btnMenu = new JButton("To menu");
		topC.gridx = 0;
		topC.gridy = 0;
		topC.weightx = 0.0;
		topC.fill = GridBagConstraints.NONE;
		topC.anchor = GridBagConstraints.WEST;
		topC.insets = new Insets(5, 5, 5, 5);
		topPanel.add(btnMenu, topC);

		createIconButton(topPanel);
		topC.gridx = 1;
		topC.gridy = 0;
		topC.weightx = 1;
		topC.anchor = GridBagConstraints.CENTER;
		topPanel.add(iconButton, topC);

		flagCounter = new JLabel("00", JLabel.CENTER);
		flagCounter.setBackground(Color.WHITE);
		flagCounter.setForeground(Color.BLACK);
		flagCounter.setOpaque(true);
		flagCounter.putClientProperty(FlatClientProperties.STYLE,
				"arc: 5; border: 5,5,5,5,#000000");
		topC.gridx = 2;
		topC.weightx = 0;
		topC.anchor = GridBagConstraints.EAST;
		topPanel.add(flagCounter, topC);

		JLabel timerLabel = new JLabel("00:00", JLabel.CENTER);
		timerLabel.setBackground(Color.WHITE);
		timerLabel.setForeground(Color.BLACK);
		timerLabel.setOpaque(true);
		timerLabel.putClientProperty(FlatClientProperties.STYLE,
				"arc: 5; border: 5,5,5,5,#000000");
		topC.gridx = 3;
		topC.weightx = 0;
		topC.anchor = GridBagConstraints.EAST;
		topPanel.add(timerLabel, topC);
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				seconds++;
				if (seconds % 60 == 0) {
					seconds -= 60;
					minutes += 1;
				}
				if (seconds < 60 && minutes < 100) {
					String timerSecondsString = (seconds < 10) ? "0" + seconds : String.valueOf(seconds);
					String timerMinutesString = (minutes < 10) ? "0" + minutes : String.valueOf(minutes);
					timerLabel.setText(timerMinutesString + ":" + timerSecondsString);
				}
			}
		});

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 10, 5);
		add(topPanel, c);

		JPanel gameField = new JPanel(new GridLayout(fieldSize[0], fieldSize[1], 3, 3));
		gameField.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		createButtons(gameField);

		c.gridy = 1;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 5, 5, 5);
		add(gameField, c);

		btnMenu.addActionListener(e -> retFunc.run());

		timer.start();
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
				button.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if (SwingUtilities.isLeftMouseButton(e)) {
							clickedOnCell(button);
						} else if (SwingUtilities.isRightMouseButton(e)) {
							clickedOnCellRight(button);
						} else if (SwingUtilities.isMiddleMouseButton(e)) {
							clickedOnCellMiddle(button);
						}
					}
				});
				buttons[i][j] = button;
				grid.add(button);
			}
		}
	}

	private void createIconButton(JPanel panel) {
		iconButton = new JButton();
		iconButton.setSize(32, 32);
		iconButton.setFocusPainted(false);
		iconButton.setBorderPainted(false);
		iconButton.setContentAreaFilled(false);
		iconButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
		this.tiles = loadTileSet(loadTilesetFileImage("smiley_faces.bmp"), 16, 16);
		BufferedImage scaledTile = scaleTile(this.tiles[0], 32, 32);
		ImageIcon normalIcon = new ImageIcon(scaledTile);
		iconButton.setIcon(normalIcon);
		iconButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					clickedOnIconButton();
				}
			}
		});
	}

	private void updateIconButton() {
		if (gameInterface.getGameOver()) {
			if (!gameInterface.getWinCondition()) {
				BufferedImage scaledTile = scaleTile(this.tiles[1], 32, 32);
				iconButton.setIcon(new ImageIcon(scaledTile));
			} else {
				BufferedImage scaledTile = scaleTile(this.tiles[2], 32, 32);
				iconButton.setIcon(new ImageIcon(scaledTile));
			}
		}
	}

	private void clickedOnIconButton() {
		// placeholder for reset func.
	}

	private void clickedOnCell(JButton button) {
		short i = (short) button.getClientProperty("row");
		short j = (short) button.getClientProperty("col");
		render(gameInterface.openCell(i, j));
	}

	private void clickedOnCellRight(JButton button) {
		short i = (short) button.getClientProperty("row");
		short j = (short) button.getClientProperty("col");
		render(gameInterface.toggleFlag(i, j));
		updateFlaggedCellsCounter();
	}

	private void clickedOnCellMiddle(JButton button) {
		short i = (short) button.getClientProperty("row");
		short j = (short) button.getClientProperty("col");
		render(gameInterface.openNearbyCells(i, j));
	}

	private void render(OpenCellInterface record) {
		if (record instanceof OpenCellInterface.SingleCell single) {
			renderSingleCell(single.row(), single.col());
		} else if (record instanceof OpenCellInterface.MultipleCells multiple) {
			renderCellsFromList(multiple);
		}
		gameInterface.checkWinCondition();
		if (gameInterface.getGameOver()) {
			timer.stop();
			gameOver();
		}
	}

	BufferedImage loadTilesetFileImage(String filepath) {
		try {
			BufferedImage tileset = ImageIO.read(new File(filepath));
			return tileset;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Image file not found: " + filepath, "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return null;
	}

	BufferedImage[] loadTileSet(BufferedImage tileset, int tileWidth, int tileHeight) {
		int tilesCount = tileset.getWidth() / tileWidth;
		BufferedImage[] tiles = new BufferedImage[tilesCount];
		for (int i = 0; i < tilesCount; i++) {
			tiles[i] = tileset.getSubimage(i * tileWidth, 0, tileWidth, tileHeight);
		}
		return tiles;
	}

	BufferedImage scaleTile(BufferedImage srcImage, int tWidth, int tHeight) {
		BufferedImage scaledImage = new BufferedImage(tWidth, tHeight, srcImage.getType());

		Graphics2D g2d = scaledImage.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		g2d.drawImage(srcImage, 0, 0, tWidth, tHeight, null);
		g2d.dispose();

		return scaledImage;
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
				button.setForeground(Color.white);
			}
			case FLAGGED_MINE -> {
				button.setText("F");
				button.setForeground(Color.white);
			}
			case BLOWN -> {
				button.setText("*");
				button.setForeground(Colors[0]);
				button.setBackground(Color.white);
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

	private void updateFlaggedCellsCounter() {
		short flaggedCellsCount = gameInterface.getFlaggedCellsCount();
		if (flaggedCellsCount > 99) {
			return;
		}
		String flaggedCellsCountString = (flaggedCellsCount < 10) ? "0" + flaggedCellsCount : String.valueOf(flaggedCellsCount);
		flagCounter.setText(flaggedCellsCountString);
	}

	private void gameOver() {
		updateIconButton();
		if (gameInterface.getWinCondition()) {
			JOptionPane.showMessageDialog(this, "ГОЙДА!");
			renderCellsFromList((OpenCellInterface.MultipleCells) gameInterface.getFlaggedCellsList());
		} else {
			JOptionPane.showMessageDialog(this, "ЛМАО, ПОСОСИ!");
		}
	}
}
