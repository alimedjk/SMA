package agents;

import core.Position;
import core.Environment;

import java.util.*;

/**
 * Agent cognitif : recherche BFS vers trésor le plus proche.
 */
public class CognitiveAgent extends Agent {
    private LinkedList<Position> plan = new LinkedList<>();
    private final Random rnd = new Random();

    public CognitiveAgent(int x,int y, Environment env, String id){ super(x,y,env,id); }

    private void buildPlanToNearestTreasure() {
        boolean[][] seen = new boolean[env.cols][env.rows];
        Queue<Position> q = new ArrayDeque<>();
        Map<Position,Position> parent = new HashMap<>();
        q.add(pos); seen[pos.x][pos.y] = true;
        Position found = null;

        while (!q.isEmpty()) {
            Position p = q.poll();
            if (env.cell(p).treasure != null) { found = p; break; }
            for (Position n : env.neighbors(p)) {
                if (!seen[n.x][n.y]) {
                    seen[n.x][n.y] = true;
                    parent.put(n,p);
                    q.add(n);
                }
            }
        }

        plan.clear();
        if (found != null) {
            Position cur = found;
            LinkedList<Position> rev = new LinkedList<>();
            while (!cur.equals(pos)) {
                rev.add(cur);
                cur = parent.get(cur);
            }
            Collections.reverse(rev);
            plan.addAll(rev);
        }
    }

    @Override
    public void step() {
        if (plan.isEmpty() || (plan.peekLast() != null && env.cell(plan.peekLast()).treasure == null)) {
            buildPlanToNearestTreasure();
        }
        if (!plan.isEmpty()) {
            Position next = plan.poll();
            moveTo(next);
            if (env.pickTreasure(this)) collected++;
        } else {
            // simple wandering
            List<Position> nb = neighbors();
            if (!nb.isEmpty()) moveTo(nb.get(rnd.nextInt(nb.size())));
            if (env.pickTreasure(this)) collected++;
        }
    }
}
