package engine;

import config.GameConfig;
import data.*;

import java.awt.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Utility {

    /**
     * This method reads image from their location.
     *
     * @param filePath the image path.
     * @return an {@link Image}.
     */
    public static Image readImage(String filePath) {
        try {
            return ImageIO.read(new File(filePath));
        } catch (IOException e) {
            System.err.println("-- Can not read the image file ! --");
            return null;
        }
    }

    /**
     * This method generates random (integer) number between "min" and "max".
     *
     * @param min a minimum.
     * @param max a maximum.
     * @return a random number.
     */
    public static int getRandomNumber(int min, int max) {
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        int randomNumber = random.nextInt(max - min) + min;


        return (int) (Math.random() * (max + 1 - min)) + min;

//        return randomNumber;
    }

    /**
     * This method returns a GraphicElement from a position
     *
     * @param searchedBlock: a {@link Block} .
     * @param environment: a {@link EnvironmentElement} instance.
     *
     * @return a {@link EnvironmentElement} since the position in parameter.
     * */

    public static synchronized EnvironmentElement getElementFromBlock(Environment environment, Block searchedBlock) {

//        ArrayList<EnvironmentElement> elements;

        // Synchroniser uniquement l'accès aux éléments si nécessaire
//        synchronized (environment) {
//            elements = new ArrayList<>(environment.getElements()); // Copie pour éviter les modifications concurrentes
//        }

        for(EnvironmentElement environmentElement : environment.getElements()){

            if(searchedBlock.equals(environmentElement.getBlock())) {
                return environmentElement;
            }

        }

        return null;
    }


    public static ArrayList<Integer> getGraphicPosition(EnvironmentElement element){
        ArrayList<Integer> position = new ArrayList<Integer>(2);
        position.add(element.getColumn() * GameConfig.BLOCK_SIZE);
        position.add(element.getLine() * GameConfig.BLOCK_SIZE);
        return position;
    }

    public static ArrayList<Integer> getGraphicPosition(Block block){
        ArrayList<Integer> position = new ArrayList<Integer>(2);
        position.add(block.getColumn() * GameConfig.BLOCK_SIZE);
        position.add(block.getLine() * GameConfig.BLOCK_SIZE);
        return position;
    }


    public static boolean isObstacleByBlock(Block block, Environment environment){
        EnvironmentElement element = getElementFromBlock(environment, block);
        if (element instanceof Obstacle){
            return true;
        }
        return false;

    }

    public static boolean isTreasureByBlock(Block block, Environment environment){
        EnvironmentElement element = getElementFromBlock(environment, block);
        if (element instanceof Treasure){
            return true;
        }
        return false;

    }

    public static boolean isAnimalByBlock(Block block, Environment environment){
        EnvironmentElement element = getElementFromBlock(environment, block);
        return element instanceof Animal;
    }

    /**
     * Simulates the unit time (for an iteration) defined for the simulation.
     */
    public static void unitTime() {
        try {
            Thread.sleep(GameConfig.GAME_SPEED);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    public synchronized static boolean isElementNBlockNearElement(Environment map, Block elementPosition, int nbBlocks) {
        int line = elementPosition.getLine();
        int column = elementPosition.getColumn();
        if(line + nbBlocks < map.getColumnCount()
                && line - nbBlocks > 0
                && column + nbBlocks < map.getLineCount()
                && column - nbBlocks > 0) {
            for(EnvironmentElement mapElement : map.getElements()) {
                Block mapElementPosition = mapElement.getBlock();
                int mapElementColumn = mapElementPosition.getColumn();
                int mapElementLine = mapElementPosition.getLine();

                if(mapElementColumn <= column + nbBlocks
                        && mapElementColumn >= column - nbBlocks
                        && mapElementLine <= line + nbBlocks
                        && mapElementLine >= line - nbBlocks) {
                    return true;
                }

            }
            return false;
        }
        else {
            return true;
        }

    }

    public static int getZoneByBlock(Block block) {
        return (block.getLine() / 4) * 4 + (block.getColumn() / 4);
    }

    public static synchronized EnvironmentElement getEnvironmentElementFromPosition(Environment environment, Block block){
        for(EnvironmentElement mapElement : environment.getElements()){

            int columnElement = mapElement.getColumn();
            int lineElement = mapElement.getLine();


            if(block.getColumn() == columnElement && block.getLine() == lineElement) {
                return mapElement;
            }

        }

        return null;

    }

    public static boolean isBlockOutOfMap(int line, int column){
        return line == -1 || column == -1 || line == 16 || column == 16;
    }

    public static Block[][] getBlocksByZone(Environment environment, int zone) {

        // Obtenir la grille globale des blocs
        Block[][] allBlocks = environment.getBlocks(); // Méthode hypothétique

        // Dimensions globales
        int rows = allBlocks.length;
        int cols = allBlocks[0].length;

        // Calculer les coordonnées de départ de la zone
        int startRow = (zone / Environment.NUM_BLOCKS_PER_ZONE) * Environment.NUM_BLOCKS_PER_ZONE;
        int startCol = (zone % Environment.NUM_BLOCKS_PER_ZONE) * Environment.NUM_BLOCKS_PER_ZONE;

        // Vérifier les limites (au cas où)
        if (startRow >= rows || startCol >= cols) {
            throw new IllegalArgumentException("Zone invalide ou hors limites !");
        }

        // Initialiser un tableau pour contenir les blocs de la zone
        Block[][] blocksInZone = new Block[Environment.NUM_BLOCKS_PER_ZONE][Environment.NUM_BLOCKS_PER_ZONE];

        // Parcourir les blocs de la zone
        for (int i = 0; i < Environment.NUM_BLOCKS_PER_ZONE; i++) {
            for (int j = 0; j < Environment.NUM_BLOCKS_PER_ZONE; j++) {
                blocksInZone[i][j] = allBlocks[startRow + i][startCol + j];
            }
        }

        return blocksInZone;
    }

    public synchronized static boolean isLineTreasure(int line, ArrayList<EnvironmentElement> elements){
        for (EnvironmentElement element : elements){
            if (element instanceof Treasure){

                Treasure treasure = (Treasure) element;

                if (treasure.getBlock().getLine() == line)
                    return true;
            }

        }
        return false;

    }

    public synchronized static boolean isColumnTreasure(int column, ArrayList<EnvironmentElement> elements){
        for (EnvironmentElement element : elements){
            if (element instanceof Treasure){

                Treasure treasure = (Treasure) element;

                if (treasure.getBlock().getColumn() == column)
                    return true;
            }

        }
        return false;

    }

    public static ArrayList<Integer> getZoneLibre(Environment environment){
        ArrayList<Integer> zoneLibres = new ArrayList<>();
        for(int i=1; i<Environment.NUM_BLOCKS_PER_ZONE*4; i++){
            int row = (i/4) * 4;
            int column = (i%4) * 4;
            boolean isLibre = true;
            for(int j = row; j< row + 4; j++){
                for(int k = column; k< column + 4; k++){
                    ArrayList<Agent> agents = environment.getAgents();
                    for (Agent agent : agents){
                        if(agent instanceof CommunicatingAgent communicatingAgent && agent.getBlock().equals(new Block(j,k))){
                            isLibre = false;
                        }
                    }
                }
            }
            if(isLibre){
                zoneLibres.add(i);
            }
        }
        return zoneLibres;

    }



}