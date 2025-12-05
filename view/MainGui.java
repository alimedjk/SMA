package view;

import config.GameConfig;
import data.Environment;
import engine.EnvironmentManager;
import engine.ExplorerManager;
import engine.GameBuilder;
import engine.Utility;

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

    private EnvironmentManager manager;

    private StatsPanel statsPanel;
    private GameDisplay dashboard;

    /** Contrôle de la boucle d'affichage */
    private boolean running = false;

    /** Nombre d'itérations maximum */
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

        /** Création de l’environnement */
        Environment environment = GameBuilder.buildMap();
        manager = new EnvironmentManager(environment);

        /** La MAP à GAUCHE */
        dashboard = new GameDisplay(environment, manager);
        dashboard.setPreferredSize(preferredSize);
        contentPane.add(dashboard, BorderLayout.CENTER);

        /** Panneau à DROITE : STATISTIQUES + BOUTONS */
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(250, GameConfig.WINDOW_HEIGHT));

        /** Statistiques */
        statsPanel = new StatsPanel(manager);
        rightPanel.add(statsPanel, BorderLayout.CENTER);
        
        /** Légende */
        LegendPanel legend = new LegendPanel();
        rightPanel.add(legend, BorderLayout.NORTH);

        /** Boutons */
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        startButton = new JButton("Démarrer");
        stopButton = new JButton("Arrêter");

        buttonsPanel.add(startButton);
        buttonsPanel.add(stopButton);

        rightPanel.add(buttonsPanel, BorderLayout.SOUTH);

        contentPane.add(rightPanel, BorderLayout.EAST);

        /** Actions des boutons */

        // START = reprendre la simulation
        startButton.addActionListener(e -> {
            running = true;          // relance le rafraîchissement graphique
            manager.setPaused(false); // dépauser les explorateurs
        });

        // STOP = pause
        //stopButton.addActionListener(e -> {
        //    running = false;         // arrête le rafraîchissement graphique
        //    manager.setPaused(true);  // met en pause les explorateurs
        //});
        stopButton.addActionListener(e -> {
            running = false;
            manager.setPaused(true);
        });


        /** Fenêtre */
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setResizable(false);
    }

    @Override
    public void run() {

        while (true) {

            // PAUSE DE L'AFFICHAGE
            if (!running) {
                sleep(50);
                continue;
            }

            /** Démarrer les threads des explorateurs une seule fois */
            for (ExplorerManager explorerManager : manager.getExplorerManagers()) {
                if (!explorerManager.isAlive()) {
                    explorerManager.start();
                }
            }

            sleep(GameConfig.GAME_SPEED);

            /** LOGIQUE DU JEU */
            manager.increaseNbRounds();
            Utility.unitTime();

            if (manager.getNbRounds() % 10 == 0) {
                Utility.unitTime();
            }

            /** ARRÊT TOTAL À 200 ITÉRATIONS */
            //if (manager.getNbRounds() >= maxIterations) {

            //    System.out.println("Simulation arrêtée après " + maxIterations + " itérations.");

            //    running = false;         // Arrêt de l'affichage
            //    manager.setPaused(true); // Pause des explorateurs
            //    manager.stopAll();       // ARRÊT DÉFINITIF DES THREADS

            //    break; // Sortir de la boucle principale
            //}
            
            if (manager.getNbRounds() >= maxIterations) {
                running = false;
                manager.setPaused(true);
                manager.stopAll();
                break;
            }
            /** MISE À JOUR INTERFACE */
            statsPanel.updateStats();
            dashboard.repaint();
        }

        System.out.println("Tous les threads sont arrêtés. Simulation terminée.");
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException ignored) {}
    }
}
