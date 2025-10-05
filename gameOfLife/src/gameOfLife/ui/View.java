package gameOfLife.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import gameOfLife.core.Cell;
import gameOfLife.core.Grid;
import gameOfLife.core.WorldMachine;
import gameOfLife.core.WorldMachine.GenerationListener;

public class View implements GenerationListener {

	JFrame f;

	Grid grid;

	public View(WorldMachine wm) {
		this.grid = wm.grid();
		wm.setListener(this);

		this.f = new JFrame("My Humble Game Of Life");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new MyPanel(grid));
		f.pack();
		f.setVisible(true);
	}

	static class MyPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		private Grid grid;

		// Size of a cell in pixels
		private final int cellSize = 1;
		private final int margin = 10;

		MyPanel(Grid grid) {
			this.grid = grid;
		}

		public Dimension getPreferredSize() {
			return new Dimension(512, 512);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2 = (Graphics2D) g;
			g2.scale(20 / cellSize, 20 / cellSize);
			g2.translate(margin, margin);

			for (Cell c : grid.cells()) {
				g2.fillRect(c.pos().x * cellSize, c.pos().y * cellSize, cellSize, cellSize);
			}
		}
	}

	@Override
	public void newGeneration() {
		f.repaint();
	}

}
