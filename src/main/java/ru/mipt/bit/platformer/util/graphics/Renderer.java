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

    public void renderAllEntities(TankView playerView,
                                 TankView[] enemyViews,
                                 HealthBarView playerHealthBarView,
                                 HealthBarView[] enemyHealthBarViews,
                                 ObstacleView[] treeViews,
                                 List<BulletView> bulletViews) {
        // Collect all renderables (entities)
        List<Renderable> allRenderables = new ArrayList<>();
        allRenderables.add(playerView);
        allRenderables.addAll(Arrays.asList(enemyViews));
        allRenderables.addAll(Arrays.asList(treeViews));
        allRenderables.addAll(bulletViews);
        
        // Render all entities first
        Renderable[] renderablesArray = allRenderables.toArray(new Renderable[allRenderables.size()]);
        entityRenderer.render(renderablesArray);
        
        // Render health bars on top of all entities (if any are enabled)
        List<HealthBarView> healthBarViewsList = new ArrayList<>();
        healthBarViewsList.add(playerHealthBarView);
        healthBarViewsList.addAll(Arrays.asList(enemyHealthBarViews));
        
        boolean shouldRenderHealthBars = healthBarViewsList.stream()
                .anyMatch(view -> view.getModel().isVisible());
        if (shouldRenderHealthBars) {
            batch.begin();
            for (HealthBarView healthBarView : healthBarViewsList) {
                healthBarView.render(batch);
            }
            batch.end();
        }
    }
}

