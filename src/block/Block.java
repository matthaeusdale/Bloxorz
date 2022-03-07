package block;

public class Block {
	String position;
	int x, y;

	public Block(String position, int x, int y) {
		this.position = position;
		this.x = x;
		this.y = y;
	}

	public Block(Block block) {
		this.position = block.position;
		this.x = block.x;
		this.y = block.y;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public String getPosition() {
		return this.position;
	}

	public Block moveBlock(String direction) {
		if (direction.equals("Up")) {
			if (position.equals("Up")) {
				y = y - 2;
				this.position = "Vertical";
			} else if (position.equals("Vertical")) {
				y = y - 1;
				this.position = "Up";
			} else {
				y = y - 1;
			}
		} else if (direction.equals("Down")) {
			if (position.equals("Up")) {
				y = y + 1;
				this.position = "Vertical";
			} else if (position.equals("Vertical")) {
				y = y + 2;
				this.position = "Up";
			} else {
				y = y + 1;
			}
		} else if (direction.equals("Right")) {
			if (position.equals("Up")) {
				x = x + 1;
				this.position = "Horizontal";
			} else if (position.equals("Vertical")) {
				x = x + 1;
			} else {
				x = x + 2;
				this.position = "Up";
			}
		} else if (direction.equals("Left")) {
			if (position.equals("Up")) {
				x = x - 2;
				this.position = "Horizontal";
			} else if (position.equals("Vertical")) {
				x = x - 1;
			} else {
				x = x - 1;
				this.position = "Up";
			}
		}
		return this;
	}

	@Override
	public boolean equals(Object o) {
		Block other = null;
		if (o != null && o instanceof Block) {
			other = (Block) o;
			if (other.getX() == this.x && other.getY() == this.y && other.getPosition().equals(this.position)) {
				//System.out.println("used");
				return true;
			}
		}
		return false;
	}

	public String toString() {
		return "" + x + ", " + y + " , " + position;
	}
}
