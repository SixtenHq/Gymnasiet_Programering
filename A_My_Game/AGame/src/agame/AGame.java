/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package agame;

import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author sixten.holmqvist
 */
public class AGame extends JFrame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AGame ag = new AGame();
    }

    AGame() {
        Model m = new Model();
        ServerView sW = new ServerView(m);
        ServerMenu sM = new ServerMenu(m);
        PlayerFinder pF = new PlayerFinder(m);
        Time t = new Time(m);

        this.setMinimumSize(new Dimension(800, 800));
        add(sW);

        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

}
