package levels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import javax.swing.SwingWorker;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import main.Run;
import tiles.*;
import block.*;

public class randomStage extends Stage {

	final int TILE_HEIGHT = 35, TILE_WIDTH = 40, Y_OFFSET = 140, X_OFFSET = -40, SLAB_HEIGHT = 15;
	int border = 4;
	int max_path_length = 60;
	int spawnX, spawnY, holeX, holeY;
	Run game;
	boolean testing = false;
	int shortest = Integer.MAX_VALUE;
	long startTime;
	String shortestPath;
	Color random;
	boolean calculated = false;
	int pathCount = 0;
	String[] steps;
	boolean blocked = false;
	int solvedLength = 0;

	public randomStage(Run game) {
		initStage();
		nextStage();
		setVisible(true);
		this.game = game;
		game.getFrame().addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (!blocked) {
					if (e.getKeyCode() == KeyEvent.VK_UP) {
						if (calculated && steps[pathCount].equals("up")) {
							pathCount++;
							System.out.println(steps[pathCount]);
						} else if (calculated) {
							calculated = false;
						}
						block.moveBlock("Up");
					} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						if (calculated && steps[pathCount].equals("right")) {
							pathCount++;
							System.out.println(steps[pathCount]);
						} else if (calculated) {
							calculated = false;
						}
						block.moveBlock("Right");
					} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						if (calculated && steps[pathCount].equals("left")) {
							pathCount++;
							System.out.println(steps[pathCount]);
						} else if (calculated) {
							calculated = false;
						}
						block.moveBlock("Left");
					} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						if (calculated && steps[pathCount].equals("down")) {
							pathCount++;
							System.out.println(steps[pathCount]);
						} else if (calculated) {
							calculated = false;
						}
						block.moveBlock("Down");
					}
					if (e.getKeyCode() == KeyEvent.VK_1) {
						setBoard(1);
					} else if (e.getKeyCode() == KeyEvent.VK_2) {
						setBoard(2);
					} else if (e.getKeyCode() == KeyEvent.VK_3) {
						setBoard(3);
					} else if (e.getKeyCode() == KeyEvent.VK_P) {
						printBoardToConsole();
					} else if (e.getKeyCode() == KeyEvent.VK_R) {
						nextStage();
					} else if (e.getKeyCode() == KeyEvent.VK_T) {
						testing = !testing;
					} else if (e.getKeyCode() == KeyEvent.VK_S) {
						if (calculated) {
							SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
								@Override
								public Boolean doInBackground() {
									blocked = true;
									for (int i = 0; i < shortest; i++) {

										try {
											block.moveBlock(steps[i]);
											Thread.sleep(200);
											repaint();
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										blocked = false;
										calculated = false;
										checkStatus();
									}
									return true;
								}
							};
							worker.execute();
						}

					}
					repaint();
					checkStatus();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});

	}

	private void nextStage() {
		tiles = nextStage;
		calculated = false;
		nextStage = new Tile[SIZE][SIZE];
		SwingWorker<Boolean, Void> stageWorker = new SwingWorker<>() {
			@Override
			public Boolean doInBackground() {

				System.out.println("Creating new stage...");
				initStage();
				System.out.println("New stage ready!");
				return true;
			}
		};
		SwingWorker<Boolean, Void> pathWorker = new SwingWorker<>() {
			@Override
			public Boolean doInBackground() {
				calculated = false;
				System.out.println("Calculating shortest path...");

				fastestPath();

				steps = (shortestPath + " ").split(",");
				printArray(steps);
				calculated = true;
				pathCount = 0;
//	queue = new LinkedList<>();
//	
//	for (String instr : instrs) {
//		queue.add(instr);
//		queue.
//	}

				// System.out.println(shortest + ": " + shortestPath);
				return true;
			}
		};

		stageWorker.execute();
		while (!stageWorker.isDone()) {
			stageWorker.addPropertyChangeListener(new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					pathWorker.execute();
				}

			});
		}
	}

	public void initStage() {
		// System.out.println("init");
		boolean madeHole = false;
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (i < SIZE - border && i >= border && j < SIZE - border && j >= border) {
					int shit = (int) (Math.random() * 4);

					switch (shit) {
					case 0:
						nextStage[i][j] = new Empty();
						break;
					case 1:
						nextStage[i][j] = new Solid();
						break;
					case 2:
						nextStage[i][j] = new Orange();
						break;
					case 3:
						if (!madeHole && i > 2 && j > 2) {
							nextStage[i][j] = new Hole();
							holeX = i;
							holeY = j;
							madeHole = true;
						} else
							nextStage[i][j] = new Solid();
						break;
					default:
						nextStage[i][j] = new Solid();
					}
				} else {
					nextStage[i][j] = new Empty();
				}
			}
		}

		// nextStage[10][10] = new Solid();
		padHole(2);

		spawnX = 20;
		spawnY = 20;
		while (purge())
			;

		if (!solvable()) {
			// System.out.println("unsolvable");
			initStage();
		} else {
			this.block = new Block("Up", 20, 20);
		}

	}

	private boolean purge() {
		boolean purged = false;
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (!(nextStage[i][j] instanceof Empty)) {
					int up = (nextStage[i][j - 1] instanceof Empty) ? 1 : 0;
					int down = (nextStage[i][j + 1] instanceof Empty) ? 1 : 0;
					int right = (nextStage[i + 1][j] instanceof Empty) ? 1 : 0;
					int left = (nextStage[i - 1][j] instanceof Empty) ? 1 : 0;
					if (up + down + left + right >= 3) {
						nextStage[i][j] = new Empty();
						purged = true;
					}
				}
			}
		}
		return purged;
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

	private boolean padHole(int num) {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (nextStage[i][j] instanceof Hole) {
					for (int k = -1; k < 2; k++) {
						for (int l = -1; l < 2; l++) {
							nextStage[k + i][l + j] = new Solid();
						}
					}
					nextStage[i][j] = new Hole();
					return true;
				}
			}
		}
		return false;
	}

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
		g2.fillRect(0, 0, game.WIDTH, game.HEIGHT);
		printBoard(g2);
		printBlock(g2);

	}

	private boolean solvable() {
//		startTime = System.currentTimeMillis();
//		boolean solvable = solvable(new Block(block), new Block[1], 0);
//		long end = System.currentTimeMillis();
//		System.out.println(end - startTime);
//		if (end - startTime > 4000) {
//			return true;
//		}
		return solvable(new Block("Up", 20, 20), new Block[1], 0);
	}

	private boolean solvable(Block oldPlayer, Block[] prevStates, int length) {
		// || (System.currentTimeMillis() - startTime) > 1000
		if (length > max_path_length) {
			// System.out.println(">40");
			return false;
		}

		Block player = new Block(oldPlayer);

		if (contains(prevStates, player)) {
			return false;
		}

		Block[] states = new Block[prevStates.length + 1];
		copyArray(prevStates, states);
		states[states.length - 1] = player;

		int status = checkStatus(player, nextStage);
		if (status == 1) {
			solvedLength = length;
			return true;
		} else if (status == 0) {
			return (solvable(new Block(player).moveBlock("Up"), states, length + 1)
					|| solvable(new Block(player).moveBlock("Down"), states, length + 1)
					|| solvable(new Block(player).moveBlock("Left"), states, length + 1)
					|| solvable(new Block(player).moveBlock("Right"), states, length + 1));
		} else if (status == -1) {
			return false;
		}
		return false;
	}

	private int fastestPath() {
		shortest = Integer.MAX_VALUE;
		fastestPath(new Block(block), new Block[1], "", 0);
		if (shortest < Integer.MAX_VALUE) {

			return shortest;
		}
		return -1;
	}

	private boolean fastestPath(Block oldPlayer, Block[] prevStates, String path, int length) {
		if (length > shortest || length > solvedLength) {
			return false;
		}
		Block player = new Block(oldPlayer);

		if (contains(prevStates, player)) {
			return false;
		}

		Block[] states = new Block[prevStates.length + 1];
		copyArray(prevStates, states);
		states[states.length - 1] = player;

		int status = checkStatus(player, tiles);
		if (status == 1) {
			shortest = length;
			shortestPath = path;
			return false;
		} else if (status == 0) {
			return (fastestPath(new Block(player).moveBlock("Up"), states, "" + path + "Up,", length + 1)
					|| fastestPath(new Block(player).moveBlock("Down"), states, "" + path + "Down,", length + 1)
					|| fastestPath(new Block(player).moveBlock("Left"), states, "" + path + "Left,", length + 1)
					|| fastestPath(new Block(player).moveBlock("Right"), states, "" + path + "Right,", length + 1));
		} else if (status == -1) {
			return false;
		}
		return false;
	}

	private void printArray(String[] strArr) {
		for (String b : strArr) {
			System.out.print(b + ", ");
		}
		System.out.println();
	}

	private void copyArray(Block[] arr1, Block[] arr2) {
		for (int i = 0; i < arr1.length; i++) {
			if (arr1[i] != null) {
				arr2[i] = new Block(arr1[i]);
			}
		}
	}

	public boolean contains(Block[] states, Block player) {
		for (int i = 0; i < states.length - 1; i++) {
			if (states[i] != null && states[i].equals(player))
				return true;
		}
		return false;
	}

	public int checkStatus() {
		int x = this.block.getX(), y = this.block.getY();
		String pos = this.block.getPosition();
		Tile t = tiles[x][y];
		if (pos.equals("Up") && t instanceof Hole) {
			// System.out.println("won");
			if (!testing) {
				respawn();
				nextStage();
			}
			return 1;
		} else if (pos.equals("Up") && t instanceof Orange || t instanceof Empty) {
			// System.out.println("lose up");
			if (!testing) {
				respawn();
			}
			return -1;
		} else if (!pos.equals("Up")) {
			Tile t2 = pos.equals("Vertical") ? tiles[x][y + 1] : tiles[x + 1][y];
			if (t instanceof Empty || t2 instanceof Empty) {
				// System.out.println("lose space");
				if (!testing) {
					respawn();
				}
				return -1;
			}
		}
		return 0;
	}

	public int checkStatus(Block block, Tile[][] stage) {
		int x = block.getX(), y = block.getY();
		String pos = block.getPosition();
		Tile t = stage[x][y];
		if (pos.equals("Up") && t instanceof Hole) {
			return 1;
		} else if (pos.equals("Up") && t instanceof Orange || t instanceof Empty) {
			return -1;
		} else if (!pos.equals("Up")) {
			Tile t2 = pos.equals("Vertical") ? stage[x][y + 1] : stage[x + 1][y];
			if (t instanceof Empty || t2 instanceof Empty) {
				return -1;
			}
		}
		return 0;
	}

	private void respawn() {
		this.block = new Block("Up", spawnX, spawnY);
	}

	public void printBlock(Graphics2D g2) {
		int x = this.block.getX();
		int y = this.block.getY();
		int startingX = (int) ((Math.cos(Math.PI / 18) * TILE_WIDTH * x) + (Math.sin(Math.PI / 6) * TILE_HEIGHT * y))
				+ X_OFFSET;
		int startingY = (int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT * y) - (Math.sin(Math.PI / 18) * TILE_WIDTH * x))
				+ Y_OFFSET;
		String position = this.block.getPosition();
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

	public void printBlockAux(Graphics2D g2, int startingX, int startingY, int position) {
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
		drawTile(holeX, holeY, tiles[holeX][holeY].getColor(), g2);
		for (int i = SIZE - 1; i >= 0; i--) {
			for (int j = 0; j < SIZE; j++) {
				if (!(tiles[i][j] instanceof Empty)) {
					g2.setColor(tiles[i][j].getColor());
					drawTile(i, j, tiles[i][j].getColor(), g2);
//				g2.setColor(tiles[i][j].getColor());
//				g2.fillRect(i * TILE_WIDTH, j * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
//				g2.setColor(Color.black);
//				g2.drawRect(i * TILE_WIDTH, j * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
//
//				drawTile(i, j, tiles[i][j].getColor(), g2);
				}
			}
		}
	}

	public void setBoard(int num) {
		try {
			String fileName = "stages\\level" + num + ".txt";
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
						spawnX = i;
						spawnY = j;
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

	private void drawTile(int x, int y, Color color, Graphics g2) {
		int points = 12;
		g2.setColor(color);
		int startingX = (int) ((Math.cos(Math.PI / 18) * TILE_WIDTH * x) + (Math.sin(Math.PI / 6) * TILE_HEIGHT * y))
				+ X_OFFSET;
		int startingY = (int) ((Math.cos(Math.PI / 6) * TILE_HEIGHT * y) - (Math.sin(Math.PI / 18) * TILE_WIDTH * x))
				+ Y_OFFSET;
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
}
