package gameOfLife.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serial;

import javax.swing.JFrame;
import javax.swing.JPanel;

import gameOfLife.core.Cell;
import gameOfLife.core.Grid;
import gameOfLife.utils.DoubleWrapper;

public class View {

    JFrame f = new JFrame("My Humble Game Of Life");

    Grid grid;

    private final Point translate = new Point(0, 0);
    private final DoubleWrapper zoomFactor = new DoubleWrapper(1.0);

    private final Interaction interaction;

    public View(Grid grid, int frameRate) {
        this.grid = grid;

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridViewer viewer = new GridViewer(grid, translate, zoomFactor);
        f.add(viewer);
        f.pack();
        f.setVisible(true);

        this.interaction = new Interaction(this);
        f.addKeyListener(interaction);
        viewer.addMouseListener(interaction);
        viewer.addMouseMotionListener(interaction);
        new Thread(() -> {
            while (true) {
                f.repaint(1);
                try {
                    Thread.sleep(1000 / frameRate);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public static class GridViewer extends JPanel {

        @Serial
        private static final long serialVersionUID = 1L;

        private final Grid grid;

        // Size of a cell in pixels
        private static final int cellSize = 1;
        private final Point translate;
        private final DoubleWrapper zoomFactor;

        public GridViewer(Grid grid, Point translate, DoubleWrapper zoomFactor) {
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
            g2.scale(zoomFactor.value / cellSize, zoomFactor.value / cellSize);
            g2.translate(-translate.x, -translate.y);

            for (Cell c : grid.cells()) {
                g2.fillRect(c.pos().x * cellSize, c.pos().y * cellSize, cellSize, cellSize);
            }
        }
    }

    public Point getTranslate() {
        return translate;
    }

    public void setTranslate(Point p) {
        translate.setLocation(p);
    }

    public double getZoomFactor() {
        return zoomFactor.value;
    }

    public void setZoomFactor(double zoom) {
        zoomFactor.value = zoom;
    }

    public Interaction getInteraction() {
        return interaction;
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

    public void addCell(Point p) {
        this.grid.addCell(p);
    }

}
