/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package zelda_package;

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
public class Enemie {

    int[] pPos = new int[2]; //reserverar minne för spelarens position
    int[] ePos = new int[2]; //reserverar minne för fiendens position
    int wallLengthY = 11 * 3, wallLengthX = 16 * 3; //kartan storlek
    ArrayList<ArrayList<Integer>> map = new ArrayList<>(); //tvådimentionell ArrayList med kartan
    boolean explode = false; //om en fiende håller på att explodera
    int explodeCount = 0; //nedräkning till explotion
    boolean hasExploded = false; //räkning för hu
    boolean hit = false; // om en fiende har exploderat
    BufferedImage bomb = null; //reserverar plats för bilder
    BufferedImage bombExploding = null;
    BufferedImage explotion = null;
    private ImageObserver observer;
    String filePath; //vart spelet ligger på datorn
    public ArrayList<ArrayList<Integer>> path = new ArrayList<>();

    public Enemie(ArrayList<ArrayList<Integer>> maper, int[] mPosSetter, int[] pPosStetter) {
        try { //hitaar vart spelet ligger 
            String tempFilePath = new File("Zelda").getAbsolutePath();
            filePath = tempFilePath.substring(0, tempFilePath.lastIndexOf("Zelda"));
        } catch (SecurityException ex) {
            System.out.println("!File access denied by OS or anti virus software!");
        }
        map = maper;
        ePos = mPosSetter;
        pPos = pPosStetter;
        try { //läser in bilder
            bomb = ImageIO.read(new File(filePath + "\\sprites\\bomb.png"));
            bombExploding = ImageIO.read(new File(filePath + "\\sprites\\bomb_exploding.png"));
            explotion = ImageIO.read(new File(filePath + "\\sprites\\explotion.png"));
        } catch (IOException ex) {
            System.out.println("!Could not find image for player at \"" + filePath + "sprites\"!");
        }
    }

    public void enemyTurn() { //bestämmer vad som fienden ska göra
        if (!hasExploded) {
            if (isTouching(13) || explode) {
                explode = true;
                if (explodeCount < 4) {
                    explodeCount++;
                } else {
                    explodeCount++;
                    explode();
                }
            } else {
                find();
            }
        }
    }

    public void explode() { // kollar om explotionen träffade spelaren
        hasExploded = true;
        if (isTouching(21)) {
            hit = true;
        }
    }

    public boolean isTouching(int a) { //kollar om spelaren är inom ett visst område av fienden
        //algoritmen går i en spiral utåt och tittar om spelaren står på någon av de platserna som spiralen går igenom
        int[] pos2 = {pPos[0], pPos[1]};
        int direktion = 1;
        double steps = 1;
        if (ePos[0] == pos2[0] && ePos[1] == pos2[1]) {
            return true;
        }
        for (int i = 0; i < a; i++) {
            int actualSteps = (int) Math.floor(steps); // algoritmen går samma mängd steg två gånger innan den lägger till ett steg
            direktion = direktion % 4; // ändrar riktning så att algoritmen går runt
            if (direktion == 0) {
                direktion = 4;
            }
            if (i == a) { // i slutet tar algoritmen ett steg för mycket - detta tor bort de steget
                steps--;
            }
            for (int j = 0; j < actualSteps; j++) { //där den faktiskt tar steget
                switch (direktion) {
                    case 1 ->
                        pos2[1]--;
                    case 2 ->
                        pos2[0]++;
                    case 3 ->
                        pos2[1]++;
                    case 4 ->
                        pos2[0]--;
                }
                if (ePos[0] == pos2[0] && ePos[1] == pos2[1]) { //kollar om sloritmen träffat spelaren
                    return true;
                }
            }
            steps += 0.5;
            direktion++;
        }
        return false;
    }

