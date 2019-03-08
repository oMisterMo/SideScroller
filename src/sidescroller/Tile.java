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

import java.awt.image.BufferedImage;

/**
 * A Tile is a game object that does not move during its lifespan. A Level
 * consist of multiple tiles often stored as a 2-dimensional array. A Tile has a
 * fixed width/height and has interchangeable texture.
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
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

    public BufferedImage tileImg;
    private int id = -1;

    public boolean solid = false;

    /**
     * Constructs a new solid Tile at x, y.
     *
     * @param x the x position
     * @param y the y position
     * @param id the type of tile
     */
    public Tile(float x, float y, int id) {
        super(x, y, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);

        this.id = id;
        loadImage(id);
        //All tiles are not solid at first
        this.solid = false;
    }

    /**
     * Loads the correct image when the id is changed.
     *
     * @param id spikeBlock image
     */
    public void setID(int id) {
        this.id = id;
        loadImage(id);
    }

    /**
     * Depending on the id given, load the correct asset.
     *
     * @param id tile type
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
     * Given a tile id, sets the tile image to the correct tile. Switch to 1001
     * spike SpriteSheet.
     *
     * @param id ranges from 0-9 (empty - lava)
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

    /**
     * Returns the unique tile id
     *
     * @return tile id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the tile image and collidable toggle
     *
     * @param id Tile type
     * @param b true if solid
     */
    public void setTile(int id, boolean b) {
        setID(id);  //Sets id and loads image
        this.solid = b;
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
            default:
                type = "EMPTY";
        }
        return "type: " + type;
    }

}
