package sidescroller;

import common.Helper;
import common.Tween;
import common.Vector2D;

/**
 * @author Mohammed Ibrahim
 */
public class Camera {

    public static final int STATE_DEFAULT = 0;
    public static final int STATE_SHAKE = 1;
    public static int cameraState = STATE_DEFAULT;
    //Camera shake variables
    public static int SHAKE_TIME_MS = 300;
    public static int SHAKE_OFFSET = 10;
    public static float SHAKE_SPEED = 10.0f; //1x = normal speed
    private int shakeTimer = 0;
    private boolean shakeDirection = false;
    private float xOffset = 0;
    private float yOffset = 0;

    //for tween
    long elapsedTime = 0;

    public Vector2D camPos;
    private final Player player;

    public Camera(Player player, Vector2D pos) {
        this.player = player;
        camPos = pos;
    }

    public void gameUpdate(float deltaTime) {
        //Update position
        switch (cameraState) {
            case STATE_DEFAULT:
                followPlayer(deltaTime);
                break;
            case STATE_SHAKE:
                shake(deltaTime);
                break;
        }
        //Clamp camera to world
        camPos.x = (float) Math.floor(Helper.Clamp(camPos.x, 0, 1280)); //Math.floor -> to avoid shake (walking into wall)
//        camPos.y = Helper.Clamp(camPos.y, -200, 0);
    }

    private void followPlayer(float deltaTime) {
        //Tween to position
//        elapsedTime += deltaTime * 1000;
//        System.out.println("Camera elapsed time: " + elapsedTime);
//
//        //if time gone by is less than our duration -> TWEEN
//        if (elapsedTime < 500) {
//            /*
//             * @param t current time (0 - duration)
//             * @param b beginning value (initial start)
//             * @param c change in value (final - initial)
//             } else {
//             //Camera sticks to the players position
//             camPos.x = player.bounds.x - 250;
//             }           * @param d duration (how long in ms to run)
//             */
//            camPos.x = (int) Tween.easeInOutBounce(elapsedTime, 25, 150 - 25, 500);
//
//        } else {
//            //Camera sticks to the players position
//            camPos.x = player.bounds.x - 250;
//        }

        //Camera sticks to the players position
//        camPos.x = player.bounds.x - 250;
//        camPos.y = player.playerbounds50;
//        //Juice it or lose it video
        camPos.x += ((player.bounds.x - 250) - camPos.x) * .1;
        //Juicing your camera with maths
//        camPos.x = (float) (.20 * (player.bounds.x - 250) + (camPos.x * .80));
    }

    private void shake(float deltaTime) {
        shakeTimer += deltaTime * 1000;
        //If shaking for more than 2 seconds
        if (shakeTimer > SHAKE_TIME_MS) {
            shakeTimer = 0;
            cameraState = STATE_DEFAULT;
        } else {
            applyScreenShake(deltaTime);
        }
    }

    private void applyScreenShake(float deltaTime) {
        if (shakeDirection) {
            xOffset -= SHAKE_SPEED * deltaTime * 1000;
            if (xOffset < -SHAKE_OFFSET) {
                xOffset = -SHAKE_OFFSET;
                shakeDirection = !shakeDirection;
            }
            yOffset = xOffset;
        } else {
            xOffset += SHAKE_SPEED * deltaTime * 1000;
            if (xOffset > SHAKE_OFFSET) {
                xOffset = SHAKE_OFFSET;
                shakeDirection = !shakeDirection;
            }
            yOffset = xOffset;
        }
        camPos.x = player.bounds.x + xOffset;
//        camPos.y = player.bounds.y + xOffset;
    }

}
