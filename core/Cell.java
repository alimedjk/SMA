package core;

import entities.*;
import java.util.ArrayList;
import java.util.List;

import agents.Agent;

/** Une cellule de la grille contenant objets et références */
public class Cell {
    public final int x, y;
    public Obstacle obstacle;
    public Treasure treasure;
    public HQ hq;
    public final List<Agent> agents = new ArrayList<>();  // hold Agent instances (use Object to avoid cycles here)
    public final List<Animal> animals = new ArrayList<>();
    public double signal = 0.0;

    public Cell(int x, int y) { this.x = x; this.y = y; }

    public void clear() {
        obstacle = null;
        treasure = null;
        hq = null;
        agents.clear();
        animals.clear();
        signal = 0.0;
    }
}

