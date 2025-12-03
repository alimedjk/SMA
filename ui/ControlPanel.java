package ui;

import core.Environment;
import core.Config;
import javax.swing.*;
import java.awt.*;

/**
 * Panneau de contrôle pour démarrer / arrêter / réinitialiser.
 */
public class ControlPanel extends JPanel {
    private final Environment env;
    public final JButton startBtn = new JButton("Démarrer");
    public final JButton stopBtn = new JButton("Arrêter");
    public final JButton resetBtn = new JButton("Réinitialiser");
    public final JLabel itLabel = new JLabel("Iter: 0");
    public final JLabel combatsLabel = new JLabel("Combats: 0");
    public final JLabel treasuresLabel = new JLabel("Trésors: 0");

    public ControlPanel(Environment env) {
        this.env = env;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(260, env.rows * Config.CELL_SIZE));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(new JLabel("<html><h2>SMA - Explorateurs</h2></html>"));
        add(Box.createRigidArea(new Dimension(0,8)));
        startBtn.setAlignmentX(CENTER_ALIGNMENT);
        stopBtn.setAlignmentX(CENTER_ALIGNMENT);
        resetBtn.setAlignmentX(CENTER_ALIGNMENT);
        add(startBtn); add(Box.createRigidArea(new Dimension(0,6)));
        add(stopBtn); add(Box.createRigidArea(new Dimension(0,6)));
        add(resetBtn); add(Box.createRigidArea(new Dimension(0,12)));
        add(new JLabel("Statistiques:"));
        add(itLabel); add(combatsLabel); add(treasuresLabel);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(new JLabel("<html><h3>Légende des couleurs :</h3></html>"));

        // Ligne par ligne
        add(makeLegendItem(new Color(34,139,34), "Agent Cognitif (Vert)"));
        add(makeLegendItem(new Color(160,82,45), "Agent Réactif (Marron)"));
        add(makeLegendItem(new Color(72,61,139), "Agent Communiquant (Violet)"));
        add(makeLegendItem(Color.RED, "Animal Agressif (Rouge)"));
        add(makeLegendItem(new Color(212,175,55), "Trésor (Or)"));
        add(makeLegendItem(Color.DARK_GRAY, "Obstacle (Gris foncé)"));
        add(makeLegendItem(Color.BLUE, "QG (Bleu)"));
        add(makeLegendItem(new Color(255,105,180,120), "Zone de signal (Rose)"));
    }

    public void updateLabels() {
        itLabel.setText("Iter: " + env.iterations.get());
        combatsLabel.setText("Combats: " + env.combats.get());
        treasuresLabel.setText("Trésors: " + env.treasuresCollected.get());
    }
    private JPanel makeLegendItem(Color c, String text) {
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel colorBox = new JPanel();
        colorBox.setBackground(c);
        colorBox.setPreferredSize(new Dimension(18,18));
        colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        p.add(colorBox);
        p.add(new JLabel(" " + text));
        p.setOpaque(false);
        return p;
    }

}
