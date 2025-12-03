package ui;

import core.Environment;

import javax.swing.*;
import java.awt.*;

/**
 * Main window that assemble UI.
 */
public class SMAFrame extends JFrame {
    //private final Environment env;
    private final SimulationPanel simPanel;
    private final ControlPanel controlPanel;

    public SMAFrame(Environment env) {
        //this.env = env;
        setTitle("SMA - Explorateurs");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        simPanel = new SimulationPanel(env);
        controlPanel = new ControlPanel(env);

        add(simPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public SimulationPanel getSimPanel() { return simPanel; }
    public ControlPanel getControlPanel() { return controlPanel; }
}

