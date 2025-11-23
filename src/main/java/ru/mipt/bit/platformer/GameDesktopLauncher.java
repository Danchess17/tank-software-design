package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.util.game.*;
import ru.mipt.bit.platformer.util.graphics.*;
import ru.mipt.bit.platformer.util.logic.*;
import ru.mipt.bit.platformer.util.models.*;

import java.util.Arrays;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameDesktopLauncher implements ApplicationListener {

    private static ApplicationContext applicationContext;

    private Batch batch;
    private Renderer renderer;
    private EntityRenderer entityRenderer;

    private GameInitializer initializer;
    private CommandProcessor commandProcessor;
    private GameUpdater gameUpdater;
    private BulletManager bulletManager;

    private TiledMap level;
    private MapRenderer levelRenderer;

    @Override
    public void create() {
        // Initialize Spring context
        if (applicationContext == null) {
            applicationContext = new AnnotationConfigApplicationContext(GameConfig.class);
        }
        
        batch = new SpriteBatch();
        entityRenderer = new EntityRenderer(batch);
        
        // Get components from Spring context
        initializer = applicationContext.getBean(GameInitializer.class);
        commandProcessor = applicationContext.getBean(CommandProcessor.class);
        gameUpdater = applicationContext.getBean(GameUpdater.class);
        bulletManager = applicationContext.getBean(BulletManager.class);
        
        level = initializer.getLevel();
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        renderer = new Renderer(batch, entityRenderer, levelRenderer);
    }

    @Override
    public void render() {
        clearScreen();
        float deltaTime = Gdx.graphics.getDeltaTime();
        CollisionContext collisionContext = buildCollisionContext();

        commandProcessor.processInputCommands(collisionContext);
        commandProcessor.processAICommands(collisionContext);
        gameUpdater.updateEntities(deltaTime);
        bulletManager.update(deltaTime, collisionContext);
        
        renderer.renderLevel();
        renderer.renderAllEntities(
            initializer.getPlayerView(),
            initializer.getEnemyViews(),
            initializer.getPlayerHealthBarView(),
            initializer.getEnemyHealthBarViews(),
            initializer.getTreeViews(),
            bulletManager.getBulletViews()
        );
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    private CollisionContext buildCollisionContext() {
        GameEntity[] staticEntities = initializer.getTreeModels();
        MovableGameEntity[] movers = initializer.getEntityManager().getMovableEntities();
        return new CollisionContext(Arrays.asList(staticEntities), Arrays.asList(movers));
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
        if (initializer != null) initializer.dispose();
        if (batch != null) batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
