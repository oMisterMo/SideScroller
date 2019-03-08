/* 
 * Copyright (C) 2019 Mohammed Ibrahim
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sidescroller;

import common.Vector2D;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * A simple Fireball which is fired from a Player.
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class Fireball extends DynamicGameObject {

    public static final float FIREBALL_WIDTH = 32f;
    public static final float FIREBALL_HEIGHT = 32f;
    private static final float SPEED = 150f;
    public static final int TIME_MS = 4000; //Total time to live
    private int age = TIME_MS;
    private boolean isDead = false;

    private BufferedImage fireball;
//    private Animation fireball;

    /**
     * Constructs a new Fireball at {@link x}, {@link y} with a set speed.
     *
     * @param x the horizontal position
     * @param y the vertical position
     * @param facingRight true if travelling right
     */
    public Fireball(float x, float y, boolean facingRight) {
        super(x, y, FIREBALL_WIDTH, FIREBALL_HEIGHT);

        loadImages();

        position = new Vector2D(x, y);
        float dx, dy = 0;
        if (facingRight) {
            dx = SPEED;
        } else {
            dx = -SPEED;
        }
        velocity = new Vector2D(dx, dy);
        acceleration = new Vector2D(0, 100);
    }

    private void loadImages() {
        fireball = Assets.spikeFireball;
    }

    /**
     * Dead is true if the age <= 0
     *
     * @return true if the fireball is dead
     */
    public boolean isDead() {
        return isDead;
    }

    @Override
    void gameUpdate(float deltaTime) {
        age -= deltaTime * 1000;
        //If fireball has completed time
        if (age <= 0) {
            isDead = true;
        } else {
            //its alive print time
            velocity.add(acceleration.x * deltaTime, acceleration.y * deltaTime);
            position.add(velocity.x * deltaTime, velocity.y * deltaTime);
            bounds.setRect(position.x, position.y, bounds.width, bounds.height);

            if (position.y > 512) {
                velocity.y *= -1;
            }
        }
    }

    @Override
    void gameRender(Graphics2D g) {
        g.drawImage(fireball, (int) position.x, (int) position.y, null);
//        g.drawImage(fireball, (int)position.x, (int)position.y, width, height, null);
    }

}
