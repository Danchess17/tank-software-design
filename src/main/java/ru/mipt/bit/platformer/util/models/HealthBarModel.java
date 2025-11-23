package ru.mipt.bit.platformer.util.models;

import com.badlogic.gdx.math.GridPoint2;

public class HealthBarModel implements GameEntity {
    private boolean visible;
    private float healthPercentage;
    private final TankModel tankModel; // Делегируем позицию танка

    public HealthBarModel(float initialHealthPercentage, TankModel tankModel) {
        this.visible = false;
        this.healthPercentage = initialHealthPercentage;
        this.tankModel = tankModel;
    }

    @Override
    public GridPoint2 getPosition() {
        // Делегируем позицию танка, к которому привязан health bar
        return tankModel.getPosition();
    }

    @Override
    public boolean isCollidable() {
        // Health bar is a UI element, not a physical game object
        return false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public float getHealthPercentage() {
        return healthPercentage;
    }

    public void setHealthPercentage(float healthPercentage) {
        this.healthPercentage = healthPercentage;
    }
}

