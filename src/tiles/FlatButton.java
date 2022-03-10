package tiles;

import java.awt.Color;

public class FlatButton extends Tile implements Button {
	public Tile effectedTile;
	boolean pressed = true;

	public FlatButton(int x, int y) {
		super(x, y);
	}

	public void press() {
		pressed = !isPressed();
		if (isPressed()) {
			effectedTile = new Empty(effectedTile.x(), effectedTile.y());
		} else {
			effectedTile = new Solid(effectedTile.x(), effectedTile.y());
		}
	}

	public void setEffectedTile(Tile t) {
		effectedTile = t;
	}

	@Override
	public Color getColor() {
		if (!isPressed())
			return Color.green;
		else
			return Color.red;
	}

	@Override
	public String toString() {
		return "F";
	}

	@Override
	public boolean equals(Object t) {
		if (t instanceof FlatButton) {
			return (effectedTile.equals(((FlatButton) t).effectedTile) && isPressed() == ((FlatButton) t).isPressed());
		}
		return false;
	}

	public boolean isPressed() {
		return pressed;
	}

}
