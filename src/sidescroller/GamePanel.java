package sidescroller;

import common.Helper;
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
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

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
    private float scaleTime = 1;

    //GAME VARIABLES HERE-------------------------------------------------------
    private Color backgroundColor;    //Represents colour of background

    private SpatialHashGrid grid;
    private int numOfCollision = 0;
//    private Assets assets;    //Not needed here
    private Camera camera;
    private World world;
    private Player player;
    private Clouds clouds;
    private List<Thwomp> thwomps;

    //Extras random things
    private SpriteSheet ss;
    private BufferedImage spritesheet;
    private Animation manWalk;
    private SpriteSheet ss2;
    private Animation ani1;
    private Animation ani2;
    private Animation ani3;
    private Animation ani4;

    //Debuggin
    private Point p = new Point();
    private int[] coord = new int[2];   //prob do not need
    private int[] coord2 = new int[2];  //prob do not need
    private boolean drawTemp = false;
    private float counter = 0;
    private Tile[] drawTiles = new Tile[6];

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
        player = new Player(150,
                GamePanel.GAME_HEIGHT - Tile.TILE_HEIGHT * 2 - 80,
                Player.PLAYER_WIDTH,
                Player.PLAYER_HEIGHT, world);
        thwomps = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            thwomps.add(new Thwomp(180 + (i * Thwomp.THOWMP_WIDTH), 0, Thwomp.THOWMP_WIDTH, Thwomp.THOWMP_HEIGHT, player));
        }
        clouds = new Clouds();
        camera = new Camera(player, new Vector2D());

//        initManWalk();
//        initMarioSprites();
        initGrid();
        //-----------------------------END my random test
        backgroundColor = new Color(0, 0, 0);    //Represents colour of background
//        backgroundColor = new Color(135,206,235);    //Represents colour of background

        //Load listeners
        input = new Input();
        addKeyListener(input);
