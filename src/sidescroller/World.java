/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sidescroller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * 16/05/2016
 *
 * @author Mo
 */
public class World extends GameObject {

    //double the screen width
    public static final int NO_OF_TILES_X = (GamePanel.GAME_WIDTH / Tile.TILE_WIDTH) * 2;
    public static final int NO_OF_TILES_Y = (GamePanel.GAME_HEIGHT / Tile.TILE_HEIGHT);

    public static final int NO_OF_LEVELS = 1;

    //Array holding all tiles
    private Tile[][] tiles;
    //level to load
    private BufferedImage level;

    Random ran = new Random();

    //Moves the world (x, y) units
    private final int xShift = 0;
    private final int yShift = 0;

    public World() {
        //Initial new world here
        tiles = new Tile[NO_OF_TILES_X][NO_OF_TILES_Y];
        System.out.println("No x tiles: " + NO_OF_TILES_X);
        System.out.println("No y tiles: " + NO_OF_TILES_Y);

//        tiles = new Tile[NO_OF_TILES_X][NO_OF_TILES_Y];
        //Load images
        //Initialise each Tile to empty
        clearBoard();   //sets to null
        initTiles();    //create empty tiles
        resetBoard();   //sets to empty (not needed here)

        loadLevel();
//        setBorder();
    }

    /**
     * Sets all tiles to null
     */
    public void clearBoard() {
        System.out.println("Setting all tiles to null...");
        for (int i = 0; i < NO_OF_TILES_X; i++) {
            for (int j = 0; j < NO_OF_TILES_Y; j++) {
                tiles[i][j] = null;
            }
        }
    }

    /**
     * Called from the constructor, loads all assets
     */
    private void initTiles() {
        System.out.println("Initilising....(sepll)");
        for (int i = 0; i < NO_OF_TILES_X; i++) {
            for (int j = 0; j < NO_OF_TILES_Y; j++) {
                tiles[i][j] = new Tile(i * Tile.TILE_WIDTH, j * Tile.TILE_HEIGHT, Tile.EMPTY);
            }
        }
    }

    /**
     * Sets all tiles to empty
     *
     * FIX LOGIC HERE
     */
    public void resetBoard() {
        System.out.println("Setting all tiles to empty...");
        for (int i = 0; i < NO_OF_TILES_X; i++) {
            for (int j = 0; j < NO_OF_TILES_Y; j++) {
                tiles[i][j].setID(Tile.EMPTY);
//                tiles[i][j].setTile(i, j, Tile.EMPTY);
            }
        }
    }

    /**
     * Loads either a random level or level 0
     */
    public void loadLevel() {
//        clearPositions();
        /*
         ITEM = ( R ,  G ,  B )

         Wall = (120, 120, 120)  : GRAY
         Empty = (0, 0, 0) : BLACK
         Player = (255, 255, 0): YELLOW
         Home = (255, 0, 0)  : RED
         Box = (0, 255, 0)  : GREEN
         BoxOnHome = (0, 0, 255)  : BLUE
         */

        //System.out.println("Color Model: " + level.getColorModel());
        level = Assets.level;
        int w = level.getWidth();
        int h = level.getHeight();
//        System.out.println("w " + w + "\nh " + h);

        for (int cols = 0; cols < h; cols++) {
            for (int rows = 0; rows < w; rows++) {
                int pixel = level.getRGB(rows, cols);

                int a = ((pixel & 0xff000000) >>> 24);
                int r = ((pixel & 0x00ff0000) >>> 16);
                int g = ((pixel & 0x0000ff00) >>> 8);
                int b = ((pixel & 0x000000ff));
                //System.out.println("r: "+ r + " g: "+ g + " b: "+ b + " a: " + a);
//                if(r == 255 && g == 255 && b == 255){
//                    System.out.println("White block at: " + x + " " + y);
//                }

                if (r == 0 && g == 0 && b == 0) {
                    //System.out.println("Black block at: " + x + " " + y);
                    tiles[rows][cols].setTile(rows, cols, Tile.EMPTY);

                }
                //If the current pixel is grey
                if (r == 255 && g == 0 && b == 0) {
                    //System.out.println("Wall block at: " + x + " " + y);
                    tiles[rows][cols].setTile(rows, cols, Tile.LEFT);

                }
                if (r == 0 && g == 255 && b == 0) {
                    //System.out.println("Home block at: " + x + " " + y);
                    tiles[rows][cols].setTile(rows, cols, Tile.MID);
                }
                if (r == 0 && g == 0 && b == 255) {
                    //System.out.println("Box block at: " + x + " " + y);
                    tiles[rows][cols].setTile(rows, cols, Tile.RIGHT);
                }
                if (r == 120 && g == 120 && b == 120) {
                    //System.out.println("Box block at: " + x + " " + y);
                    tiles[rows][cols].setTile(rows, cols, Tile.WALL);
                }

            }
            //System.out.println("");
        }

        //Random test - delete me
//        tiles[3][10].setID(Tile.WALL);
        tiles[3][10].setTile(3, 10, Tile.WALL);
    }

