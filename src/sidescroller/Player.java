/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sidescroller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * 07-Sep-2016, 01:56:02.
 *
 * @author Mo
 */
public class Player extends DynamicGameObject {

    //Player states
    //All states out player can be in -> Different animation for each playerState
    public static final int STATE_IDLE = 0;
    public static final int STATE_WALK = 1;
    public static final int STATE_RUN = 2;
    public static final int STATE_ACTION = 3;
    public static final int STATE_PUNCH = 4;
    public static final int STATE_JUMP = 5; //Also is falling, can seperate
    public static final int STATE_FALLING = 6; //Also is falling, can seperate
    public static final int STATE_CLIMB = 7;
    public static final int STATE_HURT = 8;
    public static final int STATE_DEAD = 9;
    public static int playerState = STATE_FALLING;

    //MovementStates
    private static final int WALK_SPEED = 200;    //x
    private static final int FALL_SPEED = 25;   //y
    private static final int JUMP_HEIGHT = 20;
    private static final int FALL_ACEL = 10;    //10 m/s
//    public boolean grounded;

    private World world;
    private BufferedImage playerImg;
    private Animation idle;
    private Animation leftWalk;
    private Animation rightWalk;

    //From dynamic game object
//    public Vector2D position;
//    public Vector2D velocity;
//    private Vector2D acceleration;
    private Font f;
    private Vector2D fontPos;
    private Stroke stroke;

//    private int width;
//    private int height;
    public Rectangle playerHitbox;  //outter blue hitbox

    private Rectangle leftHitbox;
    private Rectangle rightHitbox;
    private Rectangle topHitbox;
    private Rectangle bottomHitbox;
//    private boolean grav;

    public Player(World world) {
        this.world = world;

        loadImages();
        width = playerImg.getWidth();
        height = playerImg.getHeight();

        position = new Vector2D(300, GamePanel.GAME_HEIGHT - Tile.TILE_HEIGHT * 2 - 100);
        velocity = new Vector2D(0, 0);
        acceleration = new Vector2D(0, FALL_ACEL);

//        playerHitbox = new Rectangle((int) position.x, (int) position.y, width, height);
        playerHitbox = new Rectangle(500, GamePanel.GAME_HEIGHT - Tile.TILE_HEIGHT * 2 - 80, width, height);
        initInnerHitbox();

        f = new Font("Comic Sans MS", Font.PLAIN, 25);
        fontPos = new Vector2D(5, 60);
        stroke = new BasicStroke(1);

//        grav = true;
    }

    private void loadImages() {
        this.playerImg = Assets.player;
    }

    private void initInnerHitbox() {
        //create inner hitbox to determin left,right top bottom collision
        int w = playerHitbox.width / 4;
        int h = playerHitbox.height / 2;

        int padding = 2;//for top and bottom rects
        //RED
        topHitbox = new Rectangle(
                (playerHitbox.x + playerHitbox.width / 2) - (w / 2),
                playerHitbox.y + padding,
                w, h - padding);
        //WHITE
        bottomHitbox = new Rectangle(
                (playerHitbox.x + playerHitbox.width / 2) - (w / 2),
                playerHitbox.y + h,
                w, h - padding);
        //YELLOW
        leftHitbox = new Rectangle(topHitbox.x - w, playerHitbox.y + h / 2, w, h);
        //DARK GRAY
        rightHitbox = new Rectangle(topHitbox.x + w, playerHitbox.y + h / 2, w, h);
//        grounded = false;
        //Check if any of the inner rectangles intercept
        System.out.println("top + bottom intercept? -> "
                + topHitbox.intersects(bottomHitbox));
        System.out.println("top + right intercept? -> "
                + topHitbox.intersects(rightHitbox));
        System.out.println("right + bottom intercept? -> "
                + rightHitbox.intersects(bottomHitbox));
        System.out.println("top + left intercept? -> "
                + topHitbox.intersects(leftHitbox));
        System.out.println("left + bottom intercept? -> "
                + rightHitbox.intersects(leftHitbox));
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A) {
//            playerState = STATE_WALK;
            velocity.x = -WALK_SPEED;
            System.out.println("vel.x: " + velocity.x);
        }
        
