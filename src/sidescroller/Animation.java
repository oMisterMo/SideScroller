package sidescroller;

import java.awt.image.BufferedImage;

/**
 * 14/03/2016.
 * 
 * @author Mo
 */
public class Animation {

    private BufferedImage[] frames;    //Sprite array of current animation
    private int currentFrame;

    private long startTime;
    private long delay;
    private boolean playedOnce; //Some animation are only played once

    public Animation(){

    }

    /**
     * Determines which image in the array to return
     *
     */
    public void update(float deltaTime){
        long elapsed = (System.nanoTime() - startTime)/ 1000000;

        if(elapsed > delay){
            currentFrame ++;
//            currentFrame += delay * deltaTime;
            startTime = System.nanoTime();
        }
        //Resets frame count after full play through
        if(currentFrame >= frames.length){
            currentFrame = 0;
            playedOnce = true;
        }
    }

    public BufferedImage getImage(){
        return frames[currentFrame];
    }



    /**
     * Assign a bitmap array to frames
     *
     * @param frames The frame that's updated
     */
    public void setFrames(BufferedImage[] frames){
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
    }

    public void setDelay(long del){
        delay = del;
    }

    public int getFrame(){
        return currentFrame;
    }

    public boolean playedOnce(){
        return playedOnce;
    }
    
    public void resetAnimation(){
        currentFrame = 0;
    }
}
