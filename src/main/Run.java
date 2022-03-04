package main;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

import levels.Stage;
import levels.testStage;

public class Run {
	JFrame frame;// = new JFrame("Run");

	public static void main(String[] args) {
		new Run();
	}
	public Run() {
		//MainMenu main= new MainMenu(this);
		frame = new JFrame("Bloxorz");
		Stage testStage = new testStage(this);
		frame.getContentPane().add(testStage);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(850, 850);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
	}
	
	public JFrame getFrame() {
		return frame;
	}
}
