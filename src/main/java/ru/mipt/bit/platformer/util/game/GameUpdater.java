package ru.mipt.bit.platformer.util.game;

import ru.mipt.bit.platformer.util.TileMovement;
import ru.mipt.bit.platformer.util.graphics.TankView;

public class GameUpdater {
    private final TileMovement tileMovement;
    private final TankView playerView;
    private final TankView[] enemyViews;

    public GameUpdater(TileMovement tileMovement, TankView playerView, TankView[] enemyViews) {
        this.tileMovement = tileMovement;
        this.playerView = playerView;
        this.enemyViews = enemyViews;
    }

    public void updateEntities(float deltaTime) {
        playerView.update(deltaTime, tileMovement);
        for (TankView enemyView : enemyViews) {
            enemyView.update(deltaTime, tileMovement);
        }
    }
}

