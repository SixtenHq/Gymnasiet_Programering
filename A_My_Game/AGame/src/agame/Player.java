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
class Player extends Thing {

    private int hp;
    private boolean living;
    private int facing;
    private boolean moving;

    public Player(int hp, agame.Position Position, Model m) {
        super(Position, m, true);
        this.hp = hp;
        this.living = true;
        this.facing = 0;
    }

    public int getHp() {
        return hp;
    }

    public boolean isLiving() {
        return living;
    }

    public double getFacing() {
        return facing;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setFacing(int facing) {
        this.facing = facing;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void move(ArrayList<Thing> colidedThings) {
        if (moving) {
            boolean freeLeft = true, freeRight = true, freeUpp = true, freeDown = true;
            for (int i = 0; i < colidedThings.size(); i++) {
                double x = colidedThings.get(i).getMidelPosition().getX() - getMidelPosition().getX();
                double y = colidedThings.get(i).getMidelPosition().getY() - getMidelPosition().getY();
                if (Math.abs(y) > Math.abs(x)) {
                    if (y > 0) {
                        freeDown = false;
                    } else if (y < 0) {
                        freeUpp = false;
                    }
                } else if (Math.abs(y) < Math.abs(x)) {
                    if (x > 0) {
                        freeRight = false;
                    } else if (x < 0){
                        freeLeft = false;
                    }
                }
            }

        

        switch (facing) {
            case 1:
                if (freeUpp) {
                    getPosition().setY(getPosition().getY() - 1);
                }
                break;
            case 2:
                if (freeLeft) {
                    getPosition().setX(getPosition().getX() - 1);
                }
                break;
            case 3:
                if (freeDown) {
                    getPosition().setY(getPosition().getY() + 1);
                }
                break;
            case 4:
                if (freeRight) {
                    getPosition().setX(getPosition().getX() + 1);
                }
                break;

        }
    }

}

}
