/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sidescroller;

/**
 * UPDATED VECTOR CLASS - Latest
 *
 * 27-Dec-2016, 23:12:58.
 *
 * @author Mo
 */
public class Vector2D {

    public static final float PI = (float) Math.PI;   //delete or fix

    public static float TO_DEGREES = (1 / PI) * 180;
    public static float TO_RADIANS = (1 / 180.0f) * PI;

    //Magnitude
    protected float x;
    protected float y;

//    //Direction or angle
//    protected float angle = 0f;
    public Vector2D() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /*Getters & Setters*/
    /**
     * If we have hypotenuse and angle, we can find x and y components of Vector
     *
     * @param angle
     */
    public void setAngle(float angle) {
        float length = this.length();
        this.x = (float) Math.cos(angle) * length;
        this.y = (float) Math.sin(angle) * length;
    }

    /**
     * Can find angle if we know x and y components of Vector
     *
     * @return
     */
    public float angle() {
        //System.out.println("degrees:"+ (Math.atan2(y, x) * 180)/Math.PI);
        return (float) Math.atan2(this.y, this.x);
    }

    public void setLength(float length) {
        float angle = this.angle();
        this.x = (float) Math.cos(angle) * length;
        this.y = (float) Math.sin(angle) * length;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Quicker to remove square root for distance comparison
     *
     * @return radius squared
     */
    public float lengthSqr() {
        return this.x * this.x + this.y * this.y;
    }

    /**
     * Unit vector has a length of 1
     *
     * @return unit vector
     */
    public Vector2D normalize() {
        float length = length();
        if (length != 0) {
            this.x /= length;
            this.y /= length;
        }
        return this;
    }

    //Books methods
    public Vector2D set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2D set(Vector2D v2) {
        this.x = v2.x;
        this.y = v2.y;
        return this;
    }

    /**
     * Complete me!!!
     */
    public void rotate() {

    }

    /**
     * Gets the distance between this vector and another
     *
     * //OR v2.x - dx (final pos - initial)
     *
     * @param v2
     * @return
     */
    public float distance(Vector2D v2) {
        float dx = x - v2.x;
        float dy = y - v2.y;

        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Gets the distance between this vector and another point
     *
     * @param x
     * @param y
     * @return
     */
    public float distance(float x, float y) {
        float dx = this.x - x;
        float dy = this.y - y;

        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public float distanceSqr(Vector2D v2) {
        float dx = x - v2.x;
        float dy = y - v2.y;

        return (float) dx * dx + dy * dy;
    }

    public float distanceSqr(float x, float y) {
        float dx = this.x - x;
        float dy = this.y - y;

        return (float) dx * dx + dy * dy;
    }

    public float dotProduct(Vector2D vec) {
        return this.x * vec.x + this.y * vec.y;
    }

    /**
     * NEW CODE
     *
     * @param x
     * @param y
     * @return
     */
    public Vector2D add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    /* Returns a new vector so we can override methos 
     e.g. 
     v3 = v1 + v2 
     instead of
     v3 = v1.add(v2)
     */
    public Vector2D add(Vector2D v2) {
//        x += v2.x;
//        y += v2.y;
        Vector2D v3 = new Vector2D(x + v2.x, y + v2.y);
        return v3;
    }

    public Vector2D sub(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector2D sub(Vector2D v2) {
//        x -= v2.x;
//        y -= v2.y;
        Vector2D v3 = new Vector2D(x - v2.x, y - v2.y);
        return v3;
    }

    public Vector2D mult(float val) {
//        x *= val;
//        y *= val;
        Vector2D v3 = new Vector2D(x * val, y * val);
        return v3;
    }

    public Vector2D div(float val) {
        Vector2D v3 = new Vector2D(x / val, y / val);
        return v3;
    }

    /* *FOLLOWING METHODS ONLY ADDTO CURRECT VECTOR* */
    public void addToo(Vector2D v2) {
        this.x += v2.x;
        this.y += v2.y;
    }

    public void subFrom(Vector2D v2) {
        this.x -= v2.x;
        this.y -= v2.y;
    }

    public void multBy(float scale) {
        this.x *= scale;
        this.y *= scale;
    }

    public void divBy(float scale) {
        this.x /= scale;
        this.y /= scale;
    }

    public void addToo(float vx, float vy) {
        this.x += vx;
        this.y += vy;
    }

    //P to I: P = I - P (destination - source)
    public void subFrom(float vx, float vy) {
        this.x -= vx;
        this.y -= vy;
    }

    //-----------------Working with other vectors-------------------------
    public static Vector2D add(Vector2D v1, Vector2D v2) {
        Vector2D temp = new Vector2D();

        temp.x = v1.x + v2.x;
        temp.y = v2.x + v2.y;
        return temp;
    }

    public static Vector2D sub(Vector2D v1, Vector2D v2) {
        Vector2D temp = new Vector2D();

        temp.x = v1.x - v2.x;
        temp.y = v2.x - v2.y;
        return temp;
    }

    public static Vector2D mult(Vector2D vec, float val) {
//        x *= val;
//        y *= val;
        Vector2D v3 = new Vector2D(vec.x * val, vec.y * val);
        return v3;
    }

    /*               EXTRAS                         */
    /**
     * Calculates the momentum of an object
     *
     * F = MA F = M(vf - vi)/t F = (mvf - mvi)/t
     *
     * @param vel
     * @param mass
     * @return
     */
    public static Vector2D momentum(Vector2D vel, float mass) {
        Vector2D temp;
        temp = mult(vel, mass);
        return temp;
    }

    /**
     * Finds out the impulse given a mass and change in velocity
     *
     * A force delivered in a very small amount of time
     *
     * F = (changeOf P)/t Ft = (changeOf P)
     *
     * F = net force t = very small time
     *
     * @param finalVel in meters per second
     * @param initialVel in meters per second
     * @param mass in kilograms
     * @return
     */
    public Vector2D impulse(Vector2D finalVel, Vector2D initialVel, float mass) {
        Vector2D impulse, finalMomentum, initialMomentum;
        //Get final and intial momentum
        finalMomentum = momentum(finalVel, mass);
        initialMomentum = momentum(initialVel, mass);

        impulse = sub(finalMomentum, initialMomentum);
        return impulse;
    }

    public static Vector2D nomalize(Vector2D vec) {
        Vector2D temp = new Vector2D();
        float length = vec.length();

        temp.x = (vec.x / length);
        temp.y = (vec.y / length);

        return temp;
    }

    /**
     * 1. v1 dot v2 = 0, the two vectors are perpendicular 2. v1 dot v2 <
     * 0(negative), Θ > 90 degree 3. v1 dot v2 > 0(positive), Θ < 90 degree
     * ..................>
     *
     * Θ is the angle between vectors v1 and v2
     *
     * @param v1
     * @param v2
     * @return
     */
    public static float dotProduct(Vector2D v1, Vector2D v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }

    /**
     * Magnitude of v1 * Magnitude of v2 * cos(Θ)
     *
     * Θ is the angle between vectors v1 and v2
     *
     * @param v1
     * @param v2
     * @return
     */
    public static float angle(Vector2D v1, Vector2D v2) {

        return -1;
    }

    public static Vector2D nonAxisAlignedCollision(Vector2D v1, Vector2D v2) {
        Vector2D temp, temp2, temp3, length, reflection;
        float projection;

        temp = nomalize(v1);
        temp2 = mult(v2, -1);

        projection = dotProduct(temp2, temp);

        length = mult(temp, projection);

        reflection = mult(length, 2);

        temp3 = add(mult(reflection, -1), temp2);

        return temp3;
    }

    public static float toRad(int degree) {

        return 0;
    }

    public static int toDeg(float radian) {

        return 0;
    }

    @Override
    public String toString() {
        /*Remove int*/
//        return "x: " + this.x + ",  y: " + this.y;
//        return "x: " + (int) this.x + ",  y: " + (int) this.y;
        return "x: " + Math.round(this.x * 100.0) / 100.0 + ",  y: " 
                + Math.round(this.y * 100.0) / 100.0;
    }
}
