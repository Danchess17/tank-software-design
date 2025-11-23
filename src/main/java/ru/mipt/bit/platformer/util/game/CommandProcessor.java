package ru.mipt.bit.platformer.util.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mipt.bit.platformer.util.InputHandler;
import ru.mipt.bit.platformer.util.ai.AIController;
import ru.mipt.bit.platformer.util.graphics.HealthBarView;
import ru.mipt.bit.platformer.util.logic.CollisionContext;
import ru.mipt.bit.platformer.util.models.TankModel;
import ru.mipt.bit.platformer.util.Direction;

@Service
public class CommandProcessor {
    private final InputHandler inputHandler;
    private final AIController aiController;
    private final GameInitializer gameInitializer;
    private final CommandFactory commandFactory;

    @Autowired
    public CommandProcessor(InputHandler inputHandler, 
                           AIController aiController,
                           GameInitializer gameInitializer,
                           CommandFactory commandFactory) {
        this.inputHandler = inputHandler;
        this.aiController = aiController;
        this.gameInitializer = gameInitializer;
        this.commandFactory = commandFactory;
    }

    public void processInputCommands(CollisionContext collisionContext) {
        handleHealthBarToggle();
        handlePlayerCommands(collisionContext);
    }

    public void processAICommands(CollisionContext collisionContext) {
        TankModel[] enemyModels = gameInitializer.getEnemyModels();
        
        for (TankModel enemyModel : enemyModels) {
            if (!enemyModel.isMovementCompleted() || !enemyModel.isAlive()) continue;

            if (aiController.shouldShoot()) {
                commandFactory.createShootCommand(enemyModel).execute();
            } else {
                Direction dir = aiController.chooseDirection();
                commandFactory.createMoveCommand(enemyModel, dir, collisionContext).execute();
            }
        }
    }

    private void handleHealthBarToggle() {
        if (inputHandler.isLKeyJustPressed()) {
            HealthBarView playerHealthBarView = gameInitializer.getPlayerHealthBarView();
            HealthBarView[] enemyHealthBarViews = gameInitializer.getEnemyHealthBarViews();
            HealthBarView[] allHealthBarViews = new HealthBarView[1 + enemyHealthBarViews.length];
            allHealthBarViews[0] = playerHealthBarView;
            System.arraycopy(enemyHealthBarViews, 0, allHealthBarViews, 1, enemyHealthBarViews.length);
            commandFactory.createToggleHealthBarCommand(allHealthBarViews).execute();
        }
    }

    private void handlePlayerCommands(CollisionContext collisionContext) {
        TankModel playerModel = gameInitializer.getPlayerModel();
        
        if (!playerModel.isMovementCompleted() || !playerModel.isAlive()) return;

        if (inputHandler.isSpaceKeyJustPressed()) {
            commandFactory.createShootCommand(playerModel).execute();
        } else {
            Direction direction = inputHandler.chooseDirection();
            if (direction != null) {
                commandFactory.createMoveCommand(playerModel, direction, collisionContext).execute();
            }
        }
    }
}

