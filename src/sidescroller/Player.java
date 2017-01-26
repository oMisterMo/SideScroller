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
public class Player extends GameObject {

    //Player states
    //All states out player can be in -> Different animation for each playerState
    public static final int STATE_IDLE = 0;
    public static final int STATE_WALK = 1;
    public static final int STATE_RUN = 2;
    public static final int STATE_ACTION = 3;
    public static final int STATE_PUNCH = 4;
    public static final int STATE_JUMP = 5; //Also is falling, can seperate
    public static final int STATE_CLIMB = 6;
    public static final int STATE_HURT = 7;
    public static final int STATE_DEAD = 8;
    public static int playerState = STATE_JUMP;

    //MovementStates
    private static final int WALK_SPEED = 5;
    private static final int FALL_SPEED = 600;

    private static final int JUMP_HEIGHT = 20;
    private static final int FALL_ACEL = 10;

    private World world;
    private BufferedImage player;
    private Animation idle;
    private Animation leftWalk;
    private Animation rightWalk;

    public boolean grounded;

    public Vector2D position;
    public Vector2D velocity;
    private Vector2D acceleration;

    private Font f;
    private Vector2D fontPos;

    private Stroke stroke;

    private int width;
    private int height;
    public Rectangle hitbox;
    private boolean grav;

    public Player(World world) {
        this.world = world;

        loadImages();
        width = player.getWidth();
        height = player.getHeight();

        position = new Vector2D(300, GamePanel.GAME_HEIGHT - Tile.TILE_HEIGHT * 2 - 100);
        velocity = new Vector2D(0, 0);
        acceleration = new Vector2D(0, FALL_ACEL);

//        hitbox = new Rectangle((int) position.x, (int) position.y, width, height);
        hitbox = new Rectangle(300, GamePanel.GAME_HEIGHT - Tile.TILE_HEIGHT * 2 - 100, width, height);
        grounded = false;

        f = new Font("Comic Sans MS", Font.PLAIN, 25);
        fontPos = new Vector2D(5, 60);
        stroke = new BasicStroke(1);
        
        grav = true;

    }

    private void loadImages() {
        this.player = Assets.player;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A) {
//            playerState = STATE_WALK;
            velocity.x = -WALK_SPEED;
//            System.out.println("velX"+velocity.x);
        }
        if (key == KeyEvent.VK_D) {
//            playerState = STATE_WALK;
            velocity.x = WALK_SPEED;
        }
        if (key == KeyEvent.VK_W) {
            velocity.y = -WALK_SPEED;
        }
        if (key == KeyEvent.VK_S) {
            velocity.y = WALK_SPEED;
        }

        if (key == KeyEvent.VK_SPACE) {
//            System.out.println("JUMP");
            grav = true;
            if (grounded) {
//                playerState = STATE_JUMP;
                velocity.y = -JUMP_HEIGHT;
                grounded = false;
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
//        System.out.println("Letgo");
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_D) {
//            playerState = STATE_IDLE;
            velocity.x = 0;
        }
//        if (key == KeyEvent.VK_W || key == KeyEvent.VK_S) {
////            playerState = STATE_IDLE;
//            velocity.y = 0;
//        }
    }

    public void setGrounded(boolean val) {
        this.grounded = val;
    }

    @Override
    void gameUpdate(float deltaTime) {
        if(grav){
            System.out.println("applying grav");
            velocity.y += acceleration.y*0.1;
            velocity.y = Helper.Clamp(velocity.y, -100, 100);
        }
        
        
        //Handle collision before we commit our movement
        handleCollisions(deltaTime);

        //update position due to velocity
//        System.out.println("old: "+position);
        hitbox.x += velocity.x; //* deltaTime;
        hitbox.y += velocity.y; //* deltaTime;
//        System.out.println("after we move it: "+hitbox);
//        System.out.println("new: "+position);

//        //update players hitbox
//        hitbox.x = (int) position.x;
//        hitbox.y = (int) position.y;
    }

