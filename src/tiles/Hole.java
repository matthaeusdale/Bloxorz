package tiles;

import java.awt.Color;

public class Hole extends Tile {

	@Override
	public Color getColor() {
		return new Color(255, 0, 0, 127);
	}

	@Override
	public String toString() {
		return "H";
	}

	@Override
	public boolean equals(Tile anotherTile) {
		return anotherTile instanceof Hole;
	}

}
