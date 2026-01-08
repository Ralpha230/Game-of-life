package gameOfLife.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class WorldMachine {

    private final Grid grid;
    private final Thread t;
    private boolean paused = false;
    private Semaphore pauseSemaphore = new Semaphore(1, true);

    public WorldMachine(Grid grid, long genDuration) {
        this.grid = grid;
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
                    try {
                        pauseSemaphore.acquire();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    grid.generate();
                    for (GenerationListener l : listeners) {
                        l.newGeneration();
                        i++;
                    }
                    pauseSemaphore.release();
                }
            }
        });
    }

    public void start() {
        t.start();
    }

    public void pause() {
        if (paused) {
            pauseSemaphore.release();
            paused = false;
            return;
        }
        try {
            pauseSemaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        paused = true;
    }

    public boolean isPaused() {
        return paused;
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
