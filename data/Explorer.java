package data;

public class Explorer extends EnvironmentElement {

    // Constantes associées aux attributs des types
    public static final int COMMUNICATIVE_HEALTH = 100;
    public static final int COMMUNICATIVE_STRENGTH = 80;

    public static final int COGNITIVE_HEALTH = 70;
    public static final int COGNITIVE_STRENGTH = 50;

    public static final int REACTIVE_HEALTH = 80;
    public static final int REACTIVE_STRENGTH = 40;
    public static final int REACTIVE_EXPLORER = 0;
    public static final int COMMUNICATIVE_EXPLORER = 1;
    public static final int COGNITIVE_EXPLORER = 2;
    // Attributs
    private int health;
    private int strength;

    // Constante
    private int type;

    // Constructeur
    public Explorer(int health, int strength, int type) {
        super();
        this.health = health;
        this.strength = strength;
        this.type = type;
    }

    public Explorer(Block block, int health, int strength, int type) {
        super(block);
        this.health = health;
        this.strength = strength;
        this.type = type;
    }

    // Getters et Setters
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

    public int getExplorerType() {
        return this.type;
    }


    public void setBlock(Block newBlock) {
        super.setBlock(newBlock);
    }


	public int getType() {
		return type;
	}
	@Override
	public String toString() {
	    return "Explorer{" +
	           "type=" + getType() +
	           ", position=(" + (getBlock() != null ? getBlock().getLine() + ", " + getBlock().getColumn() : "N/A") + ")" +
	           ", health=" + health +
	           ", strength=" + strength +
	           '}';
	}

}
