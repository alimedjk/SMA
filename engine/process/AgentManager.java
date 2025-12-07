package engine.process;

import engine.characters.Agent;
import engine.mapElements.*;

public class AgentManager extends Thread {
    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    private Agent agent;

    private Arena arena;

    private ArenaManager arenaManager;
    protected Treasure treasure;

    private volatile boolean paused = false;
    private volatile boolean stopped = false;

    private boolean running = true;

    public AgentManager(Agent agent, Arena arena, ArenaManager arenaManager) {
        this.agent = agent;
        this.arena = arena;
        this.arenaManager = arenaManager;
    }

    public Treasure getTreasure() {
        return treasure;
    }

    @Override
    public void run() {

        while (!stopped) {

            while (paused && !stopped) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException ignored) {}
            }
            if (stopped) break;

            Utility.unitTime();
            Utility.unitTime();
            agent.process(treasure, arenaManager, arena);

            if (agent.getHealth() <= 0) {
                //System.out.println("L'explorateur est mort.");
                AgentFactory.setAgent(agent, arena); // Reset de la position
            }
        }
    }


    public Agent getAgent() {
        return agent;
    }

    public void setTreasure(Treasure treasure){
        this.treasure = treasure;
    }

    public void setPaused(boolean p) {
        this.paused = p;
    }

    public void stopThread() {
        this.stopped = true;
    }
}
