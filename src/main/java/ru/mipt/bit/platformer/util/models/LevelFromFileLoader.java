package ru.mipt.bit.platformer.util.models;

import com.badlogic.gdx.math.GridPoint2;
import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Gdx;

public class LevelFromFileLoader implements MapLoader {

    @Override
    public EntityManager load(String path) {
        try {
            String fileContent = Gdx.files.internal(path).readString();
            String[] lines = fileContent.split("\\r?\\n");
            List<GameEntity> entities = new ArrayList<>();
            
            for (int y = 0; y < lines.length; y++) {
                String line = lines[y];
                for (int x = 0; x < line.length(); x++) {
                    char cell = line.charAt(x);
                    GridPoint2 position = new GridPoint2(x, lines.length - 1 - y);
                    
                    if (cell == 'T') entities.add(new ObstacleModel(position));
                    else if (cell == 'X') entities.add(new TankModel(position));
                    else if (cell == '_') continue;
                }
            }
            
            return new EntityManager(entities.toArray(GameEntity[]::new));

            
        } catch (Exception e) {
            throw new RuntimeException("Failed to load level from file: " + path, e);
        }
    }
}