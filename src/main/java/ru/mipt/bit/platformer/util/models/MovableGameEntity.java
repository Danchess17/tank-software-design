package ru.mipt.bit.platformer.util.models;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.util.Direction;

public interface MovableGameEntity extends GameEntity {
    GridPoint2 getDestination();
    float getMovementProgress();
    float getRotation();
    void update(float deltaTime);
    boolean isMovementCompleted();
    void tryMove(Direction direction, GameEntity ...entities);
}