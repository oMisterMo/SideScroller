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

import common.Animation;
import common.SpriteSheet;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class Assets {

    //Mario liker payer sheet
    public static BufferedImage testLevel;  //level
    public static BufferedImage level0;  //level

    public static BufferedImage playerSheet;    //main character spritesheet
    public static BufferedImage playerMo;         //player (mo - not used)

    //Mario world sprites
    public static BufferedImage marioTL;    //top left
    public static BufferedImage marioTM;    //top mid
    public static BufferedImage marioTR;    //top right
    public static BufferedImage marioML;    //left
    public static BufferedImage marioMM;    //mid
    public static BufferedImage marioMR;    //right
    public static BufferedImage marioBlock;    //wall 1
    public static BufferedImage marioBlock2;    //wall 2
    public static BufferedImage puzzlebuddyWall;    //my wall

    //1001 Spikes tiles
    private static SpriteSheet spikeSheet;
    public static BufferedImage spikeBlock;
    public static BufferedImage spikeTL;
    public static BufferedImage spikeTM;
    public static BufferedImage spikeTR;
    public static BufferedImage spikeML;
    public static BufferedImage spikeMM;
    public static BufferedImage spikeMR;
    public static SpriteSheet spikeWaterSheet;
    public static Animation spikeWater;
    public static SpriteSheet spikeLavaSheet;
    public static Animation spikeLava;

    public static BufferedImage spikeFireball;

    //Background
    public static BufferedImage cloud;

    //Enemiers
//    public static Animation thwomp;
    public static SpriteSheet enemies;
    public static BufferedImage[] thwomp;
    public static Animation mole;

    public Assets() {
        //Loading all tiles (NOT CALLED)
//        System.out.println("Loading tiles...");
        loadImages();
    }

    public void loadImages() {
        try {
            testLevel = ImageIO.read(getClass().getResource("/assets/testFile.png"));
            level0 = ImageIO.read(getClass().getResource("/assets/level0.png"));
            playerSheet = ImageIO.read(getClass().getResource("/assets/playerSheet.png"));
            playerMo = ImageIO.read(getClass().getResource("/assets/player.png"));

            marioTL = ImageIO.read(getClass().getResource("/assets/t1.png"));
            marioTM = ImageIO.read(getClass().getResource("/assets/t2.png"));
            marioTR = ImageIO.read(getClass().getResource("/assets/t3.png"));
            marioML = ImageIO.read(getClass().getResource("/assets/t4.png"));
            marioMM = ImageIO.read(getClass().getResource("/assets/t5.png"));
            marioMR = ImageIO.read(getClass().getResource("/assets/t6.png"));
            puzzlebuddyWall = ImageIO.read(getClass().getResource("/assets/wall.png"));
            marioBlock = ImageIO.read(getClass().getResource("/assets/t10.png"));
            marioBlock2 = ImageIO.read(getClass().getResource("/assets/t11.png"));

            spikeSheet = new SpriteSheet("/assets/area4.png");
            spikeBlock = spikeSheet.getTile(3, 4, 32, 32);
            //(13,9), (14,9), (15,9)
            spikeTL = spikeSheet.getTile(13, 9, 32, 32);
            spikeTM = spikeSheet.getTile(14, 9, 32, 32);
            spikeTR = spikeSheet.getTile(15, 9, 32, 32);
            spikeML = spikeSheet.getTile(13, 10, 32, 32);
            spikeMM = spikeSheet.getTile(14, 10, 32, 32);
            spikeMR = spikeSheet.getTile(15, 10, 32, 32);
            spikeWaterSheet = new SpriteSheet("/assets/water.png");
            int numOfFrames = 3;
            BufferedImage[] testImage = new BufferedImage[numOfFrames];    //can delete
            for (int i = 0; i < numOfFrames; i++) {
                testImage[i] = spikeWaterSheet.getTile(i + 1, 1, 32, 32);
            }
            spikeWater = new Animation();
            spikeWater.setFrames(testImage);
            spikeWater.setDelay(300);

            spikeLavaSheet = new SpriteSheet("/assets/lava.png");
            numOfFrames = 4;
            testImage = new BufferedImage[numOfFrames];    //can delete
            for (int i = 0; i < numOfFrames; i++) {
                testImage[i] = spikeLavaSheet.getTile(i + 1, 1, 32, 32);
            }
            spikeLava = new Animation();
            spikeLava.setFrames(testImage);
            spikeLava.setDelay(200);

            spikeFireball = spikeSheet.getTile(15, 6, 32, 32);
            enemies = new SpriteSheet("/assets/enemies2.png");
            numOfFrames = 3;
            thwomp = new BufferedImage[numOfFrames];
            for (int i = 0; i < numOfFrames; i++) {
                thwomp[i] = ImageIO.read(getClass().getResource("/assets/thwomp" + (i + 1) + ".png"));
            }
            numOfFrames = 2;
            testImage = new BufferedImage[numOfFrames];
            for (int i = 0; i < numOfFrames; i++) {
                testImage[i] = enemies.getPosition(((i) * 36) , 0, 36, 32);
            }
            mole = new Animation();
            mole.setFrames(testImage);
            mole.setDelay(300);

            cloud = ImageIO.read(getClass().getResource("/assets/cloud1.png"));

        } catch (IOException e) {
            System.out.println("Error loading assets...");
        }
    }
}
