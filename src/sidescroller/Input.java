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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class Input implements KeyListener {

    private static boolean[] currentKey = new boolean[256];//needs improving
    private static boolean[] lastKey = new boolean[256];

    /*
        WHEN THE SYSTEM DETECTS KEYBOARD PRESES 
    */
    @Override
    public void keyTyped(KeyEvent e) {
        currentKey[e.getKeyCode()] = true;
    }

    @Override
    public void keyPressed(KeyEvent e) {
//        System.out.println("Pressed: "+e.getKeyCode());
        currentKey[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
//        System.out.println("Released: "+e.getKeyCode());
        currentKey[e.getKeyCode()] = false;
    }

    
    /*
        USED BY A GAME
    */
    public static boolean isKeyPressed(int keyCode) {
        return currentKey[keyCode];
    }
    
    /**
     * Just pressed once
     * 
     * current key and not last key pressed
     * 
     * @param keyCode
     * @return 
     */
    public static boolean isKeyTyped(int keyCode) {
        return currentKey[keyCode] && !lastKey[keyCode];
    }
    
    public static boolean isKeyReleased(int keyCode){
        return !currentKey[keyCode] && lastKey[keyCode];
    }
    
    public static void updateLastKey(){
        lastKey = currentKey.clone();
    }

}
