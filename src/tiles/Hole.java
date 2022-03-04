package tiles;

import java.awt.Color;

public class Hole extends Tile {

	@Override
	public Color getColor() {
		return Color.red;
	}

	@Override
	public String toString() {
		return "H";
	}

}