    public void find() { // algoritm för att skapa en väg som till spelaren från fienden
        path.clear();
        int[] mPosTemp = {ePos[0], ePos[1]};
        int returned = 0; //holler reda på hur många steg bakåt på vägen som tqagits
        boolean stuck = false; //blir true om det inte går att ta sig till spelaren
        while ((mPosTemp[0] != pPos[0] || mPosTemp[1] != pPos[1]) && !stuck) { //medans fienden inte tagit sig till fienden eller den inte testat alla vägar
            int xAxel = mPosTemp[0] - pPos[0], yAxel = mPosTemp[1] - pPos[1]; //hundersöker vilket håll som är närmast till spelaren
            boolean walledIn = false; //blir true om fienden behöver backa
            if (Math.abs(xAxel) >= Math.abs(yAxel) && xAxel > 0) { // de fyra if/else if som kommer här bestemer åt vilket håll fienden ska börga med at försöka gå beroende på vart spelaren är
                if (flyttPath(path, mPosTemp, -1, 0)) { // om dit ente är en vägg framör fienden eller att fienden redan gått där -> gå ditåt
                    mPosTemp[0]--;
                    returned = 0;
                } else if (yAxel >= 0 && flyttPath(path, mPosTemp, -1, 1)) {
                    mPosTemp[1]--;
                    returned = 0;
                } else if (flyttPath(path, mPosTemp, 1, 1)) {
                    mPosTemp[1]++;
                    returned = 0;
                } else if (flyttPath(path, mPosTemp, -1, 1)) {
                    mPosTemp[1]--;
                    returned = 0;
                } else if (flyttPath(path, mPosTemp, 1, 0)) {
                    mPosTemp[0]++;
                    returned = 0;
                } else {
                    walledIn = true;
                }
            } else if (Math.abs(xAxel) >= Math.abs(yAxel)) {
                if (flyttPath(path, mPosTemp, 1, 0)) {
                    mPosTemp[0]++;
                    returned = 0;
                } else if (yAxel >= 0 && flyttPath(path, mPosTemp, -1, 1)) {
                    mPosTemp[1]--;
                    returned = 0;
                } else if (flyttPath(path, mPosTemp, 1, 1)) {
                    mPosTemp[1]++;
                    returned = 0;
                } else if (flyttPath(path, mPosTemp, -1, 1)) {
                    mPosTemp[1]--;
                    returned = 0;
                } else if (flyttPath(path, mPosTemp, -1, 0)) {
                    mPosTemp[0]--;
                    returned = 0;
                } else {
                    walledIn = true;
                }
            } else if (yAxel >= 0) {
                if (flyttPath(path, mPosTemp, -1, 1)) {
                    mPosTemp[1]--;
                    returned = 0;
                } else if (xAxel >= 0 && flyttPath(path, mPosTemp, -1, 0)) {
                    mPosTemp[0]--;
                    returned = 0;
                } else if (flyttPath(path, mPosTemp, 1, 0)) {
                    mPosTemp[0]++;
                    returned = 0;
                } else if (flyttPath(path, mPosTemp, -1, 0)) {
                    mPosTemp[0]--;
                    returned = 0;
                } else if (flyttPath(path, mPosTemp, 1, 1)) {
                    mPosTemp[1]++;
                    returned = 0;
                } else {
                    walledIn = true;
                }
            } else { // om det inte går att gå någonstan -> backa tills det är möjligt
                if (flyttPath(path, mPosTemp, 1, 1)) {
                    mPosTemp[1]++;
                    returned = 0;
                } else if (xAxel >= 0 && flyttPath(path, mPosTemp, -1, 0)) {
                    mPosTemp[0]--;
                    returned = 0;
                } else if (flyttPath(path, mPosTemp, 1, 0)) {
                    mPosTemp[0]++;
                    returned = 0;
                } else if (flyttPath(path, mPosTemp, -1, 0)) {
                    mPosTemp[0]--;
                    returned = 0;
                } else if (flyttPath(path, mPosTemp, -1, 1)) {
                    mPosTemp[1]--;
                    returned = 0;
                } else {
                    walledIn = true;
                }
            }
            if (walledIn == true) { //om det inte går att gå någonstans och fienden har backat hela vägen till där den börgade -> ge upp
                if (returned >= path.size()) {
                    stuck = true;
                } else {
                    mPosTemp[0] = path.get(path.size() - 1 - returned).get(0);
                    mPosTemp[1] = path.get(path.size() - 1 - returned).get(1);
                    returned += 2;
                }
            }
            path.add(new ArrayList());
            path.get(path.size() - 1).add(mPosTemp[0]);
            path.get(path.size() - 1).add(mPosTemp[1]);
        }
        //--------härifrån-------
        int a = 0;
        if (!path.isEmpty()) {
            for (int i = 0; i < path.size(); i++) {
                if (path.get(i).get(0) == ePos[0] - 1 && path.get(i).get(1) == ePos[1]) {
                    a = i;
                }
                if (path.get(i).get(0) == ePos[0] + 1 && path.get(i).get(1) == ePos[1]) {
                    a = i;
                }
                if (path.get(i).get(0) == ePos[0] && path.get(i).get(1) == ePos[1] + 1) {
                    a = i;
                }
                if (path.get(i).get(0) == ePos[0] && path.get(i).get(1) == ePos[1] - 1) {
                    a = i;
                }
            }
            ePos[0] = path.get(a).get(0);
            ePos[1] = path.get(a).get(1);
        } 
        //------till hit------- tittar åt alla fyra platser brevid fienden och går till den platsen som den planerade vägen till spelaren paseras sist

    }

