package agents;



import core.*;
import core.Position;
import core.Environment;

//import java.util.List;
//import java.util.Random;

/**
 * Agent communiquant: immobile, émet un signal pour guider les réactifs.
 * Invincible (les animaux ne l'attaquent pas).
 */
public class CommunicatingAgent extends Agent {
    //private final Random rnd = new Random();

    public CommunicatingAgent(int x,int y, Environment env, String id){ super(x,y,env,id); }

    @Override
    public void step() {
        // immobile: do not call moveTo
        synchronized(env) {
            Cell c = env.cell(pos);
            c.signal = Math.min(1.0, c.signal + 1.0);
            for (Position n : neighbors()) {
                env.cell(n).signal = Math.max(env.cell(n).signal, 0.5);
            }
        }
        // no movement (immobile). Sleep handled by Thread.run
    }
}

