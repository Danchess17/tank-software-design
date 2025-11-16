package ru.mipt.bit.platformer.util.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Renderer {
    private final Batch batch;
    private final EntityRenderer entityRenderer;
    private final MapRenderer levelRenderer;

    public Renderer(Batch batch, EntityRenderer entityRenderer, MapRenderer levelRenderer) {
        this.batch = batch;
        this.entityRenderer = entityRenderer;
        this.levelRenderer = levelRenderer;
    }

    public void renderLevel() {
        levelRenderer.render();
    }

    public void renderAllEntities(HealthBarDecorator playerHealthDecorator,
                                 HealthBarDecorator[] enemyHealthDecorators,
                                 ObstacleView[] treeViews,
                                 List<BulletView> bulletViews) {
        List<Renderable> allRenderables = new ArrayList<>();
        allRenderables.add(playerHealthDecorator);
        allRenderables.addAll(Arrays.asList(enemyHealthDecorators));
        allRenderables.addAll(Arrays.asList(treeViews));
        allRenderables.addAll(bulletViews);
        
        Renderable[] renderablesArray = allRenderables.toArray(new Renderable[allRenderables.size()]);
        entityRenderer.render(renderablesArray);
    }

    public void renderHealthBars(HealthBarDecorator playerHealthDecorator,
                                HealthBarDecorator[] enemyHealthDecorators) {
        List<HealthBarDecorator> allDecorators = new ArrayList<>();
        allDecorators.add(playerHealthDecorator);
        allDecorators.addAll(Arrays.asList(enemyHealthDecorators));
        
        if (allDecorators.isEmpty()) return;

        boolean shouldRender = allDecorators.stream().anyMatch(HealthBarDecorator::isShowHealthBar);
        if (!shouldRender) return;

        batch.begin();
        for (HealthBarDecorator decorator : allDecorators) {
            decorator.renderHealthBar(batch);
        }
        batch.end();
    }
}

