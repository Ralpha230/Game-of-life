package gameOfLife.core;

public class Model {

    private final Grid grid;
    private final WorldMachine worldMachine;

    public Model(Grid grid, WorldMachine worldMachine) {
        this.grid = grid;
        this.worldMachine = worldMachine;
    }

    public Grid getGrid() {
        return grid;
    }

    public WorldMachine getWorldMachine() {
        return worldMachine;
    }
}
