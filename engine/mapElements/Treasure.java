package engine.mapElements;

public class Treasure extends Objet  {
    private boolean collected;

    public Treasure(Block block) {
        super(block);
        this.collected = false;
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        if (!collected) {
            this.collected = true;
            //System.out.println("Trésor collecté !");
        }
    }
}