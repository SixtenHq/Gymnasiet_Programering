/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agameplayer;

/**
 *
 * @author sixten.holmqvist
 */
public class PlayerPositions {
    private double x;
    private double y;

    public PlayerPositions(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public PlayerPositions() {
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}
