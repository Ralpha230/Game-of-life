package gameOfLife.core.pattern;

import gameOfLife.core.Cell;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Glider extends Pattern {

    public enum Orientation {
        SE,
        SW,
        NW,
        NE
    }

    private Orientation orientation;

    public Glider(Orientation orientation) {
        this.orientation = orientation;
    }

    @Override
    public Set<Cell> getCells(Point pos) {
        Set<Cell> cells = new HashSet<Cell>();
        int x = pos.x;
        int y = pos.y;
        cells.add(new Cell(new Point(x, y)));
        cells.add(new Cell(translate(pos, -1, 0)));
        cells.add(new Cell(translate(pos, -2, 0)));
        cells.add(new Cell(translate(pos, 0, -1)));
        cells.add(new Cell(translate(pos, -1, -2)));
        return cells;
    }

    private Point translate(Point p, int dx, int dy) {
        Point res = new Point(p.x, p.y);
        switch (orientation) {
            case SE:
                res.translate(dx, dy);
                return res;
            case SW:
                res.translate(-dy, dx);
                return res;
            case NW:
                res.translate(-dx, -dy);
                return res;
            case NE:
                res.translate(dy, -dx);
                return res;
            default:
                throw new RuntimeException();
        }
    }

}
