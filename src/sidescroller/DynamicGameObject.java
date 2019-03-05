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

/**
 * A dynamic game object has the ability to move across the viewport by updating
 * its position using the velocity and acceleration provided.
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class DynamicGameObject extends StaticGameObject {

    protected Vector2D acceleration;
    protected Vector2D velocity;
    protected boolean grounded = false;

    public DynamicGameObject(float x, float y, float width, float height) {
        super(x, y, width, height);
        acceleration = new Vector2D();
        velocity = new Vector2D();
    }

}
