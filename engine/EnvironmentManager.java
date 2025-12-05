package engine;

import java.util.concurrent.atomic.AtomicInteger;

import data.*;
import config.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class EnvironmentManager {
    private Environment environment;

    private ArrayList<AgentManager> agentManagers;

    private List<Agent> agents;
    private Random random;

    private ArrayList<Treasure> affectedTreasures = new ArrayList<>();

    private AtomicInteger nbRounds = new AtomicInteger(0);

    private AtomicInteger nbCollectedTreasures = new AtomicInteger(0);
    private AtomicInteger nbCombats = new AtomicInteger(0);

    public EnvironmentManager(Environment environment) {
        this.environment = environment;
        this.agents = new ArrayList<>();
        this.agentManagers = GameBuilder.buildInitMobile(environment, this);
        this.random = new Random();
    }

    public ArrayList<Treasure> getAffectedTreasures() {
        return affectedTreasures;
    }

    public void addAffectedTreasure(Treasure treasure) {
        affectedTreasures.add(treasure);
    }

    public void fight(Agent agent, Animal animal) {
        Random random = new Random();
        int currentRound = 1;

        System.out.println("Début du combat entre l'explorateur et l'animal !");

        while (currentRound <= GameConfig.NbRounds) { // Utilisation de la constante
            System.out.println("Tour " + currentRound + "/" + GameConfig.NbRounds);

            boolean explorerAttacksFirst = random.nextBoolean(); // Détermine qui attaque en premier ce tour

            if (explorerAttacksFirst) {
                // L'explorateur attaque
                int damageToAnimal = random.nextInt(30) + 20; // Dégâts entre 20 et 50
                animal.setHealth(animal.getHealth() - damageToAnimal);
                System.out.println("L'explorateur attaque et inflige " + damageToAnimal + " points de dégâts à l'animal.");
            } else {
                // L'animal attaque
                int damageToExplorer = random.nextInt(30) + 20; // Dégâts entre 20 et 50
                agent.setHealth(agent.getHealth() - damageToExplorer);
                System.out.println("L'animal attaque et inflige " + damageToExplorer + " points de dégâts à l'explorateur.");
            }

            // Vérifiez si l'explorateur est mort
            if (agent.getHealth() <= 0) {
                System.out.println("L'explorateur est mort.");
                agents.remove(agent); // Supprimer de la liste des explorateurs
                break; // Arrête le combat
            }

            // Vérifiez si l'animal est mort
            if (animal.getHealth() <= 0) {
                System.out.println("L'animal est mort.");
                this.environment.getElements().remove(animal); // Retirer l'animal de l'environnement
                environment.getElementsByBlocks().remove(animal.getBlock());
                break; // Arrête le combat
            }

            currentRound++;
        }

        if (currentRound > GameConfig.NbRounds) {
            System.out.println("Le combat se termine après " + GameConfig.NbRounds + " tours.");
        }

        // Vérification finale après tous les tours
        if (agent.getHealth() > 0 && animal.getHealth() > 0) {
            System.out.println("Le combat s'est terminé sans vainqueur !");
        }
    }

    public ArrayList<AgentManager> getExplorerManagers() {
        return agentManagers;
    }

    public void increaseNbRounds() {
        this.nbRounds.incrementAndGet();
    }

    public int getNbRounds() {
        return nbRounds.get();
    }

    public int getNbCollectedTreasures() {
        return nbCollectedTreasures.get();
    }

    public void increaseNbCollectedTreasures(){
        nbCollectedTreasures.incrementAndGet();
    }

    public int getNbCombats() {
        return nbCombats.get();
    }
    public void increaseNbCombats(){
        nbCombats.incrementAndGet();
    }
}
