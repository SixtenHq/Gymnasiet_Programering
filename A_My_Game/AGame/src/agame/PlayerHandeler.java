/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author sixten.holmqvist
 */
public class PlayerHandeler implements Runnable {

    public Thread t = new Thread(this);
    Socket s;
    DataInputStream in;
    DataOutputStream ut;
    Model m;
    Player p;
    ArrayList<Byte> skaUt = new ArrayList<>();

    public PlayerHandeler(Socket s, Model m) {
        this.s = s;
        this.m = m;
        p = m.givePlayer(this);
        if (p == null) {
            sendToBig();
        }
        try {
            in = new DataInputStream(s.getInputStream());
            ut = new DataOutputStream(s.getOutputStream());
        } catch (IOException ex) {
            System.out.println("!Could not initiate streams in " + this + "!");
        }
        t.start();
    }

    public void run() {
        while (true) {
            try {
                if (in.read() != 16) {
                    System.out.println("ERROR message wrong");
                }
                if (in.read() != 45) {
                    System.out.println("ERROR message wrong");
                }
                if (in.read() != 125) {
                    System.out.println("ERROR message wrong");
                }
                int messageType = (int) (in.read() * in.read());
                int MessageLenghtLenght = in.read();
                int MessageLenght = 0;
                for (int i = 0; i < MessageLenghtLenght; i++) {
                    MessageLenght += (int) (in.read());
                }

                switch (messageType) {
                    case 1:
                        readInput(MessageLenght);
                        break;
                    case 2:
                        p.setMoving(false);
                        break;
                }

                ut.flush();
            } catch (IOException ex) {
                System.out.println("Lost client");
                m.removePlayer(p);
                break; //Klienten har kopplat ned sig. Avsluta while
            }
        }
        try {
            s.close();
        } catch (IOException e) {
            System.out.println("!Faild to close player " + this + "!");
        }
    }

    private void readInput(int MessageLenght) throws IOException {
        int input = 0;
        for (int i = 0; i < MessageLenght; i++) {
            input += in.read();
        }

        switch (input) {
            case 87: //w
                p.setFacing(1);
                p.setMoving(true);
                break;
            case 65: //a
                p.setFacing(2);
                p.setMoving(true);
                break;
            case 83: //s
                p.setFacing(3);
                p.setMoving(true);
                break;
            case 68: //d
                p.setFacing(4);
                p.setMoving(true);
                break;

        }
    }

    public void sendGraphic() {
        if (t.isAlive()) {

            skaUt.clear();
            skaUt.add(m.getMapXSize());
            ArrayList<Byte> map = m.getMap();

            for (int i = 0; i < map.size(); i++) { //map 
                skaUt.add(map.get(i));
            }
            skaUt.add((byte) 127);

            skaUt.add((byte) m.getNumOfPlayers());
            for (int i = 0; i < m.getNumOfPlayers(); i++) {
                for (int j = 1; j < m.getPlayerPosition(i).getX() / 127; j++) {
                    skaUt.add((byte) 127);
                }
                skaUt.add((byte) (m.getPlayerPosition(i).getX() % 127));
                for (int j = 1; j < m.getPlayerPosition(i).getY() / 127; j++) {
                    skaUt.add((byte) 127);
                }
                skaUt.add((byte) (m.getPlayerPosition(i).getY() % 127));
            }

            send(0);
        }
    }

    private void sendToBig() {
        skaUt.clear();
        send(1);
    }

    public void send(int a) {
        byte[] b = new byte[skaUt.size() + 3 + 2 + 1 + skaUt.size() / 127 + 1];

        b[0] = 16; //new message identefier
        b[1] = 45; //new message identefier
        b[2] = 125; //new message identefier

        switch (a) { //spesific message identefier
            case 0:
                b[3] = 1;
                b[4] = 1;
                break;
            case 1:
                b[3] = 1;
                b[4] = 2;
        }
        if (skaUt.size() / 127 + 1 < 128) { //message length lenght
            b[5] = (byte) (skaUt.size() / 127 + 1);
        } else {
            System.out.println("To long message exeption");
        }

        int i = 0;
        for (; i < skaUt.size() / 127; i++) { //message lenght 
            b[6 + i] = 127;
        }
        if (i != 0) {
            i++;
        }
        b[6 + i] = (byte) (skaUt.size() % 127);
        i++;

        for (int j = 0; j < skaUt.size(); j++) { //the massege
            b[6 + i + j] = skaUt.get(j);
        }

        /*System.out.print("[");
        for (int j = 0; j < b.length; j++) { //skeiver ut pketet
            System.out.print(b[j] + ", ");
        }
        System.out.println("]");*/
        try {
            ut.write(b);
        } catch (IOException ex) {
            System.out.println("Error rwighting to ut stream");
        }

    }

}