    public boolean flyttPath(ArrayList<ArrayList<Integer>> path, int[] pos, int a, int xy) { //kollar om fienden kommer krokka in i en vägg eller sin egen väg om den går i en riktning
        int[] tempPos = {pos[0], pos[1]};
        tempPos[xy] += a;
        for (int i = 0; i < path.size(); i++) {
            if (path.get(i).get(0) == tempPos[0] && path.get(i).get(1) == tempPos[1]) {
                return false;
            }
        }
        return walls(tempPos);
    }

    public void addEPosX(int a) { //flyttar fienden i X-axeln
        if (flytt(ePos, a, 0)) {
            ePos[0] += a;
        }

    }

    public void addEPosY(int a) { //flyttar fienden i Y-axeln
        if (flytt(ePos, a, 1)) {
            ePos[1] += a;
        }

    }

    boolean walls(int[] pos) { //returnerar flase om position pos är i en vägg, annars true
        if (pos[0] < 0) {
            return false;
        } else if (pos[0] > wallLengthX - 1) {
            return false;
        } else if (pos[1] < 0) {
            return false;
        } else if (pos[1] > wallLengthY - 1) {
            return false;
        } else if (map.get(pos[1]).get(pos[0]) == 1) {
            return false;
        }
        return true;
    }

    boolean flytt(int[] pos, int a, int xy) { //kollar om en örflyttning i en viss riktning kommer resultera i att fienden står i en vägg
        int[] temppos = {pos[0], pos[1]};
        temppos[xy] += a;
        return walls(temppos);
    }

    public void draw(Graphics g) {
        if (explodeCount > 0 && explodeCount % 2 == 1) { //ritar ut bomben vid vanligt läge och när bomben blinkar
            g.drawImage(bombExploding, ePos[0] * 20 - 10, ePos[1] * 20 + 125 - 10, 30, 30, observer);
        } else {
            g.drawImage(bomb, ePos[0] * 20 - 10, ePos[1] * 20 + 125 - 10, 30, 30, observer);
        }
        if (explodeCount == 4) { //ritar ut explotionen
            g.drawImage(explotion, ePos[0] * 20 - 90, ePos[1] * 20 + 125 - 90, 180, 180, observer);
        }
    }

}
