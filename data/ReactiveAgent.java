package data;

import engine.AgentManager;
import engine.EnvironmentManager;
import engine.Utility;

import java.util.concurrent.atomic.AtomicInteger;

public class ReactiveAgent extends Agent {

    public static final int REACTIVE_HEALTH = 80;
    public static final int REACTIVE_STRENGTH = 40;

    public ReactiveAgent() {
        super(REACTIVE_HEALTH, REACTIVE_STRENGTH);
    }

    @Override
    public void process(Treasure treasure, EnvironmentManager environmentManager, Environment environment) {
        int zone = (block.getLine() / 4) * 4 + (block.getColumn() / 4);
        if(zone != 0 ) {
            environmentManager.increaseNbZoneExplored(zone);
        }
        if (treasure != null) {
            inform(treasure); // Transmet les positions au cognitif
            moveToTreasure(treasure, environmentManager, environment);
        } else {
            randomMove(environmentManager, environment);
        }
    }

    public int getTypeAgent() {
        return Agent.REACTIVE_AGENT;
    }

    private void inform(Treasure treasure) {
        System.out.println("Réactif a reçu les positions des trésors : " + treasure.getBlock());
    }

    public void moveToTreasure(Treasure treasure, EnvironmentManager environmentManager, Environment environment) {
        if (treasure.isCollected()){
//            goToQG();
            for(AgentManager ag : environmentManager.getExplorerManagers()){
                if(this.equals(ag.getAgent())){
                    ag.setTreasure(null);
                    break;
                }
            }
        }

        if (treasure == null) {
            System.out.println("Aucun trésor assigné.");
            return;
        }

        // Récupérer les coordonnées du trésor
        int treasureLine = treasure.getBlock().getLine();
        int treasureColumn = treasure.getBlock().getColumn();

        // Coordonnées actuelles de l'explorateur
        int currentLine = this.getBlock().getLine();
        int currentColumn = this.getBlock().getColumn();

        // Boucle de déplacement vers le trésor
        if (currentLine != treasureLine || currentColumn != treasureColumn) {
//            System.out.println("Explorateur à (" + currentLine + ", " + currentColumn + ")");
//            System.out.println("Trésor à (" + treasureLine + ", " + treasureColumn + ")");

            // Calcul du prochain mouvement
            if (currentLine < treasureLine) {
                currentLine++; // Avancer vers le bas
            } else if (currentLine > treasureLine) {
                currentLine--; // Avancer vers le haut
            } else if (currentColumn < treasureColumn) {
                currentColumn++; // Avancer vers la droite
            } else if (currentColumn > treasureColumn) {
                currentColumn--; // Avancer vers la gauche
            }

            // Vérification des obstacles avant de déplacer
            Block nextBlock = environment.getBlock(currentLine, currentColumn);
            if (Utility.isObstacleByBlock(nextBlock, environment)) {
                System.out.println("Obstacle détecté à la position (" + currentLine + ", " + currentColumn + "). Mouvement annulé.");
                randomMove(environmentManager, environment);
            }
            else {
                // Mettre à jour la position de l'explorateur
                updatePosition(currentColumn, currentLine);
            }
        }

        // Vérifier si l'explorateur atteint le trésor
        if (currentLine == treasureLine && currentColumn == treasureColumn) {
            System.out.println("Explorateur a atteint le trésor !");
            treasure.collect(); // Collecter le trésor
            environmentManager.increaseNbCollectedTreasures();
            environment.getElements().remove(treasure); // Supprimer le trésor de l'environnement
            environment.getElementsByBlocks().remove(treasure.getBlock());
        }
    }

    private void randomMove(EnvironmentManager environmentManager, Environment environment) {
        Block currentBlock = this.getBlock();
        int newLine = currentBlock.getLine();
        int newColumn = currentBlock.getColumn();

        // Générer une direction aléatoire (0: haut, 1: bas, 2: gauche, 3: droite)
        int direction = Utility.getRandomNumber(0, 3);
//        System.out.println("Explorateur à (" + newLine + ", " + newColumn + "), direction : " + direction);

        moveInDirection(direction, environmentManager, environment);
    }

    public void moveInDirection(int direction, EnvironmentManager environmentManager, Environment environment) {
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

            // Vérifier si un animal est présent sur ce bloc
            else if (Utility.isAnimalByBlock(newBlock, environment)) {
                // Les explorateurs communicatifs évitent les combats

                System.out.println("Un animal est détecté sur la position (" + newLine + ", " + newColumn + "). Combat engagé.");
                EnvironmentElement elementAnimal = Utility.getElementFromBlock(environment, newBlock);
                if (elementAnimal instanceof Animal animal) {
                    // Appeler la méthode fight
                    environmentManager.fight(this, animal);
                    environmentManager.increaseNbCombats();

                    // Vérifiez si l'explorateur est mort après le combat
                    if (this.getHealth() <= 0) {
                        System.out.println("L'explorateur est mort après le combat.");
                        environmentManager.increaseNbAgentDead();
                        return; // Stoppe l'exécution de la méthode
                    }

                    // Vérifiez si l'animal est mort
                    if (animal.getHealth() <= 0) {
                        environmentManager.increaseNbAnimalsDead();
                        System.out.println("L'animal est mort après le combat.");
                    }
                }
            }

            // Mise à jour de la position
            System.out.println("Nouvelle position : (" + newLine + ", " + newColumn + ")");
            updatePosition(newColumn, newLine);

            // Si un trésor est présent sur le bloc, le collecter (uniquement pour les explorateurs non communicatifs)
            EnvironmentElement element = Utility.getElementFromBlock(environment, newBlock);
            if (element instanceof Treasure treasure) {
                if (!treasure.isCollected()) {
                    treasure.collect(); // Collecte le trésor
                    environmentManager.increaseNbCollectedTreasures();
                    environment.getElements().remove(treasure);
                    environment.getElementsByBlocks().remove(treasure.getBlock());
                    System.out.println("Trésor collecté !");
                } else {
                    System.out.println("Le trésor sur ce bloc a déjà été collecté.");
                }
            }
        } else {
            System.out.println("Déplacement non valide pour l'explorateur.");
        }
    }

    private boolean isValidMove(int line, int column) {
        return !(Utility.isBlockOutOfMap(column, line));
    }

    public void updatePosition(int column, int line) {
        this.getBlock().set(column, line);
    }

}
