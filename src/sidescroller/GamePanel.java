package sidescroller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.Rectangle;
import javax.swing.JPanel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import common.Vector2D;

/**
 * When you remove you have to set the variable to null -> AVOID MEMORY
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

    private Input input;

    private final int FPS = 60;
    private long averageFPS;
    private float scaleTime = 1;

    //GAME VARIABLES HERE-------------------------------------------------------
    private Color backgroundColor;    //Represents colour of background

//    private Assets assets;    //Not needed here
    private Camera camera;
    private World world;
    private Player player;
    private Clouds clouds;

    //Extras random things
    private SpriteSheet ss;
    private BufferedImage spritesheet;
    private Animation manWalk;
    private SpriteSheet ss2;
    private Animation ani1;
    private Animation ani2;
    private Animation ani3;
    private Animation ani4;

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
//        assets = new Assets();
        Assets.loadImages();
        world = new World();
        player = new Player(world);
        clouds = new Clouds();
        camera = new Camera(player, new Vector2D());

//        initManWalk();
//        initMarioSprites();
        //-----------------------------END my random test
        backgroundColor = new Color(0, 0, 0);    //Represents colour of background
//        backgroundColor = new Color(135,206,235);    //Represents colour of background

        //Load listeners
        input = new Input();
        addKeyListener(input);
//        addKeyListener(new TAdapter());
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
        int counter = 0;    //can delete 

        running = true;

        image = new BufferedImage(GAME_WIDTH, GAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();

        //GAME LOOP
        while (running) {
            startTime = System.nanoTime();

            handleInput();
            gameUpdate(1f / FPS);
            Input.updateLastKey();
            gameRender(g);
            gameDraw();

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

    private void handleInput() {
        if (Input.isKeyPressed(KeyEvent.VK_N)) {
            scaleTime = 0.3f;
        } else if (Input.isKeyReleased(KeyEvent.VK_N)) {
            scaleTime = 1f;
        }
        if (Input.isKeyTyped(KeyEvent.VK_X)) {
            backgroundColor = new Color(0, 0, 0);
        }
        if (Input.isKeyTyped(KeyEvent.VK_C)) {
            backgroundColor = new Color(100, 120, 100);
        }
        player.handleInput();
        world.handleInput();
    }

    private void gameUpdate(float deltaTime) {

        //********** Do updates HERE **********
        deltaTime *= scaleTime; //Objects that are slow mo after this line

        camera.gameUpdate(deltaTime);
        world.gameUpdate(deltaTime);
        player.gameUpdate(deltaTime);
        clouds.gameUpdate(deltaTime);

        updateRandomThings();
//        //Check for collisions
//        handleCollisions(deltaTime);
    }

    private void updateRandomThings() {
//        manWalk.update();
//        ani1.update();
//        ani2.update();
//        ani3.update();
//        ani4.update();
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
        AffineTransform old = g.getTransform();

        //Camera view
        g.translate(-camera.camPos.x, -camera.camPos.y);
//        drawRandomThings();
        clouds.gameRender(g);
        world.gameRender(g);
        player.gameRender(g);
        g.translate(+camera.camPos.x, +camera.camPos.y);

        //Draw text information
        drawHelp();

        g.setTransform(old);
    }

    private void drawRandomThings() {
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
    }

    private void drawHelp() {
        g.setColor(Color.RED);
        g.drawString("FPS:" + averageFPS, 5, 25);
        player.drawInfo(g);
        if (player.grounded) {
            g.setColor(Color.YELLOW);
            g.drawOval(1000, 100, 25, 25);
        }
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
//            player.keyPressed(e);
            
//            world.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            //ship.keyReleased(e);
//            player.keyReleased(e);
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
