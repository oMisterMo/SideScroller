/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sidescroller;

import java.util.Random;

/**
 * Add ability to use own sin function
 *
 * 26-JAN-2017, 01:50:14.
 *
 * @author Mo
 */
public class Helper {

//    float value;
//    float min;
//    float max;
//    Random r;

    public Helper() {
//        value = 0f;
//        min = 0f;
//        max = 0f;

//        r = new Random();
    }

    public static float Clamp(float val, float min, float max) {
        if (val < min) {
            return min;
        } else if (val > max) {
            return max;
        }
        //We have a value within the range
        return val;
    }

    /**
     * .floor() Returns the largest (closest to positive infinity) double value
     * that is less than or equal to the argument and is equal to a mathematical
     * integer. Special cases:
     *
     * 1. If the argument value is already equal to a mathematical integer, then
     * the result is the same as the argument.
     *
     * 2. If the argument is NaN or an infinity or positive zero or negative
     * zero, then the result is the same as the argument.
     *
     * @param min
     * @param max
     * @return
     */
    public static float Random(float min, float max) {
        return (float) Math.floor(Math.random() * (max - min + 1) + min);
    }

    /**
     * No error checks (number, range)
     *
     * @param min
     * @param max
     * @return
     */
    public static double RandomDouble(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }
}
