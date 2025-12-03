package agents;


import core.Position;
import core.Environment;

import java.util.List;
import java.util.Random;

/**
 * Agent réactif: règles simples (fuite, suivre signal, ramasser).
 */
public class ReactiveAgent extends Agent {
    private final Random rnd = new Random();

    public ReactiveAgent(int x,int y, Environment env, String id){ super(x,y,env,id); }

    @Override
    public void step() {
        // Rule 1: if animal adjacent -> go to HQ (flee)
        for (Position n : neighbors()) {
            if (!env.cell(n).animals.isEmpty()) {
                moveTo(env.hq.pos);
                return;
            }
        }
        // Rule 2: if treasure here -> pick
        if (env.pickTreasure(this)) { collected++; return; }

        // Rule 3: follow local signal gradient
        Position best = null; double bestSignal = Double.NEGATIVE_INFINITY;
        for (Position n : neighbors()) {
            double s = env.cell(n).signal;
            if (s > bestSignal) { bestSignal = s; best = n; }
        }
        if (bestSignal > 0 && best != null) {
            moveTo(best);
            if (env.pickTreasure(this)) collected++;
            return;
        }

        // Random walk
        List<Position> nb = neighbors();
        if (!nb.isEmpty()) moveTo(nb.get(rnd.nextInt(nb.size())));
        if (env.pickTreasure(this)) collected++;
    }
}

