package gameOfLife;

import java.awt.Point;

import gameOfLife.core.Grid;
import gameOfLife.core.WorldMachine;
import gameOfLife.ui.View;

public class Main {

	private static final long genDuration = 100;

	public static void main(String[] args) {

		Grid grid = new Grid();
		drawGliderSE(grid, new Point(0, 0));
		drawGliderSE(grid, new Point(5, 5));
		drawBlinker(grid, new Point(10, 10));
		WorldMachine wm = new WorldMachine(grid, genDuration);
		new View(wm);

		wm.start();

	}

	private static void drawGliderSE(Grid grid, Point offset) {
		int x = offset.x;
		int y = offset.y;
		grid.addCell(new Point(x, y));
		grid.addCell(new Point(x - 1, y));
		grid.addCell(new Point(x - 2, y));
		grid.addCell(new Point(x, y - 1));
		grid.addCell(new Point(x - 1, y - 2));
	}

	private static void drawBlinker(Grid grid, Point offset) {
		int x = offset.x;
		int y = offset.y;
		grid.addCell(new Point(x, y));
		grid.addCell(new Point(x - 1, y));
		grid.addCell(new Point(x + 1, y));
	}

}
