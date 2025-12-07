package gui;

import javax.swing.*;

import engine.process.Utility;

import java.awt.*;

public class LegendPanel extends JPanel {

    public LegendPanel() {

        setLayout(new GridLayout(0, 1, 5, 5));

        add(createItem("Explorateur Cognitif", "gui/images/cognitive.png"));
        add(createItem("Explorateur Communicatif", "gui/images/communicatif.png"));
        add(createItem("Explorateur Réactif", "gui/images/reactif.png"));
        add(createItem("Trésor", "gui/images/treasure.png"));
        add(createItem("Obstacle (Roche)", "gui/images/rock.png"));
        add(createItem("Creature", "gui/images/animal2.png"));
        add(createItem("Tente 1", "gui/images/tent.png"));
        add(createItem("Tente 2", "gui/images/tent2.png"));
    }

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
