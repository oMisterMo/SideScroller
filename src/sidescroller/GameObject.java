/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sidescroller;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Represents any object which is drawn to the screen.
 *
 * Possible subclasses: -> Tile -> Particle -> Player -> Square
 *
 * ALT + SHIFT + F = auto re-factor
 * 
 * 16/05/16
 *
 * @author Mo
 */
public abstract class GameObject {

    //Width and Height of object we are drawing
//    protected int width;
//    protected int height;
    
    //Or use Vector2D for position and velocity
//    protected Vector2D position;//add
    
//    protected Rectangle hitbox;
    //Sprite image currently not used
    //BufferedImage image;

    //abstract void gameUpdate(float elapsed);
    abstract void gameUpdate(float deltaTime);

    abstract void gameRender(Graphics2D g);
    
    
    //Getters & Setter

//    public void setWidth(int width) {
//        this.width = width;
//    }
//
//    public void setHeight(int height) {
//        this.height = height;
//    }
//
//    public int getWidth() {
//        return width;
//    }
//
//    public int getHeight() {
//        return height;
//    }

//    /**
//     * Prob don't need
//     * @return 
//     */
//    public Rectangle getHitbox() {
//        return hitbox;
//    }
    
    

}
