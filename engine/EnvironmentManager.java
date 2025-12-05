package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import data.*;
import config.*;

public class EnvironmentManager {

    private Environment environment;

    private ArrayList<ExplorerManager> explorerManagers;
    private List<Explorer> explorers;
    private Random random;

    private ArrayList<Treasure> affectedTreasures = new ArrayList<>();

    private AtomicInteger nbRounds = new AtomicInteger(0);
    private AtomicInteger nbCollectedTreasures = new AtomicInteger(0);
    private AtomicInteger nbCombats = new AtomicInteger(0);

    /** NOUVEAU : états de la simulation */
    private volatile boolean paused = false;
    private volatile boolean stopped = false;

    public EnvironmentManager(Environment environment) {
        this.environment = environment;
        this.explorers = new ArrayList<>();
        this.explorerManagers = GameBuilder.buildInitMobile(environment, this);
        this.random = new Random();
    }

    // ----------- GETTERS / SETTERS DES NOUVEAUX ÉTATS -----------

    public boolean isPaused() {
        return paused;
    }

    //public void setPaused(boolean paused) {
    //    this.paused = paused;
    //}

    public boolean isStopped() {
        return stopped;
    }

    //public void stopAll() {
    //    this.stopped = true;
    //}
    public void setPaused(boolean p) {
        for (ExplorerManager em : explorerManagers) {
            em.setPaused(p);
        }
    }

    public void stopAll() {
        for (ExplorerManager em : explorerManagers) {
            em.stopThread();
        }
    }

    // --------------------------------------------------------------

    public ArrayList<Treasure> getAffectedTreasures() {
        return affectedTreasures;
    }

    public void addAffectedTreasure(Treasure treasure) {
        affectedTreasures.add(treasure);
    }

    public ArrayList<ExplorerManager> getExplorerManagers() {
        return explorerManagers;
    }

    // ------------------- ROUNDS --------------------

    public void increaseNbRounds() {
        this.nbRounds.incrementAndGet();
    }

    public int getNbRounds() {
        return nbRounds.get();
    }

    public void resetNbRounds() {
        nbRounds.set(0);
    }

    // ------------------- STATISTIQUES --------------------

    public int getNbCollectedTreasures() {
        return nbCollectedTreasures.get();
    }

    public void increaseNbCollectedTreasures() {
        nbCollectedTreasures.incrementAndGet();
    }

    public int getNbCombats() {
        return nbCombats.get();
    }

    public void increaseNbCombats() {
        nbCombats.incrementAndGet();
    }

    // -------------------- COMBAT (inchangé) -----------------------
    public void fight(Explorer explorer, Animal animal) {
        Random random = new Random();
        int currentRound = 1;

        System.out.println("Début du combat entre l'explorateur et l'animal !");

        while (currentRound <= GameConfig.NbRounds) {
            System.out.println("Tour " + currentRound + "/" + GameConfig.NbRounds);

            boolean explorerAttacksFirst = random.nextBoolean();

            if (explorerAttacksFirst) {
                int damageToAnimal = random.nextInt(30) + 20;
                animal.setHealth(animal.getHealth() - damageToAnimal);
                System.out.println("Explorateur inflige " + damageToAnimal);
            } else {
                int damageToExplorer = random.nextInt(30) + 20;
                explorer.setHealth(explorer.getHealth() - damageToExplorer);
                System.out.println("Animal inflige " + damageToExplorer);
            }

            if (explorer.getHealth() <= 0) {
                explorers.remove(explorer);
                break;
            }
            if (animal.getHealth() <= 0) {
                environment.getElements().remove(animal);
                environment.getElementsByBlocks().remove(animal.getBlock());
                break;
            }

            currentRound++;
        }

        if (currentRound > GameConfig.NbRounds) {
            System.out.println("Combat terminé sans vainqueur.");
        }
    }
}
