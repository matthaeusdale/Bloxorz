package levels;

import java.util.ArrayList;

import javax.swing.JPanel;

import block.*;
import tiles.Solid;
import tiles.Tile;

public abstract class Stage extends JPanel {
	final int SIZE = 25;
	Tile[][] tiles = new Tile[SIZE][SIZE];
	Tile[][] nextStage = new Tile[SIZE][SIZE];
	Block block;

}
