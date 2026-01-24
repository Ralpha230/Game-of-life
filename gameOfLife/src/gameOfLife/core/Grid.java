package gameOfLife.core;

import java.awt.Point;
import java.util.*;
import java.util.concurrent.Semaphore;

import gameOfLife.utils.Pair;

public class Grid {

    // Multithreading
    private static final int nbThreads = Runtime.getRuntime().availableProcessors();
    Set<Thread> threads = new HashSet<>();
    private final Semaphore vatMutex = new Semaphore(1);

    private final CellSet cells = new CellSet();
    private List<Point> vat = new LinkedList<Point>();
    private Set<Cell> res = new HashSet<>();

    public Grid() {
    }

    private long generation = 0;

    /*
     * Updates the grid to hold the next generation
     */
    synchronized void generate() {
        for (int i = 0; i < nbThreads; i++) {
            threads.add(new Thread(() -> {
                while (true) {
                    try {
                        vatMutex.acquire();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (vat.isEmpty()) {
                        vatMutex.release();
                        return;
                    }
                    Point p = vat.removeFirst();
                    vatMutex.release();
                    switch (neighbours(p).size()) {
                        case 0:
                        case 1:
                            break;
                        case 2:
                            synchronized (cells) {
                                if (cells.contains(new Cell(p))) {
                                    synchronized (res) {
                                        res.add(new Cell(p));
                                    }
                                }
                            }
                            break;
                        case 3:
                            synchronized (res) {
                                res.add(new Cell(p));
                            }
                            break;
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                            break;
                        default:
                            throw new RuntimeException("Unexpected number of neighbours");
                    }
                }
            }));
        }

        for (Cell cell : cells) {
            vat.add(cell.pos());
            int x_c = cell.pos().x;
            int y_c = cell.pos().y;
            for (int x = x_c - 1; x < x_c + 2; x++) {
                for (int y = y_c - 1; y < y_c + 2; y++) {
                    vat.add(new Point(x, y));
                }
            }
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        cells.clear();
        cells.addAll(res);
        res = new HashSet<>();
        threads = new HashSet<>();
        generation++;
    }


    /*
     * Return the collection of cells around p (except on p)
     */
    public Set<Cell> neighbours(Point p) {

        Set<Cell> res = new HashSet<>();
        for (int x = p.x - 1; x < p.x + 2; x++) {
            for (int y = p.y - 1; y < p.y + 2; y++) {
                if (x == p.x && y == p.y) {
                    continue;
                }
                Cell candidate = new Cell(new Point(x, y));
                if (cells.contains(candidate)) {
                    res.add(candidate);
                }
            }
        }

        return res;
    }

    public Set<Cell> cells() {
        return cells;
    }

    /*
     * Returns a pair containing respectively the top left and bottom right corner
     * of the grid
     */
    public Pair<Point, Point> corners() {
        if (cells.isEmpty()) {
            return new Pair<Point, Point>(new Point(0, 0), new Point(0, 0));
        }

        Point topLeft = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Point bottomRight = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
        for (Cell c : cells) {
            int x = c.pos().x;
            int y = c.pos().y;
            if (x < topLeft.x) {
                topLeft.x = x;
            }
            if (y < topLeft.y) {
                topLeft.y = y;
            }
            if (bottomRight.x < x) {
                bottomRight.x = x;
            }
            if (bottomRight.y < y) {
                bottomRight.y = y;
            }
        }

        return new Pair<Point, Point>(topLeft, bottomRight);
    }

    public void addCells(Set<Cell> cells) {
        cells.addAll(cells);
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    public boolean isAlive(Point pos) {
        return cells.contains(new Cell(pos));
    }

    public long generation() {
        return generation;
    }

}