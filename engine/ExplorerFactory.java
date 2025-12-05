package engine;

import data.Explorer;

public class ExplorerFactory {

    public static Explorer constructExplorer(int explorer_type){
        Explorer explorer =null;
        switch (explorer_type){
            case Explorer.REACTIVE_EXPLORER :
                explorer = new Explorer(Explorer.REACTIVE_HEALTH, Explorer.REACTIVE_STRENGTH, Explorer.REACTIVE_EXPLORER);
                break;

            case Explorer.COGNITIVE_EXPLORER :
                explorer = new Explorer(Explorer.COGNITIVE_HEALTH, Explorer.COGNITIVE_STRENGTH, Explorer.COGNITIVE_EXPLORER);
                break;
            case Explorer.COMMUNICATIVE_EXPLORER:
                explorer = new Explorer(Explorer.COMMUNICATIVE_HEALTH, Explorer.COMMUNICATIVE_STRENGTH, Explorer.COMMUNICATIVE_EXPLORER);
                break;

        }
        return explorer;

    }

    public static Explorer setExplorer(Explorer explorer){
        int explorer_type = explorer.getExplorerType();
        switch (explorer_type){
            case Explorer.REACTIVE_EXPLORER :
                explorer.setHealth(Explorer.REACTIVE_HEALTH);
                explorer.setStrength(Explorer.REACTIVE_STRENGTH);
                explorer.setBlock(GameBuilder.generateExplorerPosition());
                break;

            case Explorer.COGNITIVE_EXPLORER :
                explorer.setHealth(Explorer.COGNITIVE_HEALTH);
                explorer.setStrength(Explorer.COGNITIVE_STRENGTH);
                explorer.setBlock(GameBuilder.generateExplorerPosition());
                break;
            case Explorer.COMMUNICATIVE_EXPLORER:
                explorer.setHealth(Explorer.COMMUNICATIVE_HEALTH);
                explorer.setStrength(Explorer.COMMUNICATIVE_STRENGTH);
                explorer.setBlock(GameBuilder.generateExplorerPosition());
                break;

        }
        return explorer;

    }
}
