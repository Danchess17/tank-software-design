package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
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
import java.util.Arrays;

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

    private Texture treeTexture;
    private ObstacleModel[] treeModels;
    private ObstacleView[] treeViews;


    private InputHandler inputHandler;

    // private RandomGeneratorLoader loader;
    private LevelFromFileLoader loader;

    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
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

        // loader = new RandomGeneratorLoader(10, 8, 7);
        loader  = new LevelFromFileLoader();
        EntityManager entityManager = loader.load("entities_map.txt");

        // Texture decodes an image file and loads it into GPU memory, it represents a native resource
        playerTexture = new Texture("images/tank_blue.png");
        playerModel = entityManager.getTanks().get(0);
        playerView = new TankView(playerModel, playerTexture);


        treeTexture = new Texture("images/greenTree.png");
        treeModels = entityManager.getObstacles().toArray(ObstacleModel[]::new);
        treeViews = Arrays.stream(treeModels)
        .map(treeModel -> new ObstacleView(treeModel, treeTexture, groundLayer))
        .toArray(ObstacleView[]::new);

    }

    @Override
    public void render() {
        clearScreen();

        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        // check if the player has finished the previous movement
        if (playerModel.isMovementCompleted()) {
            Direction direction = inputHandler.chooseDirection();
            if (direction != null) playerModel.tryMove(direction, treeModels);
            
        }

        playerView.update(deltaTime, tileMovement);

        // render each tile of the level
        levelRenderer.render();

        Renderable[] renderables = new Renderable[treeViews.length + 1];
        renderables[0] = playerView;
        System.arraycopy(treeViews, 0, renderables, 1, treeViews.length);
        entityRenderer.render(renderables);
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