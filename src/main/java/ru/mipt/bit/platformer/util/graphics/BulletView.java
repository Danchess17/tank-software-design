package ru.mipt.bit.platformer.util.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.util.TileMovement;
import ru.mipt.bit.platformer.util.models.BulletModel;

import com.badlogic.gdx.graphics.g2d.Batch;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class BulletView implements Renderable {
    private final Texture texture;
    private TextureRegion textureRegion;
    private Rectangle rectangle;
    private BulletModel bulletModel;

    public BulletView(BulletModel bulletModel, Texture texture) {
        this.texture = texture;
        this.textureRegion = new TextureRegion(texture);
        this.rectangle = createBoundingRectangle(textureRegion);
        this.bulletModel = bulletModel;
    }

    public void update(float deltaTime, TileMovement tileMovement) {
        tileMovement.moveRectangleBetweenTileCenters(
            rectangle, bulletModel.getPosition(), bulletModel.getDestination(), bulletModel.getMovementProgress()
        );
        bulletModel.update(deltaTime);
    }

    @Override
    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, textureRegion, rectangle, bulletModel.getRotation());
    }

    @Override
    public Rectangle getBounds() { return rectangle; }

    @Override
    public Texture getTexture() { return texture; }
}

