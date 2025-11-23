package ru.mipt.bit.platformer.util.logic;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.util.models.GameEntity;
import ru.mipt.bit.platformer.util.models.MovableGameEntity;

public class CollisionContext {
    private final List<GameEntity> staticEntities;
    private final List<MovableGameEntity> movableEntities;

    public CollisionContext(List<GameEntity> staticEntities, List<MovableGameEntity> movableEntities) {
        this.staticEntities = staticEntities;
        this.movableEntities = movableEntities;
    }

    public GameEntity[] buildBlockingEntities(MovableGameEntity actor) {
        List<GameEntity> blocks = new ArrayList<>();
        blocks.addAll(staticEntities);
        for (MovableGameEntity e : movableEntities) {
            blocks.add(wrapPosition(e.getPosition()));
            // also block destination to satisfy occupancy of both cells during movement
            if (!e.isMovementCompleted()) {
                blocks.add(wrapPosition(e.getDestination()));
            }
        }
        // remove actor's own entries; actor can leave its own current cell and go to destination if free
        GridPoint2 actorPos = actor.getPosition();
        GridPoint2 actorDest = actor.getDestination();
        blocks.removeIf(b -> b.getPosition().equals(actorPos) || b.getPosition().equals(actorDest));
        return blocks.toArray(GameEntity[]::new);
    }

    private static GameEntity wrapPosition(GridPoint2 position) {
        return new GameEntity() {
            @Override
            public GridPoint2 getPosition() { return position; }
        };
    }
}

















