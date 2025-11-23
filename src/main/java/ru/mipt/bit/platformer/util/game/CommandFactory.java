package ru.mipt.bit.platformer.util.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.commands.MoveCommand;
import ru.mipt.bit.platformer.util.commands.ShootCommand;
import ru.mipt.bit.platformer.util.commands.ToggleHealthBarCommand;
import ru.mipt.bit.platformer.util.graphics.HealthBarDecorator;
import ru.mipt.bit.platformer.util.logic.Bounds;
import ru.mipt.bit.platformer.util.logic.CollisionContext;
import ru.mipt.bit.platformer.util.models.EntityManager;
import ru.mipt.bit.platformer.util.models.MovableGameEntity;
import ru.mipt.bit.platformer.util.models.TankModel;

@Component
@DependsOn({"bounds", "gameInitializer"})
public class CommandFactory {
    private final EntityManager entityManager;
    private final Bounds bounds;

    @Autowired
    public CommandFactory(EntityManager entityManager, @Lazy Bounds bounds) {
        this.entityManager = entityManager;
        this.bounds = bounds;
    }

    public ShootCommand createShootCommand(TankModel tank) {
        return new ShootCommand(tank, entityManager, bounds);
    }

    public MoveCommand createMoveCommand(MovableGameEntity entity, Direction direction, CollisionContext collisionContext) {
        return new MoveCommand(entity, direction, bounds, collisionContext);
    }

    public ToggleHealthBarCommand createToggleHealthBarCommand(HealthBarDecorator[] decorators) {
        return new ToggleHealthBarCommand(decorators);
    }
}

