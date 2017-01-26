/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sidescroller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * 06-Sep-2016, 23:03:23.
 *
 * @author Mo
 */
public class Tween extends GameObject {
    
    //FINAL VARIABLES
    public static final int TOPMID = GamePanel.GAME_WIDTH/2;
    public static final int LEFTMID = GamePanel.GAME_HEIGHT/2;
    
    //LOCAL VARIABLES
    private Vector2D position;
    private Vector2D velocity;
    
    private double dx = 0;
    private double dy = 0;
    private long start;
    private final double duration;
    private final Vector2D initialPos;
    private final Vector2D finalPos;
    
    private int width;
    private int height;

    public Tween() {
        width = 25;
        height = 25;

        position = new Vector2D(GamePanel.GAME_WIDTH / 2 - 500, GamePanel.GAME_HEIGHT / 2);
        velocity = new Vector2D(0, 0);
        initialPos = new Vector2D(GamePanel.GAME_WIDTH / 2 - 500, GamePanel.GAME_HEIGHT / 2);
        finalPos = new Vector2D(GamePanel.GAME_WIDTH / 2 + 500, GamePanel.GAME_HEIGHT / 2);
        /* dx = distence between current position and final pos */
        dx = finalPos.x - initialPos.x;
        dy = finalPos.y - initialPos.y;
        duration = 2000;//ms
        
        System.out.println("dx: " + dx);
        System.out.println("dy: " + dy + "\n");
        System.out.println("********************************");
        System.out.println("Initial pos-> " + position);
        System.out.println("Final pos-> " + finalPos);
        System.out.println("********************************");

    }

    public void begin() {
        start = System.currentTimeMillis();
    }

    /**
     *
     * @param t current time (0 - duration)
     * @param b beginning value (initial start)
     * @param c change in value (final - initial)
     * @param d duration (how long in ms to run)
     *
     * @return new position at current time
     */
    public static double linearTween(double t, double b, double c, double d) {
        return c * t / d + b;
    }

    /*----------------------------Cubic-------------------------------------*/
    public static double easeInCubic(double t, double b, double c, double d) {
        return c * (t /= d) * t * t + b;
    }

    public static double easeOutCubic(double t, double b, double c, double d) {
        return c * ((t = t / d - 1) * t * t + 1) + b;
    }

    public static double easeInOutCubic(double t, double b, double c, double d) {
        if ((t /= d / 2) < 1) {
            return c / 2 * t * t * t + b;
        }
        return c / 2 * ((t -= 2) * t * t + 2) + b;
    }

    /*----------------------------Quad-------------------------------------*/
    public static double easeInQuad(double t, double b, double c, double d) {
        return c * (t /= d) * t + b;
    }

    public static double easeOutQuad(double t, double b, double c, double d) {
        return -c * (t /= d) * (t - 2) + b;
    }

    public static double easeInOutQuad(double t, double b, double c, double d) {
        if ((t /= d / 2) < 1) {
            return c / 2 * t * t + b;
        }
        return -c / 2 * ((--t) * (t - 2) - 1) + b;
    }

    /*----------------------------Elastic------------------------------------*/
    public static double easeInElastic(double t, double b, double c, double d) {
        if (t == 0) {
            return b;
        }
        if ((t /= d) == 1) {
            return b + c;
        }
        double p = d * .3f;
        double a = c;
        double s = p / 4;
        return -(a * Math.pow(2, 10 * (t -= 1)) * Math.sin((t * d - s) * (2 * Math.PI) / p)) + b;
    }

    public static double easeOutElastic(double t, double b, double c, double d) {
        if (t == 0) {
            return b;
        }
        if ((t /= d) == 1) {
            return b + c;
        }
        double p = d * .3f;
        double a = c;
        double s = p / 4;
        return (a * Math.pow(2, -10 * t) * Math.sin((t * d - s) * (2 * Math.PI) / p) + c + b);
    }

    public static double easeInOutElastic(double t, double b, double c, double d) {
        if (t == 0) {
            return b;
        }
        if ((t /= d / 2) == 2) {
            return b + c;
        }
        double p = d * (.3f * 1.5f);
        double a = c;
        double s = p / 4;
        if (t < 1) {
            return -.5f * (a * Math.pow(2, 10 * (t -= 1)) * Math.sin((t * d - s) * (2 * Math.PI) / p)) + b;
        }
        return a * Math.pow(2, -10 * (t -= 1)) * Math.sin((t * d - s) * (2 * Math.PI) / p) * .5f + c + b;
    }

    /*-----------------------------Bounce-------------------------------------*/
    public static double easeInBounce(double t, double b, double c, double d) {
        return c - easeOutBounce(d - t, 0, c, d) + b;
    }

    public static double easeOutBounce(double t, double b, double c, double d) {
        if ((t /= d) < (1 / 2.75f)) {
            return c * (7.5625f * t * t) + b;
        } else if (t < (2 / 2.75f)) {
            return c * (7.5625f * (t -= (1.5f / 2.75f)) * t + .75f) + b;
        } else if (t < (2.5 / 2.75)) {
            return c * (7.5625f * (t -= (2.25f / 2.75f)) * t + .9375f) + b;
        } else {
            return c * (7.5625f * (t -= (2.625f / 2.75f)) * t + .984375f) + b;
        }
    }

    public static double easeInOutBounce(double t, double b, double c, double d) {
        if (t < d / 2) {
            return easeInBounce(t * 2, 0, c, d) * .5f + b;
        } else {
            return easeOutBounce(t * 2 - d, 0, c, d) * .5f + c * .5f + b;
        }
    }

    /*-----------------------------Back-------------------------------------*/
    public static double easeInBack(double t, double b, double c, double d) {
        double s = 1.70158f;
        return c * (t /= d) * t * ((s + 1) * t - s) + b;
    }

    public static double easeOutBack(double t, double b, double c, double d) {
        double s = 1.70158f;
        return c * ((t = t / d - 1) * t * ((s + 1) * t + s) + 1) + b;
    }

    public static double easeInOutBack(double t, double b, double c, double d) {
        double s = 1.70158f;
        if ((t /= d / 2) < 1) {
            return c / 2 * (t * t * (((s *= (1.525f)) + 1) * t - s)) + b;
        }
        return c / 2 * ((t -= 2) * t * (((s *= (1.525f)) + 1) * t + s) + 2) + b;
    }
    //End ease functions
    
    
    /**
     * Starts the animation by activating start
     * @param e mouse event source
     */
    public void mousePressed(MouseEvent e) {
        //int x = e.getX();
        //int y = e.getY();

        begin();
    }

    /* Update & Render */
    @Override
    void gameUpdate(float deltaTime) {
        long elapsedTime = System.currentTimeMillis() - start;
        //System.out.println(elapsedTime);

        //if time gone by is less than our duration -> TWEEN
        if (elapsedTime < duration) {
            //System.out.println("\npos.x: " + easeInQuad(elapsedTime, initialPos.x, dx, duration) );
            //position.x = easeInBack(elapsedTime, initialPos.x, dx, duration);
            //position.y = easeInBack(elapsedTime, initialPos.y, dy, duration);
            //System.out.println(elapsedTime);
            width =     (int) easeInOutBounce(elapsedTime, 25, 150-25, duration);
            height =    (int) easeInOutElastic(elapsedTime, 25, 150-25, duration);
            System.out.println("w "+width);

        } else {
            //System.out.println("*done*");
        }
        //System.out.println(position);
    }

    @Override
    void gameRender(Graphics2D g) {
        g.setColor(Color.PINK);
        g.fillOval((int) position.x, (int) position.y, width, height);
    }

}
