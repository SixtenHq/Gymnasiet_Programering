/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agame;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author sixten.holmqvist
 */
class Thing {

    private ArrayList<BufferedImage> textures = new ArrayList<>();
    private ArrayList<Integer> texureIndex = new ArrayList<>();
    private Position position;
    private Model m;
    private boolean solid;

    public Thing(Position position, Model m, boolean solid) {
        this.position = position;
        this.m = m;
        this.solid = solid;
    }

    public BufferedImage getTexture(int a) {
        return textures.get(texureIndex.get(a));
    }

    public Position getPosition() {
        return position;
    }

    public Position getPosition2() {
        return new Position(position.getX() + 50, position.getY() + 50);
    }
    
    public Position getMidelPosition(){
        return new Position(position.getX() + 25, position.getY() + 25);
    }

    
    public ArrayList<BufferedImage> getTextures() {
        return textures;
    }

    public ArrayList<Integer> getTexureIndex() {
        return texureIndex;
    }

    public Model getM() {
        return m;
    }

    public boolean isSolid() {
        return solid;
    }

}
