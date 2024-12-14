/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package zelda_package;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author sixten.holmqvist
 */
public class Player {

    String filePath; //vart spelet ligger i datorn
    int[] pPos = {23, 23}; //spelarens startposition
    int hp = 3, wallLengthY = 11 * 3, wallLengthX = 16 * 3; //kartans storlek
    ArrayList<ArrayList<Integer>> map = new ArrayList<>(); //tvådimentionel arraylist för kartan
    BufferedImage front = null; //reserverar plats för bilder
    BufferedImage back = null;
    BufferedImage left = null;
    BufferedImage right = null;
    private ImageObserver ImageObserver;
    int go; //om spelaren går av kanten av ett rum så anvends denna för att veta åt vilket håll som spelaren gick
    int lookDirektion; //används för att bestema åt vilket håll som spelaren ska titta

    public Player(ArrayList<ArrayList<Integer>> maper) {
        map = maper;
        try { //hittar platsen där spelet är lagrat
            String tempFilePath = new File("Zelda").getAbsolutePath();
            filePath = tempFilePath.substring(0, tempFilePath.lastIndexOf("Zelda"));
        } catch (SecurityException ex) {
            System.out.println("!File access denied by OS or anti virus software!");
        }
        try { //lägger till bilder
            front = ImageIO.read(new File(filePath + "\\sprites\\player_front.png"));
            back = ImageIO.read(new File(filePath + "\\sprites\\player_back.png"));
            left = ImageIO.read(new File(filePath + "\\sprites\\player_left.png"));
            right = ImageIO.read(new File(filePath + "\\sprites\\player_right.png"));
        } catch (IOException ex) {
            System.out.println("!Could not find image for player at \"" + filePath + "sprites\"!");
        }
    }

    public void uppdateMap(ArrayList<ArrayList<Integer>> maper) { //uppdaterar kartan när spelaren rör sig till ett nytt rum
        map = maper;
    }

    public boolean addPPosX(int a) { //flyttar spelaren i x-axeln om det inte är en vägg ivägen
        switch (a) {
            case 1 ->
                lookDirektion = 2;
            case -1 ->
                lookDirektion = 4;
        }
        boolean b = flytt(pPos, a, 0);
        if (flytt(pPos, a, 0)) {
            pPos[0] += a;
        }
        return b;
    }

    public boolean addPPosY(int a) { //flyttar spelaren i y-axeln om det inte är en vägg ivägen
        switch (a) {
            case 1 ->
                lookDirektion = 3;
            case -1 ->
                lookDirektion = 1;
        }
        boolean b = flytt(pPos, a, 0);
        if (flytt(pPos, a, 1)) {
            pPos[1] += a;
        }
        return b;
    }

    boolean walls(int[] pos) { //returnerar flase om position pos är i en vägg, annars true
        if (pos[0] < 0) {
            go = 4;
            return false;
        } else if (pos[0] > wallLengthX - 1) {
            go = 2;
            return false;
        } else if (pos[1] < 0) {
            go = 1;
            return false;
        } else if (pos[1] > wallLengthY - 1) {
            go = 3;
            return false;
        } else if (map.get(pos[1]).get(pos[0]) == 1) {
            return false;
        }
        return true;
    }

    boolean flytt(int[] pos, int a, int xy) { //kollar om det går att flytta spelaren eller om det kommer resultera i att spelaren hamnar i en vägg
        int[] temppos = {pos[0], pos[1]};
        temppos[xy] += a;
        return walls(temppos);
    }

    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        switch (lookDirektion) { //ritar spelaren tittandes i olika riktningar beroende på var spelaren går
            case 1 ->
                g.drawImage(back, pPos[0] * 20 - 20, pPos[1] * 20 + 125 - 20, 60, 60, ImageObserver);
            case 2 ->
                g.drawImage(right, pPos[0] * 20 - 20, pPos[1] * 20 + 125 - 20, 60, 60, ImageObserver);
            case 3 ->
                g.drawImage(front, pPos[0] * 20 - 20, pPos[1] * 20 + 125 - 20, 60, 60, ImageObserver);
            case 4 ->
                g.drawImage(left, pPos[0] * 20 - 20, pPos[1] * 20 + 125 - 20, 60, 60, ImageObserver);
            default ->
                g.drawImage(front, pPos[0] * 20 - 20, pPos[1] * 20 + 125 - 20, 60, 60, ImageObserver);
        }
    }
}
