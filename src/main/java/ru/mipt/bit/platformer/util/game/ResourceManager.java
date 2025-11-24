package ru.mipt.bit.platformer.util.game;

import com.badlogic.gdx.graphics.Texture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mipt.bit.platformer.util.graphics.HealthBarRenderer;

/**
 * Manages game resources (textures) and handles their disposal.
 * Responsible for creating and caching textures, and cleaning up resources.
 */
@Component
public class ResourceManager {
    private final TextureFactory textureFactory;
    private final HealthBarRenderer healthBarRenderer;
    
    private Texture playerTexture;
    private Texture enemyTexture;
    private Texture treeTexture;
    private Texture bulletTexture;
    
    @Autowired
    public ResourceManager(TextureFactory textureFactory, HealthBarRenderer healthBarRenderer) {
        this.textureFactory = textureFactory;
        this.healthBarRenderer = healthBarRenderer;
    }
    
    /**
     * Initialize all textures. Should be called once during game initialization.
     */
    public void initialize() {
        playerTexture = textureFactory.createPlayerTexture();
        enemyTexture = textureFactory.createEnemyTexture();
        treeTexture = textureFactory.createTreeTexture();
        bulletTexture = textureFactory.createBulletTexture();
    }
    
    // Texture getters
    public Texture getPlayerTexture() { 
        return playerTexture; 
    }
    
    public Texture getEnemyTexture() { 
        return enemyTexture; 
    }
    
    public Texture getTreeTexture() { 
        return treeTexture; 
    }
    
    public Texture getBulletTexture() { 
        return bulletTexture; 
    }
    
    /**
     * Dispose all resources. Should be called when game is shutting down.
     */
    public void dispose() {
        if (treeTexture != null) treeTexture.dispose();
        if (playerTexture != null) playerTexture.dispose();
        if (enemyTexture != null) enemyTexture.dispose();
        if (bulletTexture != null) bulletTexture.dispose();
        if (healthBarRenderer != null) healthBarRenderer.dispose();
    }
}

