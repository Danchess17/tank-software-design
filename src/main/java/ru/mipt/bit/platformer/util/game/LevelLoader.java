package ru.mipt.bit.platformer.util.game;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

@Component
public class LevelLoader {
    
    @Value("${game.level.file:level.tmx}")
    private String levelFilePath;

    public TiledMap loadLevel() {
        return new TmxMapLoader().load(levelFilePath);
    }

    public TiledMapTileLayer getGroundLayer(TiledMap level) {
        return getSingleLayer(level);
    }
}


