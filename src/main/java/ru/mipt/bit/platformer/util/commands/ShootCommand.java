package ru.mipt.bit.platformer.util.commands;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.util.Direction;
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
        if (!tank.isMovementCompleted()) return;

        Direction shootDirection = getDirectionFromRotation(tank.getRotation());
        
        GridPoint2 bulletStart = shootDirection.applyTo(tank.getPosition());
        
        if (!bounds.contains(bulletStart)) return;

        if (entityManager.getEntityAt(bulletStart).isPresent()) return; 

        BulletModel bullet = new BulletModel(bulletStart, shootDirection, tank);
        entityManager.addEntity(bullet);
    }

    private Direction getDirectionFromRotation(float rotation) {
        if (Math.abs(rotation - 90f) < 0.1f) {
            return Direction.UP;
        } else if (Math.abs(rotation - (-90f)) < 0.1f) {
            return Direction.DOWN;
        } else if (Math.abs(rotation - (-180f)) < 0.1f || Math.abs(rotation - 180f) < 0.1f) {
            return Direction.LEFT;
        } else {
            return Direction.RIGHT;
        }
    }
}

