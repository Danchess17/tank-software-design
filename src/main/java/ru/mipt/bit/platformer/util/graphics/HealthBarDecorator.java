package ru.mipt.bit.platformer.util.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.util.models.TankModel;

public class HealthBarDecorator implements Renderable {
    private final Renderable decorated;
    private final TankModel tankModel;
    private final HealthBarRenderer healthBarRenderer;
    private boolean showHealthBar;

    public HealthBarDecorator(Renderable decorated, TankModel tankModel, HealthBarRenderer healthBarRenderer) {
        this.decorated = decorated;
        this.tankModel = tankModel;
        this.healthBarRenderer = healthBarRenderer;
        this.showHealthBar = false;
    }

    public void setShowHealthBar(boolean show) {
        this.showHealthBar = show;
    }

    public boolean isShowHealthBar() {
        return showHealthBar;
    }

    @Override
    public void render(Batch batch) {
        decorated.render(batch);
        // Health bar is rendered separately after all entities to appear on top
    }
    
    public void renderHealthBar(Batch batch) {
        if (showHealthBar) {
            healthBarRenderer.render(batch, decorated.getBounds(), tankModel.getHealthPercentage());
        }
    }

    @Override
    public Rectangle getBounds() {
        return decorated.getBounds();
    }

    @Override
    public Texture getTexture() {
        return decorated.getTexture();
    }
}





