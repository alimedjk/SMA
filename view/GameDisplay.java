package view;

import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import config.GameConfig;
import data.*;
import engine.*;

/**
 * Copyright SEDAMOP - Software Engineering
 *
 * @author tianxiao.liu@cyu.fr
 *
 */
public class GameDisplay extends JPanel {

    private static final long serialVersionUID = 1L;

    private Environment map;
//    private MobileElementManager manager;
    private PaintStrategy paintStrategy = new PaintStrategy();

    private EnvironmentManager manager;

    public GameDisplay(Environment map, EnvironmentManager manager) {
        this.map = map;
        this.manager = manager;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        paintStrategy.paint(map, g);




        for (ExplorerManager explorerManager : manager.getExplorerManagers()){
            Explorer explorer = explorerManager.getExplorer();

            paintStrategy.paint(explorer, g);

        }

    }

}
