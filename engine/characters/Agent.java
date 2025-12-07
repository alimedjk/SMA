package engine.characters;

import engine.mapElements.Arena;
import engine.mapElements.ArenaElement;
import engine.mapElements.Block;
import engine.mapElements.Treasure;
import engine.process.ArenaManager;

public abstract class Agent extends ArenaElement {
    public static final int REACTIVE_AGENT = 0;
    public static final int COMMUNICATIVE_AGENT = 1;
    public static final int COGNITIVE_AGENT = 2;

    protected int health;
    protected int strength;

    public Agent(int health, int strength) {
        super();
        this.health = health;
        this.strength = strength;
    }

    public Agent() {
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setBlock(Block newBlock) {
        super.setBlock(newBlock);
    }

    @Override
    public String toString() {
        return "Explorer {" +
                "position=(" + (getBlock() != null ? getBlock().getLine() + ", " + getBlock().getColumn() : "N/A") + ")" +
                ", health=" + health +
                ", strength=" + strength +
                "type=" + getTypeAgent()+
                '}';
    }

    abstract public void process(Treasure treasure, ArenaManager environmentManager, Arena environment);

    public abstract int getTypeAgent();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        Agent other = (Agent) obj;

        if (this.health != other.health) return false;
        if (this.strength != other.strength) return false;
        if (this.getTypeAgent() != other.getTypeAgent()) return false;

        if (this.getBlock() == null && other.getBlock() != null) return false;
        if (this.getBlock() != null && !this.getBlock().equals(other.getBlock())) return false;

        return true;
    }
}
