package ru.mipt.bit.platformer.util.graphics;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.util.models.ObstacleModel;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.graphics.g2d.Batch;


import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class ObstacleView {
    private final TextureRegion textureRegion;
    private final Rectangle rectangle;


    public ObstacleView(ObstacleModel obstacleModel, Texture texture, TiledMapTileLayer groundLayer) {
        this.textureRegion = new TextureRegion(texture);
        this.rectangle = createBoundingRectangle(textureRegion);
        moveRectangleAtTileCenter(groundLayer, rectangle, obstacleModel.getPosition());
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, textureRegion, rectangle, 0);
    }
    
    public TextureRegion getRegion() { return textureRegion; }
    public Rectangle getRectangle() { return rectangle; }
}