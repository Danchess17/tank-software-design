package ru.mipt.bit.platformer.util.models;

import com.badlogic.gdx.math.GridPoint2;


public interface GameEntity {
    GridPoint2 getPosition();
    
    /**
     * Returns true if this entity can participate in collisions (blocks movement, can be hit, etc.).
     * UI elements like health bars should return false.
     */
    default boolean isCollidable() {
        return true;
    }
}