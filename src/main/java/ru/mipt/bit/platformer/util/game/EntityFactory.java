package ru.mipt.bit.platformer.util.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mipt.bit.platformer.util.graphics.HealthBarRenderer;
import ru.mipt.bit.platformer.util.graphics.HealthBarView;
import ru.mipt.bit.platformer.util.graphics.ObstacleView;
import ru.mipt.bit.platformer.util.graphics.TankView;
import ru.mipt.bit.platformer.util.models.HealthBarModel;
import ru.mipt.bit.platformer.util.models.ObstacleModel;
import ru.mipt.bit.platformer.util.models.TankModel;

import java.util.Arrays;

@Component
public class EntityFactory {
    private final HealthBarRenderer healthBarRenderer;

    @Autowired
    public EntityFactory(HealthBarRenderer healthBarRenderer) {
        this.healthBarRenderer = healthBarRenderer;
    }

    public TankView createTankView(TankModel tankModel, Texture texture) {
        return new TankView(tankModel, texture);
    }

    public HealthBarView createHealthBarView(TankView tankView, TankModel tankModel) {
        HealthBarModel healthBarModel = new HealthBarModel(tankModel.getHealthPercentage(), tankModel);
        return new HealthBarView(healthBarModel, healthBarRenderer, tankView.getBounds(), tankModel);
    }

    public ObstacleView[] createObstacleViews(ObstacleModel[] obstacleModels, Texture texture, TiledMapTileLayer groundLayer) {
        return Arrays.stream(obstacleModels)
                .map(obstacleModel -> new ObstacleView(obstacleModel, texture, groundLayer))
                .toArray(ObstacleView[]::new);
    }

    public HealthBarView[] createEnemyHealthBarViews(TankView[] enemyViews, TankModel[] enemyModels) {
        HealthBarView[] healthBarViews = new HealthBarView[enemyViews.length];
        for (int i = 0; i < enemyViews.length; i++) {
            healthBarViews[i] = createHealthBarView(enemyViews[i], enemyModels[i]);
        }
        return healthBarViews;
    }
}


