package sidescroller;

import common.Vector2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import static sidescroller.GamePanel.GAME_HEIGHT;
import static sidescroller.GamePanel.GAME_WIDTH;
import static sidescroller.GamePanel.WORLD_STATE_GAMEOVER;

/**
 * Newly created world class to store the games simulation
 *
 * 28-Nov-2017, 20:53:51.
 *
 * @author Mo
 */
public class World extends GameObject {

    //GAME VARIABLES HERE-------------------------------------------------------
    private float scaleTime = 1;
    private Color backgroundColor;    //Represents colour of background

    private SpatialHashGrid grid;
    private int numOfCollision = 0;
//    private Assets assets;    //Not needed here
    private Camera camera;
//    private World world;
    private Level level;
    private Player player;
    private Clouds clouds;
    private List<Thwomp> thwomps;

    //Debuggin
    private Point p = new Point();
    private int[] coord = new int[2];   //prob do not need
    private int[] coord2 = new int[2];  //prob do not need
    private boolean drawTemp = false;
    private float counter = 0;
    private Tile[] drawTiles = new Tile[6];

    public World() {
        //initialise varialbes
        init();
    }

    private void init() {
//        assets = new Assets();
        Assets.loadImages();
//        world = new World();
        level = new Level();
        player = new Player(150,
                GamePanel.GAME_HEIGHT - Tile.TILE_HEIGHT * 2 - 80,
                Player.PLAYER_WIDTH,
                Player.PLAYER_HEIGHT);
        thwomps = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            thwomps.add(new Thwomp(180 + (i * Thwomp.THOWMP_WIDTH), 0, Thwomp.THOWMP_WIDTH, Thwomp.THOWMP_HEIGHT, player));
        }
        clouds = new Clouds();
        camera = new Camera(player, new Vector2D());

        initGrid();
        //-----------------------------END my random test
        backgroundColor = new Color(0, 0, 0);    //Represents colour of background
//        backgroundColor = new Color(135,206,235);    //Represents colour of background
    }

    private void initGrid() {
        grid = new SpatialHashGrid(GAME_WIDTH * 2, GAME_HEIGHT, 300);    //Cell size (200 pixels)
        //Add all static tiles
        int numStaticObjects = 0;
        for (int y = 0; y < Level.NO_OF_TILES_Y; y++) {
            for (int x = 0; x < Level.NO_OF_TILES_X; x++) {
                Tile t = level.getTile(x, y);
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

    public void handleInput() {
        //Extra inputs
        if (Input.isKeyPressed(KeyEvent.VK_N)) {
            scaleTime = 0.3f;
        } else if (Input.isKeyReleased(KeyEvent.VK_N)) {
            scaleTime = 1f;
        }
        if (Input.isKeyTyped(KeyEvent.VK_X)) {
//            backgroundColor = new Color(0, 0, 0);
            setBackgroundColor(0, 0, 0, 255);
        }
        if (Input.isKeyTyped(KeyEvent.VK_C)) {
//            backgroundColor = new Color(100, 120, 100);
            setBackgroundColor(100, 120, 100, 255);
        }
        if (Input.isKeyTyped(KeyEvent.VK_R)) {
//            reset();
        }
        if (Input.isKeyTyped(KeyEvent.VK_I)) {
            setDebugTiles();
        }
        
        //Handle sub class input
        player.handleInput();
        level.handleInput();
    }

    private void setDebugTiles() {
        //Debugging (GET ALL THE TILES ALL TEMPORATILY SET A RED BORDER AROUnd THEM)
        System.out.println("******Debugging******");
        System.out.println("Getting tile left to player...");
//            coord = tileCoordsAdj(player, "left");
//            coord = tileCoordsAdj(player, "right", (int) Player.PLAYER_WIDTH);
        int amt = 2;
        coord = tileCoordsAdj(player, "left", amt, coord);
        Tile leftTile = level.getTile(coord[0], coord[1]);
        coord = tileCoordsAdj(player, "right", amt, coord);
        Tile rightTile = level.getTile(coord[0], coord[1]);
        coord = tileCoordsAdj(player, "botLeft", amt, coord);
        Tile botLeft = level.getTile(coord[0], coord[1]);
        coord = tileCoordsAdj(player, "botRight", amt, coord);
        Tile botRight = level.getTile(coord[0], coord[1]);
        coord = tileCoordsAdj(player, "topLeft", amt, coord);
        Tile topLeft = level.getTile(coord[0], coord[1]);
        coord = tileCoordsAdj(player, "topRight", amt, coord);
        Tile topRight = level.getTile(coord[0], coord[1]);
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

    @Override
    void gameUpdate(float deltaTime) {
        //********** Do updates HERE **********
        deltaTime *= scaleTime; //Objects that are slow mo after this line
//        updateCounter(deltaTime);

        camera.gameUpdate(deltaTime);
//        world.gameUpdate(deltaTime);
        level.gameUpdate(deltaTime);
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

    private void updateCounter(float deltaTime) {
        if (drawTemp) {
            counter += deltaTime;
        }
        if (counter >= 3) {
            drawTemp = false;
        }
    }

    private void handleCollisions3(float deltaTime) {
        playerCollision();
        thwompCollision();
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
        return level.getTile(x, y).solid;
    }

    private boolean isSolid(Point tile) {
        return level.getTile(tile).solid;
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

    private int pointToTileCoordsX(float x) {
        //Given an x coordinate -> returns the tile position of the array
//        System.out.println((int) Math.floor(x / Tile.TILE_WIDTH));
        return (int) Math.floor(x / Tile.TILE_WIDTH);
    }

    private int pointToTileCoordsY(float y) {
//        System.out.println((int) Math.floor(y / Tile.TILE_WIDTH));
        return (int) Math.floor(y / Tile.TILE_HEIGHT);
    }

    private Tile worldToTile(float x, float y) {
        //Given any point -> returns a tile at the position
        return level.getTile(pointToTileCoordsX(x), pointToTileCoordsY(y));
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

    private void checkGameover() {
        if (player.playerState == Player.STATE_DEAD) {
//            state = WORLD_STATE_GAMEOVER;
        }
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

    @Override
    void gameRender(Graphics2D g) {
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
//        world.gameRender(g);
        level.gameRender(g);
        player.gameRender(g);
        for (Thwomp t : thwomps) {
            t.gameRender(g);
        }
        //Draw Debuggin info above other objects
//        drawHashGrid(g);
        drawTilesBounds(g);
        g.translate(+camera.camPos.x, +camera.camPos.y);

        g.setTransform(old);
    }

    private void drawTilesBounds(Graphics2D g) {
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

    public void setBackgroundColor(int r, int g, int b, int a) {
        backgroundColor = new Color(r, g, b, a);
    }

    public Player getPlayer() {
        return player;
    }
}
