package ru.mipt.bit.platformer.util.models;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.util.Direction;

import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;
import static com.badlogic.gdx.math.MathUtils.isEqual;

public class BulletModel implements MovableGameEntity {
    private static final float MOVEMENT_SPEED = 0.2f;
    private static final int DAMAGE = 10;
    
    private GridPoint2 position;
    private GridPoint2 destination;
    private float movementProgress = 1f;
    private float rotation;
    private Direction direction;
    private boolean destroyed = false;
    private TankModel shooter; // The tank that shot this bullet

    public BulletModel(GridPoint2 startPosition, Direction direction, TankModel shooter) {
        this.position = new GridPoint2(startPosition);
        this.direction = direction;
        this.rotation = direction.rotation;
        this.shooter = shooter;
        // Start at the next cell in the direction the tank is facing
        this.destination = direction.applyTo(startPosition);
        this.movementProgress = 0f;
    }

    public int getDamage() {
        return DAMAGE;
    }

    public TankModel getShooter() {
        return shooter;
    }
    
    public Direction getDirection() {
        return direction;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void destroy() {
        this.destroyed = true;
    }

    @Override
    public void tryMove(Direction direction, GameEntity ...entities) {
        // Bullets always move in their set direction
        GridPoint2 next = this.direction.applyTo(position);
        
        // Check for collisions
        for (GameEntity entity: entities) {
            if (entity.getPosition().equals(next)) {
                destroy();
                return;
            }
        }
        
        destination.set(next);
        movementProgress = 0f;
    }

    @Override
    public boolean isMovementCompleted() { 
        return isEqual(movementProgress, 1f) || destroyed; 
    }

    @Override
    public void update(float deltaTime) {
        if (destroyed) return;
        movementProgress = continueProgress(movementProgress, deltaTime, MOVEMENT_SPEED);
        if (isEqual(movementProgress, 1f) && !destroyed) {
            position.set(destination);
            // Continue moving in the same direction
            destination.set(direction.applyTo(position));
            movementProgress = 0f;
        }
    }

    @Override
    public float getRotation() { return rotation; }

    @Override
    public GridPoint2 getPosition() { return position; }

    @Override
    public GridPoint2 getDestination() { return destination; }

    @Override
    public float getMovementProgress() { return movementProgress; }
}

