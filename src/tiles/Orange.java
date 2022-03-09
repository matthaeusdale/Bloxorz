package tiles;

import java.awt.Color;

public class Orange extends Tile {

	public Orange(int x, int y) {
		super(x, y);
	}

	@Override
	public Color getColor() {
		return new Color(255, 150, 0);
	}

	@Override
	public String toString() {
		return "O";
	}

	@Override
	public boolean equals(Tile anotherTile) {
		return anotherTile instanceof Orange;
	}
}
