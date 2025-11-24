package ru.mipt.bit.platformer.util.models;

import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.util.Direction;

import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;
import static com.badlogic.gdx.math.MathUtils.isEqual;

import java.util.Random;

public class TankModel implements MovableGameEntity {
    private static final float MOVEMENT_SPEED = 0.4f;
    private static final Random RANDOM = new Random();
    private static final int MIN_HEALTH = 80;
    private static final int MAX_HEALTH = 100;
    
    private GridPoint2 position;
    private GridPoint2 destination;
    private float movementProgress = 1f;
    private float rotation;
    private int health;
    private int maxHealth;

    public TankModel(GridPoint2 startPosition) {
        this(startPosition, RANDOM.nextInt(MAX_HEALTH - MIN_HEALTH + 1) + MIN_HEALTH);
    }

    public TankModel(GridPoint2 startPosition, int initialHealth) {
        this.position = new GridPoint2(startPosition);
        this.destination = new GridPoint2(startPosition);
        this.rotation = 0f;
        this.maxHealth = initialHealth;
        this.health = initialHealth;
    }
    
    public int getHealth() {
        return health;
    }
    
    public int getMaxHealth() {
        return maxHealth;
    }
    
    public float getHealthPercentage() {
        return maxHealth > 0 ? (float) health / maxHealth : 0f;
    }
    
    public void takeDamage(int damage) {
        health = Math.max(0, health - damage);
    }
    
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public void tryMove(Direction direction, GameEntity ...entities) {
        GridPoint2 next = direction.applyTo(position);
        rotation = direction.rotation;
        
        for (GameEntity entity: entities) {
            if (entity.getPosition().equals(next)) return;
        }
        
        destination.set(next);
        movementProgress = 0f;
    }

    @Override
    public boolean isMovementCompleted() { return isEqual(movementProgress, 1f); }

    @Override
    public void update(float deltaTime) {
        movementProgress = continueProgress(movementProgress, deltaTime, MOVEMENT_SPEED);
        if (isMovementCompleted()) position.set(destination);
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