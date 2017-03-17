/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sidescroller;

import java.awt.Graphics2D;

/**
 * An abstract class may have static fields and static methods.
 * 
 * 02-Feb-2017, 21:39:38.
 *
 * @author Mo
 */
abstract class DynamicGameObject extends GameObject{

    //Add other members
    protected Vector2D position;
    protected Vector2D acceleration;
    protected Vector2D velocity;
    
    //int or float?
    protected float width;
    protected float height;
    
    @Override
    void gameUpdate(float deltaTime) {
    }

    @Override
    void gameRender(Graphics2D g) {
    }
    
}
