package sidescroller;

import common.Vector2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Only handles static, solid spikeBlocks
 *
 * 19/05/2017 - updated to extend StaticGameObject
 *
 * 1.Choose better names for spikeBlocks 2.fix me
 *
 * 06-Sep-2016, 23:18:03.
 *
 * @author Mo
 */
public class Tile extends StaticGameObject {

    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;

    public final static int EMPTY = 0;
    public final static int WALL = 1;
    public final static int TL = 2; //top left
    public final static int TM = 3; //top mid
    public final static int TR = 4; //top right
    public final static int ML = 5; //mid left
    public final static int MM = 6; //mid mid
    public final static int MR = 7; //mid right

    public final static int WATER = 8;
    public final static int LAVA = 9;

    //Public so I can call marioTL.position.x;
    public BufferedImage tileImg;
    private int id = -1;

//    public int x;
//    public int y;
//    public Rectangle hitbox;
    public boolean solid = true;

//    public Tile(int x, int y, int id) {
//        this.x = x;
//        this.y = y;
//        this.id = id;
//
//        loadImage(id);
//
//        if (id != Tile.EMPTY) {
//            hitbox = new Rectangle(x, y, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
//            solid = true;
//        }
//    }
//    public Tile(Vector2D position, int id) {
//        this.position = position;
//        this.id = id;
//
//        loadImage(id);
//
//        if (id != Tile.EMPTY) {
//            bounds = new Rectangle.Float(position.x, position.y, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
//            solid = true;
//        }
//    }
    public Tile(float x, float y, float width, float height, int id) {
        super(x, y, width, height);

        this.id = id;
        loadImage(id);
        //All tiles are not solid at first
        this.solid = false;
    }

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

            //Extra tiles
            case WATER:
                //tileImg = Assets.spikeWater.getImage();
                break;
            case LAVA:
                //tileImg = Assets.spikeWater.getImage();
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
            //Extra tiles
            case WATER:
                //tileImg = Assets.spikeWater.getImage();
                break;
            case LAVA:
                //tileImg = Assets.spikeWater.getImage();
                break;
        }
    }

    public int getId() {
        return id;
    }

//    public void setTile(int x, int y, int id) {
//        setID(id);
//
////        System.out.println("Tile "+id);
////        System.out.println("input x "+x);
////        System.out.println("input y "+y);
////        this.x = (x * Tile.TILE_WIDTH);
////        this.y = (y * Tile.TILE_HEIGHT);
////        System.out.println("spikeBlock Width "+Tile.TILE_WIDTH);
////        System.out.println("spikeBlock Height "+Tile.TILE_HEIGHT);
////        System.out.println("pos.x "+this.x);
////        System.out.println("pos.y "+this.y+"\n");
//        if (id != Tile.EMPTY) {
//            hitbox = new Rectangle(this.x, this.y, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
//            solid = true;
//        }
//    }
    /**
     * Given a tile id and boolean, sets the tile image and collision bounds
     *
     * @param id Tile type
     * @param b true if solid
     */
    public void setTile(int id, boolean b) {
        //Sets id and loads image
        setID(id);
        this.solid = b;
//        if (solid) {
////            bounds = new Rectangle.Float(position.x, position.y, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
////            System.out.println("Tile x: "+position.x);
////            System.out.println("Tile y: "+position.y);
//            bounds.setRect(position.x, position.y, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
//        }
    }

    @Override
    public String toString() {
        String type = "";
        switch (id) {
            case TL:
                type = "TL";
                break;
            case TM:
                type = "TM";
                break;
            case TR:
                type = "TR";
                break;
            case ML:
                type = "ML";
                break;
            case MM:
                type = "MM";
                break;
            case MR:
                type = "MR";
                break;
            case WALL:
                type = "WALL";
                break;
            //Extra tiles
            case WATER:
                type = "WATER";
                break;
            case LAVA:
                type = "LAVA";
                break;
            default: type = "EMPTY";
        }
        return "type: "+type;
    }

}
