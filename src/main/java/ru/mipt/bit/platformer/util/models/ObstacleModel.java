package ru.mipt.bit.platformer.util.models;

import com.badlogic.gdx.math.GridPoint2;

public class ObstacleModel implements GameEntity {
    private final GridPoint2 position;

    public ObstacleModel(GridPoint2 position) {
        this.position = new GridPoint2(position);
    }
    
    @Override
    public GridPoint2 getPosition() { return position; }
}