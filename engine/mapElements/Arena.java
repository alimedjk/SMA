package engine.mapElements;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import engine.characters.Agent;

public class Arena {

    // Constantes
    public static final int NUM_BLOCKS_PER_ZONE = 4;
    public static final int NUM_ZONES = 4;

    private ArrayList<ArenaElement> elements;


    private ConcurrentHashMap<Block, ArenaElement> elementsByBlocks = new ConcurrentHashMap<>();
    private Block[][] blocks;

    private int lineCount;
    private int columnCount;

    public Arena(int lineCount, int columnCount) {
        this.lineCount = lineCount;
        this.columnCount = columnCount;

        blocks = new Block[lineCount][columnCount];

        elements = new ArrayList<ArenaElement>();

        for (int lineIndex = 0; lineIndex < lineCount; lineIndex++) {
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                blocks[lineIndex][columnIndex] = new Block(lineIndex, columnIndex);
            }
        }
    }

    public Block[][] getBlocks() {
        return blocks;
    }

    public int getLineCount() {
        return lineCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public Block getBlock(int line, int column) {
        return blocks[line][column];
    }

    public boolean isOnTop(Block block) {
        int line = block.getLine();
        return line == 0;
    }

    public boolean isOnBottom(Block block) {
        int line = block.getLine();
        return line == lineCount - 1;
    }

    public boolean isOnLeftBorder(Block block) {
        int column = block.getColumn();
        return column == 0;
    }

    public boolean isOnRightBorder(Block block) {
        int column = block.getColumn();
        return column == columnCount - 1;
    }

    public boolean isOnBorder(Block block) {
        return isOnTop(block) || isOnBottom(block) || isOnLeftBorder(block) || isOnRightBorder(block);
    }

    public ArrayList<ArenaElement> getElements() {
        return elements;
    }

    public void addElement(ArenaElement element) {
        elements.add(element);
    }


    public void addElements(ArrayList<ArenaElement> elements) {
        this.elements.addAll(elements);
    }

    public ArrayList<Obstacle> getObstacles(){
        ArrayList<Obstacle> obstacles = null;
        for (ArenaElement element : elements){
            if (element instanceof Obstacle){
                obstacles.add((Obstacle)element);
            }
        }
        return obstacles;
    }

    public ArrayList<Agent> getAgents(){
        ArrayList<Agent> agents = new ArrayList<>();
        for (ArenaElement element : elements){
            if (element instanceof Agent){
                agents.add((Agent)element);
            }
        }
        return agents;
    }

    public ArrayList<Treasure> getTreasures(){
        ArrayList<Treasure> obstacles = null;
        for (ArenaElement element : elements){
            if (element instanceof Treasure){
                obstacles.add((Treasure)element);
            }
        }
        return obstacles;
    }

    public void addElementsByBlock(HashMap<Block, ArenaElement> elementsByBlocks){
        this.elementsByBlocks.putAll(elementsByBlocks);
    }

    public void addElementsByBlock(Block key, ArenaElement value){
        this.elementsByBlocks.put(key, value);
    }

    public ConcurrentHashMap<Block, ArenaElement> getElementsByBlocks() {
        return elementsByBlocks;
    }
}
