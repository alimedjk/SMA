package engine.characters;

import engine.mapElements.Arena;
import engine.mapElements.Block;
import engine.mapElements.Objet;
import engine.process.AgentFactory;
import engine.process.ArenaManager;
import engine.process.Utility;

public class Animal extends Objet {
    private int health = 100;

    public Animal(Block block, int health){
        super(block);
        this.health = health;
    }

    public Animal(Block block){
        super(block);
    }

    public int getHealth(){
        return health;
    }

    public void setHealth(int health){
        this.health = health;
    }

}
