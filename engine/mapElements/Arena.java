package engine.mapElements;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import engine.characters.Agent;

public class Arena {

    public static final int NUM_BLOCKS_PER_ZONE = 4;
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

    public ArrayList<ArenaElement> getElements() {
        return elements;
    }

    public void addElement(ArenaElement element) {
        elements.add(element);
    }

    public void addElements(ArrayList<ArenaElement> elements) {
        this.elements.addAll(elements);
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

    public void addElementsByBlock(Block key, ArenaElement value){
        this.elementsByBlocks.put(key, value);
    }

    public ConcurrentHashMap<Block, ArenaElement> getElementsByBlocks() {
        return elementsByBlocks;
    }
}
