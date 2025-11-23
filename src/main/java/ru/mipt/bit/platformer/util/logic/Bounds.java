package ru.mipt.bit.platformer.util.logic;

import com.badlogic.gdx.math.GridPoint2;

public class Bounds {
    private final int width;
    private final int height;

    public Bounds(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean contains(GridPoint2 point) {
        return point.x >= 0 && point.y >= 0 && point.x < width && point.y < height;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}

















