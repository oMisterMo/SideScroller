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
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import static sidescroller.GamePanel.GAME_HEIGHT;
import static sidescroller.GamePanel.GAME_WIDTH;

/**
 * Newly created world class to store the games simulation.
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class World extends GameObject {

    private float scaleTime = 1f;
    private Color backgroundColor;

    private SpatialHashGrid grid;
    private int numOfCollision = 0;
    private Camera camera;
    private Level level;
    private Player player;
    private Clouds clouds;
    private List<Thwomp> thwomps;
    private Mole mole;

    private Point p = new Point();      //Stores the location of a tile

    //Debuggin
    private int[] coord = new int[2];   //prob do not need
    private int[] coord2 = new int[2];  //prob do not need
    private boolean drawTemp = false;
    private float counter = 0;
    private Tile[] drawTiles = new Tile[6];

    /**
     * Initialises the spatial hash grid, game objects, background, level and
     * camera.
     */
    public World() {
        init();
        System.out.println("World loaded...");
    }

    private void init() {
//        world = new World();
        level = new Level();
        player = new Player(150,
                GAME_HEIGHT - Tile.TILE_HEIGHT * 6);
        thwomps = new ArrayList<>();
        int offset = 120;
        for (int i = 0; i < 20; i++) {
            thwomps.add(
                    new Thwomp(230 + (i * Thwomp.THOWMP_WIDTH),
                            (i % 2 == 0) ? offset : 0, player)
            );
        }
        mole = new Mole(1000, GAME_HEIGHT - Tile.TILE_HEIGHT * 2 - 100);
        clouds = new Clouds();
        camera = new Camera(player);

        initGrid();
        //-----------------------------END my random test
        backgroundColor = new Color(0, 0, 0);    //Represents colour of background
    }

    private void initGrid() {
        grid = new SpatialHashGrid(GAME_WIDTH * 2, GAME_HEIGHT, 300);    //Cell size (300 pixels)
        //Add all static tiles
        int numStaticObjects = 0;   //debugging only
        for (int y = 0; y < Level.NO_OF_TILES_Y; y++) {
            for (int x = 0; x < Level.NO_OF_TILES_X; x++) {
                Tile t = level.getTile(x, y);
                if (t.solid) {
                    grid.insertStaticObject(t);
                    numStaticObjects++;
                }
            }
        }

        //debuggin only
        System.out.println("No of static objects added: " + numStaticObjects);
        grid.printInfo();
        //Add dynamic object (player)
//        grid.insertDynamicObject(player);
    }

    /**
     * Handles input from N, X, C, R and I keys.
     *
     * Slow motion method should really be in player.
     */
    public void handleInput() {
        //Extra inputs
        if (Input.isKeyPressed(KeyEvent.VK_N)) {
            scaleTime = 0.3f;
        } else if (Input.isKeyReleased(KeyEvent.VK_N)) {
            scaleTime = 1f;
        }
        if (Input.isKeyTyped(KeyEvent.VK_X)) {
            setBackgroundColor(0, 0, 0, 255);
        }
        if (Input.isKeyTyped(KeyEvent.VK_C)) {
            setBackgroundColor(100, 120, 100, 255);
        }
        if (Input.isKeyTyped(KeyEvent.VK_R)) {
            reset();
        }
        if (Input.isKeyTyped(KeyEvent.VK_I)) {
            setDebugTiles();
        }

        //Handle sub class input
//        player.handleInput();
        player.handleInput2();
        level.handleInput();
    }

    private void reset() {
        System.out.println("RESET");
        player.position.x = 150;
        player.position.y = GAME_HEIGHT - Tile.TILE_HEIGHT * 6;
        player.bounds.x = player.position.x;
        player.bounds.y = player.position.y;
        player.velocity.x = 0;
        player.velocity.y = 0;
        player.grounded = false;
        player.playerState = Player.STATE_FALLING;
        System.out.println("fall (from reset)");

//        mole.position.x = 1000;
//        mole.position.y = 400;
//        mole.bounds.y = 400;
//        mole.velocity.x = 100;
//        mole.velocity.y = 0;
//        mole.moleState = Mole.STATE_FALL;
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
        moleCollision();
    }

    private void playerCollision() {
        xCol(player);
        yCol(player);
        checkPlayerGrounded(player);
    }

    private void thwompCollision() {
        int len = thwomps.size();
        for (int i = 0; i < len; i++) {
            Thwomp thwomp = thwomps.get(i);
            if (thwomp.bounds.intersects(player.bounds)) {
//                System.out.println("HIT");
                player.playerState = Player.STATE_DEAD;
                return;
            }
        }
    }

    private void moleCollision() {
//        xCol(mole);
//        yCol(mole);
        //Mole world collisions
        int amt = 1;
        if (tileAdjIsBlocking(mole, "bot", amt) && mole.velocity.y > 0) {
            //Handle bottom collision here
            System.out.println("mole hit floor");
            mole.velocity.y = 0;
            mole.grounded = true;
            mole.moleState = Mole.STATE_WALK;
            //Push object out of tile
            while (tileAdjIsBlocking(mole, "bot", amt)) {
//                System.out.println("to touch");
                mole.position.y -= 1;
                mole.bounds.y -= 1;
            }
        }

        //Mole player collision
        if (player.bounds.intersects(mole.bounds)) {
            //We hit the mole
            if (player.velocity.y > 0 && player.position.y + Player.PLAYER_HEIGHT < mole.position.y + 10) {
                //If player is falling && player.y < mole.y + (jump buffer)
                player.jump();
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
        Player plyr = (Player) obj;
        int amt = 1;
        if (tileAdjIsBlocking(obj, "bot", amt) && obj.velocity.y > 0) {
            //Handle bottom collision here
            obj.velocity.y = 0;
            obj.grounded = true;
            plyr.playerState = Player.STATE_IDLE;
            System.out.println("idle (after hitting floor)");

            //Push object out of tile
            while (tileAdjIsBlocking(obj, "bot", amt)) {
//                System.out.println("to touch");
                obj.position.y -= 1;
                obj.bounds.y -= 1;
            }
        } else if (tileAdjIsBlocking(obj, "top", amt) && obj.velocity.y < 0) {
            obj.velocity.y = 0;
            while (tileAdjIsBlocking(obj, "top", amt)) {
//                System.out.println("to touch");
                obj.position.y += 1;
                obj.bounds.y += 1;
            }
        }
    }

    //First function to get called during collision detection
    private boolean tileAdjIsBlocking(DynamicGameObject obj, String dir, int amount) {
        //Use StringBuilder to concatinate strings
        switch (dir) {
            case "left":
            case "right":
                //Handle left/Right collision
                return ((isSolid(tileCoordsAdj(obj, dir + "Up", amount, p)))
                        || (isSolid(tileCoordsAdj(obj, dir + "Down", amount, p))));
            case "bot":
            case "top":
                //Handle up/down collision
                return (isSolid(tileCoordsAdj(obj, dir + "Left", amount, p))
                        || isSolid(tileCoordsAdj(obj, dir + "Right", amount, p)));
        }
        return false;
    }

    private boolean isSolid(int x, int y) {
        return level.getTile(x, y).solid;
    }

    private boolean isSolid(Point tile) {
        return level.getTile(tile).solid;
    }

    private int[] tileCoordsAdj(DynamicGameObject obj, String dir,
            int amount, int[] adj) {
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
                x = obj.bounds.x - amount;
                y = obj.bounds.y + obj.bounds.height / 2;
                break;
            case "right":
                x = obj.bounds.x + obj.bounds.width + amount;
                y = obj.bounds.y + obj.bounds.height / 2;
                break;
            case "botLeft":
//                System.out.println("botLeft");
                x = obj.bounds.x;
                y = (obj.bounds.y + obj.bounds.height) + amount;
                break;
            case "botRight":
//                System.out.println("botRight");
                x = (obj.bounds.x + obj.bounds.width);
                y = (obj.bounds.y + obj.bounds.height) + amount;
                break;
            case "topLeft":
                x = obj.bounds.x;
                y = (obj.bounds.y) + amount;
                break;
            case "topRight":
                x = (obj.bounds.x + obj.bounds.width);
                y = (obj.bounds.y) + amount;
                break;
        }
        adj[0] = pointToTileCoordsX(x);
        adj[1] = pointToTileCoordsY(y);
//        System.out.println("x = " + coord[0]);
//        System.out.println("y = " + coord[1]);
        return adj;
    }

    private Point tileCoordsAdj(DynamicGameObject obj, String dir, int amount, Point p) {
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
                x = obj.bounds.x;
                y = obj.bounds.y + amount + 5;
                break;
            case "leftDown":
//                System.out.println("left");
                x = obj.bounds.x;
                y = (obj.bounds.y + obj.bounds.height) - amount - 5;
                break;
            case "rightUp":
                x = (obj.bounds.x + obj.bounds.width);
                y = obj.bounds.y + amount + 5;
                break;
            case "rightDown":
                x = (obj.bounds.x + obj.bounds.width);
                y = (obj.bounds.y + obj.bounds.height) - amount - 5;
                break;
            case "botLeft":
//                System.out.println("botLeft");
                x = obj.bounds.x;
                y = (obj.bounds.y + obj.bounds.height) + amount;
                break;
            case "botRight":
//                System.out.println("botRight");
                x = (obj.bounds.x + obj.bounds.width);
                y = (obj.bounds.y + obj.bounds.height) + amount;
                break;
            case "topLeft":
                x = obj.bounds.x;
                y = (obj.bounds.y) + amount;
                break;
            case "topRight":
                x = (obj.bounds.x + obj.bounds.width);
                y = (obj.bounds.y) + amount;
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

    private void checkPlayerGrounded(DynamicGameObject obj) {
        //Change bottomHitbox to oPlayer.bounds
        int offset = 2;//How far from the bottom points from the player
        Player oPlayer = (Player) obj;
//        System.out.println("checkPlayerGrounded()");
//        System.out.println("bottom hitbox: "+oPlayer.bounds.x +", "+oPlayer.bounds.y);
//        System.out.println("bounds: "+oPlayer.bounds.x +", "+oPlayer.bounds.y);
        Tile botLeft = worldToTile(oPlayer.bounds.x + offset,
                oPlayer.bounds.y + Player.PLAYER_HEIGHT + offset);
        Tile botRight = worldToTile(oPlayer.bounds.x
                + oPlayer.bounds.width - offset,
                oPlayer.bounds.y + Player.PLAYER_HEIGHT + offset);
        if (!botLeft.solid && !botRight.solid) {
//            System.out.println("grounded = false");
            oPlayer.playerState = Player.STATE_FALLING;
            oPlayer.grounded = false;
//            if (oPlayer.playerState != Player.STATE_JUMP 
//                    || oPlayer.playerState != Player.STATE_FALLING) {
//                oPlayer.playerState = Player.STATE_FALLING;
//                System.out.println("fall [from no tiles beneath]");
//            }
        }
    }

    private void checkGameover() {
        if (player.position.y > GamePanel.GAME_HEIGHT + 100) {
            player.playerState = Player.STATE_DEAD;
        }
        if (player.playerState == Player.STATE_DEAD) {
//            state = WORLD_STATE_GAMEOVER;
            reset();
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
//                player.doInnerColCheck(collider.bounds);
            }
        }
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
        int cell = (int) grid.getCellSize();
        //draw vertical
        for (int i = cell; i < GAME_WIDTH * 2; i += cell) {
            g.drawLine(i, 0, i, GAME_HEIGHT);
        }
        for (int i = cell; i < GAME_HEIGHT; i += cell) {
            g.drawLine(0, i, GAME_WIDTH * 2, i);
        }
    }

    private void setBackgroundColor(int r, int g, int b, int a) {
        backgroundColor = new Color(r, g, b, a);
    }

    /**
     * Only for debugging, gets a reference to the player.
     *
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    @Override
    void gameUpdate(float deltaTime) {
        //********** Do updates HERE **********
        deltaTime *= scaleTime; //Objects that are slow mo after this line
        updateCounter(deltaTime);

        camera.gameUpdate(deltaTime);
        level.gameUpdate(deltaTime);
        clouds.gameUpdate(deltaTime);
        player.gameUpdate(deltaTime);
        for (Thwomp t : thwomps) {
            t.gameUpdate(deltaTime);
        }
        mole.gameUpdate(deltaTime);

//        //Check for collisions
//        handleCollisionHashgrid();
        handleCollisions3(deltaTime);
        checkGameover();
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
        clouds.gameRender(g);
        level.gameRender(g);
        player.gameRender(g);
        for (Thwomp t : thwomps) {
            t.gameRender(g);
        }
        mole.gameRender(g);
        
        
        //Draw Debuggin info above other objects
//        drawHashGrid(g);
        drawTilesBounds(g);
        g.translate(+camera.camPos.x, +camera.camPos.y);

        g.setTransform(old);
    }
}
