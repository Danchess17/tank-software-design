package ru.mipt.bit.platformer.util.models;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import com.badlogic.gdx.math.GridPoint2;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Primary
public class RandomGeneratorLoader implements MapLoader {
    private final Random random = new Random();
    
    @Value("${game.level.width:10}")
    private int levelWidth;
    
    @Value("${game.level.height:8}")
    private int levelHeight;
    
    @Value("${game.obstacles.count:7}")
    private int obstacleCount;
    
    @Value("${game.enemies.count:3}")
    private int aiTankCount;
    
    @Override
    public EntityManager load(String path) {
        List<GameEntity> entities = new ArrayList<>();
        
        for (int i = 0; i < obstacleCount; i++) {
            GridPoint2 obstaclePosition = getRandomFreePosition(entities);
            entities.add(new ObstacleModel(obstaclePosition));
        }
        
        GridPoint2 playerPos = getRandomFreePosition(entities);
        entities.add(new TankModel(playerPos));

        for (int i = 0; i < aiTankCount; i++) {
            GridPoint2 aiPos = getRandomFreePosition(entities);
            entities.add(new TankModel(aiPos));
        }
        
        return new EntityManager(entities.toArray(GameEntity[]::new));
    }

    private GridPoint2 getRandomFreePosition(List<GameEntity> existingEntities) {
        int maxAttempts = levelWidth * levelHeight * 2;
        for (int i = 0; i < maxAttempts; i++) {
            GridPoint2 position = new GridPoint2(random.nextInt(levelWidth), random.nextInt(levelHeight));
            if (existingEntities.stream().noneMatch(e -> e.getPosition().equals(position))) {
                return position;
            }
        }
        throw new IllegalStateException("Could not find free position after " + maxAttempts + " attempts");
    }
}