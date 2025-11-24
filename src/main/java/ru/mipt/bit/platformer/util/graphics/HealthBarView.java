package ru.mipt.bit.platformer.util.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.models.HealthBarModel;
import ru.mipt.bit.platformer.util.models.TankModel;

public class HealthBarView implements Renderable {
    private final HealthBarModel model;
    private final HealthBarRenderer renderer;
    private final Rectangle entityBounds; // Reference to the entity's bounds (e.g., TankView)
    private final TankModel tankModel; // Reference to update health percentage

    public HealthBarView(HealthBarModel model, HealthBarRenderer renderer, Rectangle entityBounds, TankModel tankModel) {
        this.model = model;
        this.renderer = renderer;
        this.entityBounds = entityBounds;
        this.tankModel = tankModel;
    }

    @Override
    public void render(Batch batch) {
        if (model.isVisible()) {
            // Update health percentage from TankModel
            model.setHealthPercentage(tankModel.getHealthPercentage());
            renderer.render(batch, entityBounds, model.getHealthPercentage());
        }
    }

    @Override
    public Rectangle getBounds() {
        return entityBounds;
    }

    @Override
    public Texture getTexture() {
        // Health bar doesn't have a texture, return null or a placeholder
        return null;
    }

    public HealthBarModel getModel() {
        return model;
    }
}

