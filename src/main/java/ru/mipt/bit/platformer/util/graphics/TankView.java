package ru.mipt.bit.platformer.util.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.util.TileMovement;
import ru.mipt.bit.platformer.util.models.TankModel;

import com.badlogic.gdx.graphics.g2d.Batch;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;


public class TankView {
    private TextureRegion textureRegion;
    private Rectangle rectangle;
    private TankModel tankModel;

    public TankView(TankModel tankModel, Texture texture) {
        this.textureRegion = new TextureRegion(texture);
        this.rectangle = createBoundingRectangle(textureRegion);
        this.tankModel = tankModel;
    }

    public void update(float deltaTime, TileMovement tileMovement) {
        tileMovement.moveRectangleBetweenTileCenters(
            rectangle, tankModel.getPosition(), tankModel.getDestination(), tankModel.getMovementProgress()
        );
        tankModel.update(deltaTime, tileMovement);
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, textureRegion, rectangle, tankModel.getRotation());
    }

    public TextureRegion getRegion() { return textureRegion; }
    public Rectangle getRectangle() { return rectangle; }

}