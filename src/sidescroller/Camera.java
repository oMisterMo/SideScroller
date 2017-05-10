package sidescroller;

import common.Vector2D;
/**
 * 06-Dec-2016, 04:43:06.
 *
 * @author Mo
 */
public class Camera {
    public Vector2D camPos;
    private Player player;
    
    public Camera(Player player, Vector2D pos){
        this.player = player;
        camPos = pos;
    }
    
    public void gameUpdate(float deltaTime){
//        camPos.subFrom(player.velocity.mult(deltaTime));
//        camPos.x -= player.velocity.x * deltaTime;
//        camPos.y -= player.velocity.y * deltaTime;
        
        //Camera sticks to the players position
        camPos.x = player.playerHitbox.x - 250;
        
        if(camPos.x <0){
            camPos.x = 0;
        }
        //find the end position
        if(camPos.x > 1280){
            camPos.x = 1280;
        }
//        camPos.y = player.playerHitbox.y - 450;
        //Test camera position
//        System.out.println(camPos);
    }

}
