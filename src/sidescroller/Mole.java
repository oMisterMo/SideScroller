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

import common.Helper;
import java.awt.Graphics2D;

/**
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class Mole extends DynamicGameObject {

    public static final float MOLE_WIDTH = 36;
    public static final float MOLE_HEIGHT = 32;

    public static final int STATE_WALK = 0;
    public static final int STATE_FALL = 1;
    public int moleState = STATE_FALL;
    
    public static final int WALK_SPEED = 100;

    public Mole(float x, float y, float width, float height) {
        super(x, y, width, height);
        bounds.setRect(x, y + 4, width - 5, height - 5);
        //Width and height doesn't change -> remember to add bounds to y on update
        velocity.x = WALK_SPEED;
    }
    
    public void xCol(DynamicGameObject obj){
        
    }

    @Override
    void gameUpdate(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.x = position.x;
        bounds.y = position.y + 4;
        Assets.mole.update(deltaTime);
        switch (moleState) {
            case STATE_WALK:
//                velocity.x = WALK_SPEED;
                break;
            case STATE_FALL:
                velocity.y += Level.WORLD_GRAVITY * deltaTime;
                velocity.y = Helper.Clamp(velocity.y, -10, 10);
                break;
        }

        //Keep within bounds of world (screen currently)
        if (position.x + MOLE_WIDTH >= GamePanel.GAME_WIDTH) {
            velocity.x *= -1;
        }
        if (position.x <= 0) {
            velocity.x *= -1;
        }
    }

    @Override
    void gameRender(Graphics2D g) {
        /*Since I am not using the center point when I flip the image, I must
         add the width of the sprite*/
        switch (moleState) {
            case STATE_WALK:
            case STATE_FALL:
                if (velocity.x > 0) {
                    g.drawImage(Assets.mole.getImage(), (int) position.x,
                            (int) position.y, (int) MOLE_WIDTH, (int) MOLE_HEIGHT, null);
                } else {
                    g.drawImage(Assets.mole.getImage(), (int) (position.x + MOLE_WIDTH),
                            (int) position.y, (int) -MOLE_WIDTH, (int) MOLE_HEIGHT, null);
                }
                break;
        }
        //Debuggin
        g.draw(bounds);
    }

}
