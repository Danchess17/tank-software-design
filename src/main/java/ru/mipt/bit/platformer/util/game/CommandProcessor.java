package ru.mipt.bit.platformer.util.game;

import ru.mipt.bit.platformer.util.InputHandler;
import ru.mipt.bit.platformer.util.ai.AIController;
import ru.mipt.bit.platformer.util.commands.MoveCommand;
import ru.mipt.bit.platformer.util.commands.ShootCommand;
import ru.mipt.bit.platformer.util.commands.ToggleHealthBarCommand;
import ru.mipt.bit.platformer.util.graphics.HealthBarDecorator;
import ru.mipt.bit.platformer.util.logic.Bounds;
import ru.mipt.bit.platformer.util.logic.CollisionContext;
import ru.mipt.bit.platformer.util.models.EntityManager;
import ru.mipt.bit.platformer.util.models.TankModel;
import ru.mipt.bit.platformer.util.Direction;

public class CommandProcessor {
    private final InputHandler inputHandler;
    private final AIController aiController;
    private final EntityManager entityManager;
    private final Bounds bounds;
    private final TankModel playerModel;
    private final TankModel[] enemyModels;
    private final HealthBarDecorator playerHealthDecorator;
    private final HealthBarDecorator[] enemyHealthDecorators;

    public CommandProcessor(InputHandler inputHandler, AIController aiController,
                           EntityManager entityManager, Bounds bounds,
                           TankModel playerModel, TankModel[] enemyModels,
                           HealthBarDecorator playerHealthDecorator,
                           HealthBarDecorator[] enemyHealthDecorators) {
        this.inputHandler = inputHandler;
        this.aiController = aiController;
        this.entityManager = entityManager;
        this.bounds = bounds;
        this.playerModel = playerModel;
        this.enemyModels = enemyModels;
        this.playerHealthDecorator = playerHealthDecorator;
        this.enemyHealthDecorators = enemyHealthDecorators;
    }

    public void processInputCommands(CollisionContext collisionContext) {
        handleHealthBarToggle();
        handlePlayerCommands(collisionContext);
    }

    public void processAICommands(CollisionContext collisionContext) {
        for (TankModel enemyModel : enemyModels) {
            if (!enemyModel.isMovementCompleted() || !enemyModel.isAlive()) continue;

            if (aiController.shouldShoot()) new ShootCommand(enemyModel, entityManager, bounds).execute();
            else {
                Direction dir = aiController.chooseDirection();
                new MoveCommand(enemyModel, dir, bounds, collisionContext).execute();
            }
        }
    }

    private void handleHealthBarToggle() {
        if (inputHandler.isLKeyJustPressed()) {
            HealthBarDecorator[] allDecorators = new HealthBarDecorator[1 + enemyHealthDecorators.length];
            allDecorators[0] = playerHealthDecorator;
            System.arraycopy(enemyHealthDecorators, 0, allDecorators, 1, enemyHealthDecorators.length);
            new ToggleHealthBarCommand(allDecorators).execute();
        }
    }

    private void handlePlayerCommands(CollisionContext collisionContext) {
        if (!playerModel.isMovementCompleted() || !playerModel.isAlive()) return;

        if (inputHandler.isSpaceKeyJustPressed()) new ShootCommand(playerModel, entityManager, bounds).execute();
        else {
            Direction direction = inputHandler.chooseDirection();
            new MoveCommand(playerModel, direction, bounds, collisionContext).execute();
        }

    }
}

