package levels;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import block.*;

import java.util.ArrayList;
import java.util.List;
import tiles.Empty;
import tiles.FlatButton;
import tiles.Hole;
import tiles.Orange;
import tiles.Solid;
import tiles.Tile;

public class Stage {

	int max_path_length = 35;
	final public int NUM_TILES = 25;
	Tile[][] tiles = new Tile[NUM_TILES][NUM_TILES];
	Block block;

	int border = 4;
	int solvedLength = 0;

	int spawnX, spawnY;
	int holeX, holeY;

	ArrayList<Tile> buttons;

	public Stage() {
		createRandomStage();
	}

	public Stage(int num) {
		setStage(num);
	}

	public Stage(int x, int y) {
		createStageWithBlock(x, y);
	}

	private void createStageWithBlock(int x, int y) {
		for (int i = 0; i < NUM_TILES; i++) {
			for (int j = 0; j < NUM_TILES; j++) {
				if (i < NUM_TILES - border && i >= border && j < NUM_TILES - border && j >= border) {
					int random = (int) (Math.random() * 3);

					switch (random) {
					case 0:
						tiles[i][j] = new Empty(i, j);
						break;
					case 1:
						tiles[i][j] = new Solid(i, j);
						break;
					case 2:
						tiles[i][j] = new Orange(i, j);
						break;
					default:
						tiles[i][j] = new Solid(i, j);
					}
				} else {
					tiles[i][j] = new Empty(i, j);
				}
			}
		}

		while (purge())
			;
		
		//createButtons();
		createBlock(x, y);
		createHole();

		// nextStage[10][10] = new Solid();

		

		if (!solvable()) {
			// System.out.println("unsolvable");
			createStageWithBlock(x, y);
		}
	}

	private void createRandomStage() {
		for (int i = 0; i < NUM_TILES; i++) {
			for (int j = 0; j < NUM_TILES; j++) {
				if (i < NUM_TILES - border && i >= border && j < NUM_TILES - border && j >= border) {
					int random = (int) (Math.random() * 3);

					switch (random) {
					case 0:
						tiles[i][j] = new Empty(i, j);
						break;
					case 1:
						tiles[i][j] = new Solid(i, j);
						break;
					case 2:
						tiles[i][j] = new Orange(i, j);
						break;
					default:
						tiles[i][j] = new Solid(i, j);
					}
				} else {
					tiles[i][j] = new Empty(i, j);
				}
			}
		}
		
		while (purge())
			;
		

		//createButtons();
		createBlock();
		createHole();

		// nextStage[10][10] = new Solid();

		
		if (!solvable()) {
			// System.out.println("unsolvable");
			createRandomStage();
		}
		removeUnreachable();
	}

	private void removeUnreachable() {
		// TODO Auto-generated method stub
		
	}

	private boolean createBlock() {
		do {
			spawnX = (int) ((Math.random() * (NUM_TILES - 6)) + 3);
			spawnY = (int) ((Math.random() * (NUM_TILES - 6)) + 3);
		} while (Math.abs(spawnX - holeX) < 2);
//		System.out.println(spawnX);
//		System.out.println(spawnY);
		pad(spawnX, spawnY);
		block = new Block("Up", spawnX, spawnY);
		return true;
	}

	private boolean createBlock(int x, int y) {
		spawnX = x;
		spawnY = y;
		pad(spawnX, spawnY);
		block = new Block("Up", spawnX, spawnY);
		return true;
	}

	private void createHole() {
		do {
			holeX = (int) ((Math.random() * (NUM_TILES - 6)) + 3);
			holeY = (int) ((Math.random() * (NUM_TILES - 6)) + 3);
		} while (Math.abs(spawnX - holeX) < 7 || Math.abs(spawnY - holeY) < 7);

//		System.out.println(holeX);
//		System.out.println(holeY);

		pad(holeX, holeY);
		tiles[holeX][holeY] = new Hole(holeX, holeY);
	}

	private boolean pad(int x, int y) {
		for (int k = -1; k <= 1; k++) {
			for (int l = -1; l <= 1; l++) {
				tiles[k + x][l + y] = new Solid(x, y);
			}
		}
		return true;
	}

	private void createButtons() {
		for (int i = 0; i < 5; i++) {
			Tile button = findRandomSolid();
			tiles[button.x()][button.y()] = new FlatButton(button.x(), button.y());
			// System.out.println("Button: " + button.x() + ", " + button.y());
			Tile solid = findRandomSolid();
			FlatButton b = (FlatButton) tiles[button.x()][button.y()];
			b.setEffectedTile(solid);
			b.press();
			b.press();
			// System.out.println(solid.x() + ", " + solid.y());
		}
	}

