package gameOfLife.core;

import java.awt.*;
import java.util.*;

/*
This class implements the Set interface while granting optimized performances for finding a cell based on two-dimensional coordinates
 */
public class CellSet implements Set<Cell> {

    private final Map<Integer, Map<Integer, Cell>> grid = new HashMap<>();
    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (!(o instanceof Cell cell)) {
            return false;
        }
        Point pos = cell.pos();
        if (!grid.containsKey(pos.x)) {
            return false;
        }
        return grid.get(pos.x).containsKey(pos.y);
    }

    @Override
    public Iterator<Cell> iterator() {
        return new CellIterator(grid);
    }

    @Override
    public Object[] toArray() {
        Object[] res = new Object[size];
        int i = 0;
        for (int x : grid.keySet()) {
            Map<Integer, Cell> column = grid.get(x);
            for (int y : column.keySet()) {
                res[i++] = column.get(y);
            }
        }
        return res;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] res = (T[]) toArray();
        for (int i = 0; i < res.length; i++) {
            a[i] = (T) res[i];
        }
        return res;
    }

    @Override
    public boolean add(Cell cell) {
        Point pos = cell.pos();
        int x = pos.x;
        int y = pos.y;
        if (grid.containsKey(x)) {
            Map<Integer, Cell> column = grid.get(x);
            if (column.put(y, cell) == null) {
                size++;
                return true;
            }
            return false;
        }
        Map<Integer, Cell> column = new HashMap<>();
        column.put(y, cell);
        grid.put(x, column);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (!contains(o)) {
            return false;
        }
        for (int x : grid.keySet()) {
            Map<Integer, Cell> column = grid.get(x);
            if (column.remove(o) != null) {
                size--;
                return true;
            }
        }
        throw new RuntimeException("Set contains cell but cell was not found");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Cell> c) {
        boolean res = false;
        for (Cell cell : c) {
            res = add(cell) || res;
        }
        return res;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean res = false;
        Iterator<Cell> iter = iterator();
        while (iter.hasNext()) {
            Cell cell = iter.next();
            if (!c.contains(cell)) {
                remove(cell);
                res = true;
            }
        }
        return res;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean res = false;
        for (Object o : c) {
            res = remove(o) || res;
        }
        return res;
    }

    @Override
    public void clear() {
        grid.clear();
        size = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Set set)) {
            return false;
        }
        if (set.size() == size) {
            return false;
        }
        for (Object curr : set) {
            if (!contains(curr)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        Iterator<Cell> iter = iterator();
        int res = 0;
        while (iter.hasNext()) {
            Cell cell = iter.next();
            res += cell.hashCode();
        }
        return res;
    }

    class CellIterator implements Iterator<Cell> {

        private final Map<Integer, Map<Integer, Cell>> grid;
        private boolean hasNoNext = false;
        private final Iterator<Integer> xIterator;
        private Iterator<Integer> yIterator;
        // Current column
        private Map<Integer, Cell> column;

        CellIterator(Map<Integer, Map<Integer, Cell>> grid) {
            this.grid = grid;
            this.xIterator = grid.keySet().iterator();
            if (!xIterator.hasNext()) {
                this.hasNoNext = true;
                return;
            }
            this.column = grid.get(xIterator.next());
            this.yIterator = column.keySet().iterator();

            if (!yIterator.hasNext()) {
                throw new RuntimeException("Column is empty");
            }
        }

        @Override
        public boolean hasNext() {
            if (hasNoNext) {
                return false;
            }
            if (yIterator.hasNext()) {
                return true;
            }
            return xIterator.hasNext();
        }

        @Override
        public Cell next() {
            if (yIterator.hasNext()) {
                return column.get(yIterator.next());
            }
            column = grid.get(xIterator.next());
            yIterator = column.keySet().iterator();
            return column.get(yIterator.next());
        }
    }
}
