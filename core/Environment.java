package core;

import entities.*;
import agents.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Environment central: grille, liste d'agents/animaux, règles de simulation.
 */
public class Environment {
    public final int cols, rows;
    public final Cell[][] grid;
    public HQ hq;
    public final List<Agent> agents = Collections.synchronizedList(new ArrayList<>());
    public final List<Animal> animals = Collections.synchronizedList(new ArrayList<>());
    public final AtomicInteger iterations = new AtomicInteger(0);
    public final AtomicInteger combats = new AtomicInteger(0);
    public final AtomicInteger treasuresCollected = new AtomicInteger(0);

    private final Random rnd = new Random(0); // deterministic seed for repeatability

    // zones capacity tracking
    private final int[] zoneCounts;

    public Environment(int cols, int rows) {
        this.cols = cols; this.rows = rows;
        grid = new Cell[cols][rows];
        for (int x=0;x<cols;x++) for (int y=0;y<rows;y++) grid[x][y] = new Cell(x,y);
        zoneCounts = new int[Config.ZONES];
        reset();
    }

    public Cell cell(int x,int y){ return grid[x][y]; }
    public Cell cell(Position p){ return grid[p.x][p.y]; }
    public boolean inBounds(int x,int y){ return x>=0 && y>=0 && x<cols && y<rows; }

    public boolean cellEmpty(int x,int y){
        if (!inBounds(x,y)) return false;
        Cell c = cell(x,y);
        return c.hq==null && c.obstacle==null && c.treasure==null && c.animals.isEmpty() && c.agents.isEmpty();
    }

    // zone index by x coordinate (vertical bands)
    public int zoneIndex(Position p) {
        int zoneWidth = Math.max(1, cols / Config.ZONES);
        int idx = Math.min(Config.ZONES - 1, p.x / zoneWidth);
        return idx;
    }

    public List<Position> neighbors(Position p) {
        List<Position> nb = new ArrayList<>();
        int[] dx = {-1,1,0,0}; int[] dy = {0,0,-1,1};
        for (int i=0;i<4;i++){
            int nx = p.x + dx[i], ny = p.y + dy[i];
            if (inBounds(nx,ny) && cell(nx,ny).obstacle==null) nb.add(new Position(nx,ny));
        }
        return nb;
    }

    public synchronized void reset() {
        // clear grid
        for (int x=0;x<cols;x++) for (int y=0;y<rows;y++) grid[x][y].clear();
        agents.clear();
        animals.clear();
        Arrays.fill(zoneCounts, 0);
        iterations.set(0); combats.set(0); treasuresCollected.set(0);

        // place HQ at top-left (0,0) as requested
        hq = new HQ(0, 0);
        cell(hq.pos).hq = hq;

        // obstacles
        for (int i=0;i<Config.INITIAL_OBSTACLES;i++){
            int x,y; do { x = rnd.nextInt(cols); y = rnd.nextInt(rows); } while (!cellEmpty(x,y));
            cell(x,y).obstacle = new Obstacle(x,y);
        }

        // treasures
        for (int i=0;i<Config.INITIAL_TREASURES;i++){
            int x,y; do { x = rnd.nextInt(cols); y = rnd.nextInt(rows); } while (!cellEmpty(x,y));
            cell(x,y).treasure = new Treasure(x,y);
        }

        // animals (they are threads, created but not started here)
        for (int i=0;i<Config.INITIAL_ANIMALS;i++){
            int x,y; do { x = rnd.nextInt(cols); y = rnd.nextInt(rows); } while (!cellEmpty(x,y));
            Animal a = new Animal(x,y,this);
            animals.add(a);
            cell(x,y).animals.add(a);
        }

        // cognitive agents at HQ
        for (int i=0;i<Config.INITIAL_COGNITIVE;i++){
            CognitiveAgent c = new CognitiveAgent(hq.pos.x, hq.pos.y, this, "Cog-"+(i+1));
            agents.add(c);
            cell(c.pos).agents.add(c);
        }
        // reactive
        for (int i=0;i<Config.INITIAL_REACTIVE;i++){
            ReactiveAgent r = new ReactiveAgent(hq.pos.x, hq.pos.y, this, "React-"+(i+1));
            agents.add(r);
            cell(r.pos).agents.add(r);
        }
        // communicators: place respecting zone capacity Config.COMM_PER_ZONE
        int attempts = 0;
        int commToPlace = Math.min(Config.INITIAL_COMM_HINT, Config.ZONES * Config.COMM_PER_ZONE);
        while (commToPlace > 0 && attempts < 1000) {
            // choose random zone then a random coordinate inside that zone
            int zone = rnd.nextInt(Config.ZONES);
            if (zoneCounts[zone] >= Config.COMM_PER_ZONE) { attempts++; continue; }
            // pick x within zone
            int zoneWidth = Math.max(1, cols / Config.ZONES);
            int x0 = zone * zoneWidth;
            int x1 = Math.min(cols-1, x0 + zoneWidth - 1);
            int x = x0 + rnd.nextInt(Math.max(1, x1 - x0 + 1));
            int y = rnd.nextInt(rows);
            if (!cellEmpty(x,y)) { attempts++; continue; }
            CommunicatingAgent com = new CommunicatingAgent(x,y,this,"Comm-zone"+zone+"#"+(zoneCounts[zone]+1));
            agents.add(com);
            cell(com.pos).agents.add(com);
            zoneCounts[zone]++;
            commToPlace--;
            attempts++;
        }
    }

    public synchronized void step() {
        iterations.incrementAndGet();
        // animals act (copy to avoid concurrent modification)
        List<Animal> animalsCopy = new ArrayList<>(animals);
        for (Animal a : animalsCopy) {
            // animals are threads; we keep step for safe sequential fallback
            a.step();
        }

        // agents normally run in their own threads; keep this for fallback actions
        //List<Agent> agentsCopy = new ArrayList<>(agents);
        //for (Agent ag : agentsCopy) {
            // if agent threads are not active, we can call step() here.
            // but primary model uses threads.
        //}

        // decay signals lightly
        for (int x=0;x<cols;x++) for (int y=0;y<rows;y++){
            Cell c = cell(x,y);
            c.signal *= 0.95;
            if (c.signal < 0.01) c.signal = 0.0;
        }
    }

    public synchronized void animalAttacks(Animal a, Agent ag) {
        // If target is a CommunicatingAgent -> ignore (invincible)
        if (ag instanceof CommunicatingAgent) return;
        combats.incrementAndGet();
        // teleport to HQ (as requested: choice 1)
        cell(ag.pos).agents.remove(ag);
        ag.pos = new Position(hq.pos.x, hq.pos.y);
        cell(ag.pos).agents.add(ag);
        ag.onBeaten();
    }

    public synchronized boolean pickTreasure(Agent ag) {
        Cell c = cell(ag.pos);
        if (c.treasure != null) {
            c.treasure = null;
            treasuresCollected.incrementAndGet();
            return true;
        }
        return false;
    }
}

