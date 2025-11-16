package gameOfLife.core;

import java.util.Collection;
import java.util.HashSet;

public class WorldMachine extends Thread {

	private Grid grid;
	private long genDuration;

	public WorldMachine(Grid grid, long genDuration) {
		this.grid = grid;
		this.genDuration = genDuration;
		this.listeners = new HashSet<>();
	}

	public Grid grid() {
		return grid;
	}

	private static int i = 0;

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
