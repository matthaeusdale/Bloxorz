package tiles;

import java.awt.Color;

public abstract class Tile {
	public abstract Color getColor();

	public abstract String toString();

	private int x;
	private int y;

	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	@Override
	public boolean equals(Object otherTile) {
		if (otherTile instanceof Tile) {
			Tile t = (Tile) otherTile;
			return t.x == x && t.y == y;
		}
		return false;

	}

}