    private void handleCollisions(float deltaTime) {
        //Goes through every tile in the game
        for (int j = 0; j < World.NO_OF_TILES_Y; j++) {
            for (int i = 0; i < World.NO_OF_TILES_X; i++) {
                Tile t = world.getTile(i, j);
                if (t.solid) {
                    //If player hits a single solid tile
                    if (col((hitbox.x + velocity.x), 
                            (hitbox.y + velocity.y), t)) {
//                        System.out.println("HIT");
                        int n = 0;
                        //Get as close to the wall as possible
                        while (!col((hitbox.x + sign(velocity.x)),
                                (hitbox.y + sign(velocity.y)), t)) {
                            System.out.println("Inside while loop!: " + n++);
                            System.out.println("sign(velocity.x) = "+sign(velocity.x));
                            System.out.println("sign(velocity.y) = "+sign(velocity.y));
                            hitbox.x += sign(velocity.x);
                            hitbox.y += sign(velocity.y);
                        }
                        grounded = true;
                        grav = false;
//                        Player.playerState = Player.STATE_IDLE;
                        
                        velocity.x = 0;
                        velocity.y = 0;
                        System.out.println("On Platform");
                    }else{
//                        grav = true;
                    }
                    
                }
            }
        }
    }

//    private void handleCollisionsOLD(float deltaTime) {
//        /*
//         Don't let player enter tile
//            
//         1.push player out of tile
//         2.set correct states
//         */
////            Vector2D overlap = new Vector2D();
////            Rectangle overlap = new Rectangle();
////            Rectangle.intersect(player.hitbox, t.hitbox, overlap);
////            System.out.println("************************");
////            System.out.println("player: " + player.hitbox);
////            System.out.println("hitbox: " + t.hitbox);
////            System.out.println("overlap: " + overlap);
////            System.out.println("");
////
////            if (overlap.height < overlap.width) {
////                player.position.y += overlap.height * sign(player.velocity.y);
////            } else {
////                player.position.x += overlap.width * sign(player.velocity.x);
////            }
////            pushOut(overlap);
//
//        Tile t = world.getTile(3, 10);
////        if ( col(position.x + (velocity.x * deltaTime), position.y, t) ) {
////            System.out.println("-x- col");
////            System.out.println("sign vel.x: "+sign(velocity.x));
////            while(!col(position.x + sign(velocity.x) * deltaTime, position.y, t)){
////                position.x += sign(velocity.x);
////            }
////            velocity.x = 0;
////            System.out.println(position);
////        }
////        if ( col(position.x, position.y + (velocity.y * deltaTime), t) ) {
////            System.out.println("-y- col");
////            while(!col(position.x, position.y + sign(velocity.x) * deltaTime, t)){
////                position.y += sign(velocity.y);
////            }
////            velocity.y = 0;
////            System.out.println(position);
////        }
//
//        //Just before intercept
////        System.out.println(hitbox);
//        if (col(hitbox.x + velocity.x, hitbox.y
//                + velocity.y, t)) {
//            System.out.println("HIT");
////            System.out.println("sign vel.x: " + sign(velocity.x));
////            System.out.println("sign vel.y: " + sign(velocity.y));
//            int n = 0;
//            //Get as close to the wall as possible
//            while (!col(hitbox.x + sign(velocity.x),
//                    hitbox.y + sign(velocity.y), t)) {
//                System.out.println("Inside while loop!: " + n++);
//                System.out.println(sign(velocity.x));
//                System.out.println(sign(velocity.y));
//                hitbox.x += sign(velocity.x);
//                hitbox.y += sign(velocity.y);
//            }
//            velocity.x = 0;
//            velocity.y = 0;
//            grounded = true;
//            
//            System.out.println("On Platform");
//        }
////        System.out.println(hitbox);
//
//    }

    /**
     * Player tile collision
     *
     * @param ob
     * @param tile
     * @return
     */
    private boolean collides(Player player, Tile tile) {
        //does first object collides with second?
//        return player.getHitbox().intersects(tile.hitbox);
//        Rectangle test = player.hitbox;
//        test.x +=
        return tile.hitbox.intersects(hitbox);
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
//        return tile.hitbox.contains(player.position.x + player.velocity.x * dt, player.position.y);
////        return player.getHitbox().intersects(tile.hitbox);
//    }
    private boolean col(float x, float y, Tile t) {
        //does first object collides with second?
//        hitbox.x += x; hitbox.y+=y;
//        System.out.println("old hitbox: "+this.hitbox);
//        System.out.println("new hitbox: "+ (this.hitbox.x + x));
        return t.hitbox.intersects(x, y, hitbox.width, hitbox.height);
    }

    private void updateIdle() {
//        System.out.println("STATE: Idle");
//        grounded = true;
        //Update idle animation
//        moving = false;
//        left = false;
//        right = false;
    }

    private void updateWalk() {
//        System.out.println("STATE: Walking");
        //Check if left or right
//        grounded = true;
    }

    private void updateJump() {
//        System.out.println("STATE: Jumping");

//        grounded = false;
        //TIMES BY DELTA TIME
        if(!grounded){
            velocity.y += acceleration.y*0.1;
        }
        
    }

    /**
     * ************UPDATE & RENDER**************
     */
    @Override
    void gameRender(Graphics2D g) {
        g.setStroke(stroke);
        g.setColor(Color.BLUE);
        //Draw hitbox
//        g.fillRect((int) position.x, (int) position.y, width, height);
        g.drawRect((int) hitbox.x, (int) hitbox.y, width, height);
        //Draw player
//        g.drawImage(player, (int) position.x, (int) position.y, null);

//        drawInfo(g);
    }

    public void drawInfo(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(f);
        g.drawString("Pos: " + String.valueOf(position), fontPos.x, fontPos.y);
        g.drawString("Vel: " + String.valueOf(velocity), fontPos.x, fontPos.y + 35);
        g.drawString("Acc: " + String.valueOf(acceleration), fontPos.x, fontPos.y + 65);
    }

}
