package sidescroller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JPanel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.util.ArrayList;

/**
 * Always when you remove you have to set the variable to null -> AVOID MEMORY
 * LEAKS
 *
 * 06-Sep-2016, 23:03:23.
 *
 * @author Mo
 */
public class GamePanel extends JPanel implements Runnable {

    //GAMES WIDTH & HEIGHT
    public static final int GAME_WIDTH = 1280;
    public static final int GAME_HEIGHT = 576;

    private Thread thread;
    private boolean running;
    private BufferedImage image;
    private Graphics2D g;

    private final int FPS = 60;
    private long averageFPS;
    //dont need -> Checks to see if game loop sleeps for negative time
    private int counter = 0;

    //GAME VARIABLES HERE-------------------------------------------------------
    private Color backgroundColor;    //Represents colour of background

    private Assets assets;
    private Camera camera;
    private World world;
    private Player player;

    private SpriteSheet ss;
    private BufferedImage spritesheet;
    private Animation manWalk;
    private SpriteSheet ss2;
    private Animation ani1;
    private Animation ani2;
    private Animation ani3;
    private Animation ani4;

    private int num = 0;

    //CONSTRUCTOR
    public GamePanel() {
        super();
        setPreferredSize(new Dimension(GAME_WIDTH - 10, GAME_HEIGHT - 10));
        setFocusable(true);
        //System.out.println(requestFocusInWindow());
        requestFocus(); //-> platform dependant

        //initialise varialbes
        init();
    }

    private void init() {
        assets = new Assets();
        world = new World();
        player = new Player(world);
        camera = new Camera(player, new Vector2D());

        initManWalk();
        initMarioSprites();

        //-----------------------------END my random test
        backgroundColor = new Color(0, 0, 0);    //Represents colour of background
        //backgroundColor = new Color(255, 255, 255);    //Represents colour of background

        //Load listeners
        addKeyListener(new TAdapter());
        addMouseListener(new MAdapter());
    }

    private void initManWalk() {
        ss = new SpriteSheet("C:\\Users\\Mo\\Games\\"
                + "myGames\\Pictures\\SpriteSheets\\man_walk.png");
        spritesheet = ss.getSpriteSheet();
        BufferedImage[] mario;
        mario = new BufferedImage[9];
        for (int i = 0; i < 9; i++) {
            mario[i] = ss.getTile(i + 1, 4, 64, 64);
        }
        manWalk = new Animation();
        manWalk.setFrames(mario);
        manWalk.setDelay(100);
    }

    private void initMarioSprites() {
        ss2 = new SpriteSheet("C:\\Users\\Mo\\Games\\"
                + "myGames\\Pictures\\SpriteSheets\\general.png");
        //Yoshi Coin
        BufferedImage[] general = new BufferedImage[6];
        for (int i = 0; i < 6; i++) {
            general[i] = ss2.getTile(i + 1, 2, 16, 32);
        }
        ani1 = new Animation();
        ani1.setFrames(general);
        ani1.setDelay(100);

        //Block
        general = new BufferedImage[4];
        for (int i = 0; i < 4; i++) {
            general[i] = ss2.getTile(i + 1, 5, 16, 16);
        }
        ani2 = new Animation();
        ani2.setFrames(general);
        ani2.setDelay(130);

        //Block 2
        general = new BufferedImage[4];
        for (int i = 0; i < 4; i++) {
            general[i] = ss2.getTile(i + 1, 6, 16, 16);
        }
        ani3 = new Animation();
        ani3.setFrames(general);
        ani3.setDelay(130);

        //Music block
        general = new BufferedImage[4];
        for (int i = 0; i < 4; i++) {
            general[i] = ss2.getTile(i + 1, 15, 16, 16);
        }
        ani4 = new Animation();
        ani4.setFrames(general);
        ani4.setDelay(130);
    }

