package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.util.game.*;
import ru.mipt.bit.platformer.util.graphics.*;
import ru.mipt.bit.platformer.util.logic.*;
import ru.mipt.bit.platformer.util.models.*;

import java.util.Arrays;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private static ApplicationContext applicationContext;

    private Batch batch;
    private Renderer renderer;

    private GameInitializer initializer;
    private EntityRegistry entityRegistry;
    private EntityManager entityManager;
    private CommandProcessor commandProcessor;
    private GameUpdater gameUpdater;
    private BulletManager bulletManager;

    @Override
    public void create() {
        // Initialize Spring context
        if (applicationContext == null) {
            applicationContext = new AnnotationConfigApplicationContext(GameConfig.class);
        }
        
        // Get all components from Spring context
        batch = applicationContext.getBean(Batch.class);
        renderer = applicationContext.getBean(Renderer.class);
        
        initializer = applicationContext.getBean(GameInitializer.class);
        entityRegistry = applicationContext.getBean(EntityRegistry.class);
        entityManager = applicationContext.getBean(EntityManager.class);
        commandProcessor = applicationContext.getBean(CommandProcessor.class);
        gameUpdater = applicationContext.getBean(GameUpdater.class);
        bulletManager = applicationContext.getBean(BulletManager.class);
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
            entityRegistry.getPlayerView(),
            entityRegistry.getEnemyViews(),
            entityRegistry.getPlayerHealthBarView(),
            entityRegistry.getEnemyHealthBarViews(),
            entityRegistry.getTreeViews(),
            bulletManager.getBulletViews()
        );
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    private CollisionContext buildCollisionContext() {
        GameEntity[] staticEntities = entityRegistry.getTreeModels();
        MovableGameEntity[] movers = entityManager.getMovableEntities();
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
