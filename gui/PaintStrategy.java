package gui;

import java.awt.*;

import java.util.ArrayList;

import config.GameConfig;
import engine.characters.Agent;
import engine.characters.Animal;
import engine.mapElements.*;
import engine.process.Utility;


public class PaintStrategy {

    public void paint(Arena map, Graphics graphics) {
        int blockSize = GameConfig.BLOCK_SIZE;
        Block[][] blocks = map.getBlocks();
        ArrayList<ArenaElement> elements = new ArrayList<>(map.getElements());


        for (int lineIndex = 0; lineIndex < map.getLineCount(); lineIndex++) {
            for (int columnIndex = 0; columnIndex < map.getColumnCount(); columnIndex++) {
                Block block = blocks[lineIndex][columnIndex];

                if ((lineIndex < 4) && (columnIndex < 4)) {
                    graphics.setColor(new Color(96, 96, 96));

                    graphics.fillRect(block.getColumn() * blockSize, block.getLine() * blockSize, blockSize, blockSize);
                    if ((lineIndex+columnIndex) % 4 == 1){
                        ArrayList<Integer> position = Utility.getGraphicPosition(block);

                       graphics.drawImage(Utility.readImage("gui/images/tent2.png"),
                                position.get(0), position.get(1), GameConfig.BLOCK_SIZE,
                                GameConfig.BLOCK_SIZE, null);
                    } else if ((lineIndex+columnIndex) % 4 == 3) {

                        ArrayList<Integer> position = Utility.getGraphicPosition(block);

                       graphics.drawImage(Utility.readImage("gui/images/tent2.png"),
                                position.get(0), position.get(1), GameConfig.BLOCK_SIZE,
                                GameConfig.BLOCK_SIZE, null);
                    }
                }
                else {
                    graphics.setColor(new Color(190, 190, 190));

                    graphics.fillRect(block.getColumn() * blockSize, block.getLine() * blockSize, blockSize, blockSize);

                    graphics.setColor(Color.BLACK);
                    graphics.drawRect(block.getColumn() * blockSize, block.getLine() * blockSize, blockSize, blockSize);

                    graphics.setColor(Color.BLACK);
                    ArrayList<Integer> position = Utility.getGraphicPosition(block);

                }
            }
        }

        graphics.setColor(Color.RED);
        int gridSize = Arena.NUM_BLOCKS_PER_ZONE * blockSize;

        Graphics2D g2d = (Graphics2D) graphics;

        Stroke originalStroke = g2d.getStroke();

        g2d.setStroke(new BasicStroke(3));

        g2d.setColor(Color.black);




        for (int line = 0; line <= map.getLineCount(); line += Arena.NUM_BLOCKS_PER_ZONE) {
            for (int column = 0; column <= map.getColumnCount(); column += Arena.NUM_BLOCKS_PER_ZONE) {
                int x = column * blockSize;
                int y = line * blockSize;
                int width = Math.min(gridSize, (map.getColumnCount() - column) * blockSize);
                int height = Math.min(gridSize, (map.getLineCount() - line) * blockSize);

                graphics.drawRect(x, y, width, height);
            }
        }

        g2d.setStroke(originalStroke);

        for (ArenaElement element : elements){
            if (element instanceof Treasure)
                paintTreasure(graphics, (Treasure) element);

            else if (element instanceof Obstacle)
                paintObstacle(graphics, (Obstacle) element);

            else if (element instanceof Animal)
                paintAnimal(graphics, (Animal) element);
        }
    }

    public static void paint(Agent agent, Graphics g) {

        ArrayList<Integer> explorerPos = Utility.getGraphicPosition(agent);

        String agentName = "";

        switch (agent.getTypeAgent()) {
            case Agent.COGNITIVE_AGENT :
                agentName = "cognitive";
                break;

            case Agent.COMMUNICATIVE_AGENT :
                agentName = "communicatif";
                break;

            case Agent.REACTIVE_AGENT :
                agentName = "reactif";
                break;
        }
        g.drawImage(Utility.readImage("gui/images/" + agentName + ".png"),
                explorerPos.get(0), explorerPos.get(1), GameConfig.BLOCK_SIZE,
                GameConfig.BLOCK_SIZE, null);
    }

    private static void paintObstacle(Graphics g, Obstacle obstacle) {

        ArrayList<Integer> obstaclePos = Utility.getGraphicPosition(obstacle);

        g.drawImage(Utility.readImage("gui/images/rock.png"),
                obstaclePos.get(0), obstaclePos.get(1), GameConfig.BLOCK_SIZE,
                GameConfig.BLOCK_SIZE, null);
    }

    private static void paintTreasure(Graphics g, Treasure treasure) {
        ArrayList<Integer> treasurePos = Utility.getGraphicPosition(treasure);
        g.drawImage(Utility.readImage("gui/images/treasure.png"),
                treasurePos.get(0), treasurePos.get(1), GameConfig.BLOCK_SIZE,
                GameConfig.BLOCK_SIZE, null);
    }

    private static void paintAnimal(Graphics g, Animal animal) {
        ArrayList<Integer> animalPos = Utility.getGraphicPosition(animal);
        g.drawImage(Utility.readImage("gui/images/animal2.png"),
                animalPos.get(0), animalPos.get(1), GameConfig.BLOCK_SIZE,
                GameConfig.BLOCK_SIZE, null);
    }
}
