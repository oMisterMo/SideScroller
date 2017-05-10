/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sidescroller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Holds all  (currently only spikeBlocks)

 11-Jan-2017, 12:25:37.
 *
 * @author Mo
 */
public class Assets {
    //Mario liker payer sheet
    public static BufferedImage playerSheet;
    
    public static BufferedImage level;  //level
    public static BufferedImage player;  //player (mo)
    
    //Mario world sprites
    public static BufferedImage marioTL;    //top left
    public static BufferedImage marioTM;    //top mid
    public static BufferedImage marioTR;    //top right
    public static BufferedImage marioML;    //left
    public static BufferedImage marioMM;    //mid
    public static BufferedImage marioMR;    //right
    
    public static BufferedImage marioBlock;    //wall
    public static BufferedImage marioBlock2;    //wall
    public static BufferedImage puzzlebuddyWall;    //wall
    
    //1001 Spikes tiles
    private static SpriteSheet spikeSheet;
    public static BufferedImage spikeBlock;
    public static BufferedImage spikeTL;
    public static BufferedImage spikeTM;
    public static BufferedImage spikeTR;
    public static BufferedImage spikeML;
    public static BufferedImage spikeMM;
    public static BufferedImage spikeMR;
    
    //Background
    public static BufferedImage cloud;
    

    public Assets() {
        //Loading all tiles
        System.out.println("Loading tiles...");
        loadImages();
    }

    public static void loadImages() {
        try {
            playerSheet = ImageIO.read(new File("assets\\playerSheet.png"));
            level = ImageIO.read(new File("assets\\testFile.png"));
            player = ImageIO.read(new File("assets\\player.png"));
            
            marioTL = ImageIO.read(new File("assets\\t1.png"));
            marioTM = ImageIO.read(new File("assets\\t2.png"));
            marioTR = ImageIO.read(new File("assets\\t3.png"));
            marioML = ImageIO.read(new File("assets\\t4.png"));
            marioMM = ImageIO.read(new File("assets\\t5.png"));
            marioMR = ImageIO.read(new File("assets\\t6.png"));
            puzzlebuddyWall = ImageIO.read(new File("assets\\wall.png"));
            marioBlock = ImageIO.read(new File("assets\\t10.png"));
            marioBlock2 = ImageIO.read(new File("assets\\t11.png"));
//            cloud = ImageIO.read(new File("assets\\cloud1.png"));
            
            spikeSheet = new SpriteSheet("assets\\area4.png");
            spikeBlock = spikeSheet.getTile(3, 4, 32, 32);
            //(13,9), (14,9), (15,9)
            spikeTL = spikeSheet.getTile(13, 9, 32, 32);
            spikeTM = spikeSheet.getTile(14, 9, 32, 32);
            spikeTR = spikeSheet.getTile(15, 9, 32, 32);
            spikeML = spikeSheet.getTile(13, 10, 32, 32);
            spikeMM = spikeSheet.getTile(14, 10, 32, 32);
            spikeMR = spikeSheet.getTile(15, 10, 32, 32);
            
            cloud = ImageIO.read(new File("assets\\cloud1.png"));
//            System.out.println("cloud: "+ cloud.toString());
            
        } catch (IOException e) {
            System.out.println("Error loading assets (images)...");
        }
    }
}
