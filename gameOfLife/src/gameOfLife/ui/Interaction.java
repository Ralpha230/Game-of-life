package gameOfLife.ui;

import gameOfLife.core.WorldMachine;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Interaction implements KeyListener, MouseListener {

    private View view;
    private WorldMachine worldMachine;

    private State state = State.IDLE;

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
                state = State.CTRL_PRESSED;
                return;
            case KeyEvent.VK_UP:
                switch (state) {
                    case State.IDLE:
                        view.translate(new Point(0, 1));

                        return;
                    case State.CTRL_PRESSED:
                        view.zoomIn();
                        return;
                }
            case KeyEvent.VK_DOWN:
                switch (state) {
                    case State.IDLE:
                        view.translate(new Point(0, -1));
                        return;
                    case State.CTRL_PRESSED:
                        view.zoomOut();
                        return;
                }
            case KeyEvent.VK_LEFT:
                view.translate(new Point(1, 0));
                return;
            case KeyEvent.VK_RIGHT:
                view.translate(new Point(-1, 0));
                return;
            case KeyEvent.VK_ENTER:
                switch (state) {
                    case State.CTRL_PRESSED:
                        view.resetZoom();
                        return;
                }
                return;
            case KeyEvent.VK_SPACE:
                if (worldMachine != null) {
                    worldMachine.pause();
                }
                return;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getExtendedKeyCode()) {
            case KeyEvent.VK_CONTROL:
                state = State.IDLE;
                return;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

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

    enum State {
        IDLE, CTRL_PRESSED
    }

}
