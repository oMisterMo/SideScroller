package sidescroller;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Only handles static, solid spikeBlocks
 *
 * 1.Choose better names for spikeBlocks 2.fix me
 *
 * 06-Sep-2016, 23:18:03.
 *
 * @author Mo
 */
public class Tile {

    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;

    public final static int EMPTY = 0;
    public final static int WALL = 1;
    public final static int TL = 2;
    public final static int TM = 3;
    public final static int TR = 4;
    public final static int ML = 5;
    public final static int MM = 6;
    public final static int MR = 7;

    private int id = -1;
    /*
     Public so I can call marioTL.position.x;
     */
    public BufferedImage tileImg;
    public int x;
    public int y;
    public Rectangle hitbox;

    public boolean solid = false;

    public Tile(int x, int y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;

        loadImage(id);

        if (id != Tile.EMPTY) {
            hitbox = new Rectangle(x, y, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
            solid = true;
        }

//        hitbox = new Rectangle(x, y, width, height)
    }

//    public void setHitbox(int x, int y, int width, int height) {
//        hitbox = new Rectangle(x, y, width, height);
//    }
    /**
     * When we change the id, load correct image
     *
     * @param id spikeBlock image
     */
    public void setID(int id) {
        this.id = id;
        loadImage(id);
    }

    /**
     * Depending on the id given, load the correct spikeBlock image
     *
     * @param path image path
     */
    private void loadImage(int id) {
        switch (id) {
            case TL:
                tileImg = Assets.marioTL;
                break;
            case TM:
                tileImg = Assets.marioTM;
                break;
            case TR:
                tileImg = Assets.marioTR;
                break;
            case ML:
                tileImg = Assets.marioML;
                break;
            case MM:
                tileImg = Assets.marioMM;
                break;
            case MR:
                tileImg = Assets.marioMR;
                break;
            case WALL:
//                tileImg = Assets.puzzlebuddyWall;
//                double num = Math.random();
//                if(num < 0.5){
//                    tileImg = Assets.marioBlock;
//                }else{
//                    tileImg = Assets.marioBlock2;
//                }

//                tileImg = Assets.marioBlock2;
                tileImg = Assets.marioBlock;

                break;
        }
    }

    /**
     * Switch to 1001 spike spikeBlockset
     *
     * Given a tile id, set the tile image to the correct tile
     *
     * @param id 0-7 (empty - wall)
     */
    public void loadNewImage(int id) {
        switch (id) {
            case TL:
                tileImg = Assets.spikeTL;
                break;
            case TM:
                tileImg = Assets.spikeTM;
                break;
            case TR:
                tileImg = Assets.spikeTR;
                break;
            case ML:
                tileImg = Assets.spikeML;
                break;
            case MM:
                tileImg = Assets.spikeMM;
                break;
            case MR:
                tileImg = Assets.spikeMR;
                break;
            case WALL:
//                tileImg = Assets.puzzlebuddyWall;
                tileImg = Assets.spikeBlock;
                break;
        }
    }

    public int getId() {
        return id;
    }

    public void setTile(int x, int y, int id) {
        setID(id);

//        System.out.println("Tile "+id);
//        System.out.println("input x "+x);
//        System.out.println("input y "+y);
//        this.x = (x * Tile.TILE_WIDTH);
//        this.y = (y * Tile.TILE_HEIGHT);
//        System.out.println("spikeBlock Width "+Tile.TILE_WIDTH);
//        System.out.println("spikeBlock Height "+Tile.TILE_HEIGHT);
//        System.out.println("pos.x "+this.x);
//        System.out.println("pos.y "+this.y+"\n");
        if (id != Tile.EMPTY) {
            hitbox = new Rectangle(this.x, this.y, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
            solid = true;
        }
    }
}
