package gui;

import config.GameConfig;
import engine.mapElements.Arena;
import engine.process.AgentManager;
import engine.process.ArenaManager;
import engine.process.GameBuilder;
import engine.process.Utility;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainGui extends JFrame implements Runnable {

    private static final long serialVersionUID = 1L;

    private final static Dimension preferredSize =
            new Dimension(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);

    private ArenaManager manager;

    private StatsPanel statsPanel;
    private GameDisplay gameDisplay;

    private boolean running = false;

    private int maxIterations = 200;

    private JButton startButton;
    private JButton stopButton;

    public MainGui(String title) {
        super(title);
        init();
    }

    private void init() {

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        Arena environment = GameBuilder.buildArena();
        manager = new ArenaManager(environment);

        gameDisplay = new GameDisplay(environment, manager);
        gameDisplay.setPreferredSize(preferredSize);
        contentPane.add(gameDisplay, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(250, GameConfig.WINDOW_HEIGHT));

        statsPanel = new StatsPanel(manager);
        rightPanel.add(statsPanel, BorderLayout.CENTER);

        LegendPanel legend = new LegendPanel();
        rightPanel.add(legend, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        startButton = new JButton("Démarrer");
        stopButton = new JButton("Arrêter");

        buttonsPanel.add(startButton);
        buttonsPanel.add(stopButton);

        rightPanel.add(buttonsPanel, BorderLayout.SOUTH);

        contentPane.add(rightPanel, BorderLayout.EAST);

        startButton.addActionListener(e -> {
            running = true;
            manager.setPaused(false);
        });

        stopButton.addActionListener(e -> {
            running = false;
            manager.setPaused(true);
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);

    }

    @Override
    public void run() {

        while (true) {

            if (!running) {
                sleep(50);
                continue;
            }

            for (AgentManager explorerManager : manager.getAgentManagers()) {
                if (!explorerManager.isAlive()) {
                    explorerManager.start();
                }
            }

            sleep(GameConfig.GAME_SPEED);

            manager.increaseNbRounds();
            Utility.unitTime();

            statsPanel.updateStats();
            gameDisplay.repaint();

            if (manager.getNbRounds() >= maxIterations || manager.getNbCollectedTreasures() >= GameConfig.NB_TREASURES) {
                running = false;
                manager.setPaused(true);
                manager.stopAll();
                break;
            }
        }

        System.out.println("Tous les threads sont arrêtés. Simulation terminée.");
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException ignored) {}
    }
}