package engine.process;

import config.GameConfig;
import engine.characters.Agent;
import engine.characters.Animal;
import engine.mapElements.*;

import java.util.ArrayList;
import java.util.List;

public class GameBuilder {

    public static Arena buildMap() {
        return initMapElements();
    }

    public static Arena initMapElements(){
        Arena board = new Arena(GameConfig.LINE_COUNT, GameConfig.COLUMN_COUNT);

        generateRandomTreasures(GameConfig.NB_TREASURES, board);

        board.addElements(initObstacles(GameConfig.NB_OBSTACLES, board));

        board.addElements(initAnimals(GameConfig.NB_ANIMALS, board));


        return board;

    }
    
    public static Block generateAgentPosition(int type, Arena board) {

        int line = Utility.getRandomNumber(0, GameConfig.NB_ZONES_LIG - 1);

        int column;
        List<Integer> cols = new ArrayList<>();
        int start = (line % 2 == 0) ? 1 : 0;

        for (int c = start; c < GameConfig.NB_ZONES_COL; c += 2) {
            cols.add(c);
        }
        column = cols.get(Utility.getRandomNumber(0, cols.size() - 1));
        if (type != Agent.COMMUNICATIVE_AGENT) {
            return new Block(line, column);
        }
        int zone = getZone(board);

        int ro = (zone / 4) * GameConfig.NB_ZONES_LIG;
        int col = (zone % 4) * GameConfig.NB_ZONES_COL;

        return new Block(ro + line, col + column);
    }



    private static int getZone(Arena board) {
        ArrayList<Integer> zoneLibres = Utility.getZoneLibre(board);
        System.out.println("zone libres " + zoneLibres);
        int indexZoneLibre = Utility.getRandomNumber(1, zoneLibres.size()-1);
        return zoneLibres.get(indexZoneLibre);
    }


        public static ArrayList<AgentManager> buildInitMobile(Arena board, ArenaManager environmentManager) {

//        intializeExplorer(environment, manager);

        ArrayList<AgentManager> managers = new ArrayList<>();
        managers.addAll(generateAgentManagers(board, environmentManager, GameConfig.NB_REACTIFS, Agent.REACTIVE_AGENT));
        managers.addAll(generateAgentManagers(board, environmentManager, GameConfig.NB_COGNITIFS, Agent.COGNITIVE_AGENT));
        managers.addAll(generateAgentManagers(board, environmentManager, GameConfig.NB_COMMUNICANTS, Agent.COMMUNICATIVE_AGENT));

        return managers;
    }

//    private static void intializeExplorer(Environment environment, ExplorerManager manager) {
//        Block block = environment.getBlock(GameConfig.LINE_COUNT - 1, (GameConfig.COLUMN_COUNT - 1) / 2);
//        Explorer aircraft = new Explorer(block);
//        manager.set(aircraft);
//    }

    public static ArrayList<AgentManager> generateAgentManagers(Arena board, ArenaManager environmentManager, int nbExplorerManagers, int type){

        ArrayList<AgentManager> managers = new ArrayList<AgentManager>();
        int middle = nbExplorerManagers / 2;
        for (int i = 0; i < nbExplorerManagers; i++) {
            Agent agent = null;
            if(type == Agent.REACTIVE_AGENT){
                if(i < middle){
                    agent = AgentFactory.constructExplorer(type, 1);
                }else {
                    agent = AgentFactory.constructExplorer(type, 2);
                }
            }else{
                 if(i < middle){
                    agent = AgentFactory.constructExplorerCognitif(type, 1);
                }else {
                    agent = AgentFactory.constructExplorerCognitif(type, 2);
                }
                 
            }
            

            agent.setBlock(generateAgentPosition(type, board));
            board.addElement(agent);
            managers.add(new AgentManager(agent, board, environmentManager));
        }
        return managers;
    }

    public static void generateRandomTreasures(int nbTreasures, Arena board) {
        int line, column;
        for(int i = 0; i < nbTreasures; i++) {
            do {
                line = Utility.getRandomNumber(0, (GameConfig.NB_ZONES_LIG * 4) - 1) ;
                column = Utility.getRandomNumber(0, (GameConfig.NB_ZONES_COL * 4) - 1 );

            } while(Utility.isElementNBlockNearElement(board, new Block(line, column), 2)
                    && ((column / 4 == 0) && (line / 4 == 0)));

            Block position = new Block(line, column);
            Treasure treasure = new Treasure(position);
            board.addElementsByBlock(position, treasure);
            board.addElement(treasure);
//                environment.addElement((Treasure)StaticElementFactory.createStaticElement(
//                        StaticElementFactory.TREASURE, position));
        }
    }

    public static ArrayList<ArenaElement> initObstacles(int nbSlowingDownObstacles, Arena board){
        ArrayList<ArenaElement> obstacles = new ArrayList<ArenaElement>();
        int column, line, i, randInstance;
        Block obstaclePosition;
        for(i = 0; i<nbSlowingDownObstacles; i++) {
            do {
                column = Utility.getRandomNumber(1, board.getColumnCount());
                line = Utility.getRandomNumber(1, board.getLineCount());
                obstaclePosition = new Block(line, column);
            }while((Utility.getEnvironmentElementFromPosition(board, obstaclePosition) != null
                    || ((column / 4 == 0) && (line / 4 == 0)))
                    || Utility.isLineTreasure(line, board.getElements())
                    || Utility.isColumnTreasure(column, board.getElements()));

            Obstacle obstacle =new Obstacle(new Block(line, column));
            obstacles.add(obstacle);
            board.addElementsByBlock(obstacle.getBlock(), obstacle);
        }

        return obstacles;

    }

    public static ArrayList<ArenaElement> initAnimals(int nbSlowingDownAnimals, Arena board){
        ArrayList<ArenaElement> animals = new ArrayList<>();
        int column, line, i;
        Block animalPosition;
        for(i = 0; i<nbSlowingDownAnimals; i++) {
            do {
                column = Utility.getRandomNumber(1, board.getColumnCount());
                line = Utility.getRandomNumber(1, board.getLineCount());
                animalPosition = new Block(line, column);
            }while(Utility.getEnvironmentElementFromPosition(board, animalPosition) != null
                    || ((column / 4 == 0) && (line / 4 == 0)));

            Animal animal =new Animal(new Block(line, column));
            animals.add(animal);
            board.addElementsByBlock(animal.getBlock(), animal);

        }

        return animals;
    }

}
