package sidescroller;

import common.Vector2D;
import java.awt.Graphics2D;

/**
 * 26-Nov-2017, 16:21:47.
 *
 * @author Mohammed Ibrahim
 */
public class Thwomp extends DynamicGameObject {

    public static float THOWMP_WIDTH = 26 * 2;
    public static float THOWMP_HEIGHT = 34 * 2;
    public float originalHeight;

    public static final int STATE_IDLE = 0;
    public static final int STATE_WARN = 1;
    public static final int STATE_ACTIVE = 2;
    public int state = STATE_IDLE;
    public float stateTime = 0;

    public Vector2D rayCast = new Vector2D();
    public Vector2D rayCast2 = new Vector2D();
    private final Player player;

    public Thwomp(float x, float y, float width, float height, Player player) {
        super(x, y, width, height);
//        position.set(x, y);
        originalHeight = y;
//        initRayCast();
        this.player = player;
    }

    private void initRayCast() {
        //Set raycast
        rayCast.x = bounds.x;
        rayCast.y = bounds.y;
//        rayCast.setLength(50);
        System.out.println("--------------");
        System.out.println("length: " + rayCast.length());
        System.out.println("x: " + rayCast.x);
        System.out.println("y: " + rayCast.y);
//        velocity.y = 350;

        rayCast2.x = bounds.x + bounds.width;
        rayCast2.y = bounds.y;
        rayCast2.setLength(150);
    }

    public boolean isWithinWarningRange(DynamicGameObject player) {
        float x = position.x - player.position.x;
        return (x >= -Player.PLAYER_WIDTH && x <= Thwomp.THOWMP_WIDTH);
    }

    public boolean isWithinActiveRange(DynamicGameObject player) {
        //        System.out.println(thwomp.position.sub(player.position));
        float x = position.x - player.position.x;
        return (x >= -20 && x <= 20);
    }

    public void setState(int state) {
        if (state < STATE_IDLE || state > STATE_ACTIVE) {
            System.out.println("Incorrect state");
            return;
        }
        this.state = state;
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

    public void update() {
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

    private void makeFall() {
        velocity.y = 350;
    }

    private void makeReturn() {
        if (position.y <= originalHeight) {
            velocity.y = 0;
            position.y = originalHeight;    //clamp back to position
            bounds.y = originalHeight;
        } else {
            velocity.y = -200;
//            System.out.println("going back");
        }
    }

    public void updateBounds(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.y += velocity.y * deltaTime;
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

    private void drawRayCast(Graphics2D g) {
        g.drawLine((int) rayCast.x, (int) rayCast.y, (int) rayCast.x, (int) rayCast.length());
    }
}
