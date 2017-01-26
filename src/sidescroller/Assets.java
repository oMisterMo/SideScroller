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
 * Holds all  (currently only tiles)
 *
 * 11-Jan-2017, 12:25:37.
 *
 * @author Mo
 */
public class Assets {

    public static BufferedImage level;  //level
    public static BufferedImage player;  //player
    public static BufferedImage t1;    //left
    public static BufferedImage t2;    //mid
    public static BufferedImage t3;    //right
    public static BufferedImage t4;    //wall

    public Assets() {
        //Loading all tiles
        System.out.println("Loading tiles...");
        loadImages();
    }

    private void loadImages() {
        try {
            level = ImageIO.read(new File("assets\\testFile.png"));
            player = ImageIO.read(new File("assets\\player.png"));
            
            t1 = ImageIO.read(new File("assets\\t1.png"));
            t2 = ImageIO.read(new File("assets\\t2.png"));
            t3 = ImageIO.read(new File("assets\\t3.png"));
            t4 = ImageIO.read(new File("assets\\wall.png"));
        } catch (IOException e) {
            System.out.println("Error reading file");
        }
    }
}
