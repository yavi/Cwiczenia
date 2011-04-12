/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package playground;

import java.util.Random;

/**
 *
 * @author yavi
 */
public class Creature {
    static final int MAX_GENES = 255;
    boolean isAlive;
    int age;
    short foodLevel;
    String name;
    int x,y;

    int geneField[] = new int[MAX_GENES];

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Creature() {
        isAlive = true;
        age = 0;
        foodLevel = (short) (Math.random() * 100);
    }

    public Creature(Creature f1, Creature f2)
    {

    }
}