        if (key == KeyEvent.VK_D) {
//            playerState = STATE_WALK;
            velocity.x = WALK_SPEED;
            System.out.println("vel.x: " + velocity.x);
        }
            
        if (key == KeyEvent.VK_W) {
            velocity.y = -WALK_SPEED;
            System.out.println("vel.y: " + velocity.y);
        }
        
        if (key == KeyEvent.VK_S) {
            velocity.y = WALK_SPEED;
            System.out.println("vel.y: " + velocity.y);
        }

        if (key == KeyEvent.VK_SPACE) {
//            System.out.println("JUMP");
//            grav = true;
//            if (grounded) {
////                playerState = STATE_JUMP;
//                velocity.y = -JUMP_HEIGHT;
//                grounded = false;
//            }
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
//        System.out.println("Letgo");
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_D) {
//            playerState = STATE_IDLE;
            velocity.x = 0;
            System.out.println("vel.x: " + velocity.x);
        }
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_S) {
//            playerState = STATE_IDLE;
            velocity.y = 0;
            System.out.println("vel.y: " + velocity.y);
        }
    }

    public void setGrounded(boolean val) {
//        this.grounded = val;
    }

    private void handleCollisions3() {
        /*
         1.Detect if larger hitbox intercepts a tile
         2.handle smaller intersections
         */

        //1.
        //Goes through every tile in the game (NOT GOOD)
        for (int j = 0; j < World.NO_OF_TILES_Y; j++) {
            for (int i = 0; i < World.NO_OF_TILES_X; i++) {
                Tile t = world.getTile(i, j);
                if (t.solid) {
                    if (playerHitbox.intersects(t.hitbox)) {
                        System.out.println("PLAYER HITS TILE");
                    }
//                    //If player hits a single solid tile
//                    if (col((playerHitbox.x + velocity.x),
//                            (playerHitbox.y + velocity.y), t)) {
////                        System.out.println("HIT");
//                        int n = 0;
//
//                        velocity.x = 0;
//                        velocity.y = 0;
//                        System.out.println("On Platform");
//                    }

                }
            }
        }
    }

    /**
     * Ensure objects are not travelling too fast to avoid tunneling
     *
     * @param deltaTime
     */
    private void handleCollisions2(float deltaTime) {
        /*
         1.Detect if larger hitbox intercepts a tile
         2.handle smaller intersections
         */

        //1.
        //Goes through every tile in the game (NOT GOOD)
        for (int j = 0; j < World.NO_OF_TILES_Y; j++) {
            for (int i = 0; i < World.NO_OF_TILES_X; i++) {
                Tile t = world.getTile(i, j);
                if (t.solid) {
                    //If player hits a single solid tile
                    if (col((playerHitbox.x + velocity.x),
                            (playerHitbox.y + velocity.y), t)) {
//                        System.out.println("HIT");
                        int n = 0;

                        velocity.x = 0;
                        velocity.y = 0;
                        System.out.println("On Platform");
                    }

                }
            }
        }
    }

    private void handleCollisions(float deltaTime) {
        //Goes through every tile in the game
        for (int j = 0; j < World.NO_OF_TILES_Y; j++) {
            for (int i = 0; i < World.NO_OF_TILES_X; i++) {
                Tile t = world.getTile(i, j);
                if (t.solid) {
                    //If player hits a single solid tile
                    if (col((playerHitbox.x + velocity.x),
                            (playerHitbox.y + velocity.y), t)) {
//                        System.out.println("HIT");
                        int n = 0;
                        //Get as close to the wall as possible
                        while (!col((playerHitbox.x + sign(velocity.x)),
                                (playerHitbox.y + sign(velocity.y)), t)) {
                            System.out.println("Inside while loop!: " + n++);
                            System.out.println("sign(velocity.x) = " + sign(velocity.x));
                            System.out.println("sign(velocity.y) = " + sign(velocity.y));
                            playerHitbox.x += sign(velocity.x);
                            playerHitbox.y += sign(velocity.y);
                        }
//                        grounded = true;
//                        grav = false;
//                        Player.playerState = Player.STATE_IDLE;

                        velocity.x = 0;
                        velocity.y = 0;
                        System.out.println("On Platform");
                    }

                }
            }
        }
    }

    /**
     * Player tile collision
     *
     * @param ob
     * @param tile
     * @return
     */
    private boolean collides(Player player, Tile tile) {
        //does first object collides with second?
//        return player.getHitbox().intersects(tile.playerHitbox);
//        Rectangle test = player.playerHitbox;
//        test.x +=
        return tile.hitbox.intersects(playerHitbox);
    }

    /**
     * If x > 0 return 1 else -1
     *
     * @param x
     * @return Returns 1 or -1 depending on state of input
     */
    private int sign(float x) {
        int num = -1;
        if (x >= 0) {
            return 1;
        }
        return num;
    }

