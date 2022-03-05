package tiles;

import java.awt.Color;

public class Orange extends Tile {

	@Override
	public Color getColor() {
		return Color.ORANGE;
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
