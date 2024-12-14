/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agame;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 *
 * @author sixten.holmqvist
 */
public class Model { //mime för bilder

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Thing> things = new ArrayList<>();
    private ArrayList<Byte> map = new ArrayList<>();
    private ArrayList<Tile> tiles = new ArrayList<>();
    private ArrayList<ServerView> viewList = new ArrayList<>();
    private String textDocument = "map.txt";
    private ArrayList<BufferedImage> textures = new ArrayList<>();
    private ArrayList<String> textureFile = new ArrayList<>();
    private byte mapXSize = 10;
    private ArrayList<PlayerHandeler> playerHandelers = new ArrayList<>();

    public Model() {
        addMap();
    }

    public void registerView(ServerView v) {
        viewList.add(v);
    }

    public void uppdateViews() {
        for (int i = 0; i < viewList.size(); i++) {
            viewList.get(i).uppdate();
        }
    }

    private void addMap() {
        Scanner txsc = null;
        try {
            txsc = new Scanner(new File(textDocument));
        } catch (FileNotFoundException ex) {
            System.out.println("!Error in Model/addMap - Map file not found!");
        }
        while (txsc.hasNext()) {
            try {
                map.add(Byte.valueOf(txsc.next()));
            } catch (NumberFormatException e) {
                System.out.println("!Error in Model/addMap - Map file wrong format!");
            }
        }
        System.out.println(map);

        for (int i = 0; i < map.size(); i++) {
            boolean solid = false;
            String textureFile = null;
            switch (map.get(i)) {
                case 1:
                    solid = false;
                    textureFile = "textures\\dirt.png";
                    break;
                case 2:
                    solid = true;
                    textureFile = "textures\\wall.png";
                    break;
                case 3:
                    solid = true;
                    textureFile = "textures\\water.png";
                    break;
                default:
                    System.out.println("!Error in Model/addMap - map array problem!");

            }
            if (textureFile != null) {
                String[] temp = {textureFile};
                tiles.add(new Tile(solid, new Position(i % mapXSize * 50, i / mapXSize * 50), this, temp));
                things.add(tiles.get(tiles.size() - 1));
            }
        }
    }

    public ArrayList<Byte> getMap() {
        ArrayList<Byte> temp = new ArrayList<>();
        for (Byte b : map) {
            temp.add(b);
        }
        return temp;
    }

    public void getTexture(String textureFile, ArrayList<BufferedImage> textures, ArrayList<Integer> texureIndex) {
        if (!(this.textures == textures)) {
            this.textures = textures;
        }
        boolean preExisting = false;
        for (int i = 0; i < textures.size(); i++) {
            if (!textureFile.equals(this.textureFile.get(i))) {
                preExisting = true;
            }
        }
        if (!preExisting) {
            try {
                textures.add(ImageIO.read(new File(textureFile)));
            } catch (IOException ex) {
                System.out.println("!Error in Model/getTexture - textureFile wrong!");
            }
        }
        texureIndex.add(((textures.size() - 1)));
    }

    public int getNumOfTiles() {
        return tiles.size();
    }

    public Tile getTile(int a) {
        return tiles.get(a);
    }

    public Player givePlayer(PlayerHandeler pH) {

        if (players.size() > 126) {
            return null;
        }
        playerHandelers.add(pH);
        players.add(new Player(5, new Position(100, 100), this));
        things.add(players.get(players.size() - 1));
        return players.get(players.size() - 1);
    }

    public void movePlayers() {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).move(collision(i));

        }
    }

    public int getNumOfPlayers() {
        return players.size();
    }

    public Position getPlayerPosition(int a) {
        return players.get(a).getPosition();
    }

    private ArrayList<Thing> collision(int a) {
        ArrayList<Thing> colidedThings = new ArrayList<>();
        for (int i = 0; i < things.size(); i++) {
            if (players.get(a) != things.get(i)) {
                if (things.get(i).isSolid()) {
                    double ax1 = players.get(a).getPosition().getX();
                    double ay1 = players.get(a).getPosition().getY();
                    double ax2 = players.get(a).getPosition2().getX();
                    double ay2 = players.get(a).getPosition2().getY();

                    // Hörn för rektangel i
                    double bx1 = things.get(i).getPosition().getX();
                    double by1 = things.get(i).getPosition().getY();
                    double bx2 = things.get(i).getPosition2().getX();
                    double by2 = things.get(i).getPosition2().getY();

                    boolean colideing = true;
                    if (ax2 < bx1 || ax1 > bx2 || ay2 < by1 || ay1 > by2) {
                        colideing = false;

                    }
                    if (colideing) {
                        colidedThings.add(things.get(i));
                    }
                }

            }
        }
        return colidedThings;
    }


    public void removePlayer(Player p) {
        things.remove(p);
        players.remove(p);
        //playerHandelers.remove(pH);
    }

    public byte getMapXSize() {
        return mapXSize;
    }

    public void uppdatePlayerViews() {
        for (int i = 0; i < playerHandelers.size(); i++) {
            playerHandelers.get(i).sendGraphic();
        }

    }

}
