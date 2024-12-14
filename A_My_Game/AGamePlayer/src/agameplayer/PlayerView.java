/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agameplayer;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;

/**
 *
 * @author sixten.holmqvist
 */
public class PlayerView extends JPanel implements KeyListener {

    private AGamePlayer aGP;

    public PlayerView(AGamePlayer aGM) {
        this.aGP = aGM;
        this.addKeyListener(this);
        this.setFocusable(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        aGP.sendKey(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        aGP.stopSendKey();
    }

    public void uppdate() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try{
        if (aGP.isAktive()) {
            for (int i = 0; i < aGP.getMap().size(); i++) {
                g.drawImage(aGP.getTexture(aGP.getMap().get(i)), i * 50 % (aGP.getMapXSize() * 50), i * 50 / (aGP.getMapXSize() * 50) * 50, 50, 50, this);
            }

            if (aGP.getpP().size() > 0) {
                for (int i = 0; i < aGP.getpP().size(); i++) {

                    g.fillOval((int) aGP.getpP().get(i).getX(), (int) aGP.getpP().get(i).getY(), 50, 50);
                }
            }
        }
        } catch (Exception e){
            System.out.println("0<0");
        }
        
    }
}