    //METHODS
    /*
     Is called after our JPanel has been added to the JFrame component.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {
        long startTime;
        long timeTaken;
        long frameCount = 0;
        long totalTime = 0;
        long waitTime;
        long targetTime = 1000 / FPS;

        //To test independent speed
        long start2 = 0;
        long timeMillis2 = 0;
        long timeMillis3 = 0;
        long timeMillis4 = 0;

        running = true;

        image = new BufferedImage(GAME_WIDTH, GAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();

        //GAME LOOP
        while (running) {
            startTime = System.nanoTime();

            //start2 = System.nanoTime();
//            System.out.println("dt:"+ (1f / FPS));
            gameUpdate(1f / FPS);
            //timeMillis2 = (System.nanoTime() - start2) / 1000000;

            //start2 = System.nanoTime();
            gameRender(g);
            //timeMillis3 = (System.nanoTime() - start2) / 1000000;

            //start2 = System.nanoTime();
            gameDraw();
            //timeMillis4 = (System.nanoTime() - start2) / 1000000;

//            gameUpdate();
//            gameRender(g);
//            gameDraw();
            //How long it took to run
            timeTaken = (System.nanoTime() - startTime) / 1000000;
            //              16ms - targetTime
            waitTime = targetTime - timeTaken;

            //System.out.println(timeTaken);
            if (waitTime < 0) {
                //I get a negative value at the beg
                System.out.println(counter++ + " - NEGATIVE: " + waitTime);
                System.out.println("targetTime = " + targetTime);
                System.out.println("timeTaken = " + timeTaken + "\n");
            }

//            //Speed TEST methods
//            if(frameCount >= 58) {
//                //Test the time taken to run
//                System.out.println("Update time: " + timeMillis2);
//                System.out.println("Render time: " + timeMillis3);
//                System.out.println("Draw time: " + timeMillis4);
//                System.out.println("------------------------------------------");
//            }
            try {
                //System.out.println("Sleeping for: " + waitTime);
                //thread.sleep(waitTime);
                Thread.sleep(waitTime);
            } catch (Exception ex) {

            }
            totalTime += System.nanoTime() - startTime;
            frameCount++;

            //If the current frame == 60  we calculate the average frame count
            if (frameCount >= FPS) {
                averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
                //System.out.println("Average fps: " + averageFPS);
            }
        }

    }

    private void handleCollisions(float dt) {
//        System.out.println(collides(player, world.getTile(3, 10)));

//        checkOneTile();
//        checkAllTiles();
    }

    private void pushOut(Rectangle overlap) {
        if (player.velocity.x > 0) {
            player.position.x -= overlap.width - 1;
        } else if (player.velocity.x < 0) {
            player.position.x += overlap.width + 1;
        }
        if (player.velocity.y > 0) {
            player.position.y -= overlap.height - 1;
        } else if (player.velocity.x < 0) {
            player.position.y += overlap.height + 1;
        }
    }

//    private void checkOneTile() {
//        Tile t = world.getTile(3, 10);
//        if (collides(player, t)) {
//            /*
//             Don't let player enter tile
//            
//             1.push player out of tile
//             2.set correct states
//             */
////            Vector2D overlap = new Vector2D();
//            Rectangle overlap = new Rectangle();
//            Rectangle.intersect(player.playerHitbox, t.playerHitbox, overlap);
//            System.out.println("************************");
//            System.out.println("player: " + player.playerHitbox);
//            System.out.println("playerHitbox: " + t.playerHitbox);
//            System.out.println("overlap: " + overlap);
//            System.out.println("");
//
//            if (overlap.height < overlap.width) {
//                player.position.y += overlap.height * sign(player.velocity.y);
//            } else {
//                player.position.x += overlap.width * sign(player.velocity.x);
//            }
//            pushOut(overlap);
//        }
//    }
//    private void checkAllTiles() {
//        /*
//         **Have collidable abstract class**
//         -dont need to go through every tile
//         -dont need to check if tile is solid
//         */
//        
//        //Goes through every tile in the game
//        for (int j = 0; j < World.NO_OF_TILES_Y; j++) {
//            for (int i = 0; i < World.NO_OF_TILES_X; i++) {
//                Tile t = world.getTile(i, j);
//                if (t.solid) {
//                    //If player hits a single solid tile
//                    if (collides(player, t)) {
//                        System.out.println("hit.." + num++);
//                        player.grounded = true;
//                        Player.playerState = Player.STATE_IDLE;
////                        player.velocity.x =0;
//                        player.velocity.y = 0;
////                        Rectangle overLap = new Rectangle();
////                        Rectangle.intersect(player.playerHitbox, t.playerHitbox, overLap);
//////                        pushOut(overLap);
//                        player.position.y = t.y - Tile.TILE_HEIGHT;
//
//                    }
//                }
//            }
//        }
//    }
    /**
     * If x > 0 return 1 else -1
     *
     * @param x
     * @return Returns 1 or -1 depending on state of input
     */
    private int sign(float x) {
        int num = 1;
        if (x > 0) {
            return -1;
        }
        return num;
    }

//    private void testCol() {
//        Tile t = world.getTile(3, 10);
//        if (hor_col(player, t, dt)) {
//            System.out.println("HIT");
//            while (!hor_col2(player, t, dt)) {
//                player.position.x += sign(player.velocity.x);
//                System.out.println("Pushing out");
//                System.out.println(sign(player.velocity.x));
//            }
//            player.velocity.x = 0;
//        }
//        player.position.x += player.velocity.x * dt;
//    }
    private void pushOut(Player p, Tile t) {
//        p.playerHitbox.
    }

//    private boolean collides(GameObject ob, GameObject ob2) {
//        //does first object collides with second?
//        return ob.getHitbox().intersects(ob2.getHitbox());
//    }
//    /**
//     * Player tile collision
//     *
//     * @param ob
//     * @param tile
//     * @return
//     */
//    private boolean collides(Player player, Tile tile) {
//        //does first object collides with second?
//        return player.getHitbox().intersects(tile.playerHitbox);
//    }
    /**
     * Player tile collision
     *
     * @param ob
     * @param tile
     * @return
     */
    private boolean hor_col(Player player, Tile tile, float dt) {
        //does first object collides with second?
        return tile.hitbox.contains(player.position.x + player.velocity.x * dt, player.position.y);
//        return player.getHitbox().intersects(tile.playerHitbox);
    }

