package gameOfLife;

import java.awt.Point;

import gameOfLife.core.Grid;
import gameOfLife.core.Model;
import gameOfLife.core.WorldMachine;
import gameOfLife.ui.View;

public class Main {

    private static final long genDuration = 100;
    private static final int frameRate = 30;

    public static void main(String[] args) {

        Grid grid = new Grid();
        WorldMachine wm = new WorldMachine(grid, genDuration);
        Model model = new Model(grid, wm);
        View view = new View(model, frameRate);

        wm.start();

    }
}
