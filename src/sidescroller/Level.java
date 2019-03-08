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
 * The Level class represents a single playing field the player must traverse.
 * All level data is loaded from a bitmap image indexed pixel by pixel. Once the
 * level data is loaded, the tiles are set appropriately ready for the world to
 * use.
 *
 * A Level may be rendered using primitive shapes only which is useful for
 * debugging.
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class Level extends GameObject {

    //Double the screen width
    public static final int NO_OF_TILES_X
            = (GamePanel.GAME_WIDTH / Tile.TILE_WIDTH) * 2;   //80
    public static final int NO_OF_TILES_Y
            = (GamePanel.GAME_HEIGHT / Tile.TILE_HEIGHT);     //18
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

    /**
     * Loads the default level.
     */
    public Level() {
        //Initial new world here
        tiles = new Tile[NO_OF_TILES_Y][NO_OF_TILES_X];
        System.out.println("No x tiles: " + NO_OF_TILES_X);
        System.out.println("No y tiles: " + NO_OF_TILES_Y);

        //Set up tiles array
        nullTiles();    //sets to null
        initTiles();    //create empty tiles
        resetBoard();   //sets to empty (not really needed here)

        loadLevel(Assets.level);

//        setBorder();
        printNoSolidBlocks();   //can delete
        System.out.println("Level loaded...");
    }

    /**
     * Sets all tiles to null.
     */
    public void nullTiles() {
        System.out.println("Setting all tiles to null...");
        for (int y = 0; y < NO_OF_TILES_Y; y++) {
            for (int x = 0; x < NO_OF_TILES_X; x++) {
                tiles[y][x] = null;
            }
        }
    }

    /**
     * Initialises all tiles.
     */
    private void initTiles() {
        System.out.println("Initilising....(sepll)");
        for (int y = 0; y < NO_OF_TILES_Y; y++) {
            for (int x = 0; x < NO_OF_TILES_X; x++) {
//                tiles[i][j] = new Tile(i * Tile.TILE_WIDTH, j * Tile.TILE_HEIGHT, Tile.EMPTY);
//                tiles[i][j] = new Tile(new Vector2D(i * Tile.TILE_WIDTH, j * Tile.TILE_HEIGHT), Tile.EMPTY);
                tiles[y][x] = new Tile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT,
                        Tile.EMPTY);
            }
        }
    }

    /**
     * Sets all tiles to empty.
     */
    public void resetBoard() {
        System.out.println("Setting all tiles to empty...");
        for (int y = 0; y < NO_OF_TILES_Y; y++) {
            for (int x = 0; x < NO_OF_TILES_X; x++) {
                tiles[y][x].setTile(Tile.EMPTY, false);
            }
        }
    }

    /**
     * Loads the default test level.
     */
    public void loadLevel() {
        loadLevel(Assets.levelTest);
    }

    /**
     * Loads the level data from the argument {@link level} into the
     * {@link tiles} array.
     *
     * @param level image containing level details
     */
    public void loadLevel(BufferedImage level) {
        this.level = level;
        int w = this.level.getWidth();
        int h = this.level.getHeight();
//        System.out.println("w " + w + "\nh " + h);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int pixel = this.level.getRGB(x, y);
                //Get color of pixel in level array
                int a = ((pixel & 0xff000000) >>> 24);
                int r = ((pixel & 0x00ff0000) >>> 16);
                int g = ((pixel & 0x0000ff00) >>> 8);
                int b = ((pixel & 0x000000ff));

                //Depending on the color of a pixel, set the tile
                if (r == 0 && g == 0 && b == 0) {
                    tiles[y][x].setTile(Tile.EMPTY, false);
                    continue;   //We found a tile, do not need to check others
                }
                if (r == 255 && g == 0 && b == 0) {
                    tiles[y][x].setTile(Tile.TL, true);
                    continue;
                }
                if (r == 0 && g == 255 && b == 0) {
                    tiles[y][x].setTile(Tile.TM, true);
                    continue;
                }
                if (r == 0 && g == 0 && b == 255) {
                    tiles[y][x].setTile(Tile.TR, true);
                    continue;
                }
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
                }
                if (r == 120 && g == 120 && b == 120) {
                    tiles[y][x].setTile(Tile.WALL, true);
                    continue;
                }
                //Water animated tile
                if (r == 0 && g == 255 && b == 255) {
                    tiles[y][x].setTile(Tile.EMPTY, false);
                    continue;
                }
                //Lava animated tile
                if (r == 200 && g == 100 && b == 0) {
                    tiles[y][x].setTile(Tile.EMPTY, false);
//                    continue;
                }

            }
        }
    }

    /**
     * Loops though the {@link level} bitmap and loads the 1001 spikes tile set.
     *
     * This method is unnecessary as we should be able to switch assets without
     * having to loop through the level data again.
     */
    private void setNewTiles(BufferedImage level) {
        this.level = level;
        int w = this.level.getWidth();
        int h = this.level.getHeight();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int pixel = this.level.getRGB(x, y);

                int a = ((pixel & 0xff000000) >>> 24);
                int r = ((pixel & 0x00ff0000) >>> 16);
                int g = ((pixel & 0x0000ff00) >>> 8);
                int b = ((pixel & 0x000000ff));

                int id = tiles[y][x].getId();
                if (r == 0 && g == 0 && b == 0) {
                    tiles[y][x].loadNewImage(id);
                    continue;
                }
                //If the current pixel is red
                if (r == 255 && g == 0 && b == 0) {
                    tiles[y][x].loadNewImage(id);
                    continue;
                }
                if (r == 0 && g == 255 && b == 0) {
                    tiles[y][x].loadNewImage(id);
                    continue;
                }
                if (r == 0 && g == 0 && b == 255) {
                    tiles[y][x].loadNewImage(id);
                    continue;
                }
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
                }
                //wall
                if (r == 120 && g == 120 && b == 120) {
                    tiles[y][x].loadNewImage(id);
                    continue;
                }

                //Extra tiles
                if (r == 0 && g == 255 && b == 255) {
                    tiles[y][x].setTile(Tile.WATER, false);
                    continue;
                }
                if (r == 200 && g == 100 && b == 0) {
                    tiles[y][x].setTile(Tile.LAVA, false);
//                    continue;
                }
            }
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
        System.out.printf("*There are %d solid blocks*\n", num);
    }

    private void setTileId(int y, int x, int id) {
        tiles[y][x].setID(id);
    }

    private int getTileId(int x, int y) {
        return tiles[y][x].getId();
    }

    /**
     * Gets a tile at index {@link x}, {@link y} from {@link tiles}. If a range
     * greater than or less than the size of the array is given the value is
     * capped.
     *
     * @param x the x index
     * @param y the y index
     * @return tile at {@link x}, {@link y}
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

    /**
     * Gets a tile from the {@link tiles} list.
     *
     * @param point index into {@link tiles}
     * @return tile at point.x, point.y
     */
    public Tile getTile(Point point) {
        return getTile(point.x, point.y);
    }

    /**
     * Sets the state of the current render mode.
     */
    public void handleInput() {
        //Hitbox
        if (Input.isKeyTyped(KeyEvent.VK_X)) {
            renderMode = HITBOX_MODE;
        }
        //Mario
        if (Input.isKeyTyped(KeyEvent.VK_C)) {
            renderMode = BITMAP_MODE;
            loadLevel(Assets.level);
        }
        //1001 Spikes
        if (Input.isKeyTyped(KeyEvent.VK_V)) {
            renderMode = BITMAP_MODE;
            setNewTiles(Assets.level);
        }
    }

    /* *************** UPDATE & RENDER ******************* */
    @Override
    void gameUpdate(float deltaTime) {
        //If im moving any of the world tiles

        //Update animated tiles
        Assets.spikeWater.update(deltaTime);
        Assets.spikeLava.update(deltaTime);
    }

    @Override
    void gameRender(Graphics2D g) {
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
        //g.draw() -> repeated code
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
                        g.drawImage(Assets.spikeWater.getImage(),
                                (int) t.position.x + xShift,
                                (int) t.position.y + yShift, null);
                        break;
                    case Tile.LAVA:
                        g.drawImage(Assets.spikeLava.getImage(),
                                (int) t.position.x + xShift,
                                (int) t.position.y + yShift, null);
                        break;
                }
            }
        }
    }
}
