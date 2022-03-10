package main;

import java.util.ArrayList;

import block.Block;
import tiles.Button;

public class State {
	ArrayList<Button> buttons;
	Block block;

	public State(ArrayList<Button> buttons, Block block) {
		this.buttons = buttons;
		this.block = block;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof State) {
			State s = (State) o;
			for (Button b : s.buttons) {
				
			}
		}
		return false;
	}
}
