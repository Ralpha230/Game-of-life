package gameOfLife.core;

import java.util.Collection;
import java.util.HashSet;

public class WorldMachine {

    private final Grid grid;
    private long genDuration;
    private final Thread t;

    public WorldMachine(Grid grid, long genDuration) {
        this.grid = grid;
        this.genDuration = genDuration;
        this.listeners = new HashSet<>();
        this.t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(genDuration);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    grid.generate();
                    for (GenerationListener l : listeners) {
                        l.newGeneration();
                        i++;
                    }
                }
            }
        });
    }

    public void start() {
        t.start();
    }

    public Grid grid() {
        return grid;
    }

    private static int i = 0;

    // Subscription

    Collection<GenerationListener> listeners;

    public interface GenerationListener {

        /*
         * This method is called whenever the machine updates its grid
         */
        void newGeneration();
    }

    public void setListener(GenerationListener l) {
        listeners.add(l);
    }

}
