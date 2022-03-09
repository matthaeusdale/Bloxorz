package tiles;

import java.awt.Color;

public abstract class Tile {
	public abstract Color getColor();

	public abstract String toString();

	public abstract boolean equals(Tile anotherTile);

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
}
