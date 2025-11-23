package ru.mipt.bit.platformer.util.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mipt.bit.platformer.util.graphics.HealthBarDecorator;
import ru.mipt.bit.platformer.util.graphics.HealthBarRenderer;
import ru.mipt.bit.platformer.util.graphics.ObstacleView;
import ru.mipt.bit.platformer.util.graphics.TankView;
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

    public HealthBarDecorator createHealthBarDecorator(TankView tankView, TankModel tankModel) {
        return new HealthBarDecorator(tankView, tankModel, healthBarRenderer);
    }

    public ObstacleView[] createObstacleViews(ObstacleModel[] obstacleModels, Texture texture, TiledMapTileLayer groundLayer) {
        return Arrays.stream(obstacleModels)
                .map(obstacleModel -> new ObstacleView(obstacleModel, texture, groundLayer))
                .toArray(ObstacleView[]::new);
    }

    public HealthBarDecorator[] createEnemyHealthDecorators(TankView[] enemyViews, TankModel[] enemyModels) {
        HealthBarDecorator[] decorators = new HealthBarDecorator[enemyViews.length];
        for (int i = 0; i < enemyViews.length; i++) {
            decorators[i] = createHealthBarDecorator(enemyViews[i], enemyModels[i]);
        }
        return decorators;
    }
}

