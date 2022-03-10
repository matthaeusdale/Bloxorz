package main;

import javax.swing.*;

import levels.*;

public class Run {
	Frame frame;
	public final int WIDTH = 1250, HEIGHT = 1000;

	public static void main(String[] args) {
		new Run();
	}

	public Run() {
		// MainMenu main= new MainMenu(this);
		frame = new Frame("Bloxorz");
		
		// frame.setResizable(false);
	}

	public Frame getFrame() {
		return frame;
	}
}
