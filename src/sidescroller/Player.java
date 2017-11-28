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
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player within our game world
 *
 * 32 pixels = 1 meter
 *
 * 07-Sep-2016, 01:56:02.
 *
 * @author Mo
 */
public class Player extends DynamicGameObject {

    public static final float PLAYER_WIDTH = 28;    //32
    public static final float PLAYER_HEIGHT = 28;   //32
    //Player states
    //All states out playerMo can be in -> Different animation for each playerState
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
    public int playerState = STATE_FALLING;

    //MovementStates    ( * 32 pixels = 1 meter)
    private static final int TERMINAL_VELOCITY = 200;   //6.25 m/s
    private static final int WALK_SPEED = 150;          //x
    private static final int RUN_SPEED = 200;           //x
    private static final int JUMP_HEIGHT = 380;         //y impulse
//    private static final int FALL_ACEL = 800;         //10 m/s

    private World world;
    private SpriteSheet playerSheet;
    private Animation idle;
    private Animation idle2;
    private Animation walk;
    private Animation jump;
    private boolean facingRight = true;
    private int idleCounter = 0;

    //public Rectangle.Float bounds;  //outter blue hitbox
    private Rectangle.Float leftHitbox;
    private Rectangle.Float rightHitbox;
    private Rectangle.Float topHitbox;
    public Rectangle.Float bottomHitbox;
    public boolean grounded = false;

    private List<Fireball> fireball;

    //For debugging****************************
    private Font f;
    private Vector2D fontPos;
    private Stroke stroke;
    private String state_debug = "FALLING";
    private int numOfCollision = 0;

    public Player(float x, float y, float width, float height, World world) {
        super(x, y, width, height);
//        position.set(x, y);
        this.world = world;

        loadImages();
        initAnimations();
//        setPlayerSize();  // I think I do not need

        //Current the position is the hitbox
//        position = new Vector2D(150, GamePanel.GAME_HEIGHT - Tile.TILE_HEIGHT * 2 - 80);
//        velocity = new Vector2D(0, 0);
//        acceleration = new Vector2D(0, FALL_ACEL);
//        bounds = new Rectangle.Float(150,
//                GamePanel.GAME_HEIGHT - Tile.TILE_HEIGHT * 2 - 80,
//                width,
//                height);
//        acceleration.set(0, FALL_ACEL);   //Using worlds gravity now!!!
        initInnerHitbox();
        initFont();
        fireball = new ArrayList<>(5);
    }