    private boolean hor_col2(Player player, Tile tile, float dt) {
        //does first object collides with second?
        return tile.hitbox.contains(sign(player.position.x + player.velocity.x * dt), player.position.y);
//        return player.getHitbox().intersects(tile.playerHitbox);
    }

    /**
     * Player tile collision
     *
     * @param ob
     * @param tile
     * @return
     */
    private boolean ver_col(Player player, Tile tile, float dt) {
        //does first object collides with second?
        return tile.hitbox.contains(player.position.x, player.position.y + player.velocity.y * dt);
//        return player.getHitbox().intersects(tile.playerHitbox);
    }

    private void gameUpdate(float deltaTime) {

        //********** Do updates HERE **********
//        camera.gameUpdate(deltaTime);

        world.gameUpdate(deltaTime);
        player.gameUpdate(deltaTime);

//        manWalk.update();
//        ani1.update();
//        ani2.update();
//        ani3.update();
//        ani4.update();

//        //Check for collisions
//        handleCollisions(deltaTime);
    }

    /**
     * ******************** UPDATE & RENDER *************************
     */
    private void gameRender(Graphics2D g) {
        //Clear screen
        g.setColor(backgroundColor);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        //********** Do drawings HERE **********
        //When camera moves, so does game world
        //--uptohere
//        AffineTransform old = g.getTransform();

//        AffineTransform tran = new AffineTransform();
//        tran.translate(GAME_WIDTH/2, GAME_HEIGHT);
//        tran.scale(3.5, 3.5);
//        tran.translate(-GAME_WIDTH/2, -GAME_HEIGHT);
//        g.setTransform(tran);
        //REMOVE ABOVE &  +100, + g.setTransform(old);
        //Draw here

//        g.translate(camera.camPos.x + 100, camera.camPos.y);
        //Draw random animations
//        g.drawImage(manWalk.getImage(), 300, 430, null);
//        g.drawImage(ani1.getImage(),
//                (int) GamePanel.GAME_WIDTH / 2 - 150, (int) GamePanel.GAME_HEIGHT / 2 + 80,
//                null);
//        g.drawImage(ani2.getImage(),
//                (int) GamePanel.GAME_WIDTH / 2 + 25 - 150, (int) GamePanel.GAME_HEIGHT / 2 + 80,
//                null);
//        g.drawImage(ani3.getImage(),
//                (int) GamePanel.GAME_WIDTH / 2 + 50 - 150, (int) GamePanel.GAME_HEIGHT / 2 + 80,
//                null);
//        g.drawImage(ani4.getImage(),
//                (int) GamePanel.GAME_WIDTH / 2 + 75 - 150, (int) GamePanel.GAME_HEIGHT / 2 + 80,
//                null);
        world.gameRender(g);
        player.gameRender(g);
//        g.translate(-camera.camPos.x, -camera.camPos.y);

        //Draw text information----------
        g.setColor(Color.RED);
//        g.fillRect(0, 0, 10, 10);//delete me origin 
        g.drawString("FPS:" + averageFPS, 5, 25);
        player.drawInfo(g);
//        if (player.grounded) {
//            g.setColor(Color.YELLOW);
//            g.drawOval(700, 100, 50, 50);
//        }

//        g.setTransform(old);
    }

    private void gameDraw() {
        Graphics g2 = this.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
    }

    /**
     * *************** EVENT HANDERLERS ***********************
     */
    //Handle Input ** Inner Class
    private class TAdapter extends KeyAdapter {

        //When a key is pressed, let the CRAFT class deal with it.
        @Override
        public void keyPressed(KeyEvent e) {
            //Handle player from world movement
            player.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            //ship.keyReleased(e);
            player.keyReleased(e);
        }
    }

    private class MAdapter implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            //System.out.println("CLICKED");

            //Clicked in one position
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //System.out.println("PRESSED");

            //tween.mousePressed(e);
            //transition.mousePressed(e);
            //dragon.mousePressed(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //System.out.println("RELEASED");
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //System.out.println("ENTERED");
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //System.out.println("EXITED");
        }

    }

    //Getters and Setters
    public Color getColor() {
        return backgroundColor;
    }

}