//        addKeyListener(new TAdapter());
        addMouseListener(new MAdapter());
    }

    private void initGrid() {
        grid = new SpatialHashGrid(GAME_WIDTH * 2, GAME_HEIGHT, 300);    //Cell size (200 pixels)
        //Add all static tiles
        int numStaticObjects = 0;
        for (int y = 0; y < World.NO_OF_TILES_Y; y++) {
            for (int x = 0; x < World.NO_OF_TILES_X; x++) {
                Tile t = world.getTile(x, y);
                if (t.solid) {
                    grid.insertStaticObject(t);
                    numStaticObjects++;
                }
            }
        }
        System.out.println("No of static objects added: " + numStaticObjects);
        System.out.println("No of static cells: " + grid.staticCells.length);
        int n = 10;
        System.out.println("Cell " + n + " contains: " + grid.staticCells[n].size() + " objects");
        //Add dynamic object (player)
//        grid.insertDynamicObject(player);
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
        if (Input.isKeyTyped(KeyEvent.VK_R)) {
            reset();
        }
        if (Input.isKeyTyped(KeyEvent.VK_I)) {
            //Debugging (GET ALL THE TILES ALL TEMPORATILY SET A RED BORDER AROUnd THEM)
            System.out.println("******Debugging******");
            System.out.println("Getting tile left to player...");
//            coord = tileCoordsAdj(player, "left");
//            coord = tileCoordsAdj(player, "right", (int) Player.PLAYER_WIDTH);
            int amt = 2;
            coord = tileCoordsAdj(player, "left", amt, coord);
            Tile leftTile = world.getTile(coord[0], coord[1]);
            coord = tileCoordsAdj(player, "right", amt, coord);
            Tile rightTile = world.getTile(coord[0], coord[1]);
            coord = tileCoordsAdj(player, "botLeft", amt, coord);
            Tile botLeft = world.getTile(coord[0], coord[1]);
            coord = tileCoordsAdj(player, "botRight", amt, coord);
            Tile botRight = world.getTile(coord[0], coord[1]);
            coord = tileCoordsAdj(player, "topLeft", amt, coord);
            Tile topLeft = world.getTile(coord[0], coord[1]);
            coord = tileCoordsAdj(player, "topRight", amt, coord);
            Tile topRight = world.getTile(coord[0], coord[1]);
            drawTiles[0] = leftTile;
            drawTiles[1] = rightTile;
            drawTiles[2] = botLeft;
            drawTiles[3] = botRight;
            drawTiles[4] = topLeft;
            drawTiles[5] = topRight;
//            drawTiles[0] = world.getTile(coord[0], coord[1]);
            counter = 0;
            drawTemp = true;
        }
        player.handleInput();
        world.handleInput();
    }

    private void reset() {
        System.out.println("RESET");
        player.position.x = 150;
        player.position.y = GamePanel.GAME_HEIGHT - Tile.TILE_HEIGHT * 2 - 80;
        player.bounds.x = 150;
        player.bounds.y = GamePanel.GAME_HEIGHT - Tile.TILE_HEIGHT * 2 - 80;
        player.velocity.x = 0;
        player.velocity.y = 0;
        player.grounded = false;
    }

    private void gameUpdate(float deltaTime) {
        //********** Do updates HERE **********
        deltaTime *= scaleTime; //Objects that are slow mo after this line
        updateCounter(deltaTime);

        camera.gameUpdate(deltaTime);
        world.gameUpdate(deltaTime);
        clouds.gameUpdate(deltaTime);
        player.gameUpdate(deltaTime);
        for (Thwomp t : thwomps) {
            t.gameUpdate(deltaTime);
        }
//        updateRandomThings();

//        //Check for collisions
//        handleCollisions(deltaTime);
//        handleCollisionHashgrid();
        handleCollisions3(deltaTime);
        checkGameover();
    }

    private void handleCollisionHashgrid() {
        List<StaticGameObject> colliders = grid.getPotentialColliders(player);
        int len = colliders.size();
//        System.out.println("potential coll: "+len);
        for (int i = 0; i < len; i++) {
            StaticGameObject collider = colliders.get(i);
//            numOfCollision++;
//            System.out.println(numOfCollision);

            //If player bounds intersects any other static object within cell
            if (player.bounds.intersects(collider.bounds)) {
//                System.out.println("outer");
                System.out.println("Player bounds");
//                worldToTile(player.bottomHitbox.x, player.bottomHitbox.y);
                worldToTile(player.bounds.x, player.bounds.y);
                player.doInnerColCheck(collider.bounds);
            }
        }
    }

    private void handleCollisions3(float deltaTime) {
        playerCollision();
        thwompCollision();
    }

    private void checkGameover() {
        if (player.playerState == Player.STATE_DEAD) {
            state = WORLD_STATE_GAMEOVER;
        }
    }

    private void playerCollision() {
        xCol(player);
        yCol(player);
        checkGrounded(player);
    }

    private void thwompCollision() {
        //        if (isSolid(pointToTileCoordsX(thwomp.bounds.x + Thwomp.THOWMP_WIDTH / 2), pointToTileCoordsY(thwomp.bounds.y + Thwomp.THOWMP_HEIGHT + 1))) {
//            thwomp.velocity.y = 0;
//        }
        int len = thwomps.size();
        for (int i = 0; i < len; i++) {
            Thwomp thwomp = thwomps.get(i);
            if (thwomp.bounds.intersects(player.bounds)) {
//                System.out.println("HIT");
                player.playerState = Player.STATE_DEAD;
            }
        }
    }

    private void updateCounter(float deltaTime) {
        if (drawTemp) {
            counter += deltaTime;
        }
        if (counter >= 3) {
            drawTemp = false;
        }
    }

    private void xCol(DynamicGameObject obj) {
        int amt = 1;    //How far the tile is from the player
        //Check left tile
        if (tileAdjIsBlocking(obj, "left", amt) && obj.velocity.x < 0) {
            obj.velocity.x = 0;
            while (tileAdjIsBlocking(obj, "left", amt)
                    && !tileAdjIsBlocking(obj, "right", amt)) {
                obj.position.x += 1;
                obj.bounds.x += 1;
            }
            //Check right tile
        } else if (tileAdjIsBlocking(obj, "right", amt) && obj.velocity.x > 0) {
            obj.velocity.x = 0;
            while (tileAdjIsBlocking(obj, "right", amt)
                    && !tileAdjIsBlocking(obj, "left", amt)) {
                obj.position.x -= 1;
                obj.bounds.x -= 1;
            }
        }
    }

    private void yCol(DynamicGameObject obj) {
//        Tile current = worldToTile(player.bounds.x, player.bounds.y);
        Player oPlayer = (Player) obj;
        int amt = 1;
        if (tileAdjIsBlocking(obj, "bot", amt) && obj.velocity.y > 0) {
            oPlayer.velocity.y = 0;
            oPlayer.grounded = true;
            while (tileAdjIsBlocking(obj, "bot", amt)) {
//                System.out.println("to touch");
                obj.position.y -= 1;
                obj.bounds.y -= 1;
            }
        } else if (tileAdjIsBlocking(obj, "top", amt) && obj.velocity.y < 0) {
            oPlayer.velocity.y = 0;
            while (tileAdjIsBlocking(obj, "top", amt)) {
//                System.out.println("to touch");
                obj.position.y += 1;
                oPlayer.bounds.y += 1;
            }
        }
    }

    //First function to get called during collision detection
    private boolean tileAdjIsBlocking(DynamicGameObject player, String dir, int amount) {
        //Use StringBuilder to concatinate strings
        switch (dir) {
            case "left":
            case "right":
                //Handle left/Right collision
//                tileCoordsAdj(player, dir, amount, coord);
//                return (isSolid(coord[0], coord[1]));
                //Replace with one line below
                return ((isSolid(tileCoordsAdj(player, dir + "Up", amount, p)))
                        || (isSolid(tileCoordsAdj(player, dir + "Down", amount, p))));
            case "bot":
            case "top":
                //Handle up/down collision
//                tileCoordsAdj(player, dir + "Left", amount, coord);
//                tileCoordsAdj(player, dir + "Right", amount, coord2);
//                return (isSolid(coord[0], coord[1]) || isSolid(coord2[0], coord2[1]));
                return (isSolid(tileCoordsAdj(player, dir + "Left", amount, p))
                        || isSolid(tileCoordsAdj(player, dir + "Right", amount, p)));
        }
        return false;
    }

    private boolean isSolid(int x, int y) {
        return world.getTile(x, y).solid;
    }

    private boolean isSolid(Point tile) {
        return world.getTile(tile).solid;
    }

