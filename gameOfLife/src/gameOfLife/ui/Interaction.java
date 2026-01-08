package gameOfLife.ui;

import gameOfLife.core.WorldMachine;

import java.awt.Point;
import java.awt.event.*;

public class Interaction implements KeyListener, MouseListener, MouseMotionListener {

    private final View view;
    private WorldMachine worldMachine;

    private KeyState keyState = KeyState.IDLE;

    public Interaction(View view, WorldMachine worldMachine) {
        this.view = view;
        this.worldMachine = worldMachine;
    }

    public Interaction(View view) {
        this.view = view;
    }

    public void setWorldMachine(WorldMachine worldMachine) {
        this.worldMachine = worldMachine;
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
                        view.translate(new Point(0, -1));
                        return;
                    case KeyState.CTRL_PRESSED:
                        view.zoomIn();
                        return;
                }
            case KeyEvent.VK_DOWN:
                switch (keyState) {
                    case KeyState.IDLE:
                        view.translate(new Point(0, 1));
                        return;
                    case KeyState.CTRL_PRESSED:
                        view.zoomOut();
                        return;
                }
            case KeyEvent.VK_LEFT:
                view.translate(new Point(-1, 0));
                return;
            case KeyEvent.VK_RIGHT:
                view.translate(new Point(1, 0));
                return;
            case KeyEvent.VK_ENTER:
                switch (keyState) {
                    case KeyState.CTRL_PRESSED:
                        view.resetZoom();
                        return;
                }
                return;
            case KeyEvent.VK_SPACE:
                if (worldMachine != null) {
                    worldMachine.pause();
                    view.setMouseDisplay(worldMachine.isPaused());
                }
                return;
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
        if (worldMachine.isPaused()) {
            view.addCell(pixelToGrid(e.getPoint()));
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
        if (worldMachine.isPaused()) {
            view.addCell(pixelToGrid(e.getPoint()));
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        view.setMousePositionOnGrid(pixelToGrid(e.getPoint()));
    }

    enum KeyState {
        IDLE, CTRL_PRESSED
    }

    private Point pixelToGrid(Point pix) {
        return new Point((int) (pix.x / view.getZoomFactor() + view.getTranslate().x), (int) (pix.y / view.getZoomFactor() + view.getTranslate().y));
    }

}
