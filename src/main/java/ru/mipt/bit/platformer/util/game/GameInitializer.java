package ru.mipt.bit.platformer.util.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.util.InputHandler;
import ru.mipt.bit.platformer.util.TileMovement;
import ru.mipt.bit.platformer.util.ai.AIController;
import ru.mipt.bit.platformer.util.ai.RandomAIController;
import ru.mipt.bit.platformer.util.graphics.*;
import ru.mipt.bit.platformer.util.logic.Bounds;
import ru.mipt.bit.platformer.util.logic.BulletCollisionHandler;
import ru.mipt.bit.platformer.util.models.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameInitializer {
    private final TiledMap level;
    private final TiledMapTileLayer groundLayer;
    private final TileMovement tileMovement;
    private final EntityManager entityManager;
    private final Bounds bounds;
    private final MapLoader loader;

    // Player
    private final Texture playerTexture;
    private final TankModel playerModel;
    private final TankView playerView;
    private final HealthBarDecorator playerHealthDecorator;

    // Enemies
    private final Texture enemyTexture;
    private final TankModel[] enemyModels;
    private final TankView[] enemyViews;
    private final HealthBarDecorator[] enemyHealthDecorators;

    // Obstacles
    private final Texture treeTexture;
    private final ObstacleModel[] treeModels;
    private final ObstacleView[] treeViews;

    // Bullets
    private final Texture bulletTexture;
    private final List<BulletModel> bullets;
    private final BulletCollisionHandler bulletCollisionHandler;

    // Services
    private final InputHandler inputHandler;
    private final HealthBarRenderer healthBarRenderer;
    private final AIController aiController;

    public GameInitializer(InputHandler inputHandler, 
                          HealthBarRenderer healthBarRenderer, 
                          AIController aiController) {
        this.inputHandler = inputHandler;
        this.healthBarRenderer = healthBarRenderer;
        this.aiController = aiController;
        
        // Initialize level
        level = new TmxMapLoader().load("level.tmx");
        groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        // Initialize entities
        loader = new RandomGeneratorLoader(10, 8, 7, 3);
        entityManager = loader.load("entities_map.txt");
        bounds = new Bounds(groundLayer.getWidth(), groundLayer.getHeight());

        // Initialize player
        playerTexture = new Texture("images/tank_blue.png");
        playerModel = entityManager.getTanks().get(0);
        playerView = new TankView(playerModel, playerTexture);
        playerHealthDecorator = new HealthBarDecorator(playerView, playerModel, healthBarRenderer);

        // Initialize obstacles
        treeTexture = new Texture("images/greenTree.png");
        treeModels = entityManager.getObstacles().toArray(ObstacleModel[]::new);
        treeViews = Arrays.stream(treeModels)
                .map(treeModel -> new ObstacleView(treeModel, treeTexture, groundLayer))
                .toArray(ObstacleView[]::new);

        // Initialize enemies
        enemyTexture = new Texture("images/tank_blue.png");
        enemyModels = entityManager.getTanks().stream().skip(1).toArray(TankModel[]::new);
        enemyViews = Arrays.stream(enemyModels)
                .map(enemyModel -> new TankView(enemyModel, enemyTexture))
                .toArray(TankView[]::new);
        enemyHealthDecorators = new HealthBarDecorator[enemyViews.length];
        for (int i = 0; i < enemyViews.length; i++) {
            enemyHealthDecorators[i] = new HealthBarDecorator(enemyViews[i], enemyModels[i], healthBarRenderer);
        }

        // Initialize bullets
        bulletTexture = createBulletTexture();
        bullets = new ArrayList<>();
        bulletCollisionHandler = new BulletCollisionHandler(entityManager, bounds, treeModels, bullets);
    }

    private Texture createBulletTexture() {
        int size = 24;
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0);
        pixmap.fill();
        Color orange = new Color(1.0f, 0.5f, 0.0f, 1.0f);
        pixmap.setColor(orange);
        pixmap.fillCircle(size / 2, size / 2, size / 2 - 2);
        Color lightOrange = new Color(1.0f, 0.7f, 0.2f, 1.0f);
        pixmap.setColor(lightOrange);
        pixmap.fillCircle(size / 2, size / 2, size / 3);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    // Getters
    public TiledMap getLevel() { return level; }
    public TiledMapTileLayer getGroundLayer() { return groundLayer; }
    public TileMovement getTileMovement() { return tileMovement; }
    public EntityManager getEntityManager() { return entityManager; }
    public Bounds getBounds() { return bounds; }
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

