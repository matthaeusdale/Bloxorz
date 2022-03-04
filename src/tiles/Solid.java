package tiles;

import java.awt.Color;

public class Solid extends Tile {

	@Override
	public Color getColor() {
		return Color.GRAY;
	}

	@Override
	public String toString() {
		return "S";
	}
}
