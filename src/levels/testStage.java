package levels;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFrame;

import main.Run;
import tiles.*;
import block.*;

public class testStage extends Stage implements MouseListener, MouseMotionListener, ActionListener {

	Run game;
	final int TILE_WIDTH = 30;
	int spawnX = 10;
	int spawnY = 10;
	boolean testing = false;

	public testStage(Run game) {
		initStage();
		addMouseListener(this);
		addMouseMotionListener(this);
		// addKeyListener(this);
		setBackground(Color.black);
		setVisible(true);
		this.game = game;
		game.getFrame().addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					block.moveBlock("Up");
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					block.moveBlock("Right");
				} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					block.moveBlock("Left");
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					block.moveBlock("Down");
				} else if (e.getKeyCode() == KeyEvent.VK_1) {
					try {
						setBoard("stages\\level1.txt");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else if (e.getKeyCode() == KeyEvent.VK_2) {
					try {
						setBoard("stages\\level2.txt");
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				} else if (e.getKeyCode() == KeyEvent.VK_3) {
					try {
						setBoard("stages\\level3.txt");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else if (e.getKeyCode() == KeyEvent.VK_P) {
					printBoardToConsole();
				} else if (e.getKeyCode() == KeyEvent.VK_R) {
					initStage();
				} else if (e.getKeyCode() == KeyEvent.VK_T) {
					testing = !testing;
				}
				checkStatus();
				repaint();
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		});
	}

	@Override
	public void initStage() {
		boolean madeHole = false;
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				int shit = (int) (Math.random() * 4);

				switch (shit) {
				case 0:
					tiles[i][j] = new Empty();
					break;
				case 1:
					tiles[i][j] = new Solid();
					break;
				case 2:
					tiles[i][j] = new Orange();
					break;
				case 3:
					if (!madeHole) {
						tiles[i][j] = new Hole();
						madeHole = true;
					} else
						tiles[i][j] = new Solid();
					break;
				default:
					tiles[i][j] = new Solid();
				}
			}
		}
		tiles[10][10] = new Solid();
		this.block = new Block("Up", 10, 10);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.gray);
		printBoard(g2);
		printBlock(g2);
	}

	public void checkStatus() {
		int x = this.block.getX(), y = this.block.getY();
		String pos = this.block.getPosition();
		Tile t = tiles[x][y];
		if (pos.equals("Up") && t instanceof Hole) {
			System.out.println("won");
			if (!testing) {
				respawn();
				initStage();
			}
		} else if (pos.equals("Up") && t instanceof Orange || t instanceof Empty) {
			System.out.println("lose up");
			if (!testing) {
				respawn();
			}
		} else if (!pos.equals("Up")) {
			Tile t2 = pos.equals("Vertical") ? tiles[x][y + 1] : tiles[x + 1][y];
			if (t instanceof Empty || t2 instanceof Empty) {
				System.out.println("lose space");
				if (!testing) {
					respawn();
				}
			}
		}
	}

	private void respawn() {
		this.block = new Block("Up", spawnX, spawnY);
	}

	public void printBlock(Graphics2D g2) {
		int x = this.block.getX();
		int y = this.block.getY();
		int startingX = (int) ((Math.cos(Math.PI / 18) * TILE_WIDTH * x) + (Math.sin(Math.PI / 6) *
				TILE_HEIGHT * y));
		int startingY = (int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT * y) - (Math.sin(Math.PI / 18) *
				TILE_WIDTH * x)) + 60;
		String position = this.block.getPosition();
		g2.setColor(Color.BLUE);
		if (position.equals("Up")) {
			printBlockAux(g2, startingX, startingY, 1);
			// g2.fill3DRect(TILE_WIDTH * x, TILE_WIDTH * y, TILE_WIDTH, TILE_WIDTH, true);
		} else if (position.equals("Horizontal")) {
			// g2.fill3DRect(TILE_WIDTH * x, TILE_WIDTH * y, TILE_WIDTH * 2, TILE_WIDTH,
			// true);
			printBlockAux(g2, startingX, startingY, 2);
		} else {
			// g2.fill3DRect(TILE_WIDTH * x, TILE_WIDTH * y, TILE_WIDTH, TILE_WIDTH * 2,
			// true);
			printBlockAux(g2, startingX, startingY, 3);
		}
	}

	public void printBlockAux(Graphics2D g2, int startingX, int startingY, int position) {
		if (position == 1) { // up
			int[] xPoints = { 0, 0,
					(int) (Math.cos(Math.PI / 18) * TILE_WIDTH),
					(int) ((Math.cos(Math.PI / 18) * TILE_WIDTH) + (Math.sin(Math.PI / 6) * TILE_HEIGHT)),
					(int) ((Math.cos(Math.PI / 18) * TILE_WIDTH) + (Math.sin(Math.PI / 6) * TILE_HEIGHT)),
					(int) (Math.sin(Math.PI / 6) * TILE_HEIGHT) };
			int[] yPoints = { 0, -2 * TILE_HEIGHT,
					(int) -((Math.sin(Math.PI / 18) * TILE_WIDTH) + 1) - 2 * TILE_HEIGHT,
					(int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT) - (Math.sin(Math.PI / 18) * TILE_WIDTH))
							- 2 * TILE_HEIGHT,
					(int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT) - (Math.sin(Math.PI / 18) * TILE_WIDTH)),
					(int) (Math.cos(Math.PI / 6) * TILE_HEIGHT) };
			for (int m = 0; m < 6; m++) {
				xPoints[m] = xPoints[m] + startingX;
				yPoints[m] = yPoints[m] + startingY;
			}
			g2.fillPolygon(xPoints, yPoints, 6);
		} else if (position == 2) { // horizontal
			int[] xPoints = { 0, 0,
					(int) (Math.cos(Math.PI / 18) * TILE_WIDTH) * 2,
					(int) ((Math.cos(Math.PI / 18) * TILE_WIDTH) * 2 + (Math.sin(Math.PI / 6) * TILE_HEIGHT)),
					(int) ((Math.cos(Math.PI / 18) * TILE_WIDTH) * 2 + (Math.sin(Math.PI / 6) * TILE_HEIGHT)),
					(int) (Math.sin(Math.PI / 6) * TILE_HEIGHT) };
			int[] yPoints = { 0, -1 * TILE_HEIGHT,
					(int) -((Math.sin(Math.PI / 18) * TILE_WIDTH) * 2 + 1) - 1 * TILE_HEIGHT,
					(int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT) - (Math.sin(Math.PI / 18) * TILE_WIDTH))
							- 1 * TILE_HEIGHT,
					(int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT) - (Math.sin(Math.PI / 18) * TILE_WIDTH) * 2),
					(int) (Math.cos(Math.PI / 6) * TILE_HEIGHT) };
			for (int m = 0; m < 6; m++) {
				xPoints[m] = xPoints[m] + startingX;
				yPoints[m] = yPoints[m] + startingY;
			}
			g2.fillPolygon(xPoints, yPoints, 6);
		} else { // vertical
			int[] xPoints = { 0, 0,
					(int) (Math.cos(Math.PI / 18) * TILE_WIDTH),
					(int) ((Math.cos(Math.PI / 18) * TILE_WIDTH) + (Math.sin(Math.PI / 6) * TILE_HEIGHT) * 2),
					(int) ((Math.cos(Math.PI / 18) * TILE_WIDTH) + (Math.sin(Math.PI / 6) * TILE_HEIGHT) * 2),
					(int) (Math.sin(Math.PI / 6) * TILE_HEIGHT) * 2 };
			int[] yPoints = { 0, -1 * TILE_HEIGHT,
					(int) -((Math.sin(Math.PI / 18) * TILE_WIDTH) + 1) - 1 * TILE_HEIGHT,
					(int) (((Math.cos(Math.PI / 6) * TILE_HEIGHT) * 2) - (Math.sin(Math.PI / 18) * TILE_WIDTH)
							- 1 * TILE_HEIGHT),
					(int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT) * 2 - (Math.sin(Math.PI / 18) * TILE_WIDTH)),
					(int) (Math.cos(Math.PI / 6) * TILE_HEIGHT) * 2 };
			for (int m = 0; m < 6; m++) {
				xPoints[m] = xPoints[m] + startingX;
				yPoints[m] = yPoints[m] + startingY;
			}
			g2.fillPolygon(xPoints, yPoints, 6);
		}
	}


	public void printBoard(Graphics g2) {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				g2.setColor(tiles[i][j].getColor());
				drawTile(i, j, tiles[i][j].getColor(), g2);
				// g2.fillRect(i * TILE_WIDTH, j * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
				// g2.setColor(Color.black);
				// g2.drawRect(i * TILE_WIDTH, j * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
			}
		}
	}

	private void drawTile(int x, int y, Color color, Graphics g2) {
		g2.setColor(color);
		int startingX = (int) ((Math.cos(Math.PI / 18) * TILE_WIDTH * x) + (Math.sin(Math.PI / 6) *
				TILE_HEIGHT * y));
		int startingY = (int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT * y) - (Math.sin(Math.PI / 18) *
				TILE_WIDTH * x)) + 60;
		/*
		 * int[] xPoints = { i * TILE_WIDTH, i * TILE_WIDTH, (i + 1) * TILE_WIDTH, (i +
		 * 1) * TILE_WIDTH };
		 * int[] yPoints = { j * (TILE_WIDTH / 2), (j + 1) * (TILE_WIDTH / 2), (j + 1) *
		 * (TILE_WIDTH / 2),
		 * j * (TILE_WIDTH / 2) };
		 */
		int[] xPoints = { 0,
				(int) (Math.cos(Math.PI / 18) * TILE_WIDTH),
				(int) ((Math.cos(Math.PI / 18) * TILE_WIDTH) + (Math.sin(Math.PI / 6) * TILE_HEIGHT)),
				(int) (Math.sin(Math.PI / 6) * TILE_HEIGHT) };
		int[] yPoints = { 0,
				(int) -((Math.sin(Math.PI / 18) * TILE_WIDTH) + 1),
				(int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT) - (Math.sin(Math.PI / 18) * TILE_WIDTH)),
				(int) (Math.cos(Math.PI / 6) * TILE_HEIGHT) };
		for (int m = 0; m < 4; m++) {
			/*
			 * xPoints[m] = (int) (Math.sin((180 * 45) / (2 * Math.PI)) * xPoints[m] + 400);
			 * yPoints[m] = (int) (Math.sin((180 * 45) / (2 * Math.PI)) * yPoints[m]);
			 */
			xPoints[m] = xPoints[m] + startingX;
			yPoints[m] = yPoints[m] + startingY;
			// System.out.println("x: " + xPoints[m] + "y: " + yPoints[m]);
		}
		g2.fillPolygon(xPoints, yPoints, 4);
		g2.setColor(Color.black);
		g2.drawPolygon(xPoints, yPoints, 4);

	}

	public void setBoard(String fileName) throws IOException {

//		try {
//			inputStream = new FileReader(fileName);
//
//			int c, count = 0, lines = 0;
//			while ((c = inputStream.read()) != -1) {
//				switch ((char) c) {
//				case 'E':
//					tiles[lines][count] = new Empty();
//					break;
//				case 'H':
//					tiles[lines][count] = new Hole();
//					break;
//				case 'O':
//					tiles[lines][count] = new Orange();
//					break;
//				case 'S':
//					tiles[lines][count] = new Solid();
//					break;
//				case 'B':
//					this.block = new Block("Up", count, lines);
//					break;
//				case 10:
//					count = 0;
//					lines++;
//					break;
//				default:
//					System.out.println("Invalid character: " + c);
//				}
//			}
//			printBoardToConsole();
//		} finally {
//			if (inputStream != null) {
//				inputStream.close();
//			}
//		}
		try {
			List<String> lines = Files.readAllLines(Paths.get(fileName));

			for (int i = 0; i < lines.size(); i++) {
				char[] row = lines.get(i).toCharArray();
				for (int j = 0; j < row.length; j++) {
					char block = row[j];
					if (block == 'E') {
						tiles[j][i] = new Empty();
					} else if (block == 'H') {
						tiles[j][i] = new Hole();
					} else if (block == 'O') {
						tiles[j][i] = new Orange();
					} else if (block == 'S') {
						tiles[j][i] = new Solid();
					} else if (block == 'B') {
						this.block = new Block("Up", i, j);
						tiles[j][i] = new Solid();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printBoardToConsole() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				System.out.print(tiles[j][i]);
			}
			System.out.println();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
