package view;

import javax.swing.*;
import java.awt.*;
import engine.Utility;

public class LegendPanel extends JPanel {

    public LegendPanel() {

        setLayout(new GridLayout(0, 1, 5, 5));

        add(createItem("Explorateur Cognitif", "sprites/cognitive.png"));
        add(createItem("Explorateur Communicatif", "sprites/communicatif.png"));
        add(createItem("Explorateur Réactif", "sprites/reactif.png"));
        add(createItem("Trésor", "sprites/treasure.png"));
        add(createItem("Obstacle (Roche)", "sprites/rock.png"));
        add(createItem("Animal", "sprites/animal2.png"));
        add(createItem("Tente 1", "sprites/tent.png"));
        add(createItem("Tente 2", "sprites/tent2.png"));
    }

    /**
     * Un élément de légende : une petite icône + un texte
     */
    private JPanel createItem(String text, String imagePath) {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel icon = new JLabel(new ImageIcon(
                Utility.readImage(imagePath)
                        .getScaledInstance(25, 25, Image.SCALE_SMOOTH)
        ));

        JLabel label = new JLabel(text);

        panel.add(icon);
        panel.add(label);

        return panel;
    }
}