    /**
     * CAN DELETE
     */
    private void setBorder() {
        //Sets the TOP wall blocked
        for (int i = 0; i < NO_OF_TILES_X; i++) {
            tiles[i][0].setID(Tile.WALL);
        }
        //Sets the BOTTOM wall blocked
        for (int i = 0; i < NO_OF_TILES_X; i++) {
            tiles[i][NO_OF_TILES_Y - 1].setID(Tile.WALL);
        }
        //Sets the LEFT wall blocked
        for (int i = 0; i < NO_OF_TILES_Y; i++) {
            tiles[0][i].setID(Tile.WALL);
        }
        //Sets the RIGHT wall blocked
        for (int i = 0; i < NO_OF_TILES_Y; i++) {
            tiles[NO_OF_TILES_X - 1][i].setID(Tile.WALL);
        }
    }

    /*  Getters & Setters */
    public void setTileId(int i, int j, int id) {
        if (i < 0 || i > NO_OF_TILES_X) {
            System.out.println("Error! Can't move outside map -> x val");
            return;
        }
        if (j < 0 || j > NO_OF_TILES_Y) {
            System.out.println("Error! Can't move outside map -> y val");
            return;
        }
        tiles[i][j].setID(id);
    }

    /**
     * Gets the tile type
     *
     * @param i position of tile
     * @param j position of tile
     * @return id of tile
     */
    public int getTileId(int i, int j) {
        return tiles[i][j].getId();
    }

    /**
     * Gets the actual tile (if another class needs to access a tile)
     * @param i
     * @param j
     * @return 
     */
    public Tile getTile(int i, int j) {
        return tiles[i][j];
    }

    /**
     * *************** UPDATE & RENDER *******************
     */
    @Override
    void gameUpdate(float deltaTime) {
        //If im moving any of the world tiles
//        System.out.println("tile: "+tiles[3][10].hitbox);
//        tiles[15][NO_OF_TILES_Y-1].y -=4;
    }

    /**
     * INSTEAD OF RENDERING BASED ON LOOP, RENDER BASED ON TILE POSITION
     *
     * @param g
     */
    @Override
    void gameRender(Graphics2D g) {
        /* Called every frame */

        //NAIVE, renders all tiles (including off screen tiles)
        for (int i = 0; i < NO_OF_TILES_X; i++) {
            for (int j = 0; j < NO_OF_TILES_Y; j++) {

                //if tile is empty -> SKIP CURRENT ITERATION
                if (tiles[i][j].getId() == Tile.EMPTY) {
                    continue;
                }
                //Depending on whats on the tile, choose appropriate color to render
                if (tiles[i][j].getId() == Tile.WALL) {
                    g.setColor(Color.GRAY);
//                    g.fillRect(tiles[i][j].x +xShift, tiles[i][j].y + yShift, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
                    g.drawRect(tiles[i][j].x + xShift, tiles[i][j].y + yShift, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);

//                    g.drawImage(tiles[i][j].tileImg, tiles[i][j].x + xShift,
//                            tiles[i][j].y + yShift, null);
                } else if (tiles[i][j].getId() == Tile.LEFT) {
                    g.setColor(Color.GREEN);
//                    g.fillRect(i * TILE_WIDTH + xShift, j * TILE_HEIGHT + yShift, TILE_WIDTH - 1, TILE_HEIGHT - 1);
                    g.drawRect(tiles[i][j].x + xShift, tiles[i][j].y + yShift, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);

//                    g.drawImage(tiles[i][j].tileImg, tiles[i][j].x + xShift,
//                            tiles[i][j].y + yShift, null);
                } else if (tiles[i][j].getId() == Tile.MID) {
                    g.setColor(Color.GREEN);

//                    g.fillRect(i * TILE_WIDTH + xShift, j * TILE_HEIGHT +yShift, TILE_WIDTH-1, TILE_HEIGHT-1);
                    g.drawRect(tiles[i][j].x + xShift, tiles[i][j].y + yShift, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);

//                    g.drawImage(tiles[i][j].tileImg, tiles[i][j].x + xShift,
//                            tiles[i][j].y + yShift, null);
                } else if (tiles[i][j].getId() == Tile.RIGHT) {
                    g.setColor(Color.GREEN);
//                    g.fillRect(i * TILE_WIDTH +xShift, j * TILE_HEIGHT + yShift, TILE_WIDTH-1, TILE_HEIGHT-1);
                    g.drawRect(tiles[i][j].x + xShift, tiles[i][j].y + yShift, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);

//                    g.drawImage(tiles[i][j].tileImg, tiles[i][j].x + xShift,
//                            tiles[i][j].y + yShift, null);
                }

            }
        }
    }

}
