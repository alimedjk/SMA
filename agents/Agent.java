package agents;

import core.*;

import core.Position;
import core.Environment;

import java.util.List;

/**
 * Agent abstrait qui est aussi un thread.
 */
public abstract class Agent extends Thread {
    public volatile boolean active = true;
    public Position pos;
    protected final Environment env;
    public final String id;
    public int collected = 0;

    public Agent(int x,int y, Environment env, String id){
        this.pos = new Position(x,y);
        this.env = env;
        this.id = id;
        setName(id);
    }

    // to implement agent behavior once per tick
    public abstract void step();

    protected boolean moveTo(Position np) {
        if (!env.inBounds(np.x,np.y)) return false;
        synchronized(env) {
            if (env.cell(np).obstacle != null) return false;
            env.cell(pos).agents.remove(this);
            pos = np;
            env.cell(pos).agents.add(this);
            return true;
        }
    }

    protected List<Position> neighbors() { return env.neighbors(pos); }

    public void onBeaten() {
        // default no-op, subclasses can override
    }

    // Thread run loop
    @Override
    public void run() {
        while (active && !Thread.currentThread().isInterrupted()) {
            try {
                step();
                Thread.sleep(Config.AGENT_SLEEP_MS);
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

