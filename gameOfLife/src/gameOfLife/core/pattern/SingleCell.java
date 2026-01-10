package gameOfLife.core.pattern;

import gameOfLife.core.Cell;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class SingleCell extends Pattern {

    @Override
    public Set<Cell> getCells(Point pos) {
        Set<Cell> res = new HashSet<>();
        res.add(new Cell(pos));
        return res;
    }


}
