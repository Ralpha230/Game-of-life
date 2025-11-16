package gameOfLife.ui;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Interaction implements KeyListener, MouseListener {

	private View v;

	private State state = State.IDLE;

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
			state = State.CTRL_PRESSED;
			return;
		case KeyEvent.VK_UP:
			switch (state) {
			case State.IDLE:
				v.translate(new Point(0, 1));
				return;
			case State.CTRL_PRESSED:
				v.zoomIn();
				return;
			}
		case KeyEvent.VK_DOWN:
			switch (state) {
			case State.IDLE:
				v.translate(new Point(0, -1));
				return;
			case State.CTRL_PRESSED:
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
			switch (state) {
			case State.CTRL_PRESSED:
				v.resetZoom();
				return;
			}
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
