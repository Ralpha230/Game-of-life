package gameOfLife.ui;

import java.awt.Point;
import java.awt.event.*;

public class Interaction implements KeyListener, MouseListener, MouseMotionListener {

    private View v;

    private KeyState keyState = KeyState.IDLE;

    public Interaction(View v) {
        this.v = v;
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
                        v.translate(new Point(0, 1));
                        return;
                    case KeyState.CTRL_PRESSED:
                        v.zoomIn();
                        return;
                }
            case KeyEvent.VK_DOWN:
                switch (keyState) {
                    case KeyState.IDLE:
                        v.translate(new Point(0, -1));
                        return;
                    case KeyState.CTRL_PRESSED:
                        v.zoomOut();
                        return;
                }
            case KeyEvent.VK_LEFT:
                v.translate(new Point(1, 0));
                return;
            case KeyEvent.VK_RIGHT:
                v.translate(new Point(-1, 0));
                return;
            case KeyEvent.VK_ENTER:
                switch (keyState) {
                    case KeyState.CTRL_PRESSED:
                        v.resetZoom();
                        return;
                }
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
        v.addCell(pixelToGrid(e.getPoint()));
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
        v.addCell(pixelToGrid(e.getPoint()));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    enum KeyState {
        IDLE, CTRL_PRESSED
    }

    private Point pixelToGrid(Point pix) {
        return new Point((int) ((pix.x + v.getTranslate().x) / v.getZoomFactor()), (int) ((pix.y - v.getTranslate().y) / v.getZoomFactor()));
    }

}
