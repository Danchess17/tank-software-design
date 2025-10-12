package ru.mipt.bit.platformer.util.graphics;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.util.models.ObstacleModel;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.graphics.g2d.Batch;


import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class ObstacleView implements Renderable {
    private final Texture texture;
    private final TextureRegion textureRegion;
    private final Rectangle rectangle;


    public ObstacleView(ObstacleModel obstacleModel, Texture texture, TiledMapTileLayer groundLayer) {
        this.texture = texture;
        this.textureRegion = new TextureRegion(texture);
        this.rectangle = createBoundingRectangle(textureRegion);
        moveRectangleAtTileCenter(groundLayer, rectangle, obstacleModel.getPosition());
    }

    @Override
    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, textureRegion, rectangle, 0);
    }
    
    @Override
    public Rectangle getBounds() { return rectangle; }

    @Override
    public Texture getTexture() { return texture; }
}