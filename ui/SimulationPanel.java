package ui;

import core.*;
import agents.*;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de visualisation Swing. Ne gère pas la boucle (caller appelle env.step()).
 */
public class SimulationPanel extends JPanel {
    private final Environment env;

    public SimulationPanel(Environment env) {
        this.env = env;
        setPreferredSize(new Dimension(env.cols * Config.CELL_SIZE, env.rows * Config.CELL_SIZE));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        for (int x=0;x<env.cols;x++){
            for (int y=0;y<env.rows;y++){
                int px = x*Config.CELL_SIZE;
                int py = y*Config.CELL_SIZE;
                Cell c = env.cell(x,y);
                // background for signals
                if (c.signal > 0) {
                    float alpha = (float)Math.min(1.0, c.signal);
                    Color sc = new Color(1.0f, 1.0f - alpha*0.6f, 1.0f - alpha*0.9f, 0.25f + alpha*0.6f);
                    g2.setColor(sc);
                    g2.fillRect(px,py,Config.CELL_SIZE,Config.CELL_SIZE);
                }
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawRect(px,py,Config.CELL_SIZE,Config.CELL_SIZE);

                if (c.obstacle != null) {
                    g2.setColor(Color.DARK_GRAY);
                    g2.fillRect(px+4,py+4,Config.CELL_SIZE-8,Config.CELL_SIZE-8);
                }
                if (c.treasure != null) {
                    g2.setColor(new Color(212,175,55));
                    g2.fillOval(px+6,py+6,Config.CELL_SIZE-12,Config.CELL_SIZE-12);
                }
                if (c.hq != null) {
                    g2.setColor(Color.BLUE);
                    g2.fillRect(px+2,py+2,Config.CELL_SIZE-4,Config.CELL_SIZE-4);
                }
                if (!c.animals.isEmpty()) {
                    g2.setColor(Color.RED);
                    g2.fillOval(px+8,py+8,Config.CELL_SIZE-16,Config.CELL_SIZE-16);
                }
                if (!c.agents.isEmpty()) {
                    int i=0;
                    for (Object o : c.agents) {
                        Color col = Color.BLACK;
                        if (o instanceof CognitiveAgent) col = new Color(34,139,34);
                        if (o instanceof ReactiveAgent) col = new Color(160,82,45);
                        if (o instanceof CommunicatingAgent) col = new Color(72,61,139);
                        int sx = px + 2 + (i%2)*(Config.CELL_SIZE/2);
                        int sy = py + 2 + (i/2)*(Config.CELL_SIZE/2);
                        g2.setColor(col);
                        g2.fillRect(sx, sy, Config.CELL_SIZE/2 - 4, Config.CELL_SIZE/2 - 4);
                        i++; if (i>3) break;
                    }
                }
            }
        }
        // overlay stats
        //g2.setColor(new Color(255,255,255,200));
        //g2.fillRect(6,6,220,60);
        //g2.setColor(Color.BLACK);
        //g2.drawString("Iter: " + env.iterations.get(), 12, 24);
        //g2.drawString("Combats: " + env.combats.get(), 12, 40);
        //g2.drawString("Trésors: " + env.treasuresCollected.get(), 12, 56);
    }
}

