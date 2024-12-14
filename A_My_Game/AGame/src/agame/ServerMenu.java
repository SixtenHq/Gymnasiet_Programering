/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agame;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author sixten.holmqvist
 */
public class ServerMenu extends JPanel implements ActionListener{

    private Model m;
    private JButton btn;
    private JTextField txf;
    private JLabel lb;
    private JPanel pan;

    public ServerMenu(Model m) {
        this.m = m;
        btn = new JButton("Spawn player");
        txf = new JTextField();
        lb = new JLabel("Position[x,y]");
        pan = new JPanel();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalGlue());
        add(btn);
        
        add(lb);
        add(pan);
        txf.setPreferredSize(new Dimension(80,20));
        pan.add(txf);
        add(Box.createVerticalGlue());
        

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn){
            
        }
    }

}
