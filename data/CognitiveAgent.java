package data;

import engine.AgentManager;
import engine.EnvironmentManager;
import engine.Utility;

import java.util.concurrent.atomic.AtomicInteger;

public class CognitiveAgent extends Agent {
    public static final int COGNITIVE_HEALTH = 70;
    public static final int COGNITIVE_STRENGTH = 50;
    public int mission;
    private int cpt = 0;

    public CognitiveAgent(int mission) {
        super(COGNITIVE_HEALTH, COGNITIVE_STRENGTH);
        this.mission = mission;
    }

    @Override
    public void process(Treasure treasure, EnvironmentManager environmentManager, Environment environment) {
        int zone = (block.getLine() / 4) * 4 + (block.getColumn() / 4);
        if(zone != 0) {
              environmentManager.increaseNbZoneExplored(zone);
        }
        if (treasure != null) {
            inform(treasure); // Transmet les positions au cognitif
            moveToTreasure(treasure, environmentManager, environment);
        } else {
            Block previousBlock = new Block(this.getBlock().getLine(), this.getBlock().getColumn());
            if (this.mission == 1)
                  moveToNextZone(environmentManager, environment);
            else if (this.mission == 2)
                moveToNextZoneBis(environmentManager, environment);
            if (previousBlock.getLine() == this.block.getLine() && previousBlock.getColumn() == this.block.getColumn()) {
                randomMove(environmentManager, environment);
            }
        }
    }

    public int getTypeAgent() {
        return Agent.COGNITIVE_AGENT;
    }

    private void inform(Treasure treasure) {
        System.out.println("Cognitif a reçu les positions des trésors : " + treasure.getBlock());
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
//        else {
//            System.out.println("L'explorateur n'a pas pu atteindre le trésor.");
//        }
    }
    public void moveToNextZone(EnvironmentManager environmentManager, Environment environment) {

        int currentZone = Utility.getZoneByBlock(this.getBlock());
        if(cpt != 0)
        {
            boolean isvalid = isValidMove(this.getLine() + 1, this.getColumn());
            if (isvalid) {
                moveInDirectionBis(1, environmentManager, environment);
                cpt++;
                if (cpt == 4) cpt = 0;
            }else {
                randomMove(environmentManager, environment);
            }
        }
        else {
            if ( (currentZone / 4) % 2 == 0) {
                boolean isvalid = isValidMove(this.getLine(), this.getColumn()+1);
                if (isvalid) {
                    moveInDirectionBis(3, environmentManager, environment);
                }
                else
                {
                    isvalid = isValidMove(this.getLine() + 1, this.getColumn());
                    if (isvalid) {
                        moveInDirectionBis(1, environmentManager, environment);
                        cpt = cpt + 1;
                    }else{
                        randomMove(environmentManager, environment);
                    }
                }
            }
            else
            {
                boolean isvalid = isValidMove(this.getLine(), this.getColumn()-1);
                if (isvalid) {
                    moveInDirectionBis(2, environmentManager, environment);
                }
                else
                {
                    isvalid = isValidMove(this.getLine() + 1, this.getColumn());
                    if (isvalid) {
                        moveInDirectionBis(1, environmentManager, environment);
                        cpt = cpt + 1;
                    }else{
                        randomMove(environmentManager, environment);
                        }
                }
            }

        }
    }


    public void moveToNextZoneBis(EnvironmentManager environmentManager, Environment environment) {

        int currentZone = Utility.getZoneByBlock(this.getBlock());
        if(cpt != 0)
        {
            boolean isvalid = isValidMove(this.getLine(), this.getColumn()+1);
            if (isvalid) {
                moveInDirectionBis(3, environmentManager, environment);
                cpt++;
                if (cpt == 4) cpt = 0;
            }else {
                randomMove(environmentManager, environment);
            }
        }
        else {
            if ( (currentZone % 4) % 2 == 0) {
                boolean isvalid = isValidMove(this.getLine()+1, this.getColumn());
                if (isvalid) {
                    moveInDirectionBis(1, environmentManager, environment);
                }
                else
                {
                    isvalid = isValidMove(this.getLine(), this.getColumn()+1);
                    if (isvalid) {
                        moveInDirectionBis(3, environmentManager, environment);
                        cpt = cpt + 1;
                    }else{
                        randomMove(environmentManager, environment);
                    }
                }
            }
            else
            {
                boolean isvalid = isValidMove(this.getLine()-1, this.getColumn());
                if (isvalid) {
                    moveInDirectionBis(0, environmentManager, environment);
                }
                else
                {
                    isvalid = isValidMove(this.getLine(), this.getColumn()+1);
                    if (isvalid) {
                        moveInDirectionBis(3, environmentManager, environment);
                        cpt = cpt + 1;
                    }else{
                        randomMove(environmentManager, environment);
                    }
                }
            }

        }
    }

    public void updatePosition(int column, int line) {
        this.getBlock().set(column, line);
    }

   public void randomMove(EnvironmentManager environmentManager, Environment environment) {
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
                        return; // Stoppe l'exécution de la méthode
                    }

                    // Vérifiez si l'animal est mort
                    if (animal.getHealth() <= 0) {
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

    public void moveInDirectionBis(int direction, EnvironmentManager environmentManager, Environment environment) {
        Block currentBlock = this.getBlock();
        int newLine = currentBlock.getLine();
        int newColumn = currentBlock.getColumn();

        // Appliquer le mouvement selon la direction
        switch (direction) {
            case 0:
                    newLine--;
                    break; // Haut
            case 1:
                newLine++;
                break; // Bas
            case 2:
                    newColumn--;
                    break; // Gauche
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
                        System.out.println("L'animal est mort après le combat.");
                        environmentManager.increaseNbAnimalsDead();
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
}
