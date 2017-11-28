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
    protected Rectangle.Float bounds;

    /**
     * Should initialise here and not in subclass
     *
     * @param x x position
     * @param y y position
     * @param width width
     * @param height height
     */
    public StaticGameObject(float x, float y, float width, float height) {
        this.position = new Vector2D(x, y);
        this.bounds = new Rectangle.Float(x, y, width, height);
    }

    @Override
    void gameUpdate(float deltaTime) {
    }

    @Override
    void gameRender(Graphics2D g) {
    }

}
