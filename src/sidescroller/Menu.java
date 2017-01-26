/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sidescroller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * OLD MENU MIGHT USE ELSE WHERE
 * 05-Oct-2016, 22:53:34.
 *
 * @author Mo
 */
public class Menu extends GameObject {

    private static final int PLAY = 0;
    private static final int ABOUT = 1;
    private static final int QUIT = 2;
    private int currentChoice = PLAY;

    private final int NO_OF_LETTERS = 26;

    private SpriteSheet spriteSheet;
    private BufferedImage letters[];
    private BufferedImage lLarge;
    private BufferedImage lSmall;
    private BufferedImage rLarge;
    private BufferedImage rSmall;
    private Rectangle leftHitbox;
    private Rectangle rightHitbox;

    private BufferedImage[] wordTest;

    private BufferedImage[] play;
    private BufferedImage[] about;
    private BufferedImage[] quit;
    private BufferedImage[] boxworld;
    //clickable rect
    private Rectangle playHitbox;
    private Rectangle aboutHitbox;
    private Rectangle quitHitbox;
    private final int buttonOffset;
    
    //extra
    int x, y;

    public Menu() {
        //Represents position of boxworld logo
        this.x = 170;
        this.y = 100;

        spriteSheet = new SpriteSheet("assets\\basicFont.png");
        letters = new BufferedImage[NO_OF_LETTERS];

        int colums = 8;
        int c1 = 1;
        int c2 = 1;
        for (int i = 0; i < NO_OF_LETTERS; i++) {
            //System.out.println("val1: "+(i+1) %8+" val2:"+ (i+1) %5);
            letters[i] = spriteSheet.getTile(c2, c1, 64, 64);
            //System.out.println("c1: "+(c1)+" c2:"+ (c2));

            if (c2 >= colums) {
                c2 = 0;
                c1++;
            }
            c2++;
        }
        lLarge = spriteSheet.getTile(3, 4, 64, 64);
        rLarge = spriteSheet.getTile(4, 4, 64, 64);
        lSmall = spriteSheet.getTile(7, 4, 64, 64);
        rSmall = spriteSheet.getTile(8, 4, 64, 64);

        //Initialise hitboxs for buttons
        buttonOffset = 280;
        leftHitbox = new Rectangle(0, GamePanel.GAME_HEIGHT / 2, 64, 64);
        rightHitbox = new Rectangle(GamePanel.GAME_WIDTH - rLarge.getWidth(),
                GamePanel.GAME_HEIGHT / 2, 64, 64);
        //About and quit starts off screen to the right
        playHitbox = new Rectangle(GamePanel.GAME_WIDTH / 2 + (0 * 64) - 128, y + 200, 64 * 4, 64);
        aboutHitbox = new Rectangle(GamePanel.GAME_WIDTH / 2 + (0 * 64) - 128, y + 200, 64 * 4, 64);
        quitHitbox = new Rectangle(GamePanel.GAME_WIDTH / 2 + (0 * 64) - 128, y + 200, 64 * 4, 64);
//        aboutHitbox = new Rectangle(GamePanel.GAME_WIDTH / 2 + (0 * 64) - 128 + buttonOffset, y + 200, 64 * 5, 64);
//        quitHitbox = new Rectangle(GamePanel.GAME_WIDTH / 2 + (0 * 64) - 128 + buttonOffset * 2, y + 200, 64 * 4, 64);

        //Makes some words which we would render
//        //dont need to initiailise here 
//        play = new BufferedImage[4];
//        quit = new BufferedImage[4];
//        boxworld = new BufferedImage[8];
        createWords();

    }

    private void createWords() {
        play = makeWord("play");
        about = makeWord("about");
        quit = makeWord("quit");
        boxworld = makeWord("boxworld");

        wordTest = makeWord("shutupblad");
    }

    /**
     * Returns a buffered image array of the word given
     *
     * @param word extracts the given word from the sprite sheet
     * @return image array of word
     */
    public BufferedImage[] makeWord(String word) {
        //world must be between a-z (0- 25)
        char[] charArray = word.toCharArray();
        int asciiCode;

        BufferedImage[] myWord = new BufferedImage[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            //gets the ascii code of the lowercase character
            asciiCode = charArray[i];
            myWord[i] = letters[asciiCode - 97];
        }
//        System.out.println("letter position 0: "+ charArray[0]);
//        System.out.println("letter position 1: "+ charArray[1]);

        //Map a character to a number
        return myWord;
    }

    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        if (leftHitbox.contains(mx, my)) {
            moveMenyLeft();
        }
        if (rightHitbox.contains(mx, my)) {
            moveMenyRight();
        }
        
