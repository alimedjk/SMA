package engine.process;

import engine.characters.Agent;
import engine.characters.CognitiveAgent;
import engine.characters.CommunicativeAgent;
import engine.characters.ReactiveAgent;
import engine.mapElements.*;

public class AgentFactory {

    public static Agent constructExplorer(int explorer_type, int mission){
        Agent agent = null;

        switch (explorer_type) {
            case Agent.REACTIVE_AGENT:
                agent = new ReactiveAgent();
                break;
            case Agent.COMMUNICATIVE_AGENT:
                agent = new CommunicativeAgent();
                break;
        }

        return agent;
    }

    public static Agent constructExplorerCognitif(int explorer_type, int mission){
        Agent agent = null;
        System.out.println(explorer_type);
        switch (explorer_type) {
            case Agent.COGNITIVE_AGENT:
                agent = new CognitiveAgent(mission);
                break;
            case Agent.COMMUNICATIVE_AGENT:
                agent = new CommunicativeAgent();
                break;
        }

        return agent;
    }
    public static Agent setExplorer(Agent agent, Arena environment) {
        agent.setBlock(GameBuilder.generateAgentPosition(agent.getTypeAgent(), environment));

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
                agent.setHealth(CommunicativeAgent.COMMUNICATIVE_HEALTH);
                agent.setStrength(CommunicativeAgent.COMMUNICATIVE_STRENGTH);
                break;
        }

        return agent;
    }
}
