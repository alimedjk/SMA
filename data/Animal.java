package data;

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
