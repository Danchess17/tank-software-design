package ru.mipt.bit.platformer.util.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class HealthBarRenderer {
    private static final float BAR_WIDTH = 40f;
    private static final float BAR_HEIGHT = 4f;
    private static final float BAR_OFFSET_Y = 30f;
    private static final Color BACKGROUND_COLOR = new Color(0.2f, 0.2f, 0.2f, 0.8f);
    private static final Color HEALTH_COLOR = new Color(0.95f, 0.6f, 0.0f, 0.8f); // Orange
    private static final Color DAMAGED_COLOR = new Color(0.8f, 0.2f, 0.2f, 0.8f); // Red
    
    private final Texture whiteTexture;
    
    public HealthBarRenderer() {
        Pixmap whitePixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        whitePixmap.setColor(Color.WHITE);
        whitePixmap.fill();
        whiteTexture = new Texture(whitePixmap);
        whitePixmap.dispose();
    }
    
    public void render(Batch batch, Rectangle entityBounds, float healthPercentage) {
        float centerX = entityBounds.x + entityBounds.width / 2f;
        float barX = centerX - BAR_WIDTH / 2f;
        float barY = entityBounds.y + entityBounds.height + BAR_OFFSET_Y;
        
        Color oldColor = new Color(batch.getColor());
        
        // Background
        batch.setColor(BACKGROUND_COLOR);
        batch.draw(whiteTexture, barX, barY, BAR_WIDTH, BAR_HEIGHT);
        
        // Health bar
        float healthWidth = BAR_WIDTH * healthPercentage;
        if (healthWidth > 0) {
            Color healthColor = healthPercentage > 0.5f ? HEALTH_COLOR : DAMAGED_COLOR;
            batch.setColor(healthColor);
            batch.draw(whiteTexture, barX, barY, healthWidth, BAR_HEIGHT);
        }
        
        // Always restore to white (default) to ensure textures render correctly
        batch.setColor(Color.WHITE);
    }
    
    public void dispose() {
        whiteTexture.dispose();
    }
}


