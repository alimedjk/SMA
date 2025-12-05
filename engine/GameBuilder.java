package engine;

import config.GameConfig;
import data.*;

import java.util.ArrayList;

public class GameBuilder {

    public static Environment buildMap() {
        return initMapElements();
    }

    public static Environment initMapElements(){
        Environment environment = new Environment(GameConfig.LINE_COUNT, GameConfig.COLUMN_COUNT);

        environment = generateRandomTreasures(10, environment);

        environment.addElements(initObstacles(10, environment));


        environment.addElements(initAnimals(20, environment));


        return environment;

    }
    public static Block generateExplorerPosition(){
        // Générer une ligne aléatoire (0 à 3)
        int line = Utility.getRandomNumber(0, 4);

        // Générer une colonne en fonction de la ligne
        int column;
        if (line % 2 == 0) {
            // Lignes paires (0 et 2)
            column = (Utility.getRandomNumber(0,1) == 1) ? 1 : 3;
        } else {
            // Lignes impaires (1 et 3)
            column = (Utility.getRandomNumber(0,1) == 1) ? 0 : 2;
        }
        return new Block(line, column);
    }
    public static ArrayList<AgentManager> buildInitMobile(Environment environment, EnvironmentManager environmentManager) {

//        intializeExplorer(environment, manager);

        ArrayList<AgentManager> managers = new ArrayList<>();
        managers.addAll(generateExplorerManagers(environment, environmentManager, GameConfig.NB_REACTIFS, Agent.REACTIVE_AGENT));
        managers.addAll(generateExplorerManagers(environment, environmentManager, GameConfig.NB_COGNITIFS, Agent.COGNITIVE_AGENT));
        managers.addAll(generateExplorerManagers(environment, environmentManager, GameConfig.NB_COMMUNICANTS, Agent.COMMUNICATIVE_AGENT));

        return managers;
    }

//    private static void intializeExplorer(Environment environment, ExplorerManager manager) {
//        Block block = environment.getBlock(GameConfig.LINE_COUNT - 1, (GameConfig.COLUMN_COUNT - 1) / 2);
//        Explorer aircraft = new Explorer(block);
//        manager.set(aircraft);
//    }

    public static ArrayList<AgentManager> generateExplorerManagers(Environment environment, EnvironmentManager environmentManager, int nbExplorerManagers, int type){

        ArrayList<AgentManager> managers = new ArrayList<AgentManager>();

        for (int i = 0; i < nbExplorerManagers; i++) {
            Agent agent = ExplorerFactory.constructExplorer(type);

            agent.setBlock(generateExplorerPosition());

            managers.add(new AgentManager(agent, environment, environmentManager));
        }
        return managers;
    }

    public static Environment generateRandomTreasures(int nbTreasures, Environment environment) {
        int line, column;
        for(int i = 0; i < nbTreasures; i++) {
            do {
                line = Utility.getRandomNumber(1, Environment.NUM_ZONES * Environment.NUM_ZONES);
                column = Utility.getRandomNumber(1, Environment.NUM_ZONES * Environment.NUM_ZONES);

            } while(Utility.isElementNBlockNearElement(environment, new Block(line, column), 2)
                    && ((column / 4 == 0) && (line / 4 == 0)));

            Block position = new Block(line, column);
            Treasure treasure = new Treasure(position);
            environment.addElementsByBlock(position, treasure);
            environment.addElement(treasure);
//                environment.addElement((Treasure)StaticElementFactory.createStaticElement(
//                        StaticElementFactory.TREASURE, position));
        }
        return environment;
    }

    public static ArrayList<EnvironmentElement> initObstacles(int nbSlowingDownObstacles, Environment map){
        ArrayList<EnvironmentElement> obstacles = new ArrayList<EnvironmentElement>();
        int column, line, i, randInstance;
        Block obstaclePosition;
        for(i = 0; i<nbSlowingDownObstacles; i++) {
            do {
                column = Utility.getRandomNumber(1, map.getColumnCount());
                line = Utility.getRandomNumber(1, map.getLineCount());
                obstaclePosition = new Block(line, column);
            }while((Utility.getEnvironmentElementFromPosition(map, obstaclePosition) != null
                    || ((column / 4 == 0) && (line / 4 == 0)))
                    || Utility.isLineTreasure(line, map.getElements())
                    || Utility.isColumnTreasure(column, map.getElements()));

            Obstacle obstacle =new Obstacle(new Block(line, column));
            obstacles.add(obstacle);
            map.addElementsByBlock(obstacle.getBlock(), obstacle);
        }

        return obstacles;

    }

    public static ArrayList<EnvironmentElement> initAnimals(int nbSlowingDownAnimals, Environment map){
        ArrayList<EnvironmentElement> animals = new ArrayList<EnvironmentElement>();
        int column, line, i;
        Block animalPosition;
        for(i = 0; i<nbSlowingDownAnimals; i++) {
            do {
                column = Utility.getRandomNumber(1, map.getColumnCount());
                line = Utility.getRandomNumber(1, map.getLineCount());
                animalPosition = new Block(line, column);
            }while(!(Utility.getEnvironmentElementFromPosition(map, animalPosition) instanceof Treasure)
            		&& Utility.getEnvironmentElementFromPosition(map, animalPosition) != null
                    || ((column / 4 == 0) && (line / 4 == 0)));

            Animal animal =new Animal(new Block(line, column));
            animals.add(animal);
            map.addElementsByBlock(animal.getBlock(), animal);

        }

        return animals;

    }

}
