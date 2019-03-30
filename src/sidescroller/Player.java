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

import common.Animation;
import common.SpriteSheet;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import common.Vector2D;
import common.Helper;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player within the game world.
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class Player extends DynamicGameObject {

    public static final float PLAYER_WIDTH = 28;    //32 originally
    public static final float PLAYER_HEIGHT = 28;   //32 originally
    //Player states
    public static final int STATE_IDLE = 0;
    public static final int STATE_WALK = 1;
    public static final int STATE_RUN = 2;
    public static final int STATE_ACTION = 3;
    public static final int STATE_PUNCH = 4;
    public static final int STATE_JUMP = 5;     //Also is falling, can seperate
    public static final int STATE_FALLING = 6;  //Also is falling, can seperate
    public static final int STATE_CLIMB = 7;
    public static final int STATE_HURT = 8;
    public static final int STATE_DEAD = 9;
    public int playerState = STATE_FALLING;

    //Movement constants    ( * 32 pixels = 1 meter)
    private static final int TERMINAL_VELOCITY = 200;   //6.25 m/s
    private static final int WALK_SPEED = 150;          //x
    private static final int RUN_SPEED = 200;           //x
    private static final int JUMP_HEIGHT = 380;         //y impulse
    private static final int AIR_HOR_VEL = 250;
//    private static final int FALL_ACEL = 800;         //10 m/s

    //Player animations per state
    private Animation idle;
    private Animation idle2;
    private Animation walk;
    private Animation jump;
    private boolean facingRight = true;
    private int idleCounter = 0;

//    //public Rectangle.Float bounds;  //outter blue hitbox
//    private Rectangle.Float leftHitbox;
//    private Rectangle.Float rightHitbox;
//    private Rectangle.Float topHitbox;
//    public Rectangle.Float bottomHitbox;
//    public boolean grounded = false;
    private List<Fireball> fireball;    //Shitty fireballs

    //For debugging****************************
    private Vector2D fontPos;
    private String state_debug = "FALLING";
    private int numOfCollision = 0;

    /**
     * Constructs a new Player at x, y.
     *
     * @param x the x position
     * @param y the y position
     */
    public Player(float x, float y) {
        super(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
        initAnimations();

//        initInnerHitbox();
        fontPos = new Vector2D(5, 60);
        fireball = new ArrayList<>(5);
        System.out.println("Player created...");
    }

    private void initAnimations() {
        SpriteSheet playerSheet = new SpriteSheet(Assets.playerSheet);
        //init walk animation
        int numOfFrames = 9;
        BufferedImage[] testImage = new BufferedImage[numOfFrames];    //can delete
        for (int i = 0; i < numOfFrames; i++) {
            testImage[i] = playerSheet.getTile(i + 1, 3, 32, 32);
        }
        walk = new Animation();
        walk.setFrames(testImage);
        walk.setDelay(80);

        //init idle animation
        numOfFrames = 4;
        testImage = new BufferedImage[numOfFrames];
        for (int i = 0; i < numOfFrames; i++) {
            testImage[i] = playerSheet.getTile(i + 1, 1, 32, 32);
        }
        idle = new Animation();
        idle.setFrames(testImage);
        idle.setDelay(200);

        numOfFrames = 6;
        testImage = new BufferedImage[numOfFrames];
        int n = 0;
        for (int y = 8; y < 10; y++) {
            for (int x = 0; x < 3; x++) {
//                System.out.println("x: " + (x + 1) + ", y: " + (y + 1));
//                System.out.println();
                testImage[n] = playerSheet.getTile(x + 1, y + 1, 32, 32);
                n++;
            }
        }
        idle2 = new Animation();
        idle2.setFrames(testImage);
        idle2.setDelay(250);

        //init jump animation
        numOfFrames = 4;
        testImage = new BufferedImage[numOfFrames];

        for (int i = 0; i < numOfFrames; i++) {
            testImage[i] = playerSheet.getTile(i + 1, 2, 32, 32);
        }

        jump = new Animation();
        jump.setFrames(testImage);
        jump.setDelay(200);
    }

    private void updateFireballs(float deltaTime) {
        int size = fireball.size();
        Fireball ball;
        for (int i = 0; i < size; i++) {
            ball = fireball.get(i);
            ball.gameUpdate(deltaTime);
            if (ball.isDead()) {
                fireball.remove(ball);
                size = fireball.size();
            }
        }
    }

    /**
     * Applies an impulse to the players y velocity.
     */
    public void jump() {
        playerState = STATE_JUMP;
        velocity.y = -JUMP_HEIGHT + 100;  //impulse up
        jump.resetAnimation();
    }

    /**
     * Player walk, run, jump and fireball.
     */
    public void handleInput() {
        //Walk input
        if (Input.isKeyPressed(KeyEvent.VK_A)) {
            if (playerState != STATE_WALK) {
                System.out.println("Left walk");
                facingRight = false;
                playerState = STATE_WALK;
                velocity.x = -WALK_SPEED;
            }
            //Make player run left
            if (Input.isKeyPressed(KeyEvent.VK_M)) {
                if (playerState != STATE_RUN) {
                    System.out.println("Left run");
                    playerState = STATE_RUN;
                    velocity.x = -RUN_SPEED;
                }
            } else if (Input.isKeyReleased(KeyEvent.VK_M)) {
                if (playerState != STATE_WALK) {
                    System.out.println("walk");
                    playerState = STATE_WALK;
                    velocity.x = -WALK_SPEED;
                }
            }
        } else if (Input.isKeyPressed(KeyEvent.VK_F)) { //VK_D seems broken
            if (playerState != STATE_WALK) {
                System.out.println("right walk");
                facingRight = true;
                playerState = STATE_WALK;
                velocity.x = WALK_SPEED;
            }
            //Make player run right
            if (Input.isKeyPressed(KeyEvent.VK_M)) {
                if (playerState != STATE_RUN) {
                    System.out.println("Right run");
                    playerState = STATE_RUN;
                    velocity.x = RUN_SPEED;
                    //CAN NOT RUN TR AND JUMP (keyboard error)
                }
            } else if (Input.isKeyReleased(KeyEvent.VK_M)) {
                if (playerState != STATE_WALK) {
                    System.out.println("walk");
                    playerState = STATE_WALK;
                    velocity.x = WALK_SPEED;
                }
            }
        }
        if (Input.isKeyReleased(KeyEvent.VK_A) || Input.isKeyReleased(KeyEvent.VK_F)) {
            if (playerState != STATE_IDLE) {
                System.out.println("idle");
                playerState = STATE_IDLE;
                velocity.x = 0;
            }
        }

        //Testing jump code here
        if (Input.isKeyTyped(KeyEvent.VK_SPACE)) {
//            System.out.println("JUMP!");
            if (grounded) {
                if (playerState != STATE_JUMP) {
                    playerState = STATE_JUMP;
                    velocity.y = -JUMP_HEIGHT;  //impulse up
                    grounded = false;
//                System.out.println("JUMP!");
                    jump.resetAnimation();
                }
            }
        }

        if (Input.isKeyReleased(KeyEvent.VK_SPACE)) {
            playerState = STATE_FALLING;
            //If player released jump key while jumping up
            if (velocity.y < 0) {
                velocity.y *= 0.5;
            }
        }

        //create firball
        if (Input.isKeyTyped(KeyEvent.VK_B)) {
            if (fireball.size() < 5) {
                //Do I need a add a new instance? object pool?
                fireball.add(new Fireball(bounds.x, bounds.y, facingRight));
            }
        }
    }

    /**
     * Player walk, run, jump and fireball.
     */
    public void handleInput2() {
//        System.out.println("grounded: " + grounded);

        if (Input.isKeyPressed(KeyEvent.VK_A) && Input.isKeyPressed(KeyEvent.VK_M)) {
            //Move left
            if (playerState != STATE_RUN) {
                System.out.println("Left run");
                facingRight = false;
                playerState = STATE_RUN;
                velocity.x = -RUN_SPEED;
            }
        } else if ((Input.isKeyPressed(KeyEvent.VK_D) || Input.isKeyPressed(KeyEvent.VK_F))
                && Input.isKeyPressed(KeyEvent.VK_M)) {
            //Move right
            if (playerState != STATE_RUN) {
                facingRight = true;
                System.out.println("Right run");
                playerState = STATE_RUN;
                velocity.x = RUN_SPEED;
            }
        } else if (Input.isKeyPressed(KeyEvent.VK_A)) {
            //Walk input
            if (playerState != STATE_WALK) {
                System.out.println("Left walk");
                facingRight = false;
                playerState = STATE_WALK;
                velocity.x = -WALK_SPEED;
            }
        } else if (Input.isKeyPressed(KeyEvent.VK_D) 
                || Input.isKeyPressed(KeyEvent.VK_F)) { //VK_D seems broken
            if (playerState != STATE_WALK) {
                System.out.println("right walk");
                facingRight = true;
                playerState = STATE_WALK;
                velocity.x = WALK_SPEED;
            }
        }
        //Check for release of walk keys
        if (Input.isKeyReleased(KeyEvent.VK_A) 
                || Input.isKeyReleased(KeyEvent.VK_D)
                || Input.isKeyReleased(KeyEvent.VK_F)) {
            if (playerState != STATE_IDLE) {
                System.out.println("idle");
                playerState = STATE_IDLE;
                velocity.x = 0;
            }
        }

        //Testing jump code here
        if (Input.isKeyTyped(KeyEvent.VK_SPACE) || Input.isKeyTyped(KeyEvent.VK_W)) {
//            System.out.println("JUMP!");
            if (grounded) {
                if (playerState != STATE_JUMP) {
                    playerState = STATE_JUMP;
                    velocity.y = -JUMP_HEIGHT;  //impulse up
                    grounded = false;
                    System.out.println("JUMP!");
                    jump.resetAnimation();
                }
            }
        }

        if (Input.isKeyReleased(KeyEvent.VK_SPACE)) {
//            playerState = STATE_FALLING;
            //If player released jump key while jumping up
            if (velocity.y < 0) {
                velocity.y *= 0.5;
            }
        }

        //create firball
        if (Input.isKeyTyped(KeyEvent.VK_B)) {
            if (fireball.size() < 5) {
                //Do I need a add a new instance? object pool?
                fireball.add(new Fireball(bounds.x, bounds.y, facingRight));
            }
        }
    }

    /* *************** UPDATE & RENDER ******************* */
    @Override
    public void gameUpdate(float deltaTime) {
//        System.out.println(playerState);
        //Move player otter hitbox
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.x += (velocity.x * deltaTime);
        bounds.y += (velocity.y * deltaTime);

//        //Update players inner hitbox
//        updateInnerHitbox();
        //If player is not on the ground, he is falling
        if (!grounded /*&& velocity.y > 0*/) {
            if (playerState != STATE_FALLING) {
//                System.out.println("vel.y:" + velocity.y);
                playerState = STATE_FALLING;
                System.out.println("FALL!");
            }
        }

        switch (playerState) {
            case STATE_IDLE:
                idleCounter += deltaTime * 1000;
                idle.update(deltaTime);
                idle2.update(deltaTime);
                break;
            case STATE_WALK:
                idleCounter = 0;
                walk.setDelay(100);
                walk.update(deltaTime);
                break;
            case STATE_RUN:
                idleCounter = 0;
                walk.setDelay(50);  //doesn't work with dt
                walk.update(deltaTime);
                break;
            case STATE_JUMP:
            case STATE_FALLING:
                idleCounter = 0;
                jump.update(deltaTime);
                //Apply gravity if player is falling or jumping
                //velocity never faster than 25m/s
                velocity.y += Level.gravity.y * deltaTime;
                velocity.y = Helper.Clamp(velocity.y, -TERMINAL_VELOCITY * 3, TERMINAL_VELOCITY * 3);
                break;
            case STATE_DEAD:
                break;
        }

        //Update firballs
        updateFireballs(deltaTime);
        //Handle collision AFTER we commit our movement
    }

    @Override
    void gameRender(Graphics2D g) {
//        g.setStroke(stroke);
        switch (Level.renderMode) {
            case Level.HITBOX_MODE:
                drawHitbox(g);
                break;
            case Level.BITMAP_MODE:
                drawRender(g);
                break;
        }
        //Draw fireballs
        int size = fireball.size();
        for (int i = 0; i < size; i++) {
            fireball.get(i).gameRender(g);
        }
    }

    private void drawHitbox(Graphics2D g) {
        //Draw bounds
        g.setColor(Color.BLUE);
        g.draw(bounds);
    }

    private void drawRender(Graphics2D g) {
        //Draw playerMo
        switch (playerState) {
            case STATE_IDLE:
                //If playerMo is idle for more than 3 seconds, draw idle2
                if (idleCounter > 3000) {
                    g.drawImage(idle2.getImage(), (int) bounds.x, (int) bounds.y,
                            (int) bounds.width, (int) bounds.height, null);
                } else {
                    //Otherwise draw normal idle animation (needs optimisation) 

                    if (facingRight) {
                        g.drawImage(idle.getImage(), (int) (bounds.x + bounds.width), (int) bounds.y,
                                -(int) bounds.width, (int) bounds.height, null);
                    } else {
                        g.drawImage(idle.getImage(), (int) (bounds.x), (int) bounds.y,
                                (int) bounds.width, (int) bounds.height, null);
                    }

                }
                break;
            case STATE_WALK:
//                int sign = Helper.Sign(velocity.x);
//                System.out.println("sign: " + sign);

                if (facingRight) {
                    g.drawImage(walk.getImage(), (int) (bounds.x + bounds.width), (int) bounds.y,
                            -(int) bounds.width, (int) bounds.height, null);
                } else {
                    g.drawImage(walk.getImage(), (int) bounds.x, (int) bounds.y,
                            (int) bounds.width, (int) bounds.height, null);
                }
                break;
            case STATE_RUN:
                if (facingRight) {
                    g.drawImage(walk.getImage(), (int) (bounds.x + bounds.width), (int) bounds.y,
                            -(int) bounds.width, (int) bounds.height, null);
                } else {
                    g.drawImage(walk.getImage(), (int) bounds.x, (int) bounds.y,
                            (int) bounds.width, (int) bounds.height, null);
                }
                break;
            case STATE_JUMP:
            case STATE_FALLING:
                if (facingRight) {
                    g.drawImage(jump.getImage(), (int) (bounds.x + bounds.width), (int) bounds.y,
                            -(int) bounds.width, (int) bounds.height, null);
                } else {
                    g.drawImage(jump.getImage(), (int) bounds.x, (int) bounds.y,
                            (int) bounds.width, (int) bounds.height, null);
                }
                break;
            case STATE_DEAD:
                g.drawOval((int) bounds.x, (int) bounds.y,
                        (int) bounds.width, (int) bounds.height);
                break;
        }
//        g.drawImage(playerImg, (int) bounds.x, (int) bounds.y, null);
    }

    /**
     * Debugging, draws information regarding the player.
     *
     * @param g graphics context that provides drawing capabilities
     */
    public void drawInfo(Graphics2D g) {
        g.setColor(Color.WHITE);
//        g.drawString("Draw REAL POS", fontPos.x, fontPos.y);
//        g.drawString("Pos: " + String.valueOf(position), fontPos.x, fontPos.y);
        g.drawString("Pos: " + "x: " + (int) bounds.x + ", y: " + (int) bounds.y, fontPos.x, fontPos.y);
        g.drawString("Vel: " + String.valueOf(velocity), fontPos.x, fontPos.y + 35);
        g.drawString("Acc: " + String.valueOf(Level.gravity), fontPos.x, fontPos.y + 65);
        //draw playerMo state
        switch (playerState) {
            case STATE_IDLE:
                state_debug = "IDLE";
                break;
            case STATE_WALK:
                state_debug = "WALK";
                break;
            case STATE_RUN:
                state_debug = "RUN";
                break;
            case STATE_JUMP:
                state_debug = "JUMP";
                break;
            case STATE_FALLING:
                state_debug = "FALLING";
                break;
        }
        g.drawString(state_debug, fontPos.x, fontPos.y + 95);
        g.drawString("Col: " + String.valueOf(numOfCollision), fontPos.x, fontPos.y + 125);
    }

}
