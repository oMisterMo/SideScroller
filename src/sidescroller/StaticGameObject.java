package sidescroller;

import java.awt.Graphics2D;
import common.Vector2D;
import java.awt.Rectangle;

/**
 * 05-Feb-2017, 18:54:47.
 *
 * @author Mo
 */
public class StaticGameObject extends GameObject {

    protected Vector2D position;
//    protected float width;
//    protected float height;
    protected Rectangle.Float bounds;

    /**
     * Should initialise here and not in subclass
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public StaticGameObject(float x, float y, float width, float height) {
        this.position = new Vector2D(x, y);
        this.bounds = new Rectangle.Float(x, y, width, height);
    }

    /**
     * Updates the current game object
     *
     * @param deltaTime time since last frame
     */
    @Override
    void gameUpdate(float deltaTime) {
    }

    /**
     * Draws the screen
     *
     * @param g graphics object
     */
    @Override
    void gameRender(Graphics2D g) {
    }

}
