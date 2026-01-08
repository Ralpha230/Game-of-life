package gameOfLife.ui;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.Serial;

import javax.swing.*;

import gameOfLife.core.Cell;
import gameOfLife.core.Grid;
import gameOfLife.utils.DoubleWrapper;

public class View {

    JFrame f = new JFrame("My Humble Game Of Life");

    Grid grid;

    final Point translate = new Point(0, 0);
    final DoubleWrapper zoomFactor = new DoubleWrapper(1.0);

    final Interaction interaction;
    Boolean mustDisplayMouse = false;
    final Point mousePositionOnGrid = new Point(0, 0);

    public View(Grid grid, int frameRate) {
        this.grid = grid;

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridViewer viewer = new GridViewer(this);
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

        private final View view;

        // Size of a cell in pixels
        private static final int cellSize = 1;

        private int HUDOffset = 10;

        public GridViewer(View view) {
            this.view = view;
        }

        public Dimension getPreferredSize() {
            return new Dimension(512, 512);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            AffineTransform HUDTransform = (AffineTransform) g2.getTransform().clone();
            AffineTransform gridTransform = (AffineTransform) g2.getTransform().clone();
            gridTransform.scale(view.zoomFactor.value / cellSize, view.zoomFactor.value / cellSize);
            gridTransform.translate(-view.translate.x, -view.translate.y);

            // Painting cells
            g2.setTransform(gridTransform);
            for (Cell c : view.grid.cells()) {
                g2.fillRect(c.pos().x * cellSize, c.pos().y * cellSize, cellSize, cellSize);
            }

            // Painting mouse cursor
            if (view.mustDisplayMouse) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g2.fillRect(view.mousePositionOnGrid.x * cellSize, view.mousePositionOnGrid.y * cellSize, cellSize, cellSize);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            }

            // Painting HUD
            g2.setTransform(HUDTransform);
            g2.setFont(new Font("FreeMono", Font.BOLD, 20));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString("Generation " + view.grid.generation(), HUDOffset, fm.getAscent() + HUDOffset);
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

    public void setMousePositionOnGrid(Point p) {
        mousePositionOnGrid.setLocation(p);
    }

    public void setMouseDisplay(boolean display) {
        mustDisplayMouse = display;
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
