/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sidescroller;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Only handles static, solid tiles
 * 
 * 1.Choose better names for tiles
 * 2.fix me
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
    public final static int LEFT = 2;
    public final static int MID = 3;
    public final static int RIGHT = 4;

    private int id = -1;
    /*
     Public so I can call t1.position.x;
     */
    public BufferedImage tileImg;
    public int x;
    public int y;
    public Rectangle hitbox;
    
    public boolean solid = false;

    public Tile(int x, int y, int id) {
        this.x = x; this. y = y;
        this.id = id;
        
        loadImage(id);
        
        if(id != Tile.EMPTY){
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
     * @param id tile image
     */
    public void setID(int id){
        this.id = id;
        //I should really load images once, than reference image
        
        loadImage(id);
    }
    
    public int getId(){
        return id;
    }
    
    public void setTile(int x, int y, int id){
        setID(id);
        
//        System.out.println("Tile "+id);
//        System.out.println("input x "+x);
//        System.out.println("input y "+y);
//        this.x = (x * Tile.TILE_WIDTH);
//        this.y = (y * Tile.TILE_HEIGHT);
//        System.out.println("tile Width "+Tile.TILE_WIDTH);
//        System.out.println("tile Height "+Tile.TILE_HEIGHT);
//        System.out.println("pos.x "+this.x);
//        System.out.println("pos.y "+this.y+"\n");
        
        if(id != Tile.EMPTY){
            hitbox = new Rectangle(this.x, this.y, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
            solid = true;
        }
    }

    /**
     * Depending on the id given, load the correct tile image
     *
     * @param path image path
     */
    private void loadImage(int id) {
        switch (id) {
            case LEFT:
                tileImg = Assets.t1;
                break;
            case MID:
                tileImg = Assets.t2;
                break;
            case RIGHT:
                tileImg = Assets.t3;
                break;
            case WALL:
                tileImg = Assets.t4;
                break;
        }
    }
}
