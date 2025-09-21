package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;
import static com.badlogic.gdx.math.MathUtils.isEqual;

public class Tank {
    private TextureRegion textureRegion;
    private Rectangle rectangle;
    private GridPoint2 coordinates;
    private GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation;

    public Tank(Texture texture, GridPoint2 startPosition) {
        this.textureRegion = new TextureRegion(texture);
        this.rectangle = GdxGameUtils.createBoundingRectangle(textureRegion);
        this.coordinates = new GridPoint2(startPosition);
        this.destinationCoordinates = new GridPoint2(startPosition);
        this.rotation = 0f;
    }

    public boolean isMovementCompleted() { return isEqual(movementProgress, 1f); }

    public void tryMove(Direction direction, GridPoint2 obstacleCoordinates) {
        GridPoint2 nextCoordinates = direction.applyTo(coordinates);
        rotation = direction.rotation;
        
        // check if there's an obstacle
        if (obstacleCoordinates.equals(nextCoordinates)) return;

        destinationCoordinates.set(nextCoordinates);
        movementProgress = 0f;
    }

    public void update(float deltaTime, float movementSpeed, TileMovement tileMovement) {
        tileMovement.moveRectangleBetweenTileCenters(rectangle, coordinates, destinationCoordinates, movementProgress);
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (isMovementCompleted()) coordinates.set(destinationCoordinates);
    }

    public TextureRegion getRegion() { return textureRegion; }
    public Rectangle getRectangle() { return rectangle; }
    public float getRotation() { return rotation; }
    public GridPoint2 getCoordinates() { return coordinates; }
}