/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sidescroller;

/**
 * 06-Dec-2016, 04:43:06.
 *
 * @author Mo
 */
public class Camera {
    public Vector2D camPos;
    private final Player player;
    
    public Camera(Player player, Vector2D pos){
        this.player = player;
        camPos = pos;

    }
    
    public void gameUpdate(float deltaTime){
//        camPos.subFrom(player.velocity.mult(deltaTime));
        camPos.x -= player.velocity.x ;//* deltaTime;
//        camPos.y -= player.velocity.y * deltaTime;
    }

}
