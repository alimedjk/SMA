package engine.characters;

import engine.mapElements.Arena;
import engine.mapElements.ArenaElement;
import engine.mapElements.Block;
import engine.mapElements.Treasure;
import engine.process.AgentManager;
import engine.process.ArenaManager;
import engine.process.Utility;

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
    public void process(Treasure treasure, ArenaManager arenaManager, Arena arena) {
        int zone = (block.getLine() / 4) * 4 + (block.getColumn() / 4);

        if(zone != 0) {
            arenaManager.increaseNbZoneExplored(zone);
        }

        if (treasure != null) {
            moveToTreasure(treasure, arenaManager, arena);
        } else {
            Block previousBlock = new Block(this.getBlock().getLine(), this.getBlock().getColumn());

            if (this.mission == 1)
                  moveToNextZone(arenaManager, arena);
            else if (this.mission == 2)
                moveToNextZoneBis(arenaManager, arena);
            if (previousBlock.getLine() == this.block.getLine() && previousBlock.getColumn() == this.block.getColumn()) {
                randomMove(arenaManager, arena);
            }
        }
    }

    public int getTypeAgent() {
        return Agent.COGNITIVE_AGENT;
    }

    public void moveToTreasure(Treasure treasure, ArenaManager arenaManager, Arena arena) {

        if (treasure.isCollected()) {
            for(AgentManager ag : arenaManager.getAgentManagers()){
                if(this.equals(ag.getAgent())){
                    ag.setTreasure(null);
                    return;
                }
            }
        }

        if (treasure == null) {
            //System.out.println("Aucun trésor assigné.");
            return;
        }

        int treasureLine = treasure.getBlock().getLine();
        int treasureColumn = treasure.getBlock().getColumn();

        int currentLine = this.getBlock().getLine();
        int currentColumn = this.getBlock().getColumn();

        if (currentLine != treasureLine || currentColumn != treasureColumn) {

            if (currentLine < treasureLine) {
                currentLine++;
            } else if (currentLine > treasureLine) {
                currentLine--;
            } else if (currentColumn < treasureColumn) {
                currentColumn++;
            } else if (currentColumn > treasureColumn) {
                currentColumn--;
            }

            Block nextBlock = arena.getBlock(currentLine, currentColumn);
            if (Utility.isObstacleByBlock(nextBlock, arena)) {
                //System.out.println("Obstacle détecté à la position (" + currentLine + ", " + currentColumn + "). Mouvement annulé.");
                randomMove(arenaManager, arena);
            }
            else {
                updatePosition(currentColumn, currentLine);
            }
        }

        if (currentLine == treasureLine && currentColumn == treasureColumn) {
            //System.out.println("Explorateur a atteint le trésor !");
            treasure.collect();
            arenaManager.increaseNbCollectedTreasures();
            arena.getElements().remove(treasure);
            arena.getElementsByBlocks().remove(treasure.getBlock());
        }
    }

    public void moveToNextZone(ArenaManager arenaManager, Arena arena) {
        int currentZone = Utility.getZoneByBlock(this.getBlock());

        if(cpt != 0) {
            boolean isvalid = isValidMove(this.getLine() + 1, this.getColumn());

            if (isvalid) {
                moveInDirectionBis(1, arenaManager, arena);
                cpt++;
                if (cpt == 4) cpt = 0;
            } else {
                randomMove(arenaManager, arena);
            }
        } else {
            if ((currentZone / 4) % 2 == 0) {
                boolean isvalid = isValidMove(this.getLine(), this.getColumn()+1);

                if (isvalid) {
                    moveInDirectionBis(3, arenaManager, arena);
                } else {
                    isvalid = isValidMove(this.getLine() + 1, this.getColumn());

                    if (isvalid) {
                        moveInDirectionBis(1, arenaManager, arena);
                        cpt = cpt + 1;
                    } else {
                        randomMove(arenaManager, arena);
                    }
                }
            } else {
                boolean isvalid = isValidMove(this.getLine(), this.getColumn()-1);

                if (isvalid) {
                    moveInDirectionBis(2, arenaManager, arena);
                } else {
                    isvalid = isValidMove(this.getLine() + 1, this.getColumn());

                    if (isvalid) {
                        moveInDirectionBis(1, arenaManager, arena);
                        cpt = cpt + 1;
                    } else {
                        randomMove(arenaManager, arena);
                    }
                }
            }
        }
    }

    public void moveToNextZoneBis(ArenaManager arenaManager, Arena arena) {
        int currentZone = Utility.getZoneByBlock(this.getBlock());

        if(cpt != 0) {
            boolean isvalid = isValidMove(this.getLine(), this.getColumn()+1);

            if (isvalid) {
                moveInDirectionBis(3, arenaManager, arena);
                cpt++;
                if (cpt == 4) cpt = 0;
            } else {
                randomMove(arenaManager, arena);
            }
        } else {

            if ((currentZone % 4) % 2 == 0) {
                boolean isvalid = isValidMove(this.getLine()+1, this.getColumn());

                if (isvalid) {
                    moveInDirectionBis(1, arenaManager, arena);
                } else {
                    isvalid = isValidMove(this.getLine(), this.getColumn()+1);

                    if (isvalid) {
                        moveInDirectionBis(3, arenaManager, arena);
                        cpt = cpt + 1;
                    } else {
                        randomMove(arenaManager, arena);
                    }
                }
            } else {
                boolean isvalid = isValidMove(this.getLine()-1, this.getColumn());

                if (isvalid) {
                    moveInDirectionBis(0, arenaManager, arena);
                } else {
                    isvalid = isValidMove(this.getLine(), this.getColumn()+1);

                    if (isvalid) {
                        moveInDirectionBis(3, arenaManager, arena);
                        cpt = cpt + 1;
                    } else {
                        randomMove(arenaManager, arena);
                    }
                }
            }
        }
    }

    public void updatePosition(int column, int line) {
        this.getBlock().set(column, line);
    }

    public void randomMove(ArenaManager arenaManager, Arena arena) {
        Block currentBlock = this.getBlock();

        int newLine = currentBlock.getLine();
        int newColumn = currentBlock.getColumn();

        // Générer une direction aléatoire (0: haut, 1: bas, 2: gauche, 3: droite)
        int direction = Utility.getRandomNumber(0, 3);
        //System.out.println("Explorateur à (" + newLine + ", " + newColumn + "), direction : " + direction);

        moveInDirection(direction, arenaManager, arena);
    }

    public void moveInDirection(int direction, ArenaManager arenaManager, Arena arena) {
        Block currentBlock = this.getBlock();
        int newLine = currentBlock.getLine();
        int newColumn = currentBlock.getColumn();

        switch (direction) {
            case 0:
                if (newLine / 4 != 0 || newColumn / 4 != 0) {
                    newLine--;
                    break;
                }
            case 1:
                newLine++;
                break;
            case 2:
                if (newLine / 4 != 0 || newColumn / 4 != 0) {
                    newColumn--;
                    break;
                }
            case 3:
                newColumn++;
                break;
        }

        if (isValidMove(newLine, newColumn)) {
            Block newBlock = arena.getBlock(newLine, newColumn);

            if (Utility.isObstacleByBlock(newBlock, arena)) {
                //System.out.println("Obstacle détecté à la position (" + newLine + ", " + newColumn + "). Mouvement annulé.");
                return;
            }

            else if (Utility.isAnimalByBlock(newBlock, arena)) {
                //System.out.println("Un animal est détecté sur la position (" + newLine + ", " + newColumn + "). Combat engagé.");
                ArenaElement elementAnimal = Utility.getElementFromBlock(arena, newBlock);

                if (elementAnimal instanceof Animal animal) {
                    arenaManager.fight(this, animal);
                    arenaManager.increaseNbCombats();

                    if (this.getHealth() <= 0) {
                        //System.out.println("L'agent est mort après le combat.");
                        arenaManager.increaseNbAgentDead();
                        return;
                    }

                    if (animal.getHealth() <= 0) {
                        //System.out.println("L'animal est mort après le combat.");
                        arenaManager.increaseNbAnimalsDead();
                    }
                }
            }

            //System.out.println("Nouvelle position : (" + newLine + ", " + newColumn + ")");
            updatePosition(newColumn, newLine);

            ArenaElement element = Utility.getElementFromBlock(arena, newBlock);

            if (element instanceof Treasure treasure) {
                if (!treasure.isCollected()) {
                    System.out.println("madj");
                    treasure.collect();
                    arenaManager.increaseNbCollectedTreasures();
                    arena.getElements().remove(treasure);
                    arena.getElementsByBlocks().remove(treasure.getBlock());
                    //System.out.println("Trésor collecté !");
                } else {
                    System.out.println("Le trésor sur ce bloc a déjà été collecté.");
                }
            }
        } else {
            System.out.println("Déplacement non valide pour l'agent.");
        }
    }

    public void moveInDirectionBis(int direction, ArenaManager arenaManager, Arena arena) {
        Block currentBlock = this.getBlock();
        int newLine = currentBlock.getLine();
        int newColumn = currentBlock.getColumn();

        switch (direction) {
            case 0:
                    newLine--;
                    break;
            case 1:
                newLine++;
                break;
            case 2:
                    newColumn--;
                    break;
            case 3:
                newColumn++;
                break;
        }

        if (isValidMove(newLine, newColumn)) {
            Block newBlock = arena.getBlock(newLine, newColumn);

            if (Utility.isObstacleByBlock(newBlock, arena)) {
                //System.out.println("Obstacle détecté à la position (" + newLine + ", " + newColumn + "). Mouvement annulé.");
                return;
            }

            else if (Utility.isAnimalByBlock(newBlock, arena)) {
                //System.out.println("Un animal est détecté sur la position (" + newLine + ", " + newColumn + "). Combat engagé.");
                ArenaElement elementAnimal = Utility.getElementFromBlock(arena, newBlock);

                if (elementAnimal instanceof Animal animal) {
                    arenaManager.fight(this, animal);
                    arenaManager.increaseNbCombats();

                    if (this.getHealth() <= 0) {
                        //System.out.println("L'agent est mort après le combat.");
                        arenaManager.increaseNbAgentDead();
                        return;
                    }

                    if (animal.getHealth() <= 0) {
                        //System.out.println("L'animal est mort après le combat.");
                        arenaManager.increaseNbAnimalsDead();
                    }
                }
            }

            //System.out.println("Nouvelle position : (" + newLine + ", " + newColumn + ")");
            updatePosition(newColumn, newLine);

            ArenaElement element = Utility.getElementFromBlock(arena, newBlock);

            if (element instanceof Treasure treasure) {
                if (!treasure.isCollected()) {
                    treasure.collect();
                    arenaManager.increaseNbCollectedTreasures();
                    arena.getElements().remove(treasure);
                    arena.getElementsByBlocks().remove(treasure.getBlock());
                   // System.out.println("Trésor collecté !");
                } else {
                    System.out.println("Le trésor sur ce bloc a déjà été collecté.");
                }
            }
        } else {
            System.out.println("Déplacement non valide pour l'agent.");
        }
    }

    private boolean isValidMove(int line, int column) {
        return !(Utility.isBlockOutOfMap(line, column));
    }
}
