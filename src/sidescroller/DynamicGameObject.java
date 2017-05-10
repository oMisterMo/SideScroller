package sidescroller;

import common.Vector2D;

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
    
//    protected Vector2D friction;//?
    
    //int or float?
    protected float width;
    protected float height;
    
    
}
