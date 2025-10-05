package gameOfLife.core;

import java.awt.Point;

public class Cell {

	private Point pos;

	Cell(Point pos) {
		this.pos = pos;
	}

	public Point pos() {
		return pos;
	}

	@Override
	public boolean equals(Object o) {
		return !(o == null) && (o instanceof Cell) && ((Cell) o).pos().equals(pos);
	}

	@Override
	public int hashCode() {
		return pos.hashCode();
	}

}
