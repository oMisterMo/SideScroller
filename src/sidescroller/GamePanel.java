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
 * Game screen which ties together all classes.
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class GamePanel extends JPanel implements Runnable {

    public static final int GAME_WIDTH = 1280;
    public static final int GAME_HEIGHT = 576;
    //Game States
    public static final int WORLD_STATE_RUNNING = 0;
    public static final int WORLD_STATE_GAMEOVER = 1;
    public int state = WORLD_STATE_RUNNING;

    private boolean running = false;
    private Thread thread;
    private BufferedImage image;
    private Graphics2D g;

    private Input input;

    private final int FPS = 60;
    private long averageFPS;

    private final World world;

    /**
     * Default constructor, creates a new World
     */
    public GamePanel() {
        super();
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setFocusable(true);
        requestFocus(); //-> platform dependant

        initInput();
        world = new World();
    }

    private void initInput() {
        input = new Input();
        addKeyListener(input);
//        addKeyListener(new TAdapter());
        addMouseListener(new GamePanel.MAdapter());
    }

    //METHODS
    /**
     * Is called after the JPanel has been added to the JFrame component.
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
        long startTime = System.nanoTime();
        float deltaTime;
        long timeTaken;
        long frameCount = 0;
        long totalTime = 0;
        long waitTime;
        long targetTime = 1000 / FPS;

        running = true;
        image = new BufferedImage(GAME_WIDTH, GAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();

        //GAME LOOP
        while (running) {
            //Calculate time since last frame
            deltaTime = (System.nanoTime() - startTime) / 1_000_000_000.0f; //ns -> sec
            startTime = System.nanoTime();

            //Handle input, update and render
            handleInput();
            gameUpdate(deltaTime);
            Input.updateLastKey();
            gameRender(g);
            gameDraw();

            //How long it took to run
            timeTaken = (System.nanoTime() - startTime) / 1_000_000;    //ns -> milli
            //16ms - timeTaken
            waitTime = targetTime - timeTaken;
            try {
                //System.out.println("Sleeping for: " + waitTime);
                thread.sleep(waitTime);
            } catch (Exception e) {

            }
            totalTime += System.nanoTime() - startTime;
            frameCount++;

            /*Debug*/
            //Calculate average fps
            if (frameCount >= FPS) {
                averageFPS = 1000 / ((totalTime / frameCount) / 1000_000);
                frameCount = 0;
                totalTime = 0;
                //System.out.println("Average fps: " + averageFPS);
            }
            //Print negative wait time
            if (waitTime < 0) {
                //I get a negative value at the beg
                System.out.println("NEGATIVE: " + waitTime);
                System.out.println("targetTime = " + targetTime);
                System.out.println("timeTaken = " + timeTaken + "\n");
            }
        }
    }

    private void handleInput() {
        switch (state) {
            case WORLD_STATE_RUNNING:
                world.handleInput();
                break;
            case WORLD_STATE_GAMEOVER:

                break;
        }
    }

    /* ********************* UPDATE & RENDER ************************* */
    private void gameUpdate(float deltaTime) {
        switch (state) {
            case WORLD_STATE_RUNNING:
                world.gameUpdate(deltaTime);
                break;
            case WORLD_STATE_GAMEOVER:

                break;
        }
    }

    private void gameRender(Graphics2D g) {
        switch (state) {
            case WORLD_STATE_RUNNING:
                world.gameRender(g);
                drawHelp();
                break;
            case WORLD_STATE_GAMEOVER:
                g.setColor(Color.WHITE);
                g.drawString("GAMEOVER", GAME_WIDTH / 2 - 50, GAME_HEIGHT / 2 - 20);
                break;
        }
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

    /* *************** EVENT HANDERLERS *********************** */
    //Handle Input ** Inner Class
    private class TAdapter extends KeyAdapter {

        //When a key is pressed
        @Override
        public void keyPressed(KeyEvent e) {
            //Handle player from world movement
        }

        @Override
        public void keyReleased(KeyEvent e) {
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
            if (e.getButton() == MouseEvent.BUTTON3) {
                System.out.println("RIGT CLICKED");
                int x = e.getX();
                int y = e.getY();
                Player player = world.getPlayer();
                player.position.set(x, y);
                player.bounds.x = x;
                player.bounds.y = y;
            }
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
