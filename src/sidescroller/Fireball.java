/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sidescroller;

import common.Vector2D;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * 11-May-2017, 15:04:33.
 *
 * @author Mo
 */
public class Fireball extends DynamicGameObject {

    public static final int TIME_MS = 4000; //Total time to live
    private int currentTime = TIME_MS;
    private boolean isDead = false;

    private BufferedImage fireball;
//    private Animation fireball;

    public Fireball(float x, float y, float width, float height, boolean facingRight) {
        super(x, y, width, height);

        loadImages();
//        width = fireball.getWidth();
//        height = fireball.getHeight();

        position = new Vector2D(x, y);
        float dx, dy = 0;
        if (facingRight) {
            dx = 150;
        } else {
            dx = -150;
        }
        velocity = new Vector2D(dx, dy);
        acceleration = new Vector2D(0, 100);
    }

    private void loadImages() {
//        fireball = Assets.playerMo;
        fireball = Assets.spikeFireball;
    }

    public boolean isDead() {
        return isDead;
    }

    @Override
    void gameUpdate(float deltaTime) {
        currentTime -= deltaTime * 1000;
        //If fireball has completed time
        if (currentTime <= 0) {
            isDead = true;
        } else {
            //its alive print time
            System.out.println("fireball alive time: " + currentTime);
            velocity.addToo(acceleration.x * deltaTime, acceleration.y * deltaTime);
            position.addToo(velocity.x * deltaTime, velocity.y * deltaTime);
            bounds.setRect(position.x, position.y, bounds.width, bounds.height);

            if (position.y > 512) {
                velocity.y *= -1;
            }
        }

//        //Goes through every spikeBlock in the game (NOT GOOD)
//        for (int j = 0; j < World.NO_OF_TILES_Y; j++) {
//            for (int i = 0; i < World.NO_OF_TILES_X; i++) {
//                Tile t = world.getTile(i, j);
//                if (t.solid) {
//                    //If we have a solid spikeBlock
//                    if (bounds.intersects(t.bounds)) {
//                        //Check inner hitbox for collision and respond
//                        velocity.y *= -1;
//                    }
//                }
//            }
//        }//end outer for loop
    }

    @Override
    void gameRender(Graphics2D g) {
        g.drawImage(fireball, (int) position.x, (int) position.y, null);
//        g.drawImage(fireball, (int)position.x, (int)position.y, width, height, null);
    }

}
