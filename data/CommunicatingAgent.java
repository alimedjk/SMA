package data;

import engine.EnvironmentManager;
import engine.AgentManager;

public class CommunicatingAgent extends Agent {
    public static final int COMMUNICATIVE_HEALTH = 100;
    public static final int COMMUNICATIVE_STRENGTH = 80;

    public CommunicatingAgent() {
        super(COMMUNICATIVE_HEALTH, COMMUNICATIVE_STRENGTH);
    }

    public int getTypeAgent() {
        return Agent.COMMUNICATIVE_AGENT;
    }

    @Override
    public void process(Treasure treasure, EnvironmentManager environmentManager, Environment environment) {
        if (treasure != null) {
            inform(treasure); // Transmet les positions au cognitif
            for (AgentManager agentManager : environmentManager.getExplorerManagers()){
                if (agentManager.getTreasure() == null
                        && !environmentManager.getAffectedTreasures().contains(treasure)){
                    agentManager.setTreasure(treasure);
                    environmentManager.addAffectedTreasure(treasure);
                    break;
                }
            }
        }
    }

    private void inform(Treasure treasure) {
        System.out.println("Cognitif a reçu les positions des trésors : " + treasure.getBlock());
    }

}
