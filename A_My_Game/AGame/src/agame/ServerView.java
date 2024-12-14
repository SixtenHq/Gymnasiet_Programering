/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agame;

import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author sixten.holmqvist
 */
public class ServerView extends JPanel implements ServerViewInterface {

    private Model m;

    public ServerView(Model m) {
        this.m = m;
        m.registerView(this);

        repaint();
    }

    public void uppdate() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < m.getNumOfTiles(); i++) {
            g.drawImage(m.getTile(i).getTexture(0), (int) m.getTile(i).getPosition().getX(), (int) m.getTile(i).getPosition().getY(), 50, 50, this);
        }
        for (int i = 0; i < m.getNumOfPlayers(); i++) {

            g.fillOval((int) m.getPlayerPosition(i).getX(), (int) m.getPlayerPosition(i).getY(), 50, 50);
        }

    }

}
