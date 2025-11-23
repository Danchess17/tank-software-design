package ru.mipt.bit.platformer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import ru.mipt.bit.platformer.util.InputHandler;
import ru.mipt.bit.platformer.util.TileMovement;
import ru.mipt.bit.platformer.util.ai.AIController;
import ru.mipt.bit.platformer.util.game.GameInitializer;
import ru.mipt.bit.platformer.util.graphics.HealthBarRenderer;
import ru.mipt.bit.platformer.util.logic.Bounds;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;

@Configuration
@ComponentScan(basePackages = "ru.mipt.bit.platformer.util")
public class GameConfig {

    @Bean
    public InputHandler inputHandler() {
        return new InputHandler();
    }

    @Bean
    @Autowired
    @DependsOn("gameInitializer")
    public Bounds bounds(GameInitializer gameInitializer) {
        TiledMapTileLayer groundLayer = gameInitializer.getGroundLayer();
        Bounds bounds = new Bounds(groundLayer.getWidth(), groundLayer.getHeight());
        gameInitializer.setBounds(bounds);
        return bounds;
    }

    @Bean
    @Autowired
    @DependsOn("gameInitializer")
    public TileMovement tileMovement(GameInitializer gameInitializer) {
        TiledMapTileLayer groundLayer = gameInitializer.getGroundLayer();
        TileMovement tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
        gameInitializer.setTileMovement(tileMovement);
        return tileMovement;
    }

    @Bean
    @Autowired
    public GameInitializer gameInitializer(InputHandler inputHandler, 
                                          HealthBarRenderer healthBarRenderer, 
                                          AIController aiController,
                                          ru.mipt.bit.platformer.util.models.MapLoader mapLoader,
                                          ru.mipt.bit.platformer.util.models.EntityManager entityManager,
                                          ru.mipt.bit.platformer.util.game.TextureFactory textureFactory,
                                          ru.mipt.bit.platformer.util.game.EntityFactory entityFactory,
                                          ru.mipt.bit.platformer.util.game.LevelLoader levelLoader) {
        return new GameInitializer(inputHandler, healthBarRenderer, aiController, 
                                 mapLoader, entityManager,
                                 textureFactory, entityFactory, levelLoader);
    }
}


