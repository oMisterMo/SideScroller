/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sidescroller;

import java.awt.Rectangle;

/**
 * With interfaces, all fields are automatically public, static, and final, 
 * and all methods that you declare or define (as default methods) are public.
 * 
 * 26-Jan-2017, 14:38:19.
 *
 * @author Mo
 */
public interface Collidable {
//    Rectangle hitbox = new Rectangle();
    
    public boolean collisionDetection();
    
    /**
     * Dynamic/dynamic collision
     * @param ob
     * @param ob2
     * @return 
     */
    public boolean hit(DynamicGameObject ob, DynamicGameObject ob2);
    /**
     * Dynamic/static collision
     * @param ob
     * @param ob2
     * @return 
     */
    public boolean hit(DynamicGameObject ob, GameObject ob2);
}
