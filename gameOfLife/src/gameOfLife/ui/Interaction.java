package gameOfLife.ui;

import gameOfLife.core.Cell;
import gameOfLife.core.Model;
import gameOfLife.core.WorldMachine;
import gameOfLife.core.pattern.Glider;
import gameOfLife.core.pattern.Pattern;
import gameOfLife.core.pattern.SingleCell;

import java.awt.Point;
import java.awt.event.*;

public class Interaction implements KeyListener, MouseListener, MouseMotionListener {

    private final View view;
    private final Model model;

    private int patternToDraw = 0;
    private static Pattern patterns[] = {
            new SingleCell(),
            new Glider(Glider.Orientation.SE),
            new Glider(Glider.Orientation.SW),
            new Glider(Glider.Orientation.NW),
            new Glider(Glider.Orientation.NE)};

    private KeyState keyState = KeyState.IDLE;

    public Interaction(View view, Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getExtendedKeyCode()) {
            case KeyEvent.VK_CONTROL:
                keyState = KeyState.CTRL_PRESSED;
                return;
            case KeyEvent.VK_UP:
                switch (keyState) {
                    case KeyState.IDLE:
                        view.moveUp();
                        return;
                    case KeyState.CTRL_PRESSED:
                        view.zoomIn();
                        return;
                }
            case KeyEvent.VK_DOWN:
                switch (keyState) {
                    case KeyState.IDLE:
                        view.moveDown();
                        return;
                    case KeyState.CTRL_PRESSED:
                        view.zoomOut();
                        return;
                }
            case KeyEvent.VK_LEFT:
                view.moveLeft();
                return;
            case KeyEvent.VK_RIGHT:
                view.moveRight();
                return;
            case KeyEvent.VK_ENTER:
                switch (keyState) {
                    case KeyState.CTRL_PRESSED:
                        view.resetZoom();
                        return;
                }
                return;
            case KeyEvent.VK_SPACE:
                WorldMachine worldMachine = model.getWorldMachine();
                if (worldMachine != null) {
                    worldMachine.pause();
                    view.setMouseDisplay(worldMachine.isPaused());
                }
                return;
            case KeyEvent.VK_P:
                patternToDraw = (patternToDraw + 1) % patterns.length;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getExtendedKeyCode()) {
            case KeyEvent.VK_CONTROL:
                keyState = KeyState.IDLE;
                return;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        WorldMachine worldMachine = model.getWorldMachine();
        if (worldMachine.isPaused()) {
            drawPattern(pixelToGrid(e.getPoint()));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        WorldMachine worldMachine = model.getWorldMachine();
        if (worldMachine.isPaused()) {
            drawPattern(pixelToGrid(e.getPoint()));
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        view.setGhostPositionOnGrid(pixelToGrid(e.getPoint()));
    }

    enum KeyState {
        IDLE, CTRL_PRESSED
    }

    private Point pixelToGrid(Point pix) {
        return new Point((int) (pix.x / view.getZoomFactor() + view.getTranslate().x), (int) (pix.y / view.getZoomFactor() + view.getTranslate().y));
    }

    public Pattern getPatternToDraw() {
        return patterns[patternToDraw];
    }

    private void drawPattern(Point pos) {
        for (Cell c : patterns[patternToDraw].getCells(pos)) {
            view.addCell(c);
        }
    }

}
