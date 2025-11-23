package ru.mipt.bit.platformer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import ru.mipt.bit.platformer.util.InputHandler;
import ru.mipt.bit.platformer.util.TileMovement;
import ru.mipt.bit.platformer.util.ai.AIController;
import ru.mipt.bit.platformer.util.game.GameInitializer;
import ru.mipt.bit.platformer.util.graphics.EntityRenderer;
import ru.mipt.bit.platformer.util.graphics.HealthBarRenderer;
import ru.mipt.bit.platformer.util.graphics.Renderer;
import ru.mipt.bit.platformer.util.logic.Bounds;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;

@Configuration
@ComponentScan(basePackages = "ru.mipt.bit.platformer.util")
public class GameConfig {

    @Bean
    public InputHandler inputHandler() {
        return new InputHandler();
    }

    /**
     * Creates the main SpriteBatch for rendering.
     * Singleton scope ensures only one Batch instance exists.
     */
    @Bean
    @Scope("singleton")
    public Batch batch() {
        return new SpriteBatch();
    }

    /**
     * Creates EntityRenderer that handles rendering of game entities.
     * Depends on Batch bean.
     */
    @Bean
    @Autowired
    public EntityRenderer entityRenderer(Batch batch) {
        return new EntityRenderer(batch);
    }

    /**
     * Creates MapRenderer for rendering the TiledMap level.
     * Depends on LevelInitializer (for TiledMap) and Batch.
     */
    @Bean
    @Autowired
    @DependsOn({"levelInitializer", "batch"})
    public MapRenderer mapRenderer(ru.mipt.bit.platformer.util.game.LevelInitializer levelInitializer, Batch batch) {
        TiledMap level = levelInitializer.getLevel();
        return createSingleLayerMapRenderer(level, batch);
    }

    /**
     * Creates the main Renderer that coordinates level and entity rendering.
     * Depends on Batch, EntityRenderer, and MapRenderer.
     */
    @Bean
    @Autowired
    @DependsOn({"batch", "entityRenderer", "mapRenderer"})
    public Renderer renderer(Batch batch, EntityRenderer entityRenderer, MapRenderer mapRenderer) {
        return new Renderer(batch, entityRenderer, mapRenderer);
    }

    @Bean
    @Autowired
    @DependsOn("levelInitializer")
    public Bounds bounds(ru.mipt.bit.platformer.util.game.LevelInitializer levelInitializer) {
        TiledMapTileLayer groundLayer = levelInitializer.getGroundLayer();
        Bounds bounds = new Bounds(groundLayer.getWidth(), groundLayer.getHeight());
        return bounds;
    }

    @Bean
    @Autowired
    @DependsOn("levelInitializer")
    public TileMovement tileMovement(ru.mipt.bit.platformer.util.game.LevelInitializer levelInitializer) {
        TiledMapTileLayer groundLayer = levelInitializer.getGroundLayer();
        TileMovement tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
        return tileMovement;
    }

    @Bean
    @Autowired
    @DependsOn({"levelInitializer", "bounds", "tileMovement"})
    public GameInitializer gameInitializer(
            ru.mipt.bit.platformer.util.game.LevelInitializer levelInitializer,
            ru.mipt.bit.platformer.util.models.EntityManager entityManager,
            InputHandler inputHandler,
            HealthBarRenderer healthBarRenderer,
            AIController aiController,
            ru.mipt.bit.platformer.util.game.ResourceManager resourceManager,
            Bounds bounds,
            TileMovement tileMovement) {
        GameInitializer gameInitializer = new GameInitializer(
            levelInitializer,
            entityManager,
            inputHandler,
            healthBarRenderer,
            aiController,
            resourceManager
        );
        gameInitializer.setBounds(bounds);
        gameInitializer.setTileMovement(tileMovement);
        return gameInitializer;
    }
}


