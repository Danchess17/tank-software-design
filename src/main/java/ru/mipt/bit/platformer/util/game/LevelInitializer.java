package ru.mipt.bit.platformer.util.game;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mipt.bit.platformer.util.graphics.HealthBarView;
import ru.mipt.bit.platformer.util.graphics.ObstacleView;
import ru.mipt.bit.platformer.util.graphics.TankView;
import ru.mipt.bit.platformer.util.models.EntityManager;
import ru.mipt.bit.platformer.util.models.GameEntity;
import ru.mipt.bit.platformer.util.models.ObstacleModel;
import ru.mipt.bit.platformer.util.models.TankModel;

import java.util.Arrays;

/**
 * Initializes the game level: loads map, creates entities and their views.
 * Coordinates initialization of level, entities, and views.
 */
@Component
public class LevelInitializer implements InitializingBean {
    private final LevelLoader levelLoader;
    private final ru.mipt.bit.platformer.util.models.MapLoader mapLoader;
    private final EntityManager entityManager;
    private final EntityFactory entityFactory;
    private final ResourceManager resourceManager;
    private final EntityRegistry entityRegistry;
    
    private TiledMap level;
    private TiledMapTileLayer groundLayer;
    
    @Autowired
    public LevelInitializer(LevelLoader levelLoader,
                           ru.mipt.bit.platformer.util.models.MapLoader mapLoader,
                           EntityManager entityManager,
                           EntityFactory entityFactory,
                           ResourceManager resourceManager,
                           EntityRegistry entityRegistry) {
        this.levelLoader = levelLoader;
        this.mapLoader = mapLoader;
        this.entityManager = entityManager;
        this.entityFactory = entityFactory;
        this.resourceManager = resourceManager;
        this.entityRegistry = entityRegistry;
    }
    
    @Override
    public void afterPropertiesSet() {
        // Initialize resources first
        resourceManager.initialize();
        
        // Load level
        level = levelLoader.loadLevel();
        groundLayer = levelLoader.getGroundLayer(level);
        
        // Load entities from map
        ru.mipt.bit.platformer.util.models.EntityManager loadedEntityManager = mapLoader.load("entities_map.txt");
        GameEntity[] entities = loadedEntityManager.getEntities();
        entityManager.initialize(entities);
        
        // Initialize player
        TankModel playerModel = entityManager.getTanks().get(0);
        TankView playerView = entityFactory.createTankView(playerModel, resourceManager.getPlayerTexture());
        HealthBarView playerHealthBarView = entityFactory.createHealthBarView(playerView, playerModel);
        
        // Initialize obstacles
        ObstacleModel[] treeModels = entityManager.getObstacles().toArray(ObstacleModel[]::new);
        ObstacleView[] treeViews = entityFactory.createObstacleViews(
            treeModels, 
            resourceManager.getTreeTexture(), 
            groundLayer
        );
        
        // Initialize enemies
        TankModel[] enemyModels = entityManager.getTanks().stream()
            .skip(1)
            .toArray(TankModel[]::new);
        TankView[] enemyViews = Arrays.stream(enemyModels)
            .map(enemyModel -> entityFactory.createTankView(enemyModel, resourceManager.getEnemyTexture()))
            .toArray(TankView[]::new);
        HealthBarView[] enemyHealthBarViews = entityFactory.createEnemyHealthBarViews(enemyViews, enemyModels);
        
        // Register all entities and views
        entityRegistry.initialize(
            playerModel, playerView, playerHealthBarView,
            enemyModels, enemyViews, enemyHealthBarViews,
            treeModels, treeViews
        );
    }
    
    public TiledMap getLevel() { 
        return level; 
    }
    
    public TiledMapTileLayer getGroundLayer() { 
        return groundLayer; 
    }
}

