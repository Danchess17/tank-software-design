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
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.util.*;
import ru.mipt.bit.platformer.util.graphics.ObstacleView;
import ru.mipt.bit.platformer.util.graphics.TankView;
import ru.mipt.bit.platformer.util.models.ObstacleModel;
import ru.mipt.bit.platformer.util.models.TankModel;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

import org.lwjgl.system.CallbackI.V;


public class GameDesktopLauncher implements ApplicationListener {

    private Batch batch;

    private TiledMap level;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;

    private Texture playerTexture;
    private TankModel playerModel;
    private TankView playerView;

    private Texture treeTexture;
    private ObstacleModel treeModel;
    private ObstacleView treeView;

    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        // load level tiles
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        // Texture decodes an image file and loads it into GPU memory, it represents a native resource
        playerTexture = new Texture("images/tank_blue.png");
        playerModel = new TankModel(new GridPoint2(1, 1));
        playerView = new TankView(playerModel, playerTexture);


        treeTexture = new Texture("images/greenTree.png");
        treeModel = new ObstacleModel(new GridPoint2(1, 3));
        treeView = new ObstacleView(treeModel, treeTexture, groundLayer);
        
    }

    @Override
    public void render() {
        clearScreen();

        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        // check if the player has finished the previous movement
        if (playerModel.isMovementCompleted()) {
            Direction direction = null;

            if (Gdx.input.isKeyPressed(UP) || Gdx.input.isKeyPressed(W)) direction = Direction.UP;
            else if (Gdx.input.isKeyPressed(LEFT) || Gdx.input.isKeyPressed(A)) direction = Direction.LEFT;
            else if (Gdx.input.isKeyPressed(DOWN) || Gdx.input.isKeyPressed(S)) direction = Direction.DOWN;
            else if (Gdx.input.isKeyPressed(RIGHT) || Gdx.input.isKeyPressed(D)) direction = Direction.RIGHT;

            // if direction is selected — attempt to move
            if (direction != null) playerModel.tryMove(direction, treeModel.getPosition());
            
        }

        playerView.update(deltaTime, tileMovement);

        // render each tile of the level
        levelRenderer.render();

        // start recording all drawing commands
        batch.begin();

        // render player
        playerView.render(batch);

        // render tree obstacle
        treeView.render(batch);

        // submit all drawing requests
        batch.end();
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
        level.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}