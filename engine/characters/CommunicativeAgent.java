package engine.characters;

import java.util.ArrayList;
import engine.mapElements.Arena;
import engine.mapElements.ArenaElement;
import engine.mapElements.Block;
import engine.mapElements.Treasure;
import engine.process.AgentManager;
import engine.process.ArenaManager;
import engine.process.Utility;

public class CommunicativeAgent extends Agent {
    public static final int COMMUNICATIVE_HEALTH = 100;
    public static final int COMMUNICATIVE_STRENGTH = 80;


    public CommunicativeAgent() {
        super(COMMUNICATIVE_HEALTH, COMMUNICATIVE_STRENGTH);
    }

    public int getTypeAgent() {
        return Agent.COMMUNICATIVE_AGENT;
    }

    @Override
    public void process(Treasure treasure, ArenaManager arenaManager, Arena arena) {
        Block currentBlock = this.getBlock();

        int currentZone = Utility.getZoneByBlock(currentBlock);
        Block[][] blocksInZone = Utility.getBlocksByZone(arena, currentZone);

        //System.out.println("Zone " + currentZone);
       // System.out.println("taille col" + blocksInZone[0].length);
        //System.out.println("taille row" + blocksInZone.length);

        ArrayList<Agent> agents = new ArrayList<>();
        ArrayList<Treasure> treasures = new ArrayList<>();

        for (int i = 0; i < blocksInZone.length; i++) {
            for (int j = 0; j < blocksInZone[i].length; j++) {
                Block block = blocksInZone[i][j];
                ArenaElement element = Utility.getElementFromBlock(arena, block);
                if (element != null) {
                    if (element instanceof Treasure) {
                        treasures.add((Treasure) element);
                    }

                    if (element instanceof Agent &&  ! (element instanceof CommunicativeAgent)) {
                        agents.add((Agent) element);
                    }
                }
            }
        }

        int compteur = 0;
        for (AgentManager agentManager : arenaManager.getAgentManagers()) {
            for (Agent agent : agents) {
                if (agentManager.getAgent().equals(agent)) {
                    if (compteur < treasures.size()) {
                        Treasure treas = treasures.get(compteur);
                        if (agentManager.getTreasure() == null
                                && !arenaManager.getAffectedTreasures().contains(treas)) {
                            agentManager.setTreasure(treas);
                            arenaManager.addAffectedTreasure(treas);
                            compteur++;
                            break;
                        }
                    }
                }
            }
        }
    }

}
