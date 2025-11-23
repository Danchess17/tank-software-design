package ru.mipt.bit.platformer.util.models;

import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import com.badlogic.gdx.math.GridPoint2;

@Component
public class EntityManager {
    private GameEntity[] entities;

    public EntityManager() {
        this.entities = new GameEntity[0];
    }

    public EntityManager(GameEntity ... entities) {
        this.entities = entities;
    }
    
    public void initialize(GameEntity ... entities) {
        this.entities = entities;
    }

    public GameEntity[] getEntities() { return entities; }
    
    public void addEntity(GameEntity entity) {
        GameEntity[] newEntities = new GameEntity[entities.length + 1];
        System.arraycopy(entities, 0, newEntities, 0, entities.length);
        newEntities[entities.length] = entity;
        entities = newEntities;
    }

    public void removeEntity(int index) {
        if (index < 0 || index >= entities.length) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        GameEntity[] newEntities = new GameEntity[entities.length - 1];
        System.arraycopy(entities, 0, newEntities, 0, index);
        System.arraycopy(entities, index + 1, newEntities, index, entities.length - index - 1);
        entities = newEntities;
    }

    public boolean removeEntity(GameEntity entity) {
        for (int i = 0; i < entities.length; i++) {
            if (entities[i].equals(entity)) {
                removeEntity(i);
                return true;
            }
        }
        return false;
    }

    public MovableGameEntity[] getMovableEntities() {
        return Arrays.stream(entities)
                .filter(entity -> entity instanceof MovableGameEntity)
                .toArray(MovableGameEntity[]::new);
    }

    public GameEntity[] getStaticEntities() {
        return Arrays.stream(entities)
                .filter(entity -> !(entity instanceof MovableGameEntity))
                .toArray(GameEntity[]::new);
    }

    public Optional<GameEntity> getEntityAt(GridPoint2 position) {
        return Arrays.stream(entities)
                .filter(entity -> entity.getPosition().equals(position))
                .filter(GameEntity::isCollidable) // Only return collidable entities
                .findFirst();
    }

    public <T extends GameEntity> List<T> getEntitiesByType(Class<T> entityType) {
        return Arrays.stream(entities)
                .filter(entityType::isInstance)
                .map(entityType::cast)
                .collect(Collectors.toList());
    }

    public List<TankModel> getTanks() {
        return getEntitiesByType(TankModel.class);
    }
    
    public List<ObstacleModel> getObstacles() {
        return getEntitiesByType(ObstacleModel.class);
    }
}