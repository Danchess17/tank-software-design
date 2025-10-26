package ru.mipt.bit.platformer.util.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;


public interface Renderable {
    void render(Batch batch);
    Rectangle getBounds();
    Texture getTexture();
}