package view;

import java.awt.*;

import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.util.ArrayList;

import config.GameConfig;
import data.*;
import engine.Utility;


public class PaintStrategy {
    /**
     *
     *This method draws the blocks of the map and graphical elements such as treasures, obstacles, and forests.
     *It takes a Map object and a Graphics object as parameters to draw.
     *@param graphics - Graphics to draw on
     *@param map - Map to be drawn
     */

    public void paint(Environment map, Graphics graphics) {
        int blockSize = GameConfig.BLOCK_SIZE;
        Block[][] blocks = map.getBlocks();
        ArrayList<EnvironmentElement> elements = map.getElements();


        for (int lineIndex = 0; lineIndex < map.getLineCount(); lineIndex++) {
            for (int columnIndex = 0; columnIndex < map.getColumnCount(); columnIndex++) {
                Block block = blocks[lineIndex][columnIndex];

                if ((lineIndex < 4) && (columnIndex < 4)) {
                    graphics.setColor(new Color(210, 105, 30)); // Marron orangé

                    graphics.fillRect(block.getColumn() * blockSize, block.getLine() * blockSize, blockSize, blockSize);
                    if ((lineIndex+columnIndex) % 4 == 1){
                        ArrayList<Integer> position = Utility.getGraphicPosition(block);

                        graphics.drawImage(Utility.readImage("tp_explorateurs-main/sprites/tent.png"),
                                position.get(0), position.get(1), GameConfig.BLOCK_SIZE,
                                GameConfig.BLOCK_SIZE, null);
                    } else if ((lineIndex+columnIndex) % 4 == 3) {

                        ArrayList<Integer> position = Utility.getGraphicPosition(block);

                        graphics.drawImage(Utility.readImage("tp_explorateurs-main/sprites/tent2.png"),
                                position.get(0), position.get(1), GameConfig.BLOCK_SIZE,
                                GameConfig.BLOCK_SIZE, null);
                    }
                }
                else {
                    graphics.setColor(new Color(85, 107, 47)); // Vert gazon

                    graphics.fillRect(block.getColumn() * blockSize, block.getLine() * blockSize, blockSize, blockSize);

                    // Dessin des bordures
                    graphics.setColor(Color.BLACK); // Couleur des bordures
                    graphics.drawRect(block.getColumn() * blockSize, block.getLine() * blockSize, blockSize, blockSize);

                    graphics.setColor(Color.BLACK); // Couleur des bordures
                    ArrayList<Integer> position = Utility.getGraphicPosition(block);


//                    graphics.drawImage(Utility.readImage("sprites/grass.png"),
//                            position.get(0), position.get(1), GameConfig.BLOCK_SIZE,
//                            GameConfig.BLOCK_SIZE, null);
                }

            }
        }

        // Dessin des bordures pour chaque carré de 16 blocs
        graphics.setColor(Color.RED); // Couleur des bordures
        int gridSize = Environment.NUM_BLOCKS_PER_ZONE * blockSize; // Taille d'un carré de 16 blocs

        // Conversion de Graphics en Graphics2D
        Graphics2D g2d = (Graphics2D) graphics;

        // Sauvegarde du stroke actuel (utile si vous voulez le restaurer plus tard)
        Stroke originalStroke = g2d.getStroke();

        // Définir un nouveau stroke avec une largeur personnalisée (par exemple, 3 pixels)
        g2d.setStroke(new BasicStroke(3));

        // Dessiner une ligne plus large
        g2d.setColor(Color.black);




        for (int line = 0; line <= map.getLineCount(); line += Environment.NUM_BLOCKS_PER_ZONE) {
            for (int column = 0; column <= map.getColumnCount(); column += Environment.NUM_BLOCKS_PER_ZONE) {
                // Coordonnées des bordures
                int x = column * blockSize;
                int y = line * blockSize;
                int width = Math.min(gridSize, (map.getColumnCount() - column) * blockSize);
                int height = Math.min(gridSize, (map.getLineCount() - line) * blockSize);

                // Dessin des rectangles pour les carrés de 16x16 blocs
                graphics.drawRect(x, y, width, height);
            }
        }

        // Restauration du stroke original
        g2d.setStroke(originalStroke);

        for (EnvironmentElement element : elements){
            if (element instanceof Treasure)
                paintTreasure(graphics, (Treasure) element);

            else if (element instanceof Obstacle)
                paintObstacle(graphics, (Obstacle) element);

            else if (element instanceof Animal)
                paintAnimal(graphics, (Animal) element);


        }


    }

    /**
     *
     *This method draws mobile characters on the map. It takes a list of MobileElementManager as parameter.
     *@param g - Graphics to draw on
     *@param agent - list of MobileElementManager containing mobile characters to draw
     */
    public static void paint(Agent agent, Graphics g) {

        ArrayList<Integer> explorerPos = Utility.getGraphicPosition(agent);

        String explorerName = "";

        switch (agent.getTypeAgent()) {
            case Agent.COGNITIVE_AGENT :
                explorerName = "cognitive";
                break;

            case Agent.COMMUNICATIVE_AGENT :
                explorerName = "communicatif";
                break;

            case Agent.REACTIVE_AGENT :
                explorerName = "reactif";
                break;

        }

        g.drawImage(Utility.readImage("tp_explorateurs-main/sprites/" + explorerName + ".png"),
                explorerPos.get(0), explorerPos.get(1), GameConfig.BLOCK_SIZE,
                GameConfig.BLOCK_SIZE, null);

    }


    /**
     *
     *@brief This method allows to draw an obstacle on the canvas.
     *@param g The graphics context to draw on.
     *@param obstacle The obstacle to draw.
     */
    private static void paintObstacle(Graphics g, Obstacle obstacle) {

        ArrayList<Integer> obstaclePos = Utility.getGraphicPosition(obstacle);

        g.drawImage(Utility.readImage("tp_explorateurs-main/sprites/rock.png"),
                obstaclePos.get(0), obstaclePos.get(1), GameConfig.BLOCK_SIZE,
                GameConfig.BLOCK_SIZE, null);
    }

    /**
     *
     *This method draws a treasure on the canvas.
     *@param g The graphics context to draw on.
     *@param treasure The treasure to draw.
     */

    private static void paintTreasure(Graphics g, Treasure treasure) {
//        Block treasurePos = treasure.getBlock();
        ArrayList<Integer> treasurePos = Utility.getGraphicPosition(treasure);


        g.drawImage(Utility.readImage("tp_explorateurs-main/sprites/treasure.png"),
                treasurePos.get(0), treasurePos.get(1), GameConfig.BLOCK_SIZE,
                GameConfig.BLOCK_SIZE, null);

    }

    private static void paintAnimal(Graphics g, Animal animal) {
//        Block animalPos = animal.getBlock();
        ArrayList<Integer> animalPos = Utility.getGraphicPosition(animal);


        g.drawImage(Utility.readImage("tp_explorateurs-main/sprites/animal2.png"),
                animalPos.get(0), animalPos.get(1), GameConfig.BLOCK_SIZE,
                GameConfig.BLOCK_SIZE, null);

    }

}
