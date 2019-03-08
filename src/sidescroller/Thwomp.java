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

import common.Vector2D;
import java.awt.Graphics2D;

/**
 * The Thwomp Class implements a Mario like enemy.
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class Thwomp extends DynamicGameObject {

    private static final int scale = 2;

    public static final float THOWMP_WIDTH = 26 * scale;
    public static final float THOWMP_HEIGHT = 34 * scale;

    public static final int STATE_IDLE = 0;
    public static final int STATE_WARN = 1;
    public static final int STATE_ACTIVE = 2;
    public int state = STATE_IDLE;

    private Vector2D leftRay = new Vector2D();
    private Vector2D rightRay = new Vector2D();
    private final Player player;
    public float stateTime = 0;
    private final float originalHeight;

    /**
     * Constructs a new Thwomp at {@link x}, {@link y}
     *
     * @param x the x position
     * @param y the y position
     * @param player reference to player
     */
    public Thwomp(float x, float y, Player player) {
        super(x, y, THOWMP_WIDTH, THOWMP_HEIGHT);
        originalHeight = y;
        this.player = player;
    }

    /**
     * Not currently in use
     */
    private void initRayCast() {
        //Set raycast
        leftRay.x = bounds.x;
        leftRay.y = bounds.y;
//        rayCast.setLength(50);
        System.out.println("--------------");
        System.out.println("length: " + leftRay.length());
        System.out.println("x: " + leftRay.x);
        System.out.println("y: " + leftRay.y);
//        velocity.y = 350;

        rightRay.x = bounds.x + bounds.width;
        rightRay.y = bounds.y;
        rightRay.setLength(150);
    }

    private boolean isWithinWarningRange(DynamicGameObject player) {
        float x = position.x - player.position.x;
        return (x >= -Player.PLAYER_WIDTH && x <= Thwomp.THOWMP_WIDTH);
    }

    private boolean isWithinActiveRange(DynamicGameObject player) {
        //        System.out.println(thwomp.position.sub(player.position));
        float x = position.x - player.position.x;
        return (x >= -20 && x <= 20);
    }
    
    private void setState(int state) {
        if (state < STATE_IDLE || state > STATE_ACTIVE) {
            System.out.println("Incorrect state");
            return;
        }
        this.state = state;
    }

    private void makeFall() {
        velocity.y = 350;   //magic numer
    }

    private void makeReturn() {
        if (position.y <= originalHeight) {
            velocity.y = 0;
            position.y = originalHeight;    //clamp back to position
            bounds.y = originalHeight;
        } else {
            velocity.y = -200;  //magic number
//            System.out.println("going back");
        }
    }

    private void update() {
        //AN ENEMY SHOULD ENCAPSULATE ALL ITS BEHAVIOUS!! 
        if (isWithinActiveRange(player)) {
            if (state != Thwomp.STATE_ACTIVE) {
                setState(Thwomp.STATE_ACTIVE);
                stateTime = 0;
//                System.out.println("State = active");
            }
        } else if (isWithinWarningRange(player)) {
            if (state != Thwomp.STATE_WARN) {
                setState(Thwomp.STATE_WARN);
                stateTime = 0;
//                System.out.println("state = warn");
            }

        } else {
            if (state != Thwomp.STATE_IDLE) {
                setState(Thwomp.STATE_IDLE);
                stateTime = 0;
//                System.out.println("state = idle");
            }
        }
    }

    private void updateBounds(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.y += velocity.y * deltaTime;
    }

    private void drawRayCast(Graphics2D g) {
        g.drawLine((int) leftRay.x, (int) leftRay.y, (int) leftRay.x, (int) leftRay.length());
    }

    @Override
    public void gameUpdate(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.y += velocity.y * deltaTime;
        switch (state) {
            case STATE_IDLE:
            case STATE_WARN:
                makeReturn();
                break;
            case STATE_ACTIVE:
                makeFall();
                break;
        }
        update();
        stateTime += deltaTime;
//        System.out.println(stateTime);
    }

    @Override
    public void gameRender(Graphics2D g) {
        switch (state) {
            case STATE_IDLE:
                g.drawImage(Assets.thwomp[0], (int) position.x, (int) position.y,
                        (int) THOWMP_WIDTH, (int) THOWMP_HEIGHT, null);
                break;
            case STATE_WARN:
                g.drawImage(Assets.thwomp[1], (int) position.x, (int) position.y,
                        (int) THOWMP_WIDTH, (int) THOWMP_HEIGHT, null);
                break;
            case STATE_ACTIVE:
                g.drawImage(Assets.thwomp[2], (int) position.x, (int) position.y,
                        (int) THOWMP_WIDTH, (int) THOWMP_HEIGHT, null);
                break;
        }
//        drawRayCast(g);
    }
}