	private Tile findRandomSolid() {
		int x = 0, y = 0;

		while (!(tiles[x][y] instanceof Solid)) {
			x = (int) (Math.random() * NUM_TILES);
			y = (int) (Math.random() * NUM_TILES);
			// System.out.println(x + ", " + y);
//			if (count % 100 == 0)
//				System.out.println(count);
//			count++;
		}

		return tiles[x][y];
	}

	private boolean purge() {
		boolean purged = false;
		for (int i = 0; i < NUM_TILES; i++) {
			for (int j = 0; j < NUM_TILES; j++) {
				if (!(tiles[i][j] instanceof Empty)) {
					int up = (tiles[i][j - 1] instanceof Empty) ? 1 : 0;
					int down = (tiles[i][j + 1] instanceof Empty) ? 1 : 0;
					int right = (tiles[i + 1][j] instanceof Empty) ? 1 : 0;
					int left = (tiles[i - 1][j] instanceof Empty) ? 1 : 0;
					if (up + down + left + right >= 3) {
						tiles[i][j] = new Empty(i, j);
						purged = true;
					}
				}
			}
		}
		return purged;
	}

	private boolean solvable() {
//		startTime = System.currentTimeMillis();
//		boolean solvable = solvable(new Block(block), new Block[1], 0);
//		long end = System.currentTimeMillis();
//		System.out.println(end - startTime);
//		if (end - startTime > 4000) {
//			return true;
//		}
		return solvable(new Block(block), new Block[1], 0);
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

		int status = checkStatus(player, tiles);
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

	private boolean contains(Block[] states, Block player) {
		for (int i = 0; i < states.length - 1; i++) {
			if (states[i] != null && states[i].equals(player))
				return true;
		}
		return false;
	}

	private void copyArray(Block[] arr1, Block[] arr2) {
		for (int i = 0; i < arr1.length; i++) {
			if (arr1[i] != null) {
				arr2[i] = new Block(arr1[i]);
			}
		}
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

	public int checkStatus() {
		int x = this.block.getX(), y = this.block.getY();
		String pos = this.block.getPosition();
		Tile t = tiles[x][y];
		Tile t2 = pos.equals("Vertical") ? tiles[x][y + 1] : tiles[x + 1][y];
		if (pos.equals("Up") && t instanceof Hole) {
//			// System.out.println("won");
//			if (!testing) {
//				respawn();
//				nextStage();
//			}
			return 1;
		} else if (pos.equals("Up") && t instanceof Orange || t instanceof Empty) {
			// System.out.println("lose up");
//			if (!testing) {
//				
//			}
//			respawn();
			return -1;
		} else if (!pos.equals("Up")) {
			if (t instanceof Empty || t2 instanceof Empty) {
				// System.out.println("lose space");
//				if (!testing) {
//				respawn();
//				}

				return -1;
			}
		}
		if (t instanceof FlatButton) {
			int effX = ((FlatButton) t).effectedTile.x();
			int effY = ((FlatButton) t).effectedTile.y();
			// System.out.println("one");
			((FlatButton) t).press();
			tiles[effX][effY] = ((FlatButton) t).isPressed() ? new Empty(effX, effY) : new Solid(effX, effY);
		}
		if (t2 instanceof FlatButton && !pos.equals("Up")) {
			((FlatButton) t2).press();
			int effX = ((FlatButton) t2).effectedTile.x();
			int effY = ((FlatButton) t2).effectedTile.y();
			tiles[effX][effY] = ((FlatButton) t2).isPressed() ? new Empty(effX, effY) : new Solid(effX, effY);
			// System.out.println("two");
		}
		return 0;
	}

	private void setStage(int num) {
		try {
			String fileName = "stages\\level" + num + ".txt";
			List<String> lines = Files.readAllLines(Paths.get(fileName));

			for (int i = 0; i < lines.size(); i++) {
				char[] row = lines.get(i).toCharArray();
				for (int j = 0; j < row.length; j++) {
					char block = row[j];
					if (block == 'E') {
						tiles[j][i] = new Empty(i, j);
					} else if (block == 'H') {
						tiles[j][i] = new Hole(i, j);
					} else if (block == 'O') {
						tiles[j][i] = new Orange(i, j);
					} else if (block == 'S') {
						tiles[j][i] = new Solid(i, j);
					} else if (block == 'B') {
						this.block = new Block("Up", i, j);
						spawnX = i;
						spawnY = j;
						tiles[j][i] = new Solid(i, j);
					} else if (block == 'F') {
						tiles[j][i] = new FlatButton(i, j);
						((FlatButton) tiles[j][i]).setEffectedTile(findRandomSolid());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void respawn() {
		this.block = new Block("Up", spawnX, spawnY);
	}

	public Block getBlock() {
		return block;
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public int getHoleX() {
		return holeX;
	}

	public int getHoleY() {
		return holeY;
	}
}
