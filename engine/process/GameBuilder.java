package engine.process;

import config.GameConfig;
import engine.characters.Agent;
import engine.characters.Animal;
import engine.mapElements.*;

import java.util.ArrayList;
import java.util.List;

public class GameBuilder {

    public static Arena buildArena() {
        return initArenaElements();
    }

    public static Arena initArenaElements(){
        Arena arena = new Arena(GameConfig.LINE_COUNT, GameConfig.COLUMN_COUNT);

        generateRandomTreasures(GameConfig.NB_TREASURES, arena);

        arena.addElements(initObstacles(GameConfig.NB_OBSTACLES, arena));

        arena.addElements(initAnimals(GameConfig.NB_ANIMALS, arena));

        return arena;

    }
    
    public static Block generateAgentPosition(int type, Arena arena) {

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
        int zone = getZone(arena);

        int ro = (zone / 4) * GameConfig.NB_ZONES_LIG;
        int col = (zone % 4) * GameConfig.NB_ZONES_COL;

        return new Block(ro + line, col + column);
    }



    private static int getZone(Arena arena) {
        ArrayList<Integer> zoneLibres = Utility.getZoneLibre(arena);
       // System.out.println("zone libres " + zoneLibres);
        int indexZoneLibre = Utility.getRandomNumber(1, zoneLibres.size()-1);
        return zoneLibres.get(indexZoneLibre);
    }


    public static ArrayList<AgentManager> buildAgents(Arena arena, ArenaManager arenaManager) {
        ArrayList<AgentManager> managers = new ArrayList<>();
        managers.addAll(generateAgentManagers(arena, arenaManager, GameConfig.NB_REACTIFS, Agent.REACTIVE_AGENT));
        managers.addAll(generateAgentManagers(arena, arenaManager, GameConfig.NB_COGNITIFS, Agent.COGNITIVE_AGENT));
        managers.addAll(generateAgentManagers(arena, arenaManager, GameConfig.NB_COMMUNICANTS, Agent.COMMUNICATIVE_AGENT));

        return managers;
    }

    public static ArrayList<AgentManager> generateAgentManagers(Arena arena, ArenaManager arenaManager, int nbAgentManagers, int type){

        ArrayList<AgentManager> managers = new ArrayList<AgentManager>();
        int middle = nbAgentManagers / 2;
        for (int i = 0; i < nbAgentManagers; i++) {
            Agent agent = null;
            if(type == Agent.REACTIVE_AGENT){
                agent = AgentFactory.constructAgent(type);
            } else {
                 if(i < middle){
                    agent = AgentFactory.constructAgentCognitif(type, 1);
                } else {
                    agent = AgentFactory.constructAgentCognitif(type, 2);
                }
            }

            agent.setBlock(generateAgentPosition(type, arena));
            arena.addElement(agent);
            managers.add(new AgentManager(agent, arena, arenaManager));
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
        }
    }

    public static ArrayList<ArenaElement> initObstacles(int nbObstacles, Arena board){
        ArrayList<ArenaElement> obstacles = new ArrayList<ArenaElement>();
        int column, line, i;
        Block obstaclePosition;
        for(i = 0; i<nbObstacles; i++) {
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

    public static ArrayList<ArenaElement> initAnimals(int nbAnimals, Arena board){
        ArrayList<ArenaElement> animals = new ArrayList<>();
        int column, line, i;
        Block animalPosition;
        for(i = 0; i<nbAnimals; i++) {
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
