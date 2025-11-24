package ru.mipt.bit.platformer.util.game;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import ru.mipt.bit.platformer.util.InputHandler;
import ru.mipt.bit.platformer.util.TileMovement;
import ru.mipt.bit.platformer.util.ai.AIController;
import ru.mipt.bit.platformer.util.graphics.HealthBarRenderer;
import ru.mipt.bit.platformer.util.logic.Bounds;
import ru.mipt.bit.platformer.util.models.EntityManager;

/**
 * Simplified GameInitializer that coordinates initialization and provides access
 * to level-related objects (TiledMap, Bounds, TileMovement).
 * Entity initialization is handled by LevelInitializer.
 * Entity/View access is provided by EntityRegistry.
 * Resource management is handled by ResourceManager.
 */
public class GameInitializer implements InitializingBean {
    private final LevelInitializer levelInitializer;
    private final EntityManager entityManager;
    private final InputHandler inputHandler;
    private final HealthBarRenderer healthBarRenderer;
    private final AIController aiController;
    private final ResourceManager resourceManager;
    
    private TileMovement tileMovement;
    private Bounds bounds;

    @Autowired
    public GameInitializer(LevelInitializer levelInitializer,
                          EntityManager entityManager,
                          InputHandler inputHandler,
                          HealthBarRenderer healthBarRenderer,
                          AIController aiController,
                          ResourceManager resourceManager) {
        this.levelInitializer = levelInitializer;
        this.entityManager = entityManager;
        this.inputHandler = inputHandler;
        this.healthBarRenderer = healthBarRenderer;
        this.aiController = aiController;
        this.resourceManager = resourceManager;
    }
    
    @Override
    public void afterPropertiesSet() {
        // Bounds and TileMovement will be injected via setters from @Bean methods
        // If not injected, they will be created here
        if (bounds == null) {
            TiledMapTileLayer groundLayer = levelInitializer.getGroundLayer();
            bounds = new Bounds(groundLayer.getWidth(), groundLayer.getHeight());
        }
        if (tileMovement == null) {
            TiledMapTileLayer groundLayer = levelInitializer.getGroundLayer();
            tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
        }
    }

    // Level-related getters (for backward compatibility and GameConfig)
    public TiledMap getLevel() { 
        return levelInitializer.getLevel(); 
    }
    
    public TiledMapTileLayer getGroundLayer() { 
        return levelInitializer.getGroundLayer(); 
    }
    
    public TileMovement getTileMovement() { 
        return tileMovement; 
    }
    
    public EntityManager getEntityManager() { 
        return entityManager; 
    }
    
    public Bounds getBounds() { 
        return bounds; 
    }
    
    // Setters for injection from @Bean methods
    @Autowired(required = false)
    public void setTileMovement(TileMovement tileMovement) {
        this.tileMovement = tileMovement;
    }
    
    @Autowired(required = false)
    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }
    
    // Service getters (for backward compatibility)
    public InputHandler getInputHandler() { 
        return inputHandler; 
    }
    
    public HealthBarRenderer getHealthBarRenderer() { 
        return healthBarRenderer; 
    }
    
    public AIController getAiController() { 
        return aiController; 
    }

    // Cleanup
    public void dispose() {
        resourceManager.dispose();
        levelInitializer.getLevel().dispose();
    }
}

