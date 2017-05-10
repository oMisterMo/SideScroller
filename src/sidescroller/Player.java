package sidescroller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import common.Vector2D;
import common.Helper;

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
    private static final int mulSpeed = 200;

    private static final int WALK_SPEED = 150;    //x
    private static final int RUN_SPEED = 200;    //x
//    private static final int FALL_SPEED = 25;   //y
    private static final int JUMP_HEIGHT = 340;
    private static final int FALL_ACEL = 800;    //10 m/s

    private World world;
    private BufferedImage playerImg;    //player.png (hence mo)

    private SpriteSheet playerSheet;
    private Animation idle;
    private Animation idle2;
    private Animation walk;
    private Animation jump;
    private boolean facingRight = true;
    private int idleCounter = 0;

//From dynamic game object
//    public Vector2D position;
//    public Vector2D velocity;
//    private Vector2D acceleration;
//    private float width;
//    private float height;
    public Rectangle.Float playerHitbox;  //outter blue hitbox
    private Rectangle.Float leftHitbox;
    private Rectangle.Float rightHitbox;
    private Rectangle.Float topHitbox;
    private Rectangle.Float bottomHitbox;
    public boolean grounded = false;

    //For debugging****************************
    private Font f;
    private Vector2D fontPos;
    private Stroke stroke;
    private String state_debug = "FALLING";
    private int numOfCollision = 0;

    public Player(World world) {
        this.world = world;

        loadImages();
        initAnimations();
        setPlayerSize();

        //Current the position is the hitbox
        position = new Vector2D(150, GamePanel.GAME_HEIGHT - Tile.TILE_HEIGHT * 2 - 80);
        velocity = new Vector2D(0, 0);
        acceleration = new Vector2D(0, FALL_ACEL);

        playerHitbox = new Rectangle.Float(position.x,
                position.y,
                width,
                height);
        initInnerHitbox();
        initFont();
    }

    private void loadImages() {
        this.playerImg = Assets.player;
        this.playerSheet = new SpriteSheet(Assets.playerSheet);
    }

    private void initAnimations() {
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

    private void setPlayerSize() {
//        //testing different widths and innerhitbox size
//        width = 20;
//        height = 20;
        width = playerImg.getWidth();
        height = playerImg.getHeight();
        System.out.println("player width: " + width);
        System.out.println("player height: " + height);
    }

    private void initInnerHitbox() {
        //RED
        topHitbox = new Rectangle.Float();
        //WHITE
        bottomHitbox = new Rectangle.Float();
        //YELLOW
        leftHitbox = new Rectangle.Float();
        //DARK GRAY
        rightHitbox = new Rectangle.Float();
        updateInnerHitbox();

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

    private void updateInnerHitbox() {
        //create inner hitbox to determin left, right top bottom collision
        float w = playerHitbox.width / 4;
        float h = playerHitbox.height / 2;

        //RED
        topHitbox.x = (playerHitbox.x + playerHitbox.width / 2) - (w);
        topHitbox.y = playerHitbox.y;
        topHitbox.width = w * 2;
        topHitbox.height = h / 2;
        //WHITE
        bottomHitbox.x = (playerHitbox.x + playerHitbox.width / 2) - (w);
        bottomHitbox.y = playerHitbox.y + h + h / 2;
        bottomHitbox.width = w * 2;
        bottomHitbox.height = h / 2;
        //YELLOW
        leftHitbox.x = topHitbox.x - w;
        leftHitbox.y = playerHitbox.y + h / 2;
        leftHitbox.width = w;
        leftHitbox.height = h;
        //DARK GRAY
        rightHitbox.x = topHitbox.x + w * 2;
        rightHitbox.y = playerHitbox.y + h / 2;
        rightHitbox.width = w;
        rightHitbox.height = h;
    }

    private void initFont() {
//        f = new Font("Comic Sans MS", Font.PLAIN, 25);
//        f = new Font("Times new roman", Font.PLAIN, 25);
        f = new Font("Courier new", Font.PLAIN, 25);
        fontPos = new Vector2D(5, 60);
        stroke = new BasicStroke(1);
    }

//    public void keyPressed(KeyEvent e) {
//        int key = e.getKeyCode();
//
//        if (key == KeyEvent.VK_A) {
////            playerState = STATE_WALK;
//
////            //walk with acceleration
////            if(velocity.x > 0){
////                velocity.x -= WALK_SPEED;
////            }else if(velocity.x > -WALK_SPEED){ //walk_speed = 200
////                velocity.x -= 150;
////            }
//            velocity.x = -WALK_SPEED;
////            System.out.println("vel.x: " + velocity.x);
//            System.out.println("TL-held");
//        }
//
//        if (key == KeyEvent.VK_D) {
////            playerState = STATE_WALK;
//
////            //walk with acceleration
////            if(velocity.x < 0){
////                velocity.x += WALK_SPEED;
////            }else if(velocity.x < WALK_SPEED){
////                velocity.x += 150;
////            }
//            velocity.x = WALK_SPEED;
////            System.out.println("vel.x: " + velocity.x);
//            System.out.println("TR-held");
//        }
//
//        if (key == KeyEvent.VK_W) {
////            playerState = STATE_JUMP;
////            velocity.y = -WALK_SPEED;
////            System.out.println("vel.y: " + velocity.y);
//        }
//
//        if (key == KeyEvent.VK_S) {
////            velocity.y = WALK_SPEED;
////            System.out.println("vel.y: " + velocity.y);
//        }
//
//        if (key == KeyEvent.VK_SPACE) {
////            System.out.println("JUMP");
////            grav = true;
//            if (grounded) {
//                playerState = STATE_JUMP;
//                velocity.y = -JUMP_HEIGHT;  //impulse up
//                grounded = false;
//            }
//        }
//    }
//
//    public void keyReleased(KeyEvent e) {
//        int key = e.getKeyCode();
////        System.out.println("Letgo");
//        if (key == KeyEvent.VK_A) {
//            velocity.x = 0;//set friction rate insted
//        }
//
//        if (key == KeyEvent.VK_D) {
////            playerState = STATE_IDLE;
//            velocity.x = 0;//set friction rate insted
//
////            System.out.println("vel.x: " + velocity.x);
//        }
//        if (key == KeyEvent.VK_W || key == KeyEvent.VK_S) {
////            playerState = STATE_IDLE;
//            velocity.y = 0;
////            System.out.println("vel.y: " + velocity.y);
//        }
//
//        if (key == KeyEvent.VK_SPACE) {
////            System.out.println("Release JUMP");
//            playerState = STATE_FALLING;
//
//            //If player is on up jump arch
//            if (velocity.y < 0) {
//                velocity.y *= 0.5;
//            }
//        }
//    }
    public void setGrounded(boolean val) {
//        this.grounded = val;
    }

    private void collision_one() {
        /*
         1.Detect if larger hitbox intercepts a spikeBlock
         2.handle smaller intersections
         */

        //Goes through every spikeBlock in the game (NOT GOOD)
        for (int j = 0; j < World.NO_OF_TILES_Y; j++) {
            for (int i = 0; i < World.NO_OF_TILES_X; i++) {
                Tile t = world.getTile(i, j);
                if (t.solid) {
                    //If we have a solid spikeBlock
                    numOfCollision++;
                    if (playerHitbox.intersects(t.hitbox)) {
                        //Check inner hitbox for collision and respond
                        doInnerColCheck(t.hitbox);
                    }
                }
            }
        }//end outer for loop
    }

    private void doInnerColCheck(Rectangle wall) {
//        System.out.println("inner");
        //top
        if (topHitbox.intersects(wall)) {
//            System.out.println("top");
            velocity.y = 0;
            playerHitbox.y = wall.y + wall.height;
        }
        //left
        if (leftHitbox.intersects(wall)) {
//            System.out.println("left");
//            velocity.x = 0;
            playerHitbox.x = wall.x + wall.width - 2f;   //could remove the 2's
        }
        //right
        if (rightHitbox.intersects(wall)) {
//            System.out.println("right");
//            velocity.x = 0;
            playerHitbox.x = wall.x - playerHitbox.width + 2; //removes the 2's
        }
        //bottom
        if (bottomHitbox.intersects(wall)) {
//            System.out.println("bot");
            grounded = true;
            velocity.y = 0;
            playerHitbox.y = wall.y - playerHitbox.height;
        }
    }

    public void handleInput() {

        //Walk input
        if (Input.isKeyPressed(KeyEvent.VK_A)) {
            facingRight = false;
//            System.out.println("A");
            playerState = STATE_WALK;
            velocity.x = -WALK_SPEED;

            //Make player run left
            if (Input.isKeyPressed(KeyEvent.VK_M)) {
//                System.out.println("Left_RUN");
                playerState = STATE_RUN;
                velocity.x = -RUN_SPEED;
            } else if (Input.isKeyReleased(KeyEvent.VK_M)) {
                playerState = STATE_WALK;
                velocity.x = -WALK_SPEED;
            }
        } else if (Input.isKeyPressed(KeyEvent.VK_F)) { //VK_D seems broken
            facingRight = true;
//            System.out.println("D");
            playerState = STATE_WALK;
            velocity.x = WALK_SPEED;

            //Make player run right
            if (Input.isKeyPressed(KeyEvent.VK_M)) {
//                System.out.println("Right_RUN");
                playerState = STATE_RUN;
                velocity.x = RUN_SPEED;
                //CAN NOT RUN TR AND JUMP

            } else if (Input.isKeyReleased(KeyEvent.VK_M)) {
                playerState = STATE_WALK;
                velocity.x = WALK_SPEED;
            }
        } else {
            playerState = STATE_IDLE;
            velocity.x = 0;
        }

        //Testing jump code here
        if (Input.isKeyTyped(KeyEvent.VK_SPACE)) {
            System.out.println("JUMP!");
            if (grounded) {
                playerState = STATE_JUMP;
                velocity.y = -JUMP_HEIGHT;  //impulse up
                grounded = false;
//                System.out.println("JUMP!");
            }
            jump.resetAnimation();
        }

        if (Input.isKeyReleased(KeyEvent.VK_SPACE)) {
            playerState = STATE_FALLING;
            //If player released jump key while jumping up
            if (velocity.y < 0) {
                velocity.y *= 0.5;
            }
        }

//        //make run
//        if (Input.isKeyPressed(KeyEvent.VK_M)) {
//            playerState = STATE_RUN;
//            if (facingRight) {
//                velocity.x = RUN_SPEED;
//            } else {
//                velocity.x = -RUN_SPEED;
//            }
////            velocity.x = Helper.Clamp(velocity.x, -RUN_SPEED, RUN_SPEED);
//        }
        //Jump code was here...
    }

    /**
     * ************UPDATE & RENDER**************
     */
    @Override
    void gameUpdate(float deltaTime) {
        //If player is not on the ground, he is falling
        if (!grounded) {
            playerState = STATE_FALLING;
        }
        //Apply gravity if player is falling or jumping
        if (playerState == STATE_FALLING || playerState == STATE_JUMP) {
            //velocity never faster than 25m/s
            velocity.y += acceleration.y * deltaTime;
            velocity.y = Helper.Clamp(velocity.y, -mulSpeed * 3, mulSpeed * 3);
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
                walk.setDelay(50);  //doesn't work with dt
                walk.update(deltaTime);
                break;
            case STATE_JUMP:
            case STATE_FALLING:
                idleCounter = 0;
                jump.update(deltaTime);
                break;
        }
        //->collision_one() was here

        //Move player otter hitbox
        playerHitbox.x += (velocity.x * deltaTime);
        playerHitbox.y += (velocity.y * deltaTime);

        //Handle collision AFTER we commit our movement
        collision_one();
        //Update players inner hitbox
        updateInnerHitbox();
    }

    @Override
    void gameRender(Graphics2D g) {
        g.setStroke(stroke);

        switch (World.renderMode) {
            case World.HITBOX_MODE:
                drawHitbox(g);
                break;
            case World.BITMAP_MODE:
                drawRender(g);
                break;
//            case BITMAP_SPIKE_MODE:
//                drawSpikeRender(g);
//                break;
        }
    }

    private void drawHitbox(Graphics2D g) {
        //Draw playerHitbox
        g.setColor(Color.BLUE);
        g.draw(playerHitbox);

//        drawInfo(g);
        //Draw all hit boxes
        g.setColor(Color.ORANGE);
        g.fill(leftHitbox);
        g.setColor(Color.GREEN);
        g.fill(rightHitbox);
        g.setColor(Color.RED);
        g.fill(topHitbox);
        g.setColor(Color.WHITE);
        g.fill(bottomHitbox);
    }

    private void drawRender(Graphics2D g) {
        //Draw player
        switch (playerState) {
            case STATE_IDLE:
                //If player is idle for more than 3 seconds, draw idle2
                if (idleCounter > 3000) {
                    g.drawImage(idle2.getImage(), (int) playerHitbox.x, (int) playerHitbox.y, null);
                } else {
                    //Otherwise draw normal idle animation
                    if (facingRight) {
                        g.drawImage(idle.getImage(), (int) playerHitbox.x + 32, (int) playerHitbox.y,
                                -(int) playerHitbox.width, (int) playerHitbox.height, null);
                    } else {
                        g.drawImage(idle.getImage(), (int) playerHitbox.x, (int) playerHitbox.y,
                                (int) playerHitbox.width, (int) playerHitbox.height, null);
                    }
                }
                break;
            case STATE_WALK:
                if (facingRight) {
                    g.drawImage(walk.getImage(), (int) playerHitbox.x + 32, (int) playerHitbox.y,
                            -(int) playerHitbox.width, (int) playerHitbox.height, null);
                } else {
                    g.drawImage(walk.getImage(), (int) playerHitbox.x, (int) playerHitbox.y,
                            (int) playerHitbox.width, (int) playerHitbox.height, null);
                }
                break;
            case STATE_RUN:
                if (facingRight) {
                    g.drawImage(walk.getImage(), (int) playerHitbox.x + 32, (int) playerHitbox.y,
                            -(int) playerHitbox.width, (int) playerHitbox.height, null);
                } else {
                    g.drawImage(walk.getImage(), (int) playerHitbox.x, (int) playerHitbox.y,
                            (int) playerHitbox.width, (int) playerHitbox.height, null);
                }
                break;
            case STATE_JUMP:
            case STATE_FALLING:
                if (facingRight) {
                    g.drawImage(jump.getImage(), (int) playerHitbox.x + 32, (int) playerHitbox.y,
                            -(int) playerHitbox.width, (int) playerHitbox.height, null);
                } else {
                    g.drawImage(jump.getImage(), (int) playerHitbox.x, (int) playerHitbox.y,
                            (int) playerHitbox.width, (int) playerHitbox.height, null);
                }
                break;
        }
//        g.drawImage(playerImg, (int) playerHitbox.x, (int) playerHitbox.y, null);
    }

    public void drawInfo(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(f);
//        g.drawString("Draw REAL POS", fontPos.x, fontPos.y);
//        g.drawString("Pos: " + String.valueOf(position), fontPos.x, fontPos.y);
        g.drawString("Pos: " + "x: " + (int) playerHitbox.x + ", y: " + (int) playerHitbox.y, fontPos.x, fontPos.y);
        g.drawString("Vel: " + String.valueOf(velocity), fontPos.x, fontPos.y + 35);
        g.drawString("Acc: " + String.valueOf(acceleration), fontPos.x, fontPos.y + 65);
        //draw player state
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
