package gameOfLife.ui;

import java.awt.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;

import gameOfLife.core.Cell;
import gameOfLife.core.Model;
import gameOfLife.core.pattern.Pattern;
import gameOfLife.utils.DoubleWrapper;

public class View extends JPanel {

    // Size of a cell in pixels
    private static final int cellSize = 1;
    // Amount of pixels by which the camera moves at a time by default
    private static final int defaultMoveSpeed = 50;
    // Offset of the HUD from the edge of the window
    private static final int HUDOffset = 10;

    private final JFrame frame = new JFrame("My Humble Game Of Life");
    private final Model model;
    private final Interaction interaction;

    private final Point translate = new Point(0, 0);
    private final DoubleWrapper zoomFactor = new DoubleWrapper(1.0);
    private Boolean mustDisplayMouse = true;
    private final Point ghostPositionOnGrid = new Point(0, 0);


    public View(Model model, int frameRate) {
        this.model = model;
        this.interaction = new Interaction(this, model);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);

        frame.addKeyListener(interaction);
        addMouseListener(interaction);
        addMouseMotionListener(interaction);
        new Thread(() -> {
            while (true) {
                frame.repaint(0);
                try {
                    Thread.sleep(1000 / frameRate);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public Dimension getPreferredSize() {
        return new Dimension(512, 512);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        AffineTransform HUDTransform = (AffineTransform) g2.getTransform().clone();
        AffineTransform gridTransform = (AffineTransform) g2.getTransform().clone();
        gridTransform.scale(zoomFactor.value / cellSize, zoomFactor.value / cellSize);
        gridTransform.translate(-translate.x, -translate.y);

        // Painting cells
        g2.setTransform(gridTransform);
        for (Cell c : model.getGrid().cells()) {
            g2.fillRect(c.pos().x * cellSize, c.pos().y * cellSize, cellSize, cellSize);
        }

        // Painting ghost pattern
        if (mustDisplayMouse) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            Pattern pattern = interaction.getPatternToDraw();
            for (Cell c : pattern.getCells(ghostPositionOnGrid)) {
                g2.fillRect(c.pos().x * cellSize, c.pos().y * cellSize, cellSize, cellSize);
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        }

        // Painting HUD
        g2.setTransform(HUDTransform);
        g2.setFont(new Font("FreeMono", Font.BOLD, 20));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString("Generation " + model.getGrid().generation(), HUDOffset, fm.getAscent() + HUDOffset);

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

    public void setGhostPositionOnGrid(Point p) {
        ghostPositionOnGrid.setLocation(p);
    }

    public void setMouseDisplay(boolean display) {
        mustDisplayMouse = display;
    }

    // Interaction

    public void moveUp() {
        translate(new Point(0, (int) -(defaultMoveSpeed / getZoomFactor())));
    }

    public void moveDown() {
        translate(new Point(0, (int) (defaultMoveSpeed / getZoomFactor())));
    }

    public void moveLeft() {
        translate(new Point((int) -(defaultMoveSpeed / getZoomFactor()), 0));
    }

    public void moveRight() {
        translate(new Point((int) (defaultMoveSpeed / getZoomFactor()), 0));
    }

    private void translate(Point delta) {
        translate.x += delta.x;
        translate.y += delta.y;
    }

    public void zoomIn() {
        zoomFactor.value *= 1.5;
    }

    public void zoomOut() {
        zoomFactor.value /= 1.5;
    }

    public void resetZoom() {
        zoomFactor.value = 1;
    }

    public void addCell(Cell cell) {
        model.getGrid().addCell(cell);
    }

}
