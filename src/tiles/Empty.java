package tiles;

import java.awt.Color;

public class Empty extends Tile {

	public Empty(int x, int y) {
		super(x, y);
	}

	@Override
	public Color getColor() {
		return Color.BLACK;
	}

	@Override
	public String toString() {
		return "E";
	}

	@Override
	public boolean equals(Object anotherTile) {
		return anotherTile instanceof Empty;
	}

}
