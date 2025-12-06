package view;

import config.GameConfig;
import data.Environment;
import engine.*;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * Copyright SEDAMOP - Software Engineering
 *
 * @author tianxiao.liu@cyu.fr
 *
 */
public class MainGui extends JFrame implements Runnable {

    private static final long serialVersionUID = 1L;

//    private Environment environment;

    private final static Dimension preferredSize = new Dimension(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);

    public final static Dimension statsPanelPreferredSize = new Dimension(GameConfig.WINDOW_WIDTH/2, GameConfig.WINDOW_HEIGHT);

    private EnvironmentManager manager;

    private StatsPanel statsPanel;

    private GameDisplay dashboard;

    public MainGui(String title) {
        super(title);
        init();
    }

    private void init() {

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        Environment environment = GameBuilder.buildMap();
        manager = new EnvironmentManager(environment);

        dashboard = new GameDisplay(environment, manager);
        statsPanel = new StatsPanel(manager);

        dashboard.setPreferredSize(preferredSize);
        contentPane.add(dashboard, BorderLayout.CENTER);

        statsPanel.setPreferredSize(statsPanelPreferredSize);
        contentPane.add(statsPanel, BorderLayout.WEST);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setPreferredSize(preferredSize);
        setResizable(false);
    }

    @Override
    public void run() {
        final int MAX_ROUNDS = 200;

        while (manager.getNbRounds() < MAX_ROUNDS) {

            for (AgentManager agentManager : manager.getExplorerManagers()) {
                if (!agentManager.isAlive()) {
                    agentManager.start();
                }
            }

            try {
                Thread.sleep(GameConfig.GAME_SPEED);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

            // Augmente le nombre de tours
            manager.increaseNbRounds();

            // Simulation d'une unité de temps
            Utility.unitTime();

            if (manager.getNbRounds() % 10 == 0) {
                Utility.unitTime();
            }

            // Mise à jour de l'interface
            statsPanel.updateStats();
            dashboard.repaint();
            statsPanel.repaint();
        }

        // Fin de la boucle
        System.out.println("Le jeu est terminé ");
    }


}
