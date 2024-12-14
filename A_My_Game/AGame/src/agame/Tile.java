/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agame;

import java.util.ArrayList;

/**
 *
 * @author sixten.holmqvist
 */
class Tile extends Thing {

    

    public Tile(boolean solid, Position Position, Model m, String[] textureFile) {
        super(Position, m, solid);
        
        for (int i = 0; i < textureFile.length; i++) {
            super.getM().getTexture(textureFile[i], super.getTextures(),super.getTexureIndex());
        }
    }

    

}
