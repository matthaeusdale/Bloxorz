package levels;

import java.util.ArrayList;

import javax.swing.JPanel;

import block.*;
import tiles.Solid;
import tiles.Tile;

public abstract class Stage extends JPanel {
	int SIZE = 20;
	Tile[][] tiles = new Tile[SIZE][SIZE];
	Block block;

	abstract public void initStage();
}
