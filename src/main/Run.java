package main;

import javax.swing.*;

import levels.*;

public class Run {
	JFrame frame;
	public final int WIDTH = 1250, HEIGHT = 1000;

	public static void main(String[] args) {
		new Run();
	}

	public Run() {
		// MainMenu main= new MainMenu(this);
		frame = new JFrame("Bloxorz");
		Stage randomStage = new randomStage(this);
		frame.getContentPane().add(randomStage);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(1250, 1000);
		frame.setLocationRelativeTo(null);
		// frame.setResizable(false);
	}

	public JFrame getFrame() {
		return frame;
	}
}
