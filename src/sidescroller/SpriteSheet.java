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
 * 27-Sep-2016, 16:01:26.
 *
 * @author Mo
 */
public class SpriteSheet {
    
    private BufferedImage spriteSheet;
    
    public SpriteSheet(BufferedImage image){
        this.spriteSheet = image;
    }
    
    public SpriteSheet(String path){
        String spritePath = path;
        try{
            spriteSheet = ImageIO.read(new File(spritePath));
        }catch(IOException e){
            System.out.println("Can not open sprite sheet");
        }
    }
    
    public BufferedImage getTile(int x, int y, int width, int height){
        BufferedImage tile = spriteSheet
                .getSubimage((x*width)-width, (y*height)-height,
                        width, height);
        return tile;
    }
    
    
    
    /* **** GETTERS & SETTERS **** */
    public BufferedImage getSpriteSheet(){
        return spriteSheet;
    }
}