        if(playHitbox.contains(mx, my)){
            System.out.println("PLAY");
        }
        if(aboutHitbox.contains(mx, my)){
            System.out.println("ABOUT");
        }
        if(quitHitbox.contains(mx, my)){
            System.out.println("QUIT");
        }
    }

    /**
     * This properly tomorrow with paper and pen
     */
    private void moveMenyLeft() {
        if(currentChoice == PLAY){
            return;
        }
        currentChoice-=1;
    }

    private void moveMenyRight() {
        if(currentChoice == QUIT){
            return;
        }
        currentChoice+=1;
    }
    
    private void setButtonState(int buttonState){
        
    }

    @Override
    void gameUpdate(float deltaTime) {
        switch (currentChoice) {
            case PLAY:
                //do some updating
                //playHitbox.x -=1;
                
                break;
            case ABOUT:
                break;
            case QUIT:
                break;
        }
    }

    @Override
    void gameRender(Graphics2D g) {
        //Draw the letter boxworld
//        g.drawImage(letters[1], x, y, null);
//        g.drawImage(letters[14], x+64,y, null);
//        g.drawImage(letters[23], x+(64*2),y, null);
//        g.drawImage(letters[22], x+(64*3),y, null);
//        g.drawImage(letters[14], x+(64*4),y, null);
//        g.drawImage(letters[17], x+(64*5),y, null);
//        g.drawImage(letters[11], x+(64*6),y, null);
//        g.drawImage(letters[3], x+(64*7),y, null);
        for (int i = 0; i < boxworld.length; i++) {
            g.drawImage(boxworld[i], x + (i * 64), y, null);
        }

        switch (currentChoice) {
            case PLAY:
                //Draw play
                for (int i = 0; i < play.length; i++) {
                    g.drawImage(play[i], playHitbox.x + (i * 64), playHitbox.y, null);
                }
                g.setColor(Color.WHITE);
                g.drawRect(playHitbox.x, playHitbox.y, 64 * 4, 64);

                break;
            case ABOUT:

                for (int i = 0; i < about.length; i++) {
                    g.drawImage(about[i], GamePanel.GAME_WIDTH / 2 + (i * 64) - 128, y + 200, null);
                }
                g.setColor(Color.GREEN);
                g.drawRect(aboutHitbox.x, aboutHitbox.y, 64 * 5, 64);
                break;
            case QUIT:

                for (int i = 0; i < quit.length; i++) {
                    g.drawImage(quit[i], GamePanel.GAME_WIDTH / 2 + (i * 64) - 128, y + 200, null);
                }
                g.setColor(Color.BLUE);
                g.drawRect(quitHitbox.x, quitHitbox.y, 64 * 4, 64);
                break;
        }

        //Draw the left and right buttons
//        //Draw the large version
//        g.drawImage(lSmall, 0, GamePanel.GAME_HEIGHT/2, null);
//        g.drawImage(rSmall, GamePanel.GAME_WIDTH-rLarge.getWidth(), GamePanel.GAME_HEIGHT/2, null);
        //Draw the small version
        g.drawImage(lSmall, 0, GamePanel.GAME_HEIGHT / 2, null);
        g.drawImage(rSmall, GamePanel.GAME_WIDTH - rLarge.getWidth(), GamePanel.GAME_HEIGHT / 2, null);

//        //THIS IS A TEST 
//        //prints the whole alphabet on two lines
//        for(int i=0; i<NO_OF_LETTERS/2; i++){
//            g.drawImage(letters[i], i*64, GamePanel.GAME_HEIGHT/2+100, null);
//        }
//        for(int i=NO_OF_LETTERS/2; i<NO_OF_LETTERS; i++){
//            g.drawImage(letters[i], (i-NO_OF_LETTERS/2)*64, GamePanel.GAME_HEIGHT/2+164, null);
//        }
//        for(int i=0; i<wordTest.length; i++){
//            g.drawImage(wordTest[i], GamePanel.GAME_WIDTH/2-350+(i*64), y+300, null);
//        }
    }

}
