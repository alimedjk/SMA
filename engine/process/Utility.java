package engine.process;

import config.GameConfig;
import engine.characters.Agent;
import engine.characters.Animal;
import engine.characters.CommunicativeAgent;
import engine.mapElements.*;

import java.awt.*;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class Utility {


    public static Image readImage(String filePath) {
        try {
            return ImageIO.read(new File(filePath));
        } catch (IOException e) {
            System.err.println("lecture du fichier impossible");
            return null;
        }
    }

    public static int getRandomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static synchronized ArenaElement getElementFromBlock(Arena arena, Block block) {

        ArrayList<ArenaElement> elements = new ArrayList<>(arena.getElements());
        for (ArenaElement envElem : elements) {
            if(envElem !=  null) {
                if (block.equals(envElem.getBlock())) {
                    return envElem;
                }
            }
        }
        return null;
    }


    public static ArrayList<Integer> getGraphicPosition(ArenaElement element){
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


    public static boolean isObstacleByBlock(Block block, Arena arena){
        ArenaElement element = getElementFromBlock(arena, block);
        if (element instanceof Obstacle){
            return true;
        }
        return false;

    }

    public static boolean isAnimalByBlock(Block block, Arena environment){
        ArenaElement element = getElementFromBlock(environment, block);
        return element instanceof Animal;
    }

    public static void unitTime() {
        try {
            Thread.sleep(GameConfig.GAME_SPEED);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    public synchronized static boolean isElementNBlockNearElement(Arena arena, Block elementPosition, int nbBlocks) {
        int line = elementPosition.getLine();
        int column = elementPosition.getColumn();
        if(line + nbBlocks < arena.getColumnCount()
                && line - nbBlocks > 0
                && column + nbBlocks < arena.getLineCount()
                && column - nbBlocks > 0) {
            for(ArenaElement arenaElement : arena.getElements()) {
                Block mapElementPosition = arenaElement.getBlock();
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

    public static synchronized ArenaElement getEnvironmentElementFromPosition(Arena environment, Block block){
        for(ArenaElement mapElement : environment.getElements()){

            int columnElement = mapElement.getColumn();
            int lineElement = mapElement.getLine();
            if(block.getColumn() == columnElement && block.getLine() == lineElement) {
                return mapElement;
            }

        }

        return null;

    }

    //public static boolean isBlockOutOfMap(int line, int column){
      //  return line == -1 || column == -1 || line == 16 || column == 16;
    //}

    public static boolean isBlockOutOfMap(int line, int column){
    return line < 0 || column < 0 || 
           line >= GameConfig.LINE_COUNT || 
           column >= GameConfig.COLUMN_COUNT;
}


    public static Block[][] getBlocksByZone(Arena environment, int zone) {

        // Obtenir la grille globale des blocs
        Block[][] allBlocks = environment.getBlocks(); // Méthode hypothétique

        // Dimensions globales
        int rows = allBlocks.length;
        int cols = allBlocks[0].length;

        // Calculer les coordonnées de départ de la zone
        int startRow = (zone / Arena.NUM_BLOCKS_PER_ZONE) * Arena.NUM_BLOCKS_PER_ZONE;
        int startCol = (zone % Arena.NUM_BLOCKS_PER_ZONE) * Arena.NUM_BLOCKS_PER_ZONE;

        // Vérifier les limites (au cas où)
        if (startRow >= rows || startCol >= cols) {
            throw new IllegalArgumentException("Zone invalide ou hors limites !");
        }

        // Initialiser un tableau pour contenir les blocs de la zone
        Block[][] blocksInZone = new Block[Arena.NUM_BLOCKS_PER_ZONE][Arena.NUM_BLOCKS_PER_ZONE];

        // Parcourir les blocs de la zone
        for (int i = 0; i < Arena.NUM_BLOCKS_PER_ZONE; i++) {
            for (int j = 0; j < Arena.NUM_BLOCKS_PER_ZONE; j++) {
                blocksInZone[i][j] = allBlocks[startRow + i][startCol + j];
            }
        }

        return blocksInZone;
    }

    public synchronized static boolean isLineTreasure(int line, ArrayList<ArenaElement> elements){
        for (ArenaElement element : elements){
            if (element instanceof Treasure){

                Treasure treasure = (Treasure) element;

                if (treasure.getBlock().getLine() == line)
                    return true;
            }

        }
        return false;

    }

    public synchronized static boolean isColumnTreasure(int column, ArrayList<ArenaElement> elements){
        for (ArenaElement element : elements){
            if (element instanceof Treasure){

                Treasure treasure = (Treasure) element;

                if (treasure.getBlock().getColumn() == column)
                    return true;
            }

        }
        return false;

    }

    public static ArrayList<Integer> getZoneLibre(Arena environment){
        ArrayList<Integer> zoneLibres = new ArrayList<>();
        for(int i=1; i<Arena.NUM_BLOCKS_PER_ZONE*4; i++){
            int row = (i/4) * 4;
            int column = (i%4) * 4;
            boolean isLibre = true;
            for(int j = row; j< row + 4; j++){
                for(int k = column; k< column + 4; k++){
                    ArrayList<Agent> agents = environment.getAgents();
                    for (Agent agent : agents){
                        if(agent instanceof CommunicativeAgent communicatingAgent && agent.getBlock().equals(new Block(j,k))){
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