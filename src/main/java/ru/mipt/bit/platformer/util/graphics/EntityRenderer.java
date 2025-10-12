package ru.mipt.bit.platformer.util.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;

public class EntityRenderer {
    private final Batch batch;

    public EntityRenderer(Batch batch) {
        this.batch = batch;
    }

    public Batch getBatch() { return batch; }

    public void render(Renderable ...renderables) {
        batch.begin();

        for (Renderable renderable : renderables) {
            renderable.render(batch);
        }

        batch.end();
    }
}