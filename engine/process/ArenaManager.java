package engine.process;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

import config.*;
import engine.characters.Agent;
import engine.characters.Animal;
import engine.mapElements.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ArenaManager {
    private Arena arena;

    private ArrayList<AgentManager> agentManagers;

    private List<Agent> agents;
    private Random random;

    private ArrayList<Treasure> affectedTreasures = new ArrayList<>();

    private AtomicInteger nbRounds = new AtomicInteger(0);

    private AtomicInteger nbCollectedTreasures = new AtomicInteger(0);
    private AtomicInteger nbCombats = new AtomicInteger(0);

    private AtomicInteger nbAnimalsDead = new AtomicInteger(0);
    private AtomicInteger nbAgentDead = new AtomicInteger(0);
    Set<Integer> nbZoneExplored = new ConcurrentSkipListSet<>();

    public ArenaManager(Arena arena) {
        this.arena = arena;
        this.agents = new ArrayList<>();
        this.agentManagers = GameBuilder.buildAgents(arena, this);
        this.random = new Random();
    }

    public ArrayList<Treasure> getAffectedTreasures() {
        return affectedTreasures;
    }

    public void addAffectedTreasure(Treasure treasure) {
        affectedTreasures.add(treasure);
    }

    public void setPaused(boolean p) {
        for (AgentManager em : agentManagers) {
            em.setPaused(p);
        }
    }

    public void stopAll() {
        for (AgentManager em : agentManagers) {
            em.stopThread();
        }
    }


    public void fight(Agent agent, Animal animal) {
        Random random = new Random();
        int currentRound = 1;

        //System.out.println("Début du combat entre l'agent et l'animal !");

        while (currentRound <= GameConfig.NbRounds) {
            //System.out.println("Tour " + currentRound + "/" + GameConfig.NbRounds);

            boolean explorerAttacksFirst = random.nextBoolean();

            if (explorerAttacksFirst) {
                int damageToAnimal = random.nextInt(30) + 20;
                animal.setHealth(animal.getHealth() - damageToAnimal);
                //System.out.println("L'agent attaque et inflige " + damageToAnimal + " points de dégâts à l'animal.");
            } else {
                int damageToExplorer = random.nextInt(30) + 20; // Dégâts entre 20 et 50
                agent.setHealth(agent.getHealth() - damageToExplorer);
               // System.out.println("L'animal attaque et inflige " + damageToExplorer + " points de dégâts à l'agent.");
            }

            if (agent.getHealth() <= 0) {
                //System.out.println("L'explorateur est mort.");
                agents.remove(agent);
                break;
            }

            if (animal.getHealth() <= 0) {
                //System.out.println("L'animal est mort.");
                this.arena.getElements().remove(animal);
                arena.getElementsByBlocks().remove(animal.getBlock());
                break;
            }
            currentRound++;
        }

       /* if (currentRound > GameConfig.NbRounds) {
            System.out.println("Le combat se termine après " + GameConfig.NbRounds + " tours.");
        }

        if (agent.getHealth() > 0 && animal.getHealth() > 0) {
            System.out.println("Le combat s'est terminé sans vainqueur !");
        }*/
    }

    public ArrayList<AgentManager> getAgentManagers() {
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

    public int getNbAnimalsDead() {
        return nbAnimalsDead.get();
    }
    public void increaseNbAnimalsDead(){
        nbAnimalsDead.incrementAndGet();
    }

    public int getNbAgentDead() {
        return nbAgentDead.get();
    }
    public void increaseNbAgentDead(){
        nbAgentDead.incrementAndGet();
    }

    public Set<Integer> getNbZoneExplored() {
        return nbZoneExplored;
    }
    public void increaseNbZoneExplored(int id){
        nbZoneExplored.add(id);
    }
}
