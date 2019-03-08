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
import java.util.Random;

/**
 * The Cloud class draws a simple image to the screen which wraps when it
 * reaches the left bounds of the viewport.
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class Clouds extends GameObject {

    private final static int MIN_HEIGHT = 300;
    private final int CLOUD_SPEED = 180;
    //First cloud image
    private final BufferedImage cloud;
    private int width, height;

    private Random r;
    //First cloud
    private Vector2D cloudA;
    //Second cloud
    private Vector2D cloudB;
    private int velA, velB;

    /**
     * Constructs a new cloud with a random velocity
     */
    public Clouds() {
        this.cloud = Assets.cloud;
        r = new Random();
        //Initialise first cloud image
//        System.out.println("In cloud....");
        width = cloud.getWidth();
        height = cloud.getHeight();

        cloudA = new Vector2D(GamePanel.GAME_WIDTH, r.nextInt(GamePanel.GAME_HEIGHT - height - MIN_HEIGHT));
        cloudB = new Vector2D(r.nextInt(GamePanel.GAME_WIDTH), r.nextInt(GamePanel.GAME_HEIGHT - height - MIN_HEIGHT));
        velA = r.nextInt(CLOUD_SPEED - 100) + 1;
        velB = r.nextInt(CLOUD_SPEED) + 2;
    }

    @Override
    void gameUpdate(float deltaTime) {
        //If first cloud is off the screen, reset x position to the right
        if (cloudA.x < -width) {
            //new position twice the length of the game worlk
            cloudA.x = GamePanel.GAME_WIDTH * 2 + r.nextInt(200);
            cloudA.y = r.nextInt(GamePanel.GAME_HEIGHT - height - MIN_HEIGHT);
            velA = r.nextInt(CLOUD_SPEED - 100) + 1;
        }
        if (cloudB.x < -width) {
            cloudB.x = GamePanel.GAME_WIDTH * 2;
            cloudB.y = r.nextInt(GamePanel.GAME_HEIGHT - height - MIN_HEIGHT);
            velB = r.nextInt(CLOUD_SPEED) + 1;
        }
        //Move left
        cloudA.x -= (velA * deltaTime);
        cloudB.x -= (velB * deltaTime);
    }

    @Override
    void gameRender(Graphics2D g) {
        g.drawImage(cloud, (int) cloudA.x, (int) cloudA.y, null);
        g.drawImage(cloud, (int) cloudB.x, (int) cloudB.y, null);
    }

}
