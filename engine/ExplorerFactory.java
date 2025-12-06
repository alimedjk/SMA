package engine;

import data.*;

public class ExplorerFactory {

    public static Agent constructExplorer(int explorer_type){
        Agent agent = null;

        switch (explorer_type) {
            case Agent.REACTIVE_AGENT:
                agent = new ReactiveAgent();
                break;
            case Agent.COGNITIVE_AGENT:
                agent = new CognitiveAgent();
                break;
            case Agent.COMMUNICATIVE_AGENT:
                agent = new CommunicatingAgent();
                break;
        }

        return agent;
    }

    public static Agent setExplorer(Agent agent, Environment environment) {
        agent.setBlock(GameBuilder.generateExplorerPosition(agent.getTypeAgent(), environment));

        return agent;
    }
}
