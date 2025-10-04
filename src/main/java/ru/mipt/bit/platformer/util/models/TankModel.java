package ru.mipt.bit.platformer.util.models;

import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;
import static com.badlogic.gdx.math.MathUtils.isEqual;

public class TankModel {
    private static final float MOVEMENT_SPEED = 0.4f;
    private GridPoint2 position;
    private GridPoint2 destination;
    private float movementProgress = 1f;
    private float rotation;

    public TankModel(GridPoint2 startPosition) {
        this.position = new GridPoint2(startPosition);
        this.destination = new GridPoint2(startPosition);
        this.rotation = 0f;
    }

    public boolean isMovementCompleted() { return isEqual(movementProgress, 1f); }

    public void tryMove(Direction direction, GridPoint2 obstacle) {
        GridPoint2 next = direction.applyTo(position);
        rotation = direction.rotation;
        
        // check if there's an obstacle
        if (obstacle.equals(next)) return;

        destination.set(next);
        movementProgress = 0f;
    }

    public void update(float deltaTime, TileMovement tileMovement) {
        movementProgress = continueProgress(movementProgress, deltaTime, MOVEMENT_SPEED);
        if (isMovementCompleted()) position.set(destination);
    }

    public float getRotation() { return rotation; }
    public GridPoint2 getPosition() { return position; }
    public GridPoint2 getDestination() { return destination; }
    public float getMovementProgress() { return movementProgress; }

}