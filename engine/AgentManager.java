package engine;

import data.*;

public class AgentManager extends Thread {
    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    private Agent agent;

    private Environment environment;

    private EnvironmentManager environmentManager;
    protected Treasure treasure;


    private boolean running = true;

    public AgentManager(Agent agent, Environment environment, EnvironmentManager environmentManager) {
        this.agent = agent;
        this.environment = environment;
        this.environmentManager = environmentManager;
    }

    public Treasure getTreasure() {
        return treasure;
    }

    @Override
    public void run() {

        while (running) {
            Utility.unitTime();
            Utility.unitTime();
            agent.process(treasure, environmentManager, environment);

            // Vérifiez si l'explorateur est mort après le mouvement
            if (agent.getHealth() <= 0) {
                System.out.println("L'explorateur est mort.");
                ExplorerFactory.setExplorer(agent, environment); // Reset de la position
            }
        }
    }


    public Agent getAgent() {
        return agent;
    }

    public void setTreasure(Treasure treasure){
        this.treasure = treasure;
    }
}
