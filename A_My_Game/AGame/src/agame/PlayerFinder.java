/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author sixten.holmqvist
 */
public class PlayerFinder implements Runnable {

    public Thread t = new Thread(this);
    private ServerSocket listeningSocket;
    Model m;

    public PlayerFinder(Model m) {
        this.m = m;
        try {
            listeningSocket = new ServerSocket(3000);
        } catch (IOException ex) {
            System.out.println("!Error setting socet!");
        }

        t.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket clientSocket = listeningSocket.accept();
                System.out.println(clientSocket.getInetAddress().getHostName() + " connected.");

                new PlayerHandeler(clientSocket, m);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}
