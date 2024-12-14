/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agame;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sixten.holmqvist
 */
public class Time implements Runnable {

    public Thread t = new Thread(this);
    Model m;
    public Time(Model m) {
        this.m = m;
        t.start();
    }

    public void run() {
        while (true) {
            m.movePlayers();
            m.uppdatePlayerViews();
            m.uppdateViews();
            
                
            
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                System.out.println("sleep error");
            }
            
        }

    }
}