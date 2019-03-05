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
package common;

/**
 * The Circle class specifies the 2d coordinates used to represent the center
 * of the circle and a radius.
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class Circle {

    public final Vector2D center = new Vector2D();
    public float radius;

    public Circle(float x, float y, float radius) {
        this.center.set(x, y);
        this.radius = radius;
    }
}
