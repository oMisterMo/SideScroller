/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sidescroller;

import common.Vector2D;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
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
