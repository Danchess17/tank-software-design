package ru.mipt.bit.platformer.util.game;

import org.springframework.stereotype.Component;
import ru.mipt.bit.platformer.util.graphics.HealthBarView;
import ru.mipt.bit.platformer.util.graphics.ObstacleView;
import ru.mipt.bit.platformer.util.graphics.TankView;
import ru.mipt.bit.platformer.util.models.ObstacleModel;
import ru.mipt.bit.platformer.util.models.TankModel;

/**
 * Centralized registry for game entities and their views.
 * Provides access to player, enemies, obstacles models and views.
 */
@Component
public class EntityRegistry {
    // Player
    private TankModel playerModel;
    private TankView playerView;
    private HealthBarView playerHealthBarView;
    
    // Enemies
    private TankModel[] enemyModels;
    private TankView[] enemyViews;
    private HealthBarView[] enemyHealthBarViews;
    
    // Obstacles
    private ObstacleModel[] treeModels;
    private ObstacleView[] treeViews;
    
    /**
     * Initialize the registry with all entities and views.
     * Called from LevelInitializer after entities are created.
     */
    public void initialize(
            TankModel playerModel, 
            TankView playerView, 
            HealthBarView playerHealthBarView,
            TankModel[] enemyModels, 
            TankView[] enemyViews, 
            HealthBarView[] enemyHealthBarViews,
            ObstacleModel[] treeModels, 
            ObstacleView[] treeViews) {
        this.playerModel = playerModel;
        this.playerView = playerView;
        this.playerHealthBarView = playerHealthBarView;
        this.enemyModels = enemyModels;
        this.enemyViews = enemyViews;
        this.enemyHealthBarViews = enemyHealthBarViews;
        this.treeModels = treeModels;
        this.treeViews = treeViews;
    }
    
    // Player getters
    public TankModel getPlayerModel() { 
        return playerModel; 
    }
    
    public TankView getPlayerView() { 
        return playerView; 
    }
    
    public HealthBarView getPlayerHealthBarView() { 
        return playerHealthBarView; 
    }
    
    // Enemy getters
    public TankModel[] getEnemyModels() { 
        return enemyModels; 
    }
    
    public TankView[] getEnemyViews() { 
        return enemyViews; 
    }
    
    public HealthBarView[] getEnemyHealthBarViews() { 
        return enemyHealthBarViews; 
    }
    
    // Obstacle getters
    public ObstacleModel[] getTreeModels() { 
        return treeModels; 
    }
    
    public ObstacleView[] getTreeViews() { 
        return treeViews; 
    }
}

