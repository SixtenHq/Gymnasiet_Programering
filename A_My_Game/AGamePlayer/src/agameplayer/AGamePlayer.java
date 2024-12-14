/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package agameplayer;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author sixten.holmqvist
 */
public class AGamePlayer extends JFrame implements Runnable {

    private Socket so;
    private DataInputStream in;
    private DataOutputStream ut;
    private PlayerView pV;
    private byte[] skaUt;
    private ArrayList<Byte> map = new ArrayList<>();
    private int mapXSize;
    public Thread t = new Thread(this);
    private ArrayList<PlayerPositions> pP = new ArrayList<>();
    private ArrayList<BufferedImage> textures = new ArrayList<>();
    boolean aktive = false;

    public static void main(String[] args) throws IOException {
        AGamePlayer agp = new AGamePlayer();

    }

    public AGamePlayer() throws IOException {
        so = new Socket("localhost", 3000);
        in = new DataInputStream(so.getInputStream());
        ut = new DataOutputStream(so.getOutputStream());
        pV = new PlayerView(this);
        add(pV);
        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        textures.add(ImageIO.read(new File("textures\\dirt.png")));
        textures.add(ImageIO.read(new File("textures\\wall.png")));
        textures.add(ImageIO.read(new File("textures\\water.png")));
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
                        renderMap(MessageLenght);
                        break;
                    case 2:
                        System.out.println("Lobby Full");
                        System.exit(0);
                        break;
                }
            } catch (IOException ex) {
                System.out.println("conection lost");
                System.exit(0);
            }
        }
    }

    public void renderMap(int a) throws IOException {
        map.clear();
        pP.clear();
        mapXSize = in.read();
        do {
            map.add(in.readByte());
        } while (map.get(map.size() - 1) != 127);
        map.remove(map.size() - 1);
        int numOfPlayers = in.read();
        for (int i = 0; i < numOfPlayers; i++) {
            pP.add(new PlayerPositions());
            {
                double x;
                do {
                    x = in.read();
                    pP.get(i).setX(pP.get(i).getX() + x);
                } while (x == 127);
            }
            {
                double x;
                do {
                    x = in.read();
                    pP.get(i).setY(pP.get(i).getY() + x);
                } while (x == 127);
            }
        }
        aktive = true;
        pV.uppdate();
    }

    public boolean isAktive() {
        return aktive;
    }
    

    public void sendKey(int a) {

        byte[] b = new byte[a / 127 + 1];

        int i = 0;
        for (; i < a / 127; i++) {
            b[i] = 127;
        }
        if (b.length > 1) {
            i++;
        }

        b[i] = (byte) (a % 127);
        skaUt = b;
        send(0);
    }

    public void stopSendKey() {
        byte[] b = {};
        skaUt = b;
        send(1);
    }

    public void send(int a) {
        byte[] b = new byte[skaUt.length + 3 + 2 + 1 + skaUt.length / 127 + 1];

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
                break;
        }
        if (skaUt.length / 127 + 1 < 128) { //message length lenght
            b[5] = (byte) (skaUt.length / 127 + 1);
        } else {
            System.out.println("To long message exeption");
        }

        int i = 0;
        for (; i < skaUt.length / 127; i++) { //message lenght 
            b[6 + i] = 127;
        }
        if (i != 0) {
            i++;
        }
        b[6 + i] = (byte) (skaUt.length % 127);
        i++;

        for (int j = 0; j < skaUt.length; j++) { //the massege
            b[6 + i + j] = skaUt[j];
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

    public ArrayList<Byte> getMap() {
        return map;
    }

    public int getMapXSize() {
        return mapXSize;
    }

    public ArrayList<PlayerPositions> getpP() {
        return pP;
    }

    public BufferedImage getTexture(int a) {
        return textures.get(a - 1);
    }

}