//    public int[] tileCoordsAdj(DynamicGameObject player, String dir) {
//        return tileCoordsAdj(player, dir, 1);
//    }
    public int[] tileCoordsAdj(DynamicGameObject player, String dir, int amount, int[] adj) {
        //Long ass function
        //Given a player and a positin (String) -> returns the tileCoordsForPoint
        float x = 0;    //reset
        float y = 0;
        adj[0] = 0;
        adj[1] = 0;
//        p.setLocation(0, 0);
        //If amount == empty, then 1 is assigned
        //amount = amount || 1; javascript code
        switch (dir) {
            case "left":
//                System.out.println("left");
                x = player.bounds.x - amount;
                y = player.bounds.y + player.bounds.height / 2;
                break;
            case "right":
                x = player.bounds.x + player.bounds.width + amount;
                y = player.bounds.y + player.bounds.height / 2;
                break;
            case "botLeft":
//                System.out.println("botLeft");
                x = player.bounds.x;
                y = (player.bounds.y + player.bounds.height) + amount;
                break;
            case "botRight":
//                System.out.println("botRight");
                x = (player.bounds.x + player.bounds.width);
                y = (player.bounds.y + player.bounds.height) + amount;
                break;
            case "topLeft":
                x = player.bounds.x;
                y = (player.bounds.y) + amount;
                break;
            case "topRight":
                x = (player.bounds.x + player.bounds.width);
                y = (player.bounds.y) + amount;
                break;
        }
        adj[0] = pointToTileCoordsX(x);
        adj[1] = pointToTileCoordsY(y);
//        System.out.println("x = " + coord[0]);
//        System.out.println("y = " + coord[1]);
        return adj;
    }

    public Point tileCoordsAdj(DynamicGameObject player, String dir, int amount, Point p) {
        //Long ass function
        //Given a player and a positin (String) -> returns the tileCoordsForPoint
        float x = 0;    //reset
        float y = 0;
        p.x = 0;
        p.y = 0;
//        p.setLocation(0, 0);
        //If amount == empty, then 1 is assigned
        //amount = amount || 1; javascript code
        switch (dir) {
            case "leftUp":
//                System.out.println("left");
                x = player.bounds.x;
                y = player.bounds.y + amount + 5;
                break;
            case "leftDown":
//                System.out.println("left");
                x = player.bounds.x;
                y = (player.bounds.y + player.bounds.height) - amount - 5;
                break;
            case "rightUp":
                x = (player.bounds.x + player.bounds.width);
                y = player.bounds.y + amount + 5;
                break;
            case "rightDown":
                x = (player.bounds.x + player.bounds.width);
                y = (player.bounds.y + player.bounds.height) - amount - 5;
                break;
            case "botLeft":
//                System.out.println("botLeft");
                x = player.bounds.x;
                y = (player.bounds.y + player.bounds.height) + amount;
                break;
            case "botRight":
//                System.out.println("botRight");
                x = (player.bounds.x + player.bounds.width);
                y = (player.bounds.y + player.bounds.height) + amount;
                break;
            case "topLeft":
                x = player.bounds.x;
                y = (player.bounds.y) + amount;
                break;
            case "topRight":
                x = (player.bounds.x + player.bounds.width);
                y = (player.bounds.y) + amount;
                break;
        }
        p.x = pointToTileCoordsX(x);
        p.y = pointToTileCoordsY(y);
//        System.out.println("x = " + coord[0]);
//        System.out.println("y = " + coord[1]);
        return p;
    }