    private void loadImages() {
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
//        width = playerMo.getWidth();
//        height = playerMo.getHeight();
//        System.out.println("player width: " + width);
//        System.out.println("player height: " + height);
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

//    public void updateInnerHitbox() {
//        //create inner hitbox to determin left, right top bottom collision
//        float w = bounds.width / 4;
//        float h = bounds.height / 2;
//
//        //RED
//        topHitbox.x = (bounds.x + bounds.width / 2) - (w);
//        topHitbox.y = bounds.y;
//        topHitbox.width = w * 2;
//        topHitbox.height = h / 2;
//        //WHITE
//        bottomHitbox.x = (bounds.x + bounds.width / 2) - (w);
//        bottomHitbox.y = bounds.y + h + h / 2;
//        bottomHitbox.width = w * 2;
//        bottomHitbox.height = h / 2;
//        //YELLOW
//        leftHitbox.x = topHitbox.x - w;
//        leftHitbox.y = bounds.y + h / 2;
//        leftHitbox.width = w;
//        leftHitbox.height = h;
//        //DARK GRAY
//        rightHitbox.x = topHitbox.x + w * 2;
//        rightHitbox.y = bounds.y + h / 2;
//        rightHitbox.width = w;
//        rightHitbox.height = h;
//    }
//    public void updateInnerHitbox() {
//        //create inner hitbox to determin left, right top bottom collision
//        float w = bounds.width / 4;
//        float h = bounds.height / 2;
//
//        //RED
//        topHitbox.x = (bounds.x + bounds.width / 2) - (w);
//        topHitbox.y = bounds.y;
//        topHitbox.width = w * 2;
//        topHitbox.height = h / 2;
//        //WHITE
//        bottomHitbox.x = (bounds.x + bounds.width / 2) - (w * 3) / 2;
//        bottomHitbox.y = bounds.y + h + h / 2;
//        bottomHitbox.width = w * 3;
//        bottomHitbox.height = h / 2;
//        //YELLOW
//        leftHitbox.x = topHitbox.x - w;
//        leftHitbox.y = bounds.y + h / 2;
//        leftHitbox.width = w;
//        leftHitbox.height = h;
//        //GREEN
//        rightHitbox.x = topHitbox.x + w * 2;
//        rightHitbox.y = bounds.y + h / 2;
//        rightHitbox.width = w;
//        rightHitbox.height = h;
//    }
    public void updateInnerHitbox() {
        //create inner hitbox to determin left, right top bottom collision
        float w = bounds.width / 4;
        float h = bounds.height / 2;

//        //RED
        topHitbox.x = (bounds.x + bounds.width / 2) - (w);
        topHitbox.y = bounds.y;
        topHitbox.width = w * 2;
        topHitbox.height = h / 2;
        //WHITE
        bottomHitbox.x = (bounds.x + bounds.width / 2) - (w * 3) / 2;
        bottomHitbox.y = bounds.y + bounds.height - h / 2;
        bottomHitbox.width = w * 3;
        bottomHitbox.height = h / 2;
        //YELLOW
//        System.out.println("old: " + ((bounds.x + bounds.width / 2) - w - w));
//        System.out.println("new: " + bounds.x);
        leftHitbox.x = bounds.x;
        leftHitbox.y = bounds.y + h / 2;
        leftHitbox.width = w;
        leftHitbox.height = h;
        //GREEN
//        System.out.println("old: "+((bounds.x + bounds.width / 2) - w + w * 2));
//        System.out.println("nes: "+((bounds.x + bounds.width) - leftHitbox.width));
        rightHitbox.x = (bounds.x + bounds.width) - leftHitbox.width;
        rightHitbox.y = bounds.y + h / 2;
        rightHitbox.width = w;
        rightHitbox.height = h;
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

    private void initFont() {
//        f = new Font("Comic Sans MS", Font.PLAIN, 25);
//        f = new Font("Times new roman", Font.PLAIN, 25);
        f = new Font("Courier new", Font.PLAIN, 25);
        fontPos = new Vector2D(5, 60);
        stroke = new BasicStroke(1);
    }

    public void setGrounded(boolean val) {
//        this.grounded = val;
    }

    /**
     * Loop through every single tile
     */
    private void collision_one() {
        /*
         1.Detect if larger hitbox intercepts a spikeBlock
         2.handle smaller intersections
         */

        //Goes through every spikeBlock in the game (NOT GOOD)
        for (int y = 0; y < World.NO_OF_TILES_Y; y++) {
            for (int x = 0; x < World.NO_OF_TILES_X; x++) {
                Tile t = world.getTile(x, y);
                if (t.solid) {
                    //If we have a solid spikeBlock
                    numOfCollision++;
                    System.out.println(numOfCollision); //Make it average per secondt 
                    if (bounds.intersects(t.bounds)) {
                        //Check inner hitbox for collision and respond
                        doInnerColCheck(t.bounds);
                    }
                }
            }
        }//end outer for loop
    }

    /**
     * Loop through list of solid tiles
     */
    private void collision_two() {

    }

    /**
     * Loop through whats visible to the camera(+1) only
     */
    private void collision_three() {

    }

    public void doInnerColCheck(Rectangle.Float wall) {
//        System.out.println("inner");
        //top
        if (topHitbox.intersects(wall)) {
//            System.out.println("top");
            velocity.y = 0;
            bounds.y = wall.y + wall.height;
        }
        //left
        if (leftHitbox.intersects(wall)) {
//            System.out.println("left");
            velocity.x = 0;
            bounds.x = wall.x + wall.width + 1;   //could remove the 2's
        }
        //right
        if (rightHitbox.intersects(wall)) {
//            System.out.println("right");
            velocity.x = 0;
            bounds.x = wall.x - bounds.width - 1; //removes the 2's
        }
        //bottom
//        Tile botLeft = worldToTile(bottomHitbox.x, bottomHitbox.y);
//        Tile botRight = worldToTile(bottomHitbox.x + bottomHitbox.width, bottomHitbox.y);
//        if (!botLeft.solid && !botRight.solid) {
//            grounded = false;
//        }
        if (bottomHitbox.intersects(wall)) {
            if (velocity.y > 0) {
//            System.out.println("bot");
                grounded = true;
                velocity.y = 0;
                bounds.y = wall.y - bounds.height;
//            Camera.cameraState = Camera.STATE_SHAKE;
            } else {
                System.out.println("NOO, IM MOVING UP, do not snap me pls");
            }
        }
    }

    private Tile worldToTile(float x, float y) {
        System.out.println((int) Math.floor(x / Tile.TILE_WIDTH));
        System.out.println((int) Math.floor(y / Tile.TILE_WIDTH));
        return world.getTile((int) Math.floor(x / Tile.TILE_WIDTH), (int) Math.floor(y / Tile.TILE_WIDTH));
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
//            System.out.println("JUMP!");
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

        //create firball
        if (Input.isKeyTyped(KeyEvent.VK_B)) {
            if (fireball.size() < 5) {
                //Do I need a add a new instance? object pool?
                fireball.add(new Fireball(bounds.x, bounds.y, 32, 32, facingRight));
            }
        }
    }

    /**
     * ************UPDATE & RENDER
     *
     **************
     * @param deltaTime
     */
    @Override
    public void gameUpdate(float deltaTime) {
        //Move player otter hitbox
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.x += (velocity.x * deltaTime);
        bounds.y += (velocity.y * deltaTime);

//        //Handle collision AFTER we commit our movement
//        //Update players inner hitbox
        updateInnerHitbox();

        //If player is not on the ground, he is falling
        if (!grounded) {
            playerState = STATE_FALLING;
        }
        //Apply gravity if player is falling or jumping
        if (playerState == STATE_FALLING || playerState == STATE_JUMP) {
            //velocity never faster than 25m/s
            velocity.y += World.gravity.y * deltaTime;
            velocity.y = Helper.Clamp(velocity.y, -TERMINAL_VELOCITY * 3, TERMINAL_VELOCITY * 3);
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
            case STATE_DEAD:
                break;
        }

//        collision_one();
        //Update firballs
        updateFireballs(deltaTime);
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

        //Draw inner hit boxes
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
        //Draw playerMo
        switch (playerState) {
            case STATE_IDLE:
                //If playerMo is idle for more than 3 seconds, draw idle2
                if (idleCounter > 3000) {
                    g.drawImage(idle2.getImage(), (int) bounds.x, (int) bounds.y,
                            (int) bounds.width, (int) bounds.height, null);
                } else {
                    //Otherwise draw normal idle animation
                    if (facingRight) {
                        g.drawImage(idle.getImage(), (int) (bounds.x + bounds.width), (int) bounds.y,
                                -(int) bounds.width, (int) bounds.height, null);
                    } else {
                        g.drawImage(idle.getImage(), (int) bounds.x, (int) bounds.y,
                                (int) bounds.width, (int) bounds.height, null);
                    }
                }
                break;
            case STATE_WALK:
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

    public void drawInfo(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(f);
//        g.drawString("Draw REAL POS", fontPos.x, fontPos.y);
//        g.drawString("Pos: " + String.valueOf(position), fontPos.x, fontPos.y);
        g.drawString("Pos: " + "x: " + (int) bounds.x + ", y: " + (int) bounds.y, fontPos.x, fontPos.y);
        g.drawString("Vel: " + String.valueOf(velocity), fontPos.x, fontPos.y + 35);
        g.drawString("Acc: " + String.valueOf(World.gravity), fontPos.x, fontPos.y + 65);
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
