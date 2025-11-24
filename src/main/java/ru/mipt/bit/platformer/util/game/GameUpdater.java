package ru.mipt.bit.platformer.util.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mipt.bit.platformer.util.TileMovement;
import ru.mipt.bit.platformer.util.graphics.TankView;

@Service
public class GameUpdater {
    private final EntityRegistry entityRegistry;
    private final TileMovement tileMovement;

    @Autowired
    public GameUpdater(EntityRegistry entityRegistry, TileMovement tileMovement) {
        this.entityRegistry = entityRegistry;
        this.tileMovement = tileMovement;
    }

    public void updateEntities(float deltaTime) {
        TankView playerView = entityRegistry.getPlayerView();
        TankView[] enemyViews = entityRegistry.getEnemyViews();
        
        playerView.update(deltaTime, tileMovement);
        for (TankView enemyView : enemyViews) {
            enemyView.update(deltaTime, tileMovement);
        }
    }
}

