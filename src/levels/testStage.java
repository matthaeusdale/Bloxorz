package levels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import main.Run;
import tiles.*;
import block.*;

public class testStage extends Stage implements MouseListener, MouseMotionListener, ActionListener {

	Run game;
	final int TILE_WIDTH = 30;

	public testStage(Run game) {
		initStage();
		addMouseListener(this);
		addMouseMotionListener(this);
		// addKeyListener(this);
		setBackground(Color.black);
		setVisible(true);
		this.game = game;
		game.getFrame().addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					block.moveBlock("Up");
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					block.moveBlock("Right");
				} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					block.moveBlock("Left");
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					block.moveBlock("Down");
				}
				checkStatus();
				repaint();
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

		});
	}

	@Override
	public void initStage() {
		boolean madeHole = false;
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				int shit = (int) (Math.random() * 4);

				switch (shit) {
				case 0:
					tiles[i][j] = new Space();
					break;
				case 1:
					tiles[i][j] = new Solid();
					break;
				case 2:
					tiles[i][j] = new Orange();
					break;
				case 3:
					if (!madeHole) {
						tiles[i][j] = new Hole();
						madeHole = true;
					} else
						tiles[i][j] = new Solid();
					break;
				default:
					tiles[i][j] = new Solid();
				}
			}
		}
		this.block = new Block("Up", 9, 11);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.gray);
		printBoard(g2);
		printBlock(g2);
	}

	public void checkStatus() {
		int x = this.block.getX(), y = this.block.getY();
		String pos = this.block.getPosition();
		Tile t = tiles[x][y];
		if (pos.equals("Up") && t instanceof Hole) {
			System.out.println("won");
		} else if (pos.equals("Up") && t instanceof Orange) {
			System.out.println("lose");
		} else if (true) {

		}
	}

	public void printBlock(Graphics2D g2) {
		int x = this.block.getX();
		int y = this.block.getY();
		String position = this.block.getPosition();
		g2.setColor(Color.BLUE);
		if (position.equals("Up")) {
			g2.fill3DRect(TILE_WIDTH * x, TILE_WIDTH * y, TILE_WIDTH, TILE_WIDTH, true);
		} else if (position.equals("Horizontal")) {
			g2.fill3DRect(TILE_WIDTH * x, TILE_WIDTH * y, TILE_WIDTH * 2, TILE_WIDTH, true);
		} else {
			g2.fill3DRect(TILE_WIDTH * x, TILE_WIDTH * y, TILE_WIDTH, TILE_WIDTH * 2, true);
		}
	}

	public void printBoard(Graphics g2) {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				g2.setColor(tiles[i][j].getColor());
				drawTile(i, j, tiles[i][j].getColor(), g2);
				// g2.fillRect(i * TILE_WIDTH, j * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
				g2.setColor(Color.black);
				//g2.drawRect(i * TILE_WIDTH, j * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
			}
		}
	}

	private void drawTile(int i, int j, Color color, Graphics g2) {
		g2.setColor(color);
		int[] xPoints = { i * TILE_WIDTH, i * TILE_WIDTH, (i + 1) * TILE_WIDTH, (i + 1) * TILE_WIDTH };
		int[] yPoints = { j * (TILE_WIDTH / 2), (j + 1) * (TILE_WIDTH / 2), (j + 1) * (TILE_WIDTH / 2),
				j * (TILE_WIDTH / 2) };
		for (int m = 0; m < 4; m++) {
			xPoints[m] = (int) (Math.sin((180 * 45)/ (2 * Math.PI) ) * xPoints[m] + 400);
			
			yPoints[m] = (int) (Math.sin((180 * 45)/ (2 * Math.PI) ) * yPoints[m]);
			System.out.println("x: " + xPoints[m] + "y: " + yPoints[m]);
		}
		g2.fillPolygon(xPoints, yPoints, 4);
		g2.setColor(Color.black);
		g2.drawPolygon(xPoints, yPoints, 4);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
