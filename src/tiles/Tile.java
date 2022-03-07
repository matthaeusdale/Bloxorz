package tiles;

import java.awt.Color;

public abstract class Tile {
	public abstract Color getColor();
	public abstract String toString();
	public abstract boolean equals(Tile anotherTile);
}
