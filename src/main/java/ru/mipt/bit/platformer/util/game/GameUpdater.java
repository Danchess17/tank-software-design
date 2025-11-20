package ru.mipt.bit.platformer.util.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mipt.bit.platformer.util.graphics.TankView;

@Service
public class GameUpdater {
    private final GameInitializer gameInitializer;

    @Autowired
    public GameUpdater(GameInitializer gameInitializer) {
        this.gameInitializer = gameInitializer;
    }

    public void updateEntities(float deltaTime) {
        TankView playerView = gameInitializer.getPlayerView();
        TankView[] enemyViews = gameInitializer.getEnemyViews();
        var tileMovement = gameInitializer.getTileMovement();
        
        playerView.update(deltaTime, tileMovement);
        for (TankView enemyView : enemyViews) {
            enemyView.update(deltaTime, tileMovement);
        }
    }
}

