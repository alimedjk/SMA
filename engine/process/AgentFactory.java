package engine.process;

import engine.characters.Agent;
import engine.characters.CognitiveAgent;
import engine.characters.CommunicativeAgent;
import engine.characters.ReactiveAgent;
import engine.mapElements.*;

public class AgentFactory {

    public static Agent constructAgent(int explorer_type){
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

    public static Agent constructAgentCognitif(int explorer_type, int mission){
        Agent agent = null;
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
    public static Agent setAgent(Agent agent, Arena arena) {
        agent.setBlock(GameBuilder.generateAgentPosition(agent.getTypeAgent(), arena));

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
