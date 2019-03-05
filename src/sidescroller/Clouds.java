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
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class Clouds extends GameObject {

    private final static int MIN_HEIGHT = 300;
    private final int CLOUD_SPEED = 180;
    //First cloud
    private final BufferedImage cloud;
    private int width, height;

    private Random r;
    //First cloud
    private Vector2D cloud_a_pos;
    //Second cloud
    Vector2D cloud_b_pos;

    int dx, dx2;

    public Clouds() {
        this.cloud = Assets.cloud;
        r = new Random();
        //Initialise first cloud image
//        System.out.println("In cloud....");
        width = cloud.getWidth();
        height = cloud.getHeight();

        cloud_a_pos = new Vector2D(GamePanel.GAME_WIDTH, r.nextInt(GamePanel.GAME_HEIGHT - height - MIN_HEIGHT));
        cloud_b_pos = new Vector2D(r.nextInt(GamePanel.GAME_WIDTH), r.nextInt(GamePanel.GAME_HEIGHT - height - MIN_HEIGHT));
        dx = r.nextInt(CLOUD_SPEED - 100) + 1;
        dx2 = r.nextInt(CLOUD_SPEED) + 2;
    }

    @Override
    void gameUpdate(float deltaTime) {
        //If first cloud is off the screen, reset x position to the right
        if (cloud_a_pos.x < -width) {
            //new position twice the length of the game worlk
            cloud_a_pos.x = GamePanel.GAME_WIDTH*2 + r.nextInt(200);
            cloud_a_pos.y = r.nextInt(GamePanel.GAME_HEIGHT - height - MIN_HEIGHT);
            dx = r.nextInt(CLOUD_SPEED - 100) + 1;
        }
        if (cloud_b_pos.x < -width) {
            cloud_b_pos.x = GamePanel.GAME_WIDTH*2;
            cloud_b_pos.y = r.nextInt(GamePanel.GAME_HEIGHT - height - MIN_HEIGHT);
            dx2 = r.nextInt(CLOUD_SPEED) + 1;
        }
        //Move left
        cloud_a_pos.x -= (dx * deltaTime);
        cloud_b_pos.x -= (dx2 * deltaTime);
    }

    @Override
    void gameRender(Graphics2D g) {
        g.drawImage(cloud, (int) cloud_a_pos.x, (int) cloud_a_pos.y, null);
        g.drawImage(cloud, (int) cloud_b_pos.x, (int) cloud_b_pos.y, null);
    }

}
