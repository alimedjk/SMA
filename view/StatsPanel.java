package view;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import config.GameConfig;
import data.*;
import engine.*;

/**
 * Copyright SEDAMOP - Software Engineering
 *
 * @author tianxiao.liu@cyu.fr
 *
 */
public class StatsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private EnvironmentManager manager;

    private static final Dimension labelsPreferredSize = new Dimension(GameConfig.WINDOW_WIDTH/2, GameConfig.WINDOW_HEIGHT/2);

    private JLabel iterations = new JLabel();
    private JLabel collectedTreasures = new JLabel();
    private JLabel nbCombats = new JLabel();

    public StatsPanel(EnvironmentManager manager) {
        this.manager = manager;
        init();
    }


    public void init() {
        this.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); // Utilisation de BoxLayout pour un alignement vertical
        this.add(centerPanel, BorderLayout.CENTER);

        // Ajuster la taille préférée du panneau
        centerPanel.setMaximumSize(new Dimension(200, 100)); // Exemple de taille réduite
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Marges internes

        // Ajouter les composants avec une marge
        centerPanel.add(Box.createVerticalStrut(5)); // Espacement vertical
        centerPanel.add(new JLabel("Statistiques :"));
        centerPanel.add(Box.createVerticalStrut(10)); // Espacement vertical
        centerPanel.add(iterations);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(collectedTreasures);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(nbCombats);

        updateStats();
    }

    public void updateStats(){
        iterations.setText("Nombre de tours :" + manager.getNbRounds());
        collectedTreasures.setText("Nombre de trésors collectés :" + manager.getNbCollectedTreasures());
        nbCombats.setText("Nombre de combats disputés :" + manager.getNbCombats());
    }

}
