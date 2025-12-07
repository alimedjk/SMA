package gui;

import java.awt.*;
import javax.swing.*;

import config.GameConfig;
import engine.process.ArenaManager;

public class StatsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private ArenaManager manager;

    private JLabel iterations = new JLabel();
    private JLabel collectedTreasures = new JLabel();
    private JLabel nbCombats = new JLabel();
    private JLabel nbAnimalsDead = new JLabel();
    private JLabel nbAgentDead = new JLabel();
    private JLabel nbZoneExplored = new JLabel();

    public StatsPanel(ArenaManager manager) {
        this.manager = manager;
        init();
    }

    public void init() {

        // Le panel global place tout à DROITE
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(200, GameConfig.WINDOW_HEIGHT));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel des labels alignés verticalement
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Style des labels
        Font font = new Font("Arial", Font.PLAIN, 14);
        iterations.setFont(font);
        collectedTreasures.setFont(font);
        nbCombats.setFont(font);
        nbAnimalsDead.setFont(font);
        nbAgentDead.setFont(font);
        nbZoneExplored.setFont(font);

        // Titre
        JLabel title = new JLabel("  Statistiques");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ajout des composants
        centerPanel.add(title);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(iterations);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(collectedTreasures);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(nbCombats);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(nbAnimalsDead);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(nbAgentDead);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(nbZoneExplored);
        centerPanel.add(Box.createVerticalGlue());

        // On place le panel à DROITE dans le BorderLayout
        this.add(centerPanel, BorderLayout.EAST);

        updateStats();
    }

    public void updateStats() {
        iterations.setText("Nombre de tours : " + manager.getNbRounds());
        collectedTreasures.setText("Trésors collectés : " + manager.getNbCollectedTreasures());
        nbCombats.setText("Combats disputés : " + manager.getNbCombats());
        nbAnimalsDead.setText("Nombre d'animaux tués : " +  manager.getNbAnimalsDead());
        nbAgentDead.setText("Nombre d'agents morts : " +  manager.getNbAgentDead());
        nbZoneExplored.setText("Nombre de zones explorées : " +  manager.getNbZoneExplored().size());
    }
}