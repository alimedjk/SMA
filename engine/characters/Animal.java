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

    public void process(ArenaManager environmentManager, Arena environment){
        randomMove(environmentManager, environment);
    }

    public void randomMove(ArenaManager environmentManager, Arena environment) {
        Block currentBlock = this.getBlock();
        int newLine = currentBlock.getLine();
        int newColumn = currentBlock.getColumn();

        // Générer une direction aléatoire (0: haut, 1: bas, 2: gauche, 3: droite)
        int direction = Utility.getRandomNumber(0, 3);
//        System.out.println("Explorateur à (" + newLine + ", " + newColumn + "), direction : " + direction);

        moveInDirection(direction, environmentManager, environment);
    }

    public void moveInDirection(int direction, ArenaManager environmentManager, Arena environment) {
        Block currentBlock = this.getBlock();
        int newLine = currentBlock.getLine();
        int newColumn = currentBlock.getColumn();

        // Appliquer le mouvement selon la direction
        switch (direction) {
            case 0:
                if (newLine / 4 != 0 && newColumn / 4 != 0) {
                    newLine--;
                    break; // Haut
                }
            case 1:
                newLine++;
                break; // Bas
            case 2:
                if (newLine / 4 != 0 && newColumn / 4 != 0) {
                    newColumn--;
                    break; // Gauche
                }
            case 3:
                newColumn++;
                break; // Droite
        }

        // Vérifie que le déplacement est valide
        if (isValidMove(newLine, newColumn)) {
            Block newBlock = environment.getBlock(newLine, newColumn);

            // Vérifier si le nouveau bloc est un obstacle
            if (Utility.isObstacleByBlock(newBlock, environment)) {
                System.out.println("Obstacle détecté à la position (" + newLine + ", " + newColumn + "). Mouvement annulé.");
                return; // Arrête le mouvement
            }

            if (Utility.isTreasureByBlock(newBlock, environment)){
                System.out.println("Trésor détecté à la position (" + newLine + ", " + newColumn + "). Mouvement annulé.");
                return; // Arrête le mouvement
            }

            Agent agent = (Agent) Utility.getElementFromBlock(environment, newBlock);
            if (agent instanceof CommunicativeAgent commAgent) {
                System.out.println("Explorer communicant détecté à la position (" + newLine + ", " + newColumn + "). Mouvement annulé.");
                return;
            }

            // agent reactive ou aggent cognitive donc combal
            environmentManager.fight(agent, this);
            environmentManager.increaseNbCombats();

            if (this.getHealth() <= 0) {
                System.out.println("L'animal est mort après le combat.");
                return; // Stoppe l'exécution de la méthode
            }

            // Vérifiez si l'agent est mort
            if (agent.getHealth() <= 0) {
                AgentFactory.setExplorer(agent, environment);
                System.out.println("L'agent est mort après le combat.");
            }

            // Mise à jour de la position
            System.out.println("Nouvelle position : (" + newLine + ", " + newColumn + ")");
            updatePosition(newColumn, newLine);
        } else {
            System.out.println("Déplacement non valide pour l'animal.");
        }
    }

    private boolean isValidMove(int line, int column) {
        return !(Utility.isBlockOutOfMap(column, line));
    }

    public void updatePosition(int column, int line) {
        this.getBlock().set(column, line);
    }

}
