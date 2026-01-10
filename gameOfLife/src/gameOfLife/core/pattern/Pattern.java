package gameOfLife.core.pattern;

import gameOfLife.core.Cell;

import java.awt.*;
import java.util.Set;

public abstract class Pattern {

    protected Point pos;

    public Pattern(Point pos) {
        this.pos = pos;
    }

    public abstract Set<Cell> getCells();
}