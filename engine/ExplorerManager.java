package engine;

import data.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExplorerManager extends Thread {

    private Explorer explorer;
    private Environment environment;
    private EnvironmentManager environmentManager;
    private Treasure treasure;

    private ArrayList<Integer> pathToTreasure = null;
    private Iterator<Integer> pathStep = null;

    /** Contrôles du thread */
    private volatile boolean paused = false;
    private volatile boolean stopped = false;

    public ExplorerManager(Explorer explorer, Environment environment, EnvironmentManager environmentManager) {
        this.explorer = explorer;
        this.environment = environment;
        this.environmentManager = environmentManager;
    }

    /** ----- Nouveaux contrôles ----- */

    public void setPaused(boolean p) {
        this.paused = p;
    }

    public void stopThread() {
        this.stopped = true;
    }

    /** -------------------------------- */

    public Treasure getTreasure() {
        return treasure;
    }

    @Override
    public void run() {

        int type = explorer.getExplorerType();

        while (!stopped) {

            /** MODE PAUSE : gel total du thread */
            while (paused && !stopped) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException ignored) {}
            }
            if (stopped) break;

            /** Tick de temps */
            Utility.unitTime();
            Utility.unitTime();

            /** LOGIQUE PRINCIPALE */
            if (type == Explorer.COMMUNICATIVE_EXPLORER) {
                randomMove();
                Treasure treasure = scanForTreasure();
                if (treasure != null) {

                    inform(treasure);

                    for (ExplorerManager em : environmentManager.getExplorerManagers()) {
                        if (em.getExplorer().getExplorerType() == Explorer.COGNITIVE_EXPLORER
                            && em.getTreasure() == null
                            && !environmentManager.getAffectedTreasures().contains(treasure)) {

                            em.setTreasure(treasure);
                            environmentManager.addAffectedTreasure(treasure);
                            break;
                        }
                    }
                }

            } else if (type == Explorer.COGNITIVE_EXPLORER) {
                if (treasure != null) {
                    inform(treasure);
                    moveToTreasure();
                }

            } else if (type == Explorer.REACTIVE_EXPLORER) {
                randomMove();
            }

            /** Check mort */
            if (explorer.getHealth() <= 0) {
                System.out.println("L'explorateur est mort.");
                ExplorerFactory.setExplorer(explorer);
            }
        }

        System.out.println("THREAD EXPLORER STOPPÉ");
    }

    /** -------------------------------------------------------------- */
    /**                  LOGIQUE DEPLACEMENT ET COMBAT                 */
    /** -------------------------------------------------------------- */

    public void moveInDirection(int direction) {
        Block currentBlock = explorer.getBlock();
        int newLine = currentBlock.getLine();
        int newColumn = currentBlock.getColumn();

        switch (direction) {
            case 0:
                newLine--; break;
            case 1:
                newLine++; break;
            case 2:
                newColumn--; break;
            case 3:
                newColumn++; break;
        }

        if (isValidMove(newLine, newColumn)) {
            Block newBlock = environment.getBlock(newLine, newColumn);

            // OBSTACLE
            if (Utility.isObstacleByBlock(newBlock, environment)) {
                return;
            }

            // ANIMAL
            if (Utility.isAnimalByBlock(newBlock, environment)) {

                if (explorer.getExplorerType() == Explorer.COMMUNICATIVE_EXPLORER)
                    return;

                EnvironmentElement elementAnimal = Utility.getElementFromBlock(environment, newBlock);
                if (elementAnimal instanceof Animal animal) {
                    environmentManager.fight(explorer, animal);
                    environmentManager.increaseNbCombats();

                    if (explorer.getHealth() <= 0) return;
                }
            }

            updatePosition(newColumn, newLine);

            EnvironmentElement element = Utility.getElementFromBlock(environment, newBlock);
            if (element instanceof Treasure treasure && explorer.getExplorerType() != Explorer.COMMUNICATIVE_EXPLORER) {

                if (!treasure.isCollected()) {
                    treasure.collect();
                    environmentManager.increaseNbCollectedTreasures();
                    environment.getElements().remove(treasure);
                    environment.getElementsByBlocks().remove(treasure.getBlock());
                }
            }
        }
    }

    public void updatePosition(int column, int line) {
        explorer.getBlock().set(column, line);
    }

    public boolean isValidMove(int line, int column) {
        return !Utility.isBlockOutOfMap(column, line);
    }

    public void randomMove() {
        int direction = Utility.getRandomNumber(0, 3);
        moveInDirection(direction);
    }

    public Explorer getExplorer() {
        return explorer;
    }

    /** -------------------------------------------------------------- */
    /**                   TRESOR / SCAN / MOVE                         */
    /** -------------------------------------------------------------- */

    private Treasure scanForTreasure() {
        Block currentBlock = explorer.getBlock();

        int zone = Utility.getZoneByBlock(currentBlock);
        Block[][] blocks = Utility.getBlocksByZone(environment, zone);

        for (Block[] row : blocks)
            for (Block b : row)
                if (Utility.getElementFromBlock(environment, b) instanceof Treasure t)
                    return t;

        return null;
    }

    public void moveToTreasure() {
        if (treasure == null) return;

        if (treasure.isCollected()) {
            treasure = null;
            return;
        }

        int treasureLine = treasure.getBlock().getLine();
        int treasureCol = treasure.getBlock().getColumn();

        int line = explorer.getBlock().getLine();
        int col = explorer.getBlock().getColumn();

        if (line < treasureLine) line++;
        else if (line > treasureLine) line--;
        else if (col < treasureCol) col++;
        else if (col > treasureCol) col--;

        Block next = environment.getBlock(line, col);

        if (Utility.isObstacleByBlock(next, environment)) {
            randomMove();
            return;
        }

        updatePosition(col, line);

        if (line == treasureLine && col == treasureCol) {
            treasure.collect();
            environmentManager.increaseNbCollectedTreasures();
            environment.getElements().remove(treasure);
            environment.getElementsByBlocks().remove(treasure.getBlock());
        }
    }

    private void inform(Treasure treasure) {
        // log interne, OK
    }

    public void setTreasure(Treasure treasure) {
        this.treasure = treasure;
    }
}
