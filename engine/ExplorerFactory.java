package engine;

import data.*;

public class ExplorerFactory {

    public static Agent constructExplorer(int explorer_type){
        Agent agent = null;

        switch (explorer_type) {
            case Agent.REACTIVE_AGENT:
                agent = new ReactiveAgent();
                break;
            case Agent.COMMUNICATIVE_AGENT:
                agent = new CommunicatingAgent();
                break;
        }

        return agent;
    }

    public static Agent constructExplorerCognitif(int explorer_type, int mission){
        Agent agent = null;

        switch (explorer_type) {
            case Agent.COGNITIVE_AGENT:
                agent = new CognitiveAgent(mission);
                break;
        }

        return agent;
    }
    public static Agent setExplorer(Agent agent, Environment environment) {
        agent.setBlock(GameBuilder.generateExplorerPosition(agent.getTypeAgent(), environment));

        switch (agent.getTypeAgent()){
            case Agent.REACTIVE_AGENT:
                agent.setHealth(ReactiveAgent.REACTIVE_HEALTH);
                agent.setStrength(ReactiveAgent.REACTIVE_STRENGTH);
                break;
            case Agent.COGNITIVE_AGENT:
                agent.setHealth(CognitiveAgent.COGNITIVE_HEALTH);
                agent.setStrength(CognitiveAgent.COGNITIVE_STRENGTH);
                break;
            case Agent.COMMUNICATIVE_AGENT:
                agent.setHealth(CommunicatingAgent.COMMUNICATIVE_HEALTH);
                agent.setStrength(CommunicatingAgent.COMMUNICATIVE_STRENGTH);
                break;
        }

        return agent;
    }
}
