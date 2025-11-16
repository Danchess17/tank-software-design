package ru.mipt.bit.platformer.util.commands;

import ru.mipt.bit.platformer.util.models.TankModel;
import ru.mipt.bit.platformer.util.models.BulletModel;
import ru.mipt.bit.platformer.util.models.EntityManager;
import ru.mipt.bit.platformer.util.logic.Bounds;

public class ShootCommand implements Command {
    private final TankModel tank;
    private final EntityManager entityManager;
    private final Bounds bounds;

    public ShootCommand(TankModel tank, EntityManager entityManager, Bounds bounds) {
        this.tank = tank;
        this.entityManager = entityManager;
        this.bounds = bounds;
    }

    @Override
    public void execute() {
        // Only shoot if tank is not moving
        if (!tank.isMovementCompleted()) {
            return;
        }

        // Get the direction the tank is facing based on its rotation
        ru.mipt.bit.platformer.util.Direction shootDirection = getDirectionFromRotation(tank.getRotation());
        
        // Calculate bullet start position (next cell in the direction tank is facing)
        com.badlogic.gdx.math.GridPoint2 bulletStart = shootDirection.applyTo(tank.getPosition());
        
        // Check if bullet start position is within bounds
        if (!bounds.contains(bulletStart)) {
            return;
        }

        // Check if there's already something at the bullet start position
        // (edge case: enemy tank standing right next to us)
        if (entityManager.getEntityAt(bulletStart).isPresent()) {
            return; // Can't shoot if something is blocking the start position
        }

        // Create and register the bullet
        BulletModel bullet = new BulletModel(bulletStart, shootDirection, tank);
        entityManager.addEntity(bullet);
    }

    private ru.mipt.bit.platformer.util.Direction getDirectionFromRotation(float rotation) {
        // Match rotation to direction
        if (Math.abs(rotation - 90f) < 0.1f) {
            return ru.mipt.bit.platformer.util.Direction.UP;
        } else if (Math.abs(rotation - (-90f)) < 0.1f) {
            return ru.mipt.bit.platformer.util.Direction.DOWN;
        } else if (Math.abs(rotation - (-180f)) < 0.1f || Math.abs(rotation - 180f) < 0.1f) {
            return ru.mipt.bit.platformer.util.Direction.LEFT;
        } else {
            return ru.mipt.bit.platformer.util.Direction.RIGHT;
        }
    }
}

