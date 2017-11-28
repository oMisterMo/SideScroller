package sidescroller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

/**
 * When you remove you have to set the variable to null -> AVOID MEMORY LEAKS
 *
 * 06-Sep-2016, 23:03:23.
 *
 * @author Mo
 */
public class GamePanel extends JPanel implements Runnable {

    //GAMES WIDTH & HEIGHT
    public static final int GAME_WIDTH = 1280;
    public static final int GAME_HEIGHT = 576;

    public static final int WORLD_STATE_RUNNING = 0;
    public static final int WORLD_STATE_GAMEOVER = 1;
    public int state = WORLD_STATE_RUNNING;

    private Thread thread;
    private boolean running;
    private BufferedImage image;
    private Graphics2D g;

    private Input input;

    private final int FPS = 60;
    private long averageFPS;
    
    //Game varialbes
    private final World world;

    //CONSTRUCTOR
    public GamePanel() {
        super();
        setPreferredSize(new Dimension(GAME_WIDTH - 10, GAME_HEIGHT - 10));
        setFocusable(true);
        //System.out.println(requestFocusInWindow());
        requestFocus(); //-> platform dependant

        initInput();
        world = new World();
    }

    private void initInput() {
        //Load listeners
        input = new Input();
        addKeyListener(input);
//        addKeyListener(new TAdapter());
        addMouseListener(new GamePanel.MAdapter());
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
        int counter = 0;    //can delete, counts negative waitTime

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
                System.out.println(counter++ + ": NEGATIVE: " + waitTime);
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
        world.handleInput();
    }

    /* ********************* UPDATE & RENDER ************************* */
    private void gameUpdate(float deltaTime) {
        //********** Do updates HERE **********
        world.gameUpdate(deltaTime);
    }

    private void gameRender(Graphics2D g) {
        world.gameRender(g);
        drawHelp();
    }

    private void drawHelp() {
        //Draw FPS in red
        g.setColor(Color.RED);
        g.drawString("FPS:" + averageFPS, 5, 25);
        //Draw players info
        Player p = world.getPlayer();
        p.drawInfo(g);
        //If player is on the ground, draw oval
        if (p.grounded) {
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
}
