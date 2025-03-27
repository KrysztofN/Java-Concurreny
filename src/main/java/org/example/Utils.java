package org.example;

public class Utils {

    public int getRandomInRange(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }
}
