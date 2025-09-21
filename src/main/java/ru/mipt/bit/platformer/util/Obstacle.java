package ru.mipt.bit.platformer.util;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class Obstacle {
    private final TextureRegion textureRegion;
    private final Rectangle rectangle;
    private final GridPoint2 coordinates;

    public Obstacle(Texture texture, GridPoint2 coordinates, TiledMapTileLayer groundLayer) {
        this.textureRegion = new TextureRegion(texture);
        this.rectangle = createBoundingRectangle(textureRegion);
        this.coordinates = new GridPoint2(coordinates);
        moveRectangleAtTileCenter(groundLayer, rectangle, coordinates);
    }
    
    public TextureRegion getRegion() { return textureRegion; }
    public Rectangle getRectangle() { return rectangle; }
    public GridPoint2 getCoordinates() { return coordinates; }

}