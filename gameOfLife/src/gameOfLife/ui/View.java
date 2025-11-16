package gameOfLife.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import gameOfLife.core.Cell;
import gameOfLife.core.Grid;
import gameOfLife.core.WorldMachine;
import gameOfLife.core.WorldMachine.GenerationListener;
import gameOfLife.utils.DoubleWrapper;

public class View extends JComponent implements GenerationListener {

	private static final long serialVersionUID = 1L;

	JFrame f = new JFrame("My Humble Game Of Life");

	Grid grid;

	private Point translate = new Point(0, 0);

	private DoubleWrapper zoomFactor = new DoubleWrapper(1);

	public View(WorldMachine wm) {
		this.grid = wm.grid();
		wm.setListener(this);

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new MyPanel(grid, translate, zoomFactor));
		f.pack();
		f.setVisible(true);

		Interaction interaction = new Interaction(this);
		f.addKeyListener(interaction);
		f.addMouseListener(interaction);
	}

	static class MyPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		private Grid grid;

		// Size of a cell in pixels
		private final int cellSize = 1;
		private final int margin = 10;
		private Point translate;
		private DoubleWrapper zoomFactor;

		MyPanel(Grid grid, Point translate, DoubleWrapper zoomFactor) {
			this.grid = grid;
			this.translate = translate;
			this.zoomFactor = zoomFactor;
		}

		public Dimension getPreferredSize() {
			return new Dimension(512, 512);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2 = (Graphics2D) g;
			g2.scale(20 * zoomFactor.value / cellSize, 20 * zoomFactor.value / cellSize);
			g2.translate(margin + translate.x, margin + translate.y);

			for (Cell c : grid.cells()) {
				g2.fillRect(c.pos().x * cellSize, c.pos().y * cellSize, cellSize, cellSize);
			}
		}
	}

	@Override
	public void newGeneration() {
		f.repaint(5);
	}

	// Interaction

	public void translate(Point delta) {
		translate.x += delta.x;
		translate.y += delta.y;
	}

	public void zoomIn() {
		zoomFactor.value += 0.2;
	}

	public void zoomOut() {
		zoomFactor.value -= 0.2;
	}

	public void resetZoom() {
		zoomFactor.value = 1;
	}

}
