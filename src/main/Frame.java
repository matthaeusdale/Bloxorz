package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class Frame extends JFrame {

	Panel panel;

	public Frame(String string) {
		super(string);
		Panel panel = new Panel(this);
		getContentPane().add(panel);
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setUndecorated(true);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//pack();
		//setSize(1250, 1000);
		setLocationRelativeTo(null);
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				panel.keyPressed(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});

	}
}