//    /**
//     * Player tile collision
//     *
//     * @param ob
//     * @param tile
//     * @return
//     */
//    private boolean hor_col(Player player, Tile tile, float dt) {
//        //does first object collides with second?
//        return tile.playerHitbox.contains(player.position.x + player.velocity.x * dt, player.position.y);
////        return player.getHitbox().intersects(tile.playerHitbox);
//    }
    private boolean col(float x, float y, Tile t) {
        //does first object collides with second?
//        playerHitbox.x += x; playerHitbox.y+=y;
//        System.out.println("old playerHitbox: "+this.playerHitbox);
//        System.out.println("new playerHitbox: "+ (this.playerHitbox.x + x));
        return t.hitbox.intersects(x, y, playerHitbox.width, playerHitbox.height);
    }

    private void updateIdle() {
//        System.out.println("STATE: Idle");
    }

    private void updateWalk() {
//        System.out.println("STATE: Walking");
    }

    private void updateJump() {
//        System.out.println("STATE: Jumping");
    }

    @Override
    void gameUpdate(float deltaTime) {
        if (playerState == STATE_FALLING || playerState == STATE_JUMP) {
            System.out.println("applying grav");
//            System.out.println(velocity);
            //velocity never faster than 25m/s
            velocity.y += acceleration.y * deltaTime;
            velocity.y = Helper.Clamp(velocity.y, -25, 25);
        }

        //Handle collision before we commit our movement
        
        //Move player otter hitbox
        playerHitbox.x += (velocity.x * deltaTime);
        playerHitbox.y += (velocity.y * deltaTime);
        //Move platers inner hitbox
        leftHitbox.x += (velocity.x * deltaTime);
        leftHitbox.y += (velocity.y * deltaTime);
        rightHitbox.x += (velocity.x * deltaTime);
        rightHitbox.y += (velocity.y * deltaTime);
        topHitbox.x += (velocity.x * deltaTime);
        topHitbox.y += (velocity.y * deltaTime);
        bottomHitbox.x += (velocity.x * deltaTime);
        bottomHitbox.y += (velocity.y * deltaTime);
    }

    /**
     * ************UPDATE & RENDER**************
     */
    @Override
    void gameRender(Graphics2D g) {
        g.setStroke(stroke);
        g.setColor(Color.BLUE);
        //Draw playerHitbox
//        g.fillRect((int) position.x, (int) position.y, width, height);
        g.drawRect((int) playerHitbox.x, (int) playerHitbox.y, width, height);
        //Draw player
//        g.drawImage(player, (int) position.x, (int) position.y, null);

//        drawInfo(g);
        //Draw all hit boxes
        g.setColor(Color.ORANGE);
        g.fillRect(leftHitbox.x, leftHitbox.y, leftHitbox.width, leftHitbox.height);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(rightHitbox.x, rightHitbox.y, rightHitbox.width, rightHitbox.height);
        g.setColor(Color.RED);
        g.fillRect(topHitbox.x, topHitbox.y, topHitbox.width, topHitbox.height);
        g.setColor(Color.WHITE);
        g.fillRect(bottomHitbox.x, bottomHitbox.y, bottomHitbox.width, bottomHitbox.height);
    }

    public void drawInfo(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(f);
        g.drawString("Draw REAL POS", fontPos.x, fontPos.y);
//        g.drawString("Pos: " + String.valueOf(position), fontPos.x, fontPos.y);
        g.drawString("Vel: " + String.valueOf(velocity), fontPos.x, fontPos.y + 35);
        g.drawString("Acc: " + String.valueOf(acceleration), fontPos.x, fontPos.y + 65);
    }

}
