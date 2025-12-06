package data;

import engine.EnvironmentManager;
import engine.AgentManager;
import engine.Utility;

import java.util.ArrayList;
import java.util.List;

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
        Block currentBlock = this.getBlock(); // Bloc actuel de l'explorateur

        // Obtenir la zone actuelle
        int currentZone = Utility.getZoneByBlock(currentBlock);
        Block[][] blocksInZone = Utility.getBlocksByZone(environment, currentZone);

        //System.out.println("Zone " + currentZone);
       // System.out.println("taille col" + blocksInZone[0].length);
        //System.out.println("taille row" + blocksInZone.length);

        ArrayList<Agent> agents = new ArrayList<>();
        ArrayList<Treasure> treasures = new ArrayList<>();

        // Parcourir les blocs de la zone pour trouver les trésors
        for (int i = 0; i < blocksInZone.length; i++) {
            for (int j = 0; j < blocksInZone[i].length; j++) {
                Block block = blocksInZone[i][j];
                EnvironmentElement element = Utility.getElementFromBlock(environment, block);
                if (element != null) {
                    if (element instanceof Treasure) {
                        treasures.add((Treasure) element);
                    }

                    if (element instanceof Agent &&  ! (element instanceof CommunicatingAgent)) {
                        agents.add((Agent) element);
                    }
                }
            }
        }
        //System.out.println("agents " + agents.size());

        int compteur = 0;
        for (AgentManager agentManager : environmentManager.getExplorerManagers()) {
            for (Agent agent : agents) {
                if (agentManager.getAgent().equals(agent)) {
                    if (compteur < treasures.size()) {
                        System.out.println("madji");
                        Treasure treas = treasures.get(compteur);
                        inform(treas);
                        if (agentManager.getTreasure() == null
                                && !environmentManager.getAffectedTreasures().contains(treas)) {
                            agentManager.setTreasure(treas);
                            environmentManager.addAffectedTreasure(treas);

                            compteur++;
                            break;
                        }
                    }
                }
            }
        }
    }

    private void inform(Treasure treasure) {
        System.out.println("Cognitif a reçu les positions des trésors : " + treasure.getBlock());
    }

    private Treasure scanForTreasure(Environment environment) {
        Block currentBlock = this.getBlock(); // Bloc actuel de l'explorateur

        // Obtenir la zone actuelle
        int currentZone = Utility.getZoneByBlock(currentBlock);
        Block[][] blocksInZone = Utility.getBlocksByZone(environment, currentZone);

        // Parcourir les blocs de la zone pour trouver les trésors
        for (int i = 0; i < blocksInZone.length; i++) {
            for (int j = 0; j < blocksInZone[i].length; j++) {
                Block block = blocksInZone[i][j];
                EnvironmentElement element = Utility.getElementFromBlock(environment, block);
                if (element != null && element instanceof Treasure) { // Vérifie si l'élément est un trésor
                    System.out.println("Communicatif : Trésor trouvé à " + block);
                    return (Treasure)element; // Ajoute la position du trésor
                }
            }
        }

        return null;
    }
}
