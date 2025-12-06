package data;

import engine.EnvironmentManager;

public abstract class Agent extends EnvironmentElement {
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
        return "Agent {" +
                "position=(" + (getBlock() != null ? getBlock().getLine() + ", " + getBlock().getColumn() : "N/A") + ")" +
                ", health=" + health +
                ", strength=" + strength +
                "type=" + getTypeAgent()+
                '}';
    }

    abstract public void process(Treasure treasure, EnvironmentManager environmentManager, Environment environment);

    public abstract int getTypeAgent();
}
