package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.util.*;
import ru.mipt.bit.platformer.util.graphics.*;
import ru.mipt.bit.platformer.util.models.EntityManager;
import ru.mipt.bit.platformer.util.models.LevelFromFileLoader;
import ru.mipt.bit.platformer.util.models.ObstacleModel;
import ru.mipt.bit.platformer.util.models.RandomGeneratorLoader;
import ru.mipt.bit.platformer.util.models.TankModel;
import ru.mipt.bit.platformer.util.models.GameEntity;
import ru.mipt.bit.platformer.util.models.MovableGameEntity;
import ru.mipt.bit.platformer.util.logic.Bounds;
import ru.mipt.bit.platformer.util.logic.CollisionContext;
import ru.mipt.bit.platformer.util.commands.MoveCommand;
import ru.mipt.bit.platformer.util.commands.ShootCommand;
import ru.mipt.bit.platformer.util.commands.ToggleHealthBarCommand;
import ru.mipt.bit.platformer.util.ai.RandomAIController;
import ru.mipt.bit.platformer.util.models.BulletModel;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameDesktopLauncher implements ApplicationListener {

    private Batch batch;
    private EntityRenderer entityRenderer;

    private TiledMap level;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;

    private Texture playerTexture;
    private TankModel playerModel;
    private TankView playerView;
    private HealthBarDecorator playerHealthDecorator;

    private Texture enemyTexture;
    private TankModel[] enemyModels;
    private TankView[] enemyViews;
    private HealthBarDecorator[] enemyHealthDecorators;
    private RandomAIController aiController;

    private Texture treeTexture;
    private ObstacleModel[] treeModels;
    private ObstacleView[] treeViews;

    private Texture bulletTexture;
    private List<BulletModel> bullets;
    private List<BulletView> bulletViews;

    private InputHandler inputHandler;
    private HealthBarRenderer healthBarRenderer;

    private RandomGeneratorLoader loader;
    //private LevelFromFileLoader loader;
    private EntityManager entityManager;
    private Bounds bounds;

    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    private Texture createBulletTexture() {
        int size = 24; // Bullet size - much smaller than tank (tank is ~128px)
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        
        // Fill with transparent
        pixmap.setColor(0, 0, 0, 0);
        pixmap.fill();
        
        // Draw orange circle
        Color orange = new Color(1.0f, 0.5f, 0.0f, 1.0f); // Bright orange
        pixmap.setColor(orange);
        pixmap.fillCircle(size / 2, size / 2, size / 2 - 2);
        
        // Add a brighter center for depth
        Color lightOrange = new Color(1.0f, 0.7f, 0.2f, 1.0f);
        pixmap.setColor(lightOrange);
        pixmap.fillCircle(size / 2, size / 2, size / 3);
        
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        entityRenderer = new EntityRenderer(batch);

        // load level tiles
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        inputHandler = new InputHandler();
        aiController = new RandomAIController();
        healthBarRenderer = new HealthBarRenderer();

        loader = new RandomGeneratorLoader(10, 8, 7, 3);
        //loader  = new LevelFromFileLoader();
        entityManager = loader.load("entities_map.txt");
        
        bounds = new Bounds(groundLayer.getWidth(), groundLayer.getHeight());

        // Texture decodes an image file and loads it into GPU memory, it represents a native resource
        playerTexture = new Texture("images/tank_blue.png");
        playerModel = entityManager.getTanks().get(0);
        playerView = new TankView(playerModel, playerTexture);
        playerHealthDecorator = new HealthBarDecorator(playerView, playerModel, healthBarRenderer);


        treeTexture = new Texture("images/greenTree.png");
        treeModels = entityManager.getObstacles().toArray(ObstacleModel[]::new);
        treeViews = Arrays.stream(treeModels)
        .map(treeModel -> new ObstacleView(treeModel, treeTexture, groundLayer))
        .toArray(ObstacleView[]::new);

        // setup enemies (all tanks except the first)
        enemyTexture = new Texture("images/tank_blue.png");
        enemyModels = entityManager.getTanks().stream().skip(1).toArray(TankModel[]::new);
        enemyViews = Arrays.stream(enemyModels)
        .map(enemyModel -> new TankView(enemyModel, enemyTexture))
        .toArray(TankView[]::new);
        enemyHealthDecorators = new HealthBarDecorator[enemyViews.length];
        for (int i = 0; i < enemyViews.length; i++) {
            enemyHealthDecorators[i] = new HealthBarDecorator(enemyViews[i], enemyModels[i], healthBarRenderer);
        }

        // setup bullets - create orange circle texture
        bulletTexture = createBulletTexture();
        bullets = new ArrayList<>();
        bulletViews = new ArrayList<>();

    }

    @Override
    public void render() {
        clearScreen();

        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        // build collision context
        GameEntity[] staticEntities = treeModels;
        MovableGameEntity[] movers = entityManager.getMovableEntities();
        CollisionContext collisionContext = new CollisionContext(Arrays.asList(staticEntities), Arrays.asList(movers));

        // toggle health bar command
        if (inputHandler.isLKeyJustPressed()) {
            HealthBarDecorator[] allDecorators = new HealthBarDecorator[1 + enemyHealthDecorators.length];
            allDecorators[0] = playerHealthDecorator;
            System.arraycopy(enemyHealthDecorators, 0, allDecorators, 1, enemyHealthDecorators.length);
            new ToggleHealthBarCommand(allDecorators).execute();
        }

        // player commands
        if (playerModel.isMovementCompleted() && playerModel.isAlive()) {
            // Check for shooting first
            if (inputHandler.isSpaceKeyJustPressed()) {
                new ShootCommand(playerModel, entityManager, bounds).execute();
            } else {
                Direction direction = inputHandler.chooseDirection();
                if (direction != null) {
                    new MoveCommand(playerModel, direction, bounds, collisionContext).execute();
                }
            }
        }

        // AI commands
        for (TankModel enemyModel : enemyModels) {
            if (enemyModel.isMovementCompleted() && enemyModel.isAlive()) {
                if (aiController.shouldShoot()) {
                    new ShootCommand(enemyModel, entityManager, bounds).execute();
                } else {
                    Direction dir = aiController.chooseDirection();
                    new MoveCommand(enemyModel, dir, bounds, collisionContext).execute();
                }
            }
        }

        playerView.update(deltaTime, tileMovement);
        for (TankView enemyView : enemyViews) {
            enemyView.update(deltaTime, tileMovement);
        }

        // Update bullets and handle collisions
        updateBullets(deltaTime, collisionContext);

        // render each tile of the level
        levelRenderer.render();

        // Render all entities (tanks and obstacles)
        Renderable[] renderables = new Renderable[treeViews.length + 1 + enemyHealthDecorators.length];
        renderables[0] = playerHealthDecorator;
        System.arraycopy(enemyHealthDecorators, 0, renderables, 1, enemyHealthDecorators.length);
        System.arraycopy(treeViews, 0, renderables, 1 + enemyHealthDecorators.length, treeViews.length);
        entityRenderer.render(renderables);
        
        // Render bullets
        if (!bulletViews.isEmpty()) {
            Renderable[] bulletRenderables = bulletViews.toArray(new Renderable[bulletViews.size()]);
            entityRenderer.render(bulletRenderables);
        }
        
        // Render health bars on top of all entities (after obstacles)
        if (playerHealthDecorator.isShowHealthBar() || 
            Arrays.stream(enemyHealthDecorators).anyMatch(d -> d.isShowHealthBar())) {
            batch.begin();
            playerHealthDecorator.renderHealthBar(batch);
            for (HealthBarDecorator enemyDecorator : enemyHealthDecorators) {
                enemyDecorator.renderHealthBar(batch);
            }
            batch.end();
        }
    }

    private void updateBullets(float deltaTime, CollisionContext collisionContext) {
        // Get all bullets from entity manager
        List<BulletModel> allBullets = entityManager.getEntitiesByType(BulletModel.class);
        
        // Remove destroyed bullets from entity manager
        List<BulletModel> toRemove = new ArrayList<>();
        for (BulletModel bullet : allBullets) {
            if (bullet.isDestroyed()) {
                toRemove.add(bullet);
            }
        }
        for (BulletModel bullet : toRemove) {
            entityManager.removeEntity(bullet);
        }
        
        // Update active bullets list
        bullets.clear();
        for (BulletModel bullet : allBullets) {
            if (!bullet.isDestroyed()) {
                bullets.add(bullet);
            }
        }
        
        // Update bullets and check collisions before rendering
        for (BulletModel bullet : bullets) {
            // Update bullet movement
            bullet.update(deltaTime);
            
            // Check if bullet needs to continue moving
            if (bullet.isMovementCompleted() && !bullet.isDestroyed()) {
                // Use tryMove to check collisions for next cell
                GameEntity[] blockingEntities = collisionContext.buildBlockingEntities(bullet);
                bullet.tryMove(bullet.getDirection(), blockingEntities);
            }
            
            checkBulletCollisions(bullet, collisionContext);
        }
        
        // Recreate views for all active bullets (simpler approach)
        bulletViews.clear();
        for (BulletModel bullet : bullets) {
            if (!bullet.isDestroyed()) {
                BulletView view = new BulletView(bullet, bulletTexture);
                view.update(deltaTime, tileMovement);
                bulletViews.add(view);
            }
        }
    }

    private void checkBulletCollisions(BulletModel bullet, CollisionContext collisionContext) {
        if (bullet.isDestroyed()) return;
        
        com.badlogic.gdx.math.GridPoint2 bulletPos = bullet.getPosition();
        com.badlogic.gdx.math.GridPoint2 bulletDest = bullet.getDestination();
        
        // Check collision at current position and destination
        com.badlogic.gdx.math.GridPoint2[] positionsToCheck = {bulletPos, bulletDest};
        
        for (com.badlogic.gdx.math.GridPoint2 checkPos : positionsToCheck) {
            // Check collision with tanks
            for (TankModel tank : entityManager.getTanks()) {
                if (tank == bullet.getShooter()) continue; // Don't hit the shooter
                if (!tank.isAlive()) continue;
                
                if (tank.getPosition().equals(checkPos) || tank.getDestination().equals(checkPos)) {
                    tank.takeDamage(bullet.getDamage());
                    bullet.destroy();
                    return;
                }
            }
            
            // Check collision with obstacles
            for (ObstacleModel obstacle : treeModels) {
                if (obstacle.getPosition().equals(checkPos)) {
                    bullet.destroy();
                    return;
                }
            }
            
            // Check bounds
            if (!bounds.contains(checkPos)) {
                bullet.destroy();
                return;
            }
            
            // Check collision with other bullets
            for (BulletModel otherBullet : bullets) {
                if (otherBullet == bullet) continue;
                if (otherBullet.isDestroyed()) continue;
                if (otherBullet.getPosition().equals(checkPos) || otherBullet.getDestination().equals(checkPos)) {
                    bullet.destroy();
                    otherBullet.destroy();
                    return;
                }
            }
        }
    }

    private ru.mipt.bit.platformer.util.Direction getDirectionFromRotation(float rotation) {
        // Match rotation to direction
        if (Math.abs(rotation - 90f) < 0.1f) {
            return ru.mipt.bit.platformer.util.Direction.UP;
        } else if (Math.abs(rotation - (-90f)) < 0.1f) {
            return ru.mipt.bit.platformer.util.Direction.DOWN;
        } else if (Math.abs(rotation - (-180f)) < 0.1f || Math.abs(rotation - 180f) < 0.1f) {
            return ru.mipt.bit.platformer.util.Direction.LEFT;
        } else {
            return ru.mipt.bit.platformer.util.Direction.RIGHT;
        }
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        treeTexture.dispose();
        playerTexture.dispose();
        if (enemyTexture != null) enemyTexture.dispose();
        if (bulletTexture != null) bulletTexture.dispose();
        if (healthBarRenderer != null) healthBarRenderer.dispose();
        batch.dispose();
        level.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}