//    private Tile tileAdj(DynamicGameObject player, String s, int amount) {
//        world.getTile(tileCoordsAdj(player, "1", 1), tileCoordsAdj(player, "1", 1));
//        return null;
//    }
    private Tile worldToTile(float x, float y) {
        //Given any point -> returns a tile at the position
        return world.getTile(pointToTileCoordsX(x), pointToTileCoordsY(y));
    }

    private int pointToTileCoordsX(float x) {
        //Given an x coordinate -> returns the tile position of the array
//        System.out.println((int) Math.floor(x / Tile.TILE_WIDTH));
        return (int) Math.floor(x / Tile.TILE_WIDTH);
    }

    private int pointToTileCoordsY(float y) {
//        System.out.println((int) Math.floor(y / Tile.TILE_WIDTH));
        return (int) Math.floor(y / Tile.TILE_HEIGHT);
    }

    private void checkGrounded(DynamicGameObject obj) {
        //Change bottomHitbox to oPlayer.bounds
        int amt = -4;
        Player oPlayer = (Player) obj;
        Tile botLeft = worldToTile(oPlayer.bottomHitbox.x + amt, oPlayer.bottomHitbox.y + Player.PLAYER_HEIGHT);
        Tile botRight = worldToTile(oPlayer.bottomHitbox.x + oPlayer.bottomHitbox.width - amt, oPlayer.bottomHitbox.y + Player.PLAYER_HEIGHT);
        if (!botLeft.solid && !botRight.solid) {
//            System.out.println("grounded = false");
            oPlayer.grounded = false;
        }
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
//        g.scale(1.5, 1.5);    //also add 300 to cam.pos.y

        //Camera view
        g.translate(-camera.camPos.x, -camera.camPos.y);
//        drawRandomThings();
        clouds.gameRender(g);
        world.gameRender(g);
        player.gameRender(g);
        for (Thwomp t : thwomps) {
            t.gameRender(g);
        }
        //Draw Debuggin info above other objects
//        drawHashGrid(g);
        drawTilesBounds();
        g.translate(+camera.camPos.x, +camera.camPos.y);
        //Draw text information
//        drawHelp();

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
        //Draw FPS in red
        g.setColor(Color.RED);
        g.drawString("FPS:" + averageFPS, 5, 25);
        //Draw players info
        player.drawInfo(g);
        //If player is on the ground, draw oval
        if (player.grounded) {
            g.setColor(Color.YELLOW);
            g.drawOval(1000, 100, 25, 25);
        }
    }

    private void drawTilesBounds() {
        if (drawTemp) {
//            System.out.println("drawing");
            g.setColor(Color.YELLOW);
            Tile t = drawTiles[0];
            g.draw(t.bounds);
            g.setColor(Color.GREEN);
            t = drawTiles[1];
            g.draw(t.bounds);
            g.setColor(Color.WHITE);
            t = drawTiles[2];
            g.draw(t.bounds);
            t = drawTiles[3];
            g.draw(t.bounds);
            g.setColor(Color.RED);
            t = drawTiles[4];
            g.draw(t.bounds);
            t = drawTiles[5];
            g.draw(t.bounds);
        }
    }

    private void drawHashGrid(Graphics2D g) {
        g.setColor(Color.YELLOW);
        int cell = (int) grid.cellSize;
        //draw vertical
        for (int i = cell; i < GAME_WIDTH * 2; i += cell) {
            g.drawLine(i, 0, i, GAME_HEIGHT);
        }
        for (int i = cell; i < GAME_HEIGHT; i += cell) {
            g.drawLine(0, i, GAME_WIDTH * 2, i);
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
