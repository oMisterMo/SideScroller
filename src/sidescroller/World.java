package sidescroller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

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
    //View changer
    public static final int HITBOX_MODE = 0;
    public static final int BITMAP_MODE = 1;
    public static final int BITMAP_SPIKE_MODE = 2;
    public static int renderMode = HITBOX_MODE;

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
     * Sets all spikeBlocks to null
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
     * Sets all spikeBlocks to empty
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
                    tiles[rows][cols].setTile(rows, cols, Tile.TL);
                }
                if (r == 0 && g == 255 && b == 0) {
                    //System.out.println("Home block at: " + x + " " + y);
                    tiles[rows][cols].setTile(rows, cols, Tile.TM);
                }
                if (r == 0 && g == 0 && b == 255) {
                    //System.out.println("Box block at: " + x + " " + y);
                    tiles[rows][cols].setTile(rows, cols, Tile.TR);
                }
                //new blocks
                if (r == 152 && g == 0 && b == 0) {
                    tiles[rows][cols].setTile(rows, cols, Tile.ML);
                }
                if (r == 2 && g == 138 && b == 2) {
                    tiles[rows][cols].setTile(rows, cols, Tile.MM);
                }
                if (r == 0 && g == 0 && b == 94) {
                    tiles[rows][cols].setTile(rows, cols, Tile.MR);
                }//end new blocks
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
     * Called when setting 1001 spikes spikeBlocks
     * 
     * Loops though level bitmap, depending on the pixel, load a new tile
     * of 1001 spikes type.
     */
    private void setNewTiles() {
        level = Assets.level;
        int w = level.getWidth();
        int h = level.getHeight();

        for (int cols = 0; cols < h; cols++) {
            for (int rows = 0; rows < w; rows++) {
                int pixel = level.getRGB(rows, cols);

                int a = ((pixel & 0xff000000) >>> 24);
                int r = ((pixel & 0x00ff0000) >>> 16);
                int g = ((pixel & 0x0000ff00) >>> 8);
                int b = ((pixel & 0x000000ff));
                
                //Id of tile at x,y
                int id = tiles[rows][cols].getId();
//                int id = getTileId(rows, cols);   //same line above
                if (r == 0 && g == 0 && b == 0) {
                    //System.out.println("Black block at: " + x + " " + y);
                    tiles[rows][cols].loadNewImage(id);
                }
                //If the current pixel is grey
                if (r == 255 && g == 0 && b == 0) {
                    //System.out.println("Wall block at: " + x + " " + y);
                    tiles[rows][cols].loadNewImage(id);
                }
                if (r == 0 && g == 255 && b == 0) {
                    //System.out.println("Home block at: " + x + " " + y);
                    tiles[rows][cols].loadNewImage(id);
                }
                if (r == 0 && g == 0 && b == 255) {
                    //System.out.println("Box block at: " + x + " " + y);
                    tiles[rows][cols].loadNewImage(id);
                }
                //new blocks
                if (r == 152 && g == 0 && b == 0) {
                    tiles[rows][cols].loadNewImage(id);
                }
                if (r == 2 && g == 138 && b == 2) {
                    tiles[rows][cols].loadNewImage(id);
                }
                if (r == 0 && g == 0 && b == 94) {
                    tiles[rows][cols].loadNewImage(id);
                }//end new blocks
                //wall
                if (r == 120 && g == 120 && b == 120) {
                    //System.out.println("Box block at: " + x + " " + y);
                    tiles[rows][cols].loadNewImage(id);
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
        for (int i = 0; i < NO_OF_TILES_X; i++) {
            tiles[i][0].setID(Tile.WALL);
        }
        //Sets the BOTTOM wall blocked
        for (int i = 0; i < NO_OF_TILES_X; i++) {
            tiles[i][NO_OF_TILES_Y - 1].setID(Tile.WALL);
        }
        //Sets the TL wall blocked
        for (int i = 0; i < NO_OF_TILES_Y; i++) {
            tiles[0][i].setID(Tile.WALL);
        }
        //Sets the TR wall blocked
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
     * Gets the spikeBlock type
     *
     * @param i position of spikeBlock
     * @param j position of spikeBlock
     * @return id of spikeBlock
     */
    
    public int getTileId(int i, int j) {
        return tiles[i][j].getId();
    }

    /**
     * Gets the actual spikeBlock (if another class needs to access a
     * spikeBlock)
     *
     * @param i
     * @param j
     * @return
     */
    public Tile getTile(int i, int j) {
        return tiles[i][j];
    }

    public void handleInput() {
        //Hitbox
        if (Input.isKeyTyped(KeyEvent.VK_X)) {
            renderMode = HITBOX_MODE;
        }
        //Mario
        if (Input.isKeyTyped(KeyEvent.VK_C)) {
            renderMode = BITMAP_MODE;
            loadLevel();
        }
        //1001 Spikes
        if (Input.isKeyTyped(KeyEvent.VK_V)) {
            renderMode = BITMAP_MODE;
            setNewTiles();
        }
    }

    /**
     * *************** UPDATE & RENDER *******************
     */
    @Override
    void gameUpdate(float deltaTime) {
        //If im moving any of the world tiles
//        System.out.println("spikeBlock: "+tiles[3][10].hitbox);
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

        switch (renderMode) {
            case HITBOX_MODE:
                drawHitbox(g);
                break;
            case BITMAP_MODE:
                drawRender(g);
                break;
//            case BITMAP_SPIKE_MODE:
//                drawSpikeRender(g);
//                break;
        }
    }

    private void drawHitbox(Graphics2D g) {
        //NAIVE, renders all tiles (including off screen tiles)
        for (int i = 0; i < NO_OF_TILES_X; i++) {
            for (int j = 0; j < NO_OF_TILES_Y; j++) {
                //--USE SWITCH--
                //If spikeBlock is empty -> SKIP CURRENT ITERATION
                if (tiles[i][j].getId() == Tile.EMPTY) {
                    continue;
                }
                //Depending on whats on the spikeBlock, choose appropriate color to render
                if (tiles[i][j].getId() == Tile.WALL) {
                    g.setColor(Color.GRAY);
                    g.drawRect(tiles[i][j].x + xShift, tiles[i][j].y + yShift, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
                } else if (tiles[i][j].getId() == Tile.TL) {
                    g.setColor(Color.GREEN);
                    g.drawRect(tiles[i][j].x + xShift, tiles[i][j].y + yShift, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
                } else if (tiles[i][j].getId() == Tile.TM) {
                    g.setColor(Color.GREEN);
                    g.drawRect(tiles[i][j].x + xShift, tiles[i][j].y + yShift, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
                } else if (tiles[i][j].getId() == Tile.TR) {
                    g.setColor(Color.GREEN);
                    g.drawRect(tiles[i][j].x + xShift, tiles[i][j].y + yShift, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
                } else if (tiles[i][j].getId() == Tile.ML) {
                    g.setColor(Color.GREEN);
                    g.drawRect(tiles[i][j].x + xShift, tiles[i][j].y + yShift, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
                } else if (tiles[i][j].getId() == Tile.MM) {
                    g.setColor(Color.GREEN);
                    g.drawRect(tiles[i][j].x + xShift, tiles[i][j].y + yShift, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
                } else if (tiles[i][j].getId() == Tile.MR) {
                    g.setColor(Color.GREEN);
                    g.drawRect(tiles[i][j].x + xShift, tiles[i][j].y + yShift, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
                }
            }
        }
    }

    private void drawRender(Graphics2D g) {
        //NAIVE, renders all tiles (including off screen tiles)
        for (int i = 0; i < NO_OF_TILES_X; i++) {
            for (int j = 0; j < NO_OF_TILES_Y; j++) {
                //If spikeBlock is empty -> SKIP CURRENT ITERATION
                if (tiles[i][j].getId() == Tile.EMPTY) {
                    continue;
                }
                //Depending on whats on the spikeBlock, choose appropriate color to render
                if (tiles[i][j].getId() == Tile.WALL) {
                    g.setColor(Color.GRAY);
                    g.drawImage(tiles[i][j].tileImg, tiles[i][j].x + xShift,
                            tiles[i][j].y + yShift, null);
                } else if (tiles[i][j].getId() == Tile.TL) {
                    g.setColor(Color.GREEN);
                    g.drawImage(tiles[i][j].tileImg, tiles[i][j].x + xShift,
                            tiles[i][j].y + yShift, null);
                } else if (tiles[i][j].getId() == Tile.TM) {
                    g.setColor(Color.GREEN);
                    g.drawImage(tiles[i][j].tileImg, tiles[i][j].x + xShift,
                            tiles[i][j].y + yShift, null);
                } else if (tiles[i][j].getId() == Tile.TR) {
                    g.setColor(Color.GREEN);
                    g.drawImage(tiles[i][j].tileImg, tiles[i][j].x + xShift,
                            tiles[i][j].y + yShift, null);
                } else if (tiles[i][j].getId() == Tile.ML) {
                    g.setColor(Color.GREEN);
                    g.drawImage(tiles[i][j].tileImg, tiles[i][j].x + xShift,
                            tiles[i][j].y + yShift, null);
                } else if (tiles[i][j].getId() == Tile.MM) {
                    g.setColor(Color.GREEN);
                    g.drawImage(tiles[i][j].tileImg, tiles[i][j].x + xShift,
                            tiles[i][j].y + yShift, null);
                } else if (tiles[i][j].getId() == Tile.MR) {
                    g.setColor(Color.GREEN);
                    g.drawImage(tiles[i][j].tileImg, tiles[i][j].x + xShift,
                            tiles[i][j].y + yShift, null);
                }
            }
        }
    }
}
