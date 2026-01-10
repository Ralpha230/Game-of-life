package gameOfLife.core.pattern;

import gameOfLife.core.Cell;

import java.awt.*;
import java.util.Set;

public abstract class Pattern {

    public abstract Set<Cell> getCells(Point pos);
}