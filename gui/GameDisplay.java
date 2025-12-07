package gui;

import java.awt.Graphics;

import javax.swing.JPanel;

import engine.characters.Agent;
import engine.mapElements.*;
import engine.process.*;

/**
 * Copyright SEDAMOP - Software Engineering
 *
 * @author tianxiao.liu@cyu.fr
 *
 */
public class GameDisplay extends JPanel {

    private static final long serialVersionUID = 1L;

    private Arena map;
//    private MobileElementManager manager;
    private PaintStrategy paintStrategy = new PaintStrategy();

    private ArenaManager manager;

    public GameDisplay(Arena map, ArenaManager manager) {
        this.map = map;
        this.manager = manager;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        paintStrategy.paint(map, g);


        for (AgentManager agentManager : manager.getExplorerManagers()){
            Agent agent = agentManager.getAgent();
            paintStrategy.paint(agent, g);

        }

    }

}
