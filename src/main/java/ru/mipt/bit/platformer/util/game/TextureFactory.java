package ru.mipt.bit.platformer.util.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TextureFactory {
    
    @Value("${game.textures.player:tank_blue.png}")
    private String playerTexturePath;
    
    @Value("${game.textures.enemy:tank_blue.png}")
    private String enemyTexturePath;
    
    @Value("${game.textures.tree:greenTree.png}")
    private String treeTexturePath;

    public Texture createPlayerTexture() {
        return new Texture("images/" + playerTexturePath);
    }

    public Texture createEnemyTexture() {
        return new Texture("images/" + enemyTexturePath);
    }

    public Texture createTreeTexture() {
        return new Texture("images/" + treeTexturePath);
    }

    public Texture createBulletTexture() {
        int size = 24;
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0);
        pixmap.fill();
        Color orange = new Color(1.0f, 0.5f, 0.0f, 1.0f);
        pixmap.setColor(orange);
        pixmap.fillCircle(size / 2, size / 2, size / 2 - 2);
        Color lightOrange = new Color(1.0f, 0.7f, 0.2f, 1.0f);
        pixmap.setColor(lightOrange);
        pixmap.fillCircle(size / 2, size / 2, size / 3);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
}


