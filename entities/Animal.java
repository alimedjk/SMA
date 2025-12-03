package entities;

import core.*;
import agents.Agent;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Animal : marche aléatoire et attaque agents.
 * Animal est aussi un thread.
 */
public class Animal extends Thread {
    public Position pos;
    private final Environment env;
    private final Random rnd = new Random();
    private volatile boolean active = true;

    public Animal(int x,int y, Environment env) {
        this.pos = new Position(x,y);
        this.env = env;
        setName("Animal-"+x+"x"+y);
    }

    public void step() {
        int[] dx = {-1,1,0,0};
        int[] dy = {0,0,-1,1};
        List<Position> possible = new ArrayList<>();
        for (int i=0;i<4;i++){
            int nx = pos.x + dx[i], ny = pos.y + dy[i];
            if (env.inBounds(nx,ny) && env.cell(nx,ny).obstacle==null) possible.add(new Position(nx,ny));
        }
        if (!possible.isEmpty()) {
            Position np = possible.get(rnd.nextInt(possible.size()));
            synchronized(env) {
                env.cell(pos).animals.remove(this);
                pos = np;
                env.cell(pos).animals.add(this);
                // attack agents in the same cell
                if (!env.cell(pos).agents.isEmpty()) {
                    for (Agent ag : new ArrayList<>(env.cell(pos).agents)) {
                        env.animalAttacks(this, ag);
                    }
                } else {
                    // check adjacent cells for agents (attack first found non-communicating)
                    for (Position n : env.neighbors(pos)) {
                        if (!env.cell(n).agents.isEmpty()) {
                            for (Agent ag : new ArrayList<>(env.cell(n).agents)) {
                                env.animalAttacks(this, ag);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        while (active && !Thread.currentThread().isInterrupted()) {
            try {
                step();
                Thread.sleep(Config.ANIMAL_SLEEP_MS);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void requestStop() {
        active = false;
        interrupt();
    }
}
