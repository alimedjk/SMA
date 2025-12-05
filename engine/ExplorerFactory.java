package engine;

import data.Agent;
import data.CognitiveAgent;
import data.CommunicatingAgent;
import data.ReactiveAgent;

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

    public static Agent setExplorer(Agent agent) {
        agent.setBlock(GameBuilder.generateExplorerPosition());

        return agent;
    }
}
