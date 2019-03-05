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

import common.Vector2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class Level extends GameObject {

    //Double the screen width
    public static final int NO_OF_TILES_X = (GamePanel.GAME_WIDTH / Tile.TILE_WIDTH) * 2;   //80
    public static final int NO_OF_TILES_Y = (GamePanel.GAME_HEIGHT / Tile.TILE_HEIGHT);     //18

    public static final float WORLD_GRAVITY = 1000;
    public static final Vector2D gravity = new Vector2D(0, WORLD_GRAVITY);

    public static final int NO_OF_LEVELS = 1;
    //View changer
    public static final int HITBOX_MODE = 0;
    public static final int BITMAP_MODE = 1;
    public static int renderMode = HITBOX_MODE; //initial render mode

    //Array holding all tiles
    private final Tile[][] tiles;
    //level to load
    private BufferedImage level;

    Random ran = new Random();

    //Moves the world (x, y) units
    private int xShift = 0;
    private int yShift = 0;

    public Level() {
        //Initial new world here
        tiles = new Tile[NO_OF_TILES_Y][NO_OF_TILES_X];
        System.out.println("No x tiles: " + NO_OF_TILES_X);
        System.out.println("No y tiles: " + NO_OF_TILES_Y);

//        tiles = new Tile[NO_OF_TILES_Y][NO_OF_TILES_X];
        //Load images
        //Initialise each Tile to empty
        clearBoard();   //sets to null
        initTiles();    //create empty tiles
        resetBoard();   //sets to empty (not needed here)

        loadLevel(Assets.level0);

//        setBorder();
        printNoSolidBlocks();   //can delete
        System.out.println("Level loaded...");
    }

    /**
     * Sets all spikeBlocks to null
     */
    public void clearBoard() {
        System.out.println("Setting all tiles to null...");
        for (int y = 0; y < NO_OF_TILES_Y; y++) {
            for (int x = 0; x < NO_OF_TILES_X; x++) {
                tiles[y][x] = null;
            }
        }
//        System.out.println("what is it: "+tiles[0][0].bounds);
    }

    /**
     * Called from the constructor, sets the position of all tiles
     */
    private void initTiles() {
        System.out.println("Initilising....(sepll)");
        for (int y = 0; y < NO_OF_TILES_Y; y++) {
            for (int x = 0; x < NO_OF_TILES_X; x++) {
//                tiles[i][j] = new Tile(i * Tile.TILE_WIDTH, j * Tile.TILE_HEIGHT, Tile.EMPTY);
//                tiles[i][j] = new Tile(new Vector2D(i * Tile.TILE_WIDTH, j * Tile.TILE_HEIGHT), Tile.EMPTY);
                tiles[y][x] = new Tile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT,
                        Tile.TILE_WIDTH, Tile.TILE_HEIGHT,
                        Tile.EMPTY);
            }
        }
    }

    /**
     * Sets all spikeBlocks to empty
     *
     * FIX LOGIC HERE
     */
    public void resetBoard() {
        System.out.println("Setting all tiles to empty...");
        for (int y = 0; y < NO_OF_TILES_Y; y++) {
            for (int x = 0; x < NO_OF_TILES_X; x++) {
//                tiles[i][j].setID(Tile.EMPTY);
//                tiles[i][j].setTile(i, j, Tile.EMPTY);
                tiles[y][x].setTile(Tile.EMPTY, false);
            }
        }
    }

    public void loadLevel() {
        loadLevel(Assets.testLevel);
    }

    /**
     * Loads either a random testLevel or testLevel 0
     *
     * @param l
     */
    public void loadLevel(BufferedImage l) {
//        clearPositions();
        /*
         ITEM = ( R ,  G ,  B )

         Empty = (0, 0, 0) : BLACK
         Wall = (120, 120, 120)  : GRAY
         Player = (255, 255, 0): YELLOW
         Home = (255, 0, 0)  : RED
         Box = (0, 255, 0)  : GREEN
         BoxOnHome = (0, 0, 255)  : BLUE
         */

        //System.out.println("Color Model: " + testLevel.getColorModel());
//        level = Assets.testLevel;
        level = l;
        int w = level.getWidth();
        int h = level.getHeight();
//        System.out.println("w " + w + "\nh " + h);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int pixel = level.getRGB(x, y);
                //Get color of pixel in testLevel array
                int a = ((pixel & 0xff000000) >>> 24);
                int r = ((pixel & 0x00ff0000) >>> 16);
                int g = ((pixel & 0x0000ff00) >>> 8);
                int b = ((pixel & 0x000000ff));

                //Depending on the color of a pixel, set the tile
                if (r == 0 && g == 0 && b == 0) {
                    //System.out.println("Black block at: " + x + " " + y);
                    //*************DO NOT NEED*************
                    tiles[y][x].setTile(Tile.EMPTY, false);
                    continue;   //We found a tile, do not need to check others
                }
                //If the current pixel is grey
                if (r == 255 && g == 0 && b == 0) {
                    //System.out.println("Wall block at: " + x + " " + y);
                    tiles[y][x].setTile(Tile.TL, true);
                    continue;
                }
                if (r == 0 && g == 255 && b == 0) {
                    //System.out.println("Home block at: " + x + " " + y);
                    tiles[y][x].setTile(Tile.TM, true);
                    continue;
                }
                if (r == 0 && g == 0 && b == 255) {
                    //System.out.println("Box block at: " + x + " " + y);
                    tiles[y][x].setTile(Tile.TR, true);
                    continue;
                }
                //new blocks
                if (r == 152 && g == 0 && b == 0) {
                    tiles[y][x].setTile(Tile.ML, true);
                    continue;
                }
                if (r == 2 && g == 138 && b == 2) {
                    tiles[y][x].setTile(Tile.MM, true);
                    continue;
                }
                if (r == 0 && g == 0 && b == 94) {
                    tiles[y][x].setTile(Tile.MR, true);
                    continue;
                }//end new blocks
                if (r == 120 && g == 120 && b == 120) {
                    //System.out.println("Box block at: " + x + " " + y);
                    tiles[y][x].setTile(Tile.WALL, true);
                }

//                //Extra tiles
                if (r == 0 && g == 255 && b == 255) {
                    //System.out.println("Box block at: " + x + " " + y);
                    tiles[y][x].setTile(Tile.EMPTY, false);
                }
                if (r == 200 && g == 100 && b == 0) {
                    //System.out.println("Box block at: " + x + " " + y);
                    tiles[y][x].setTile(Tile.EMPTY, false);
                }

            }
        }
    }

    /**
     * Called when setting 1001 spikes spikeBlocks
     *
     * Loops though testLevel bitmap, depending on the pixel, load a new tile of
     * 1001 spikes type.
     */
    private void setNewTiles(BufferedImage l) {
        level = l;
        int w = level.getWidth();
        int h = level.getHeight();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int pixel = level.getRGB(x, y);

                int a = ((pixel & 0xff000000) >>> 24);
                int r = ((pixel & 0x00ff0000) >>> 16);
                int g = ((pixel & 0x0000ff00) >>> 8);
                int b = ((pixel & 0x000000ff));

                //Id of tile at x,y
                int id = tiles[y][x].getId();
//                int id = getTileId(rows, cols);   //same line above
                if (r == 0 && g == 0 && b == 0) {
                    //System.out.println("Black block at: " + x + " " + y);
                    //*************DO NOT NEED*************
                    tiles[y][x].loadNewImage(id);
                    continue;
                }
                //If the current pixel is grey
                if (r == 255 && g == 0 && b == 0) {
                    //System.out.println("Wall block at: " + x + " " + y);
                    tiles[y][x].loadNewImage(id);
                    continue;
                }
                if (r == 0 && g == 255 && b == 0) {
                    //System.out.println("Home block at: " + x + " " + y);
                    tiles[y][x].loadNewImage(id);
                    continue;
                }
                if (r == 0 && g == 0 && b == 255) {
                    //System.out.println("Box block at: " + x + " " + y);
                    tiles[y][x].loadNewImage(id);
                    continue;
                }
                //new blocks
                if (r == 152 && g == 0 && b == 0) {
                    tiles[y][x].loadNewImage(id);
                    continue;
                }
                if (r == 2 && g == 138 && b == 2) {
                    tiles[y][x].loadNewImage(id);
                    continue;
                }
                if (r == 0 && g == 0 && b == 94) {
                    tiles[y][x].loadNewImage(id);
                    continue;
                }//end new blocks
                //wall
                if (r == 120 && g == 120 && b == 120) {
                    //System.out.println("Box block at: " + x + " " + y);
                    tiles[y][x].loadNewImage(id);
                    //continue;
                }

                //Extra tiles
                if (r == 0 && g == 255 && b == 255) {
                    //System.out.println("Box block at: " + x + " " + y);
                    tiles[y][x].setTile(Tile.WATER, false);
                }
                if (r == 200 && g == 100 && b == 0) {
                    //System.out.println("Box block at: " + x + " " + y);
                    tiles[y][x].setTile(Tile.LAVA, false);
                }
            }
            //System.out.println("");
        }
    }

    /**
     * CAN DELETE
     */
    private void setBorder() {
        //Sets the TOP wall blocked
        for (int y = 0; y < NO_OF_TILES_Y; y++) {
            tiles[y][0].setID(Tile.WALL);
        }
        //Sets the BOTTOM wall blocked
        for (int y = 0; y < NO_OF_TILES_Y; y++) {
            tiles[y][NO_OF_TILES_X - 1].setID(Tile.WALL);
        }
        //Sets the TL wall blocked
        for (int x = 0; x < NO_OF_TILES_X; x++) {
            tiles[0][x].setID(Tile.WALL);
        }
        //Sets the TR wall blocked
        for (int x = 0; x < NO_OF_TILES_X; x++) {
            tiles[NO_OF_TILES_Y - 1][x].setID(Tile.WALL);
        }
    }

    private void printNoSolidBlocks() {
        System.out.println("Going though all blocks...");
        int num = 0;
        for (int y = 0; y < NO_OF_TILES_Y; y++) {
            for (int x = 0; x < NO_OF_TILES_X; x++) {
                if (tiles[y][x].solid) {
                    num++;
                }
            }
        }
        System.out.println("*No of solid blocks is: " + num + "*");
    }

    /*  Getters & Setters */
    public void setTileId(int y, int x, int id) {
        tiles[y][x].setID(id);
    }

    /**
     * Gets the spikeBlock type
     *
     * @param y position of spikeBlock
     * @param x position of spikeBlock
     * @return id of spikeBlock
     */
    public int getTileId(int x, int y) {
        return tiles[y][x].getId();
    }

    /**
     * Gets the actual spikeBlock (if another class needs to access a
     * spikeBlock)
     *
     * @param y
     * @param x
     * @return
     */
    public Tile getTile(int x, int y) {
        /*CLAMP ver 1*/
//        if (x < 0 || x > NO_OF_TILES_X) {
//            System.out.println("Error! Can't move outside map -> x val");
//            return;
//        }
//        if (y < 0 || y > NO_OF_TILES_Y) {
//            System.out.println("Error! Can't move outside map -> y val");
//            return;
//        }
        /*CLAMP ver 2 (clamp between 0 - Tiles.length*/
        if (x < 0) {
            x = 0;
            System.out.println("x < 0, clamping to 0");
        }
        if (y < 0) {
            y = 0;
            System.out.println("y < 0, clamping to 0");
        }
        if (x >= NO_OF_TILES_X) {
            System.out.println("x: " + x);
            System.out.println("NO_OF_TILES_X = " + NO_OF_TILES_X);
            x = NO_OF_TILES_X - 1;
            System.out.println("x clamped to: " + (NO_OF_TILES_X - 1));
        }
        if (y >= NO_OF_TILES_Y) {
            y = NO_OF_TILES_Y - 1;
            System.out.println("y clamped to: " + (NO_OF_TILES_Y - 1));
        }
        return tiles[y][x];
    }

    public Tile getTile(Point point) {
        return getTile(point.x, point.y);
    }

    public void handleInput() {
        //Hitbox
        if (Input.isKeyTyped(KeyEvent.VK_X)) {
            renderMode = HITBOX_MODE;
        }
        //Mario
        if (Input.isKeyTyped(KeyEvent.VK_C)) {
            renderMode = BITMAP_MODE;
            loadLevel(Assets.level0);
        }
        //1001 Spikes
        if (Input.isKeyTyped(KeyEvent.VK_V)) {
            renderMode = BITMAP_MODE;
            setNewTiles(Assets.level0);
        }
    }

    /**
     * *************** UPDATE & RENDER *******************
     */
    @Override
    void gameUpdate(float deltaTime) {
        //If im moving any of the world tiles

        //Update animated tiles
        Assets.spikeWater.update(deltaTime);
        Assets.spikeLava.update(deltaTime);
    }

    /**
     * INSTEAD OF RENDERING BASED ON LOOP, RENDER BASED ON TILE POSITION
     *
     * @param g
     */
    @Override
    void gameRender(Graphics2D g) {
        /* Called every frame */

        switch (renderMode) {
            case HITBOX_MODE:
                drawHitbox(g);
                break;
            case BITMAP_MODE:
                drawRender(g);
                break;
        }
    }

    private void drawHitbox(Graphics2D g) {
        //NAIVE, renders all tiles (including off screen tiles)
        for (int y = 0; y < NO_OF_TILES_Y; y++) {
            for (int x = 0; x < NO_OF_TILES_X; x++) {
                int tileId = tiles[y][x].getId();
                switch (tileId) {
                    case Tile.EMPTY:
                        continue;
                    case Tile.WALL:
                        g.setColor(Color.GRAY);
                        g.draw(tiles[y][x].bounds);
                        break;
                    case Tile.TL:
                        g.setColor(Color.GREEN);
                        g.draw(tiles[y][x].bounds);
                        break;
                    case Tile.TM:
                        g.setColor(Color.GREEN);
                        g.draw(tiles[y][x].bounds);
                        break;
                    case Tile.TR:
                        g.setColor(Color.GREEN);
                        g.draw(tiles[y][x].bounds);
                        break;
                    case Tile.ML:
                        g.setColor(Color.GREEN);
                        g.draw(tiles[y][x].bounds);
                        break;
                    case Tile.MM:
                        g.setColor(Color.GREEN);
                        g.draw(tiles[y][x].bounds);
                        break;
                    case Tile.MR:
                        g.setColor(Color.GREEN);
                        g.draw(tiles[y][x].bounds);
                        break;
                }
            }
        }
    }

    private void drawRender(Graphics2D g) {
        //NAIVE, renders all tiles (including off screen tiles)
        for (int y = 0; y < NO_OF_TILES_Y; y++) {
            for (int x = 0; x < NO_OF_TILES_X; x++) {
                Tile t = tiles[y][x];
                int tileId = t.getId();
                switch (tileId) {
                    case Tile.EMPTY:
                        continue;
                    case Tile.WALL:
                    case Tile.TL:
                    case Tile.TM:
                    case Tile.TR:
                    case Tile.ML:
                    case Tile.MM:
                    case Tile.MR:
                        g.drawImage(t.tileImg, (int) t.position.x + xShift,
                                (int) t.position.y + yShift, null);
                        break;
                    case Tile.WATER:
                        g.drawImage(Assets.spikeWater.getImage(), (int) t.position.x + xShift,
                                (int) t.position.y + yShift, null);
                        break;
                    case Tile.LAVA:
                        g.drawImage(Assets.spikeLava.getImage(), (int) t.position.x + xShift,
                                (int) t.position.y + yShift, null);
                        break;
                }
            }
        }
    }
}
