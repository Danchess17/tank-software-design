package ru.mipt.bit.platformer.util.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import ru.mipt.bit.platformer.util.InputHandler;
import ru.mipt.bit.platformer.util.TileMovement;
import ru.mipt.bit.platformer.util.ai.AIController;
import ru.mipt.bit.platformer.util.graphics.*;
import ru.mipt.bit.platformer.util.logic.Bounds;
import ru.mipt.bit.platformer.util.logic.BulletCollisionHandler;
import ru.mipt.bit.platformer.util.models.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameInitializer implements InitializingBean {
    private final TiledMap level;
    private final TiledMapTileLayer groundLayer;
    private TileMovement tileMovement;
    private final EntityManager entityManager;
    private Bounds bounds;
    private final MapLoader loader;

    // Player
    private Texture playerTexture;
    private TankModel playerModel;
    private TankView playerView;
    private HealthBarDecorator playerHealthDecorator;

    // Enemies
    private Texture enemyTexture;
    private TankModel[] enemyModels;
    private TankView[] enemyViews;
    private HealthBarDecorator[] enemyHealthDecorators;

    // Obstacles
    private Texture treeTexture;
    private ObstacleModel[] treeModels;
    private ObstacleView[] treeViews;

    // Bullets
    private Texture bulletTexture;
    private List<BulletModel> bullets;
    private final BulletCollisionHandler bulletCollisionHandler;

    // Services
    private final InputHandler inputHandler;
    private final HealthBarRenderer healthBarRenderer;
    private final AIController aiController;
    private final TextureFactory textureFactory;
    private final EntityFactory entityFactory;
    private final LevelLoader levelLoader;

    @Autowired
    public GameInitializer(InputHandler inputHandler, 
                          HealthBarRenderer healthBarRenderer, 
                          AIController aiController,
                          BulletCollisionHandler bulletCollisionHandler,
                          MapLoader mapLoader,
                          EntityManager entityManager,
                          TextureFactory textureFactory,
                          EntityFactory entityFactory,
                          LevelLoader levelLoader) {
        this.inputHandler = inputHandler;
        this.healthBarRenderer = healthBarRenderer;
        this.aiController = aiController;
        this.bulletCollisionHandler = bulletCollisionHandler;
        this.loader = mapLoader;
        this.textureFactory = textureFactory;
        this.entityFactory = entityFactory;
        this.levelLoader = levelLoader;
        
        // Initialize level using LevelLoader
        level = levelLoader.loadLevel();
        groundLayer = levelLoader.getGroundLayer(level);

        // Initialize entities using MapLoader
        EntityManager loadedEntityManager = mapLoader.load("entities_map.txt");
        // Copy entities to the injected EntityManager using varargs
        GameEntity[] entities = loadedEntityManager.getEntities();
        entityManager.initialize(entities);
        this.entityManager = entityManager;
        
        // Bounds and TileMovement will be injected via setters from @Bean methods
    }
    
    @Override
    public void afterPropertiesSet() {
        // Create Bounds and TileMovement if they weren't injected
        if (bounds == null) {
        bounds = new Bounds(groundLayer.getWidth(), groundLayer.getHeight());
        }
        if (tileMovement == null) {
            tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
        }

        // Initialize player using factories
        playerTexture = textureFactory.createPlayerTexture();
        playerModel = entityManager.getTanks().get(0);
        playerView = entityFactory.createTankView(playerModel, playerTexture);
        playerHealthDecorator = entityFactory.createHealthBarDecorator(playerView, playerModel);

        // Initialize obstacles using factories
        treeTexture = textureFactory.createTreeTexture();
        treeModels = entityManager.getObstacles().toArray(ObstacleModel[]::new);
        treeViews = entityFactory.createObstacleViews(treeModels, treeTexture, groundLayer);

        // Initialize enemies using factories
        enemyTexture = textureFactory.createEnemyTexture();
        enemyModels = entityManager.getTanks().stream().skip(1).toArray(TankModel[]::new);
        enemyViews = Arrays.stream(enemyModels)
                .map(enemyModel -> entityFactory.createTankView(enemyModel, enemyTexture))
                .toArray(TankView[]::new);
        enemyHealthDecorators = entityFactory.createEnemyHealthDecorators(enemyViews, enemyModels);

        // Initialize bullets using TextureFactory
        bulletTexture = textureFactory.createBulletTexture();
        bullets = new ArrayList<>();
    }

    // Getters
    public TiledMap getLevel() { return level; }
    public TiledMapTileLayer getGroundLayer() { return groundLayer; }
    public TileMovement getTileMovement() { return tileMovement; }
    public EntityManager getEntityManager() { return entityManager; }
    public Bounds getBounds() { return bounds; }
    
    // Setters for injection from @Bean methods
    @Autowired(required = false)
    public void setTileMovement(TileMovement tileMovement) {
        this.tileMovement = tileMovement;
    }
    
    @Autowired(required = false)
    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }
    public TankModel getPlayerModel() { return playerModel; }
    public TankView getPlayerView() { return playerView; }
    public HealthBarDecorator getPlayerHealthDecorator() { return playerHealthDecorator; }
    public TankModel[] getEnemyModels() { return enemyModels; }
    public TankView[] getEnemyViews() { return enemyViews; }
    public HealthBarDecorator[] getEnemyHealthDecorators() { return enemyHealthDecorators; }
    public ObstacleModel[] getTreeModels() { return treeModels; }
    public ObstacleView[] getTreeViews() { return treeViews; }
    public Texture getBulletTexture() { return bulletTexture; }
    public List<BulletModel> getBullets() { return bullets; }
    public BulletCollisionHandler getBulletCollisionHandler() { return bulletCollisionHandler; }
    public InputHandler getInputHandler() { return inputHandler; }
    public HealthBarRenderer getHealthBarRenderer() { return healthBarRenderer; }
    public AIController getAiController() { return aiController; }

    // Cleanup
    public void dispose() {
        treeTexture.dispose();
        playerTexture.dispose();
        if (enemyTexture != null) enemyTexture.dispose();
        if (bulletTexture != null) bulletTexture.dispose();
        if (healthBarRenderer != null) healthBarRenderer.dispose();
        level.dispose();
    }
}

