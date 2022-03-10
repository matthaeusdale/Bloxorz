package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
//import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingWorker;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import java.beans.PropertyChangeEvent;
//import java.beans.PropertyChangeListener;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;

import tiles.*;
import block.*;
import levels.Stage;

public class Panel extends JPanel {

	final int TILE_HEIGHT = 35, TILE_WIDTH = 40, Y_OFFSET = 140, X_OFFSET = -40, SLAB_HEIGHT = 15;
	int blockOffset = 0, stageOffset = 0;
	Stage currentStage, nextStage;

	boolean finished = false;

	Frame frame;

	// Run game;
	// boolean testing = false;
	// int shortest = Integer.MAX_VALUE;
	// long startTime;
	// String shortestPath;
	// Color random;
	// boolean calculated = false;
	// int pathCount = 0;
	// String[] steps;
	// boolean blocked = false;

	public Panel(Frame frame) {
		// nextStage();
		currentStage = new Stage();
		loadStage(currentStage.getHoleX(), currentStage.getHoleY());
		this.frame = frame;
		setVisible(true);
	}

	private void animateStage() {
		SwingWorker<String, Void> animationWorker = new SwingWorker<>() {

			@Override
			public String doInBackground() {
				finished = false;
				for (int i = 0; i < 150; i++) {
					try {
						int diff = i / 10;
						blockOffset += diff;
						stageOffset += diff;
						// System.out.println(diff);
						Thread.sleep(5);
						// System.out.println(animationOffset);
						repaint();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println(stageOffset);
				nextStage();
				stageOffset = -1050;
				for (int i = 0; i < 105; i++) {
					try {
						blockOffset -= 10;
						stageOffset += 10;
						Thread.sleep(5);
						repaint();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				finished = true;

				return null;
			}
		};

		animationWorker.execute();
	}

	private void animateDeath() {
		SwingWorker<String, Void> animationWorker = new SwingWorker<>() {

			@Override
			public String doInBackground() {
				currentStage.respawn();
				blockOffset = -2000;
				for (int i = 0; i < 160; i++) {
					try {
						blockOffset += (5 + i / 10);
						Thread.sleep(5);
						repaint();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// System.out.println(blockOffset);

				finished = true;

				return null;
			}
		};

		animationWorker.execute();
	}

	private void loadStage(int x, int y) {
		SwingWorker<String, Void> stageWorker = new SwingWorker<>() {
			@Override
			public String doInBackground() {
				System.out.println("Creating new stage...");
				nextStage = new Stage(x, y);
				System.out.println("New stage ready!");
				return null;
			}
		};
		stageWorker.execute();
	}

	private void nextStage() {
		currentStage = nextStage;
		loadStage(currentStage.getHoleX(), currentStage.getHoleY());

//		try {
//			animationWorker.get();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		// calculated = false;

//		SwingWorker<Boolean, Void> pathWorker = new SwingWorker<>() {
//			@Override
//			public Boolean doInBackground() {
//				calculated = false;
//				System.out.println("Calculating shortest path...");
//
//				fastestPath();
//
//				steps = (shortestPath + " ").split(",");
//				printArray(steps);
//				calculated = true;
//				pathCount = 0;
//	queue = new LinkedList<>();
//	
//	for (String instr : instrs) {
//		queue.add(instr);
//		queue.
//	}

		// System.out.println(shortest + ": " + shortestPath);
//				return true;
//			}
//		};

//		while (!stageWorker.isDone()) {
//			stageWorker.addPropertyChangeListener(new PropertyChangeListener() {
//
//				@Override
//				public void propertyChange(PropertyChangeEvent evt) {
//					pathWorker.execute();
//				}
//
//			});
//		}
	}

//	private boolean purge() {
//		boolean purged = false;
//		for (int i = 0; i < SIZE; i++) {
//			for (int j = 0; j < SIZE; j++) {
//				if (!(nextStage[i][j] instanceof Empty)) {
//					int up = (nextStage[i][j - 1] instanceof Empty) ? 1 : 0;
//					int down = (nextStage[i][j + 1] instanceof Empty) ? 1 : 0;
//					int right = (nextStage[i + 1][j] instanceof Empty) ? 1 : 0;
//					int left = (nextStage[i - 1][j] instanceof Empty) ? 1 : 0;
//					int upL = (nextStage[i - 1][j - 1] instanceof Empty) ? 1 : 0;
//					int downL = (nextStage[i - 1][j + 1] instanceof Empty) ? 1 : 0;
//					int upR = (nextStage[i + 1][j - 1] instanceof Empty) ? 1 : 0;
//					int downR = (nextStage[i - 1][j + 1] instanceof Empty) ? 1 : 0;
//					if (up + down + left + right + upL + upR + downL + downR >= 6 || up + down + left + right >= 3) {
//						nextStage[i][j] = new Empty();
//						purged = true;
//					}
//				}
//			}
//		}
//		return purged;
//	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.BLACK);
//		Random rand = new Random();
//		float r = rand.nextFloat();
//		float gr = rand.nextFloat();
//		float b = rand.nextFloat();
//		random = new Color(r, gr, b);
		g2.fillRect(0, 0, frame.getWidth(), frame.getHeight());
		printBoard(g2);
		// printBlock(g2);

	}

//	private int fastestPath() {
//		shortest = Integer.MAX_VALUE;
//		fastestPath(new Block(block), new Block[1], "", 0);
//		if (shortest < Integer.MAX_VALUE) {
//
//			return shortest;
//		}
//		return -1;
//	}
//
//	private boolean fastestPath(Block oldPlayer, Block[] prevStates, String path, int length) {
//		if (length > shortest || length > solvedLength) {
//			return false;
//		}
//		Block player = new Block(oldPlayer);
//
//		if (contains(prevStates, player)) {
//			return false;
//		}
//
//		Block[] states = new Block[prevStates.length + 1];
//		copyArray(prevStates, states);
//		states[states.length - 1] = player;
//
//		int status = checkStatus(player, tiles);
//		if (status == 1) {
//			shortest = length;
//			shortestPath = path;
//			return false;
//		} else if (status == 0) {
//			return (fastestPath(new Block(player).moveBlock("Up"), states, "" + path + "Up,", length + 1)
//					|| fastestPath(new Block(player).moveBlock("Down"), states, "" + path + "Down,", length + 1)
//					|| fastestPath(new Block(player).moveBlock("Left"), states, "" + path + "Left,", length + 1)
//					|| fastestPath(new Block(player).moveBlock("Right"), states, "" + path + "Right,", length + 1));
//		} else if (status == -1) {
//			return false;
//		}
//		return false;
//	}

//	private void printArray(String[] strArr) {
//		for (String b : strArr) {
//			System.out.print(b + ", ");
//		}
//		System.out.println();
//	}

	private void printBlock(Graphics2D g2) {
		int x = currentStage.getBlock().getX();
		int y = currentStage.getBlock().getY();
		int startingX = (int) ((Math.cos(Math.PI / 18) * TILE_WIDTH * x) + (Math.sin(Math.PI / 6) * TILE_HEIGHT * y))
				+ X_OFFSET;
		int startingY = (int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT * y) - (Math.sin(Math.PI / 18) * TILE_WIDTH * x))
				+ Y_OFFSET + blockOffset / 2;
		String position = currentStage.getBlock().getPosition();
		g2.setColor(Color.cyan);
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

	private void printBlockAux(Graphics2D g2, int startingX, int startingY, int position) {
		if (position == 1) { // up
			int[] xPoints = { 0, 0, (int) (Math.cos(Math.PI / 18) * TILE_WIDTH),
					(int) ((Math.cos(Math.PI / 18) * TILE_WIDTH) + (Math.sin(Math.PI / 6) * TILE_HEIGHT)),
					(int) ((Math.cos(Math.PI / 18) * TILE_WIDTH) + (Math.sin(Math.PI / 6) * TILE_HEIGHT)),
					(int) (Math.sin(Math.PI / 6) * TILE_HEIGHT) };
			int[] yPoints = { 0, -2 * TILE_HEIGHT, (int) -((Math.sin(Math.PI / 18) * TILE_WIDTH) + 1) - 2 * TILE_HEIGHT,
					(int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT) - (Math.sin(Math.PI / 18) * TILE_WIDTH))
							- 2 * TILE_HEIGHT,
					(int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT) - (Math.sin(Math.PI / 18) * TILE_WIDTH)),
					(int) (Math.cos(Math.PI / 6) * TILE_HEIGHT) };
			for (int m = 0; m < 6; m++) {
				xPoints[m] = xPoints[m] + startingX;
				yPoints[m] = yPoints[m] + startingY;
			}
			g2.fillPolygon(xPoints, yPoints, 6);
			g2.setColor(Color.BLACK);
			g2.drawLine(0 + startingX, -2 * TILE_HEIGHT + startingY,
					(int) (Math.sin(Math.PI / 6) * TILE_HEIGHT) + startingX,
					(int) (Math.cos(Math.PI / 6) * TILE_HEIGHT) - 2 * TILE_HEIGHT + startingY);
			g2.drawLine(
					(int) ((Math.cos(Math.PI / 18) * TILE_WIDTH) + (Math.sin(Math.PI / 6) * TILE_HEIGHT)) + startingX,
					(int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT) - (Math.sin(Math.PI / 18) * TILE_WIDTH))
							- 2 * TILE_HEIGHT + startingY,
					(int) (Math.sin(Math.PI / 6) * TILE_HEIGHT) + startingX,
					(int) (Math.cos(Math.PI / 6) * TILE_HEIGHT) - 2 * TILE_HEIGHT + startingY);
			g2.drawLine((int) (Math.sin(Math.PI / 6) * TILE_HEIGHT) + startingX,
					(int) (Math.cos(Math.PI / 6) * TILE_HEIGHT) + startingY,
					(int) (Math.sin(Math.PI / 6) * TILE_HEIGHT) + startingX,
					(int) (Math.cos(Math.PI / 6) * TILE_HEIGHT) - 2 * TILE_HEIGHT + startingY);
			g2.drawPolygon(xPoints, yPoints, 6);
		} else if (position == 2) { // horizontal
			int[] xPoints = { 0, 0, (int) (Math.cos(Math.PI / 18) * TILE_WIDTH) * 2,
					(int) ((Math.cos(Math.PI / 18) * TILE_WIDTH) * 2 + (Math.sin(Math.PI / 6) * TILE_HEIGHT)),
					(int) ((Math.cos(Math.PI / 18) * TILE_WIDTH) * 2 + (Math.sin(Math.PI / 6) * TILE_HEIGHT)),
					(int) (Math.sin(Math.PI / 6) * TILE_HEIGHT) };
			int[] yPoints = { 0, -1 * TILE_HEIGHT,
					(int) -((Math.sin(Math.PI / 18) * TILE_WIDTH) * 2 + 1) - 1 * TILE_HEIGHT,
					(int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT) - (Math.sin(Math.PI / 18) * TILE_WIDTH) * 2)
							- 1 * TILE_HEIGHT,
					(int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT) - (Math.sin(Math.PI / 18) * TILE_WIDTH) * 2),
					(int) (Math.cos(Math.PI / 6) * TILE_HEIGHT) };
			for (int m = 0; m < 6; m++) {
				xPoints[m] = xPoints[m] + startingX;
				yPoints[m] = yPoints[m] + startingY;
			}
			g2.fillPolygon(xPoints, yPoints, 6);
			g2.setColor(Color.BLACK);
			g2.drawLine(startingX, startingY + -1 * TILE_HEIGHT,
					(int) (Math.sin(Math.PI / 6) * TILE_HEIGHT) + startingX,
					(int) (Math.cos(Math.PI / 6) * TILE_HEIGHT) - TILE_HEIGHT + startingY);
			g2.drawLine(
					startingX
							+ (int) ((Math.cos(Math.PI / 18) * TILE_WIDTH) * 2 + (Math.sin(Math.PI / 6) * TILE_HEIGHT)),
					startingY
							+ (int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT) - (Math.sin(Math.PI / 18) * TILE_WIDTH) * 2)
							- 1 * TILE_HEIGHT,
					(int) (Math.sin(Math.PI / 6) * TILE_HEIGHT) + startingX,
					(int) (Math.cos(Math.PI / 6) * TILE_HEIGHT) - TILE_HEIGHT + startingY);
			g2.drawLine(startingX + (int) (Math.sin(Math.PI / 6) * TILE_HEIGHT),
					startingY + (int) (Math.cos(Math.PI / 6) * TILE_HEIGHT),
					(int) (Math.sin(Math.PI / 6) * TILE_HEIGHT) + startingX,
					(int) (Math.cos(Math.PI / 6) * TILE_HEIGHT) - TILE_HEIGHT + startingY);
			g2.drawPolygon(xPoints, yPoints, 6);
		} else { // vertical
			int[] xPoints = { 0, 0, (int) (Math.cos(Math.PI / 18) * TILE_WIDTH),
					(int) ((Math.cos(Math.PI / 18) * TILE_WIDTH) + (Math.sin(Math.PI / 6) * TILE_HEIGHT) * 2),
					(int) ((Math.cos(Math.PI / 18) * TILE_WIDTH) + (Math.sin(Math.PI / 6) * TILE_HEIGHT) * 2),
					(int) (Math.sin(Math.PI / 6) * TILE_HEIGHT) * 2 };
			int[] yPoints = { 0, -1 * TILE_HEIGHT, (int) -((Math.sin(Math.PI / 18) * TILE_WIDTH) + 1) - 1 * TILE_HEIGHT,
					(int) (((Math.cos(Math.PI / 6) * TILE_HEIGHT) * 2) - (Math.sin(Math.PI / 18) * TILE_WIDTH)
							- 1 * TILE_HEIGHT),
					(int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT) * 2 - (Math.sin(Math.PI / 18) * TILE_WIDTH)),
					(int) (Math.cos(Math.PI / 6) * TILE_HEIGHT) * 2 };
			for (int m = 0; m < 6; m++) {
				xPoints[m] = xPoints[m] + startingX;
				yPoints[m] = yPoints[m] + startingY;
			}
			g2.fillPolygon(xPoints, yPoints, 6);
			g2.setColor(Color.BLACK);
			g2.drawLine(startingX, startingY - TILE_HEIGHT, startingX + (int) (Math.sin(Math.PI / 6) * TILE_HEIGHT) * 2,
					startingY + (int) (Math.cos(Math.PI / 6) * TILE_HEIGHT) * 2 - TILE_HEIGHT);
			g2.drawLine(
					startingX
							+ (int) ((Math.cos(Math.PI / 18) * TILE_WIDTH) + (Math.sin(Math.PI / 6) * TILE_HEIGHT) * 2),
					startingY + (int) (((Math.cos(Math.PI / 6) * TILE_HEIGHT) * 2)
							- (Math.sin(Math.PI / 18) * TILE_WIDTH) - 1 * TILE_HEIGHT),
					startingX + (int) (Math.sin(Math.PI / 6) * TILE_HEIGHT) * 2,
					startingY + (int) (Math.cos(Math.PI / 6) * TILE_HEIGHT) * 2 - TILE_HEIGHT);
			g2.drawLine(startingX + (int) (Math.sin(Math.PI / 6) * TILE_HEIGHT) * 2,
					startingY + (int) (Math.cos(Math.PI / 6) * TILE_HEIGHT) * 2,
					startingX + (int) (Math.sin(Math.PI / 6) * TILE_HEIGHT) * 2,
					startingY + (int) (Math.cos(Math.PI / 6) * TILE_HEIGHT) * 2 - TILE_HEIGHT);
			g2.drawPolygon(xPoints, yPoints, 6);
		}
	}

	public void printBoard(Graphics g2) {
		Tile[][] tiles = currentStage.getTiles();
		int numTiles = currentStage.NUM_TILES;
		Block block = currentStage.getBlock();
		boolean blockPrinted = false;
		int holeX = currentStage.getHoleX(), holeY = currentStage.getHoleY();
		drawTile(holeX, holeY, tiles[holeX][holeY].getColor(), g2);
		for (int i = numTiles - 1; i >= 0; i--) {
			for (int j = 0; j < numTiles; j++) {

				if (!(tiles[i][j] instanceof Empty)) {
					g2.setColor(tiles[i][j].getColor());
					drawTile(i, j, tiles[i][j].getColor(), g2);
				}
				if (block.getX() == i && block.getY() == j && block.getPosition().equals("Up")) {
					blockPrinted = true;
					printBlock((Graphics2D) g2);
				}

//				g2.setColor(tiles[i][j].getColor());
//				g2.fillRect(i * TILE_WIDTH, j * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
//				g2.setColor(Color.black);
//				g2.drawRect(i * TILE_WIDTH, j * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
//
//				drawTile(i, j, tiles[i][j].getColor(), g2);

			}
		}
		if (!blockPrinted) {
			printBlock((Graphics2D) g2);
		}
	}

	private void printBoardToConsole() {
		int numTiles = currentStage.NUM_TILES;
		Tile[][] tiles = currentStage.getTiles();
		for (int i = 0; i < numTiles; i++) {
			for (int j = 0; j < numTiles; j++) {
				System.out.print(tiles[j][i]);
			}
			System.out.println();
		}
	}

	private void drawTile(int x, int y, Color color, Graphics g2) {
		int points = 12;
		g2.setColor(color);
		int startingX = (int) ((Math.cos(Math.PI / 18) * TILE_WIDTH * x) + (Math.sin(Math.PI / 6) * TILE_HEIGHT * y))
				+ X_OFFSET;
		int startingY = (int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT * y) - (Math.sin(Math.PI / 18) * TILE_WIDTH * x))
				+ Y_OFFSET - stageOffset;
		/*
		 * int[] xPoints = { i * TILE_WIDTH, i * TILE_WIDTH, (i + 1) * TILE_WIDTH, (i +
		 * 1) * TILE_WIDTH }; int[] yPoints = { j * (TILE_WIDTH / 2), (j + 1) *
		 * (TILE_WIDTH / 2), (j + 1) * (TILE_WIDTH / 2), j * (TILE_WIDTH / 2) };
		 */
		int[] xPoints = { 0, (int) (Math.cos(Math.PI / 18) * TILE_WIDTH),
				(int) ((Math.cos(Math.PI / 18) * TILE_WIDTH) + (Math.sin(Math.PI / 6) * TILE_HEIGHT)),
				(int) ((Math.cos(Math.PI / 18) * TILE_WIDTH) + (Math.sin(Math.PI / 6) * TILE_HEIGHT)),
				(int) (Math.sin(Math.PI / 6) * TILE_HEIGHT), 0 };
		int[] yPoints = { 0, (int) -((Math.sin(Math.PI / 18) * TILE_WIDTH) + 1),
				(int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT) - (Math.sin(Math.PI / 18) * TILE_WIDTH)),
				(int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT) - (Math.sin(Math.PI / 18) * TILE_WIDTH)) + SLAB_HEIGHT,
				(int) (Math.cos(Math.PI / 6) * TILE_HEIGHT) + SLAB_HEIGHT, 0 + SLAB_HEIGHT };
		int[] xOutline = { 0, (int) (Math.cos(Math.PI / 18) * TILE_WIDTH),
				(int) ((Math.cos(Math.PI / 18) * TILE_WIDTH) + (Math.sin(Math.PI / 6) * TILE_HEIGHT)),
				(int) (Math.sin(Math.PI / 6) * TILE_HEIGHT), 0, 0, (int) (Math.sin(Math.PI / 6) * TILE_HEIGHT),
				(int) (Math.sin(Math.PI / 6) * TILE_HEIGHT), (int) (Math.sin(Math.PI / 6) * TILE_HEIGHT),
				(int) ((Math.cos(Math.PI / 18) * TILE_WIDTH) + (Math.sin(Math.PI / 6) * TILE_HEIGHT)),
				(int) ((Math.cos(Math.PI / 18) * TILE_WIDTH) + (Math.sin(Math.PI / 6) * TILE_HEIGHT)),
				(int) (Math.cos(Math.PI / 18) * TILE_WIDTH), 0 };
		int[] yOutline = { 0, (int) -((Math.sin(Math.PI / 18) * TILE_WIDTH) + 1),
				(int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT) - (Math.sin(Math.PI / 18) * TILE_WIDTH)),
				(int) (Math.cos(Math.PI / 6) * TILE_HEIGHT), 0, SLAB_HEIGHT,
				(int) (Math.cos(Math.PI / 6) * TILE_HEIGHT) + SLAB_HEIGHT, (int) (Math.cos(Math.PI / 6) * TILE_HEIGHT),
				(int) (Math.cos(Math.PI / 6) * TILE_HEIGHT) + SLAB_HEIGHT,
				(int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT) - (Math.sin(Math.PI / 18) * TILE_WIDTH)) + SLAB_HEIGHT,
				(int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT) - (Math.sin(Math.PI / 18) * TILE_WIDTH)),
				(int) -((Math.sin(Math.PI / 18) * TILE_WIDTH) + 1), 0 };
		for (int m = 0; m < points; m++) {
			/*
			 * xPoints[m] = (int) (Math.sin((180 * 45) / (2 * Math.PI)) * xPoints[m] + 400);
			 * yPoints[m] = (int) (Math.sin((180 * 45) / (2 * Math.PI)) * yPoints[m]);
			 */
			xOutline[m] = xOutline[m] + startingX;
			yOutline[m] = yOutline[m] + startingY;
			// System.out.println("x: " + xPoints[m] + "y: " + yPoints[m]);
		}
		for (int m = 0; m < 6; m++) {
			/*
			 * xPoints[m] = (int) (Math.sin((180 * 45) / (2 * Math.PI)) * xPoints[m] + 400);
			 * yPoints[m] = (int) (Math.sin((180 * 45) / (2 * Math.PI)) * yPoints[m]);
			 */
			xPoints[m] = xPoints[m] + startingX;
			yPoints[m] = yPoints[m] + startingY;
			// System.out.println("x: " + xPoints[m] + "y: " + yPoints[m]);
		}
		g2.fillPolygon(xPoints, yPoints, 6);
		g2.setColor(Color.DARK_GRAY);
		g2.drawPolygon(xOutline, yOutline, points);

	}

	public void keyPressed(KeyEvent e) {
//		if (!blocked) {
		Block block = currentStage.getBlock();
		if (e.getKeyCode() == KeyEvent.VK_UP) {
//				if (calculated && steps[pathCount].equals("up")) {
//					pathCount++;
//					System.out.println(steps[pathCount]);
//				} else if (calculated) {
//					calculated = false;
//				}
			block.moveBlock("Up");
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
//				if (calculated && steps[pathCount].equals("right")) {
//					pathCount++;
//					System.out.println(steps[pathCount]);
//				} else if (calculated) {
//					calculated = false;
//				}
			block.moveBlock("Right");
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
//				if (calculated && steps[pathCount].equals("left")) {
//					pathCount++;
//					System.out.println(steps[pathCount]);
//				} else if (calculated) {
//					calculated = false;
//				}
			block.moveBlock("Left");
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
//				if (calculated && steps[pathCount].equals("down")) {
//					pathCount++;
//					System.out.println(steps[pathCount]);
//				} else if (calculated) {
//					calculated = false;
//				}
			block.moveBlock("Down");
		}
//			if (e.getKeyCode() == KeyEvent.VK_1) {
//				setBoard(1);
//			} else if (e.getKeyCode() == KeyEvent.VK_2) {
//				setBoard(2);
//			} else if (e.getKeyCode() == KeyEvent.VK_3) {
//				setBoard(3);
//			} else 
		if (e.getKeyCode() == KeyEvent.VK_P) {
			printBoardToConsole();
		} else if (e.getKeyCode() == KeyEvent.VK_R) {
			nextStage();
//		} else if (e.getKeyCode() == KeyEvent.VK_T) {
//			testing = !testing;
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
//				if (calculated) {
//					SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
//						@Override
//						public Boolean doInBackground() {
//							blocked = true;
//							for (int i = 0; i < shortest; i++) {
//
//								try {
//									block.moveBlock(steps[i]);
//									Thread.sleep(200);
//									repaint();
//								} catch (InterruptedException e) {
//									e.printStackTrace();
//								}
//								blocked = false;
//								calculated = false;
//								checkStatus();
//							}
//							return true;
//						}
//					};
//					worker.execute();
		} else if (e.getKeyCode() == KeyEvent.VK_1) {
			currentStage = new Stage(1);
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}

		repaint();
		int status = currentStage.checkStatus();
		if (status == 1) {
			animateStage();
//			while (!finished) {
//				System.out.println("finished");
//				try {
//					Thread.sleep(10);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
			// System.out.println("next");

		} else if (status == -1) {
			animateDeath();
		}
	}

}
