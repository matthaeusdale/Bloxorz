package tiles;

import java.awt.Color;

public class Empty extends Tile {

	@Override
	public Color getColor() {
		return Color.BLACK;
	}

	@Override
	public String toString() {
		return "E";
	}

}
