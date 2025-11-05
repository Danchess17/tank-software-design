package ru.mipt.bit.platformer.util.commands;

import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.logic.Bounds;
import ru.mipt.bit.platformer.util.logic.CollisionContext;
import ru.mipt.bit.platformer.util.models.GameEntity;
import ru.mipt.bit.platformer.util.models.MovableGameEntity;

public class MoveCommand implements Command {
    private final MovableGameEntity entity;
    private final Direction direction;
    private final Bounds bounds;
    private final CollisionContext collisionContext;

    public MoveCommand(MovableGameEntity entity, Direction direction, Bounds bounds, CollisionContext collisionContext) {
        this.entity = entity;
        this.direction = direction;
        this.bounds = bounds;
        this.collisionContext = collisionContext;
    }

    @Override
    public void execute() {
        if (direction == null || !entity.isMovementCompleted()) return;
        GridPoint2 next = direction.applyTo(entity.getPosition());
        if (!bounds.contains(next)) return;
        GameEntity[] blocks = collisionContext.buildBlockingEntities(entity);
        entity.tryMove(direction, blocks);
    }
}

