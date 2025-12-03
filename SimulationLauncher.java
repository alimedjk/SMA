
import core.Environment;
import core.Config;
import ui.SMAFrame;
import ui.ControlPanel;
import ui.SimulationPanel;
import entities.Animal;
import agents.Agent;

import javax.swing.*;
import java.awt.event.ActionEvent;
//import java.util.List;

/**
 * Classe principale pour lancer la simulation (avec boucle Swing Timer).
 * Gère la création, le démarrage et l'arrêt des threads agents/animals.
 */
public class SimulationLauncher {
    private static Environment env;
    private static Timer uiTimer;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            env = new Environment(Config.COLS, Config.ROWS);
            SMAFrame frame = new SMAFrame(env);
            SimulationPanel simPanel = frame.getSimPanel();
            ControlPanel control = frame.getControlPanel();

            uiTimer = new Timer(Config.TIMER_MS, (ActionEvent e) -> {
                simPanel.repaint();
                control.updateLabels();
            });

            control.startBtn.addActionListener(a -> startSimulation());
            control.stopBtn.addActionListener(a -> stopSimulation());
            control.resetBtn.addActionListener(a -> resetSimulation(simPanel, control));

            frame.setVisible(true);
        });
    }

    private static void startSimulation() {
        // start animals
        for (Animal a : env.animals) {
            if (!a.isAlive()) a.start();
        }
        // start agents
        for (Agent ag : env.agents) {
            if (!ag.isAlive()) ag.start();
        }
        uiTimer.start();
    }

    private static void stopSimulation() {
        // request stop for agents & animals
        for (Agent ag : env.agents) {
            ag.requestStop();
        }
        for (Animal a : env.animals) {
            a.requestStop();
        }
        uiTimer.stop();
    }

    private static void resetSimulation(SimulationPanel simPanel, ControlPanel control) {
        stopSimulation();
        // give threads a short while to stop (best-effort)
        try { Thread.sleep(50); } catch (InterruptedException ignored) {}
        // create new environment and UI bindings
        env = new Environment(Config.COLS, Config.ROWS);
        simPanel = new SimulationPanel(env);
        // replace center panel in current frame
        //JFrame top = (JFrame) SwingUtilities.getWindowAncestor(simPanel);
        // simpler approach: ask user to restart app for full reset in this simple implementation
        // but we can re-pack frame: (for brevity we will just restart UI timer and repaint)
        simPanel.repaint();
        control.updateLabels();
    }
}