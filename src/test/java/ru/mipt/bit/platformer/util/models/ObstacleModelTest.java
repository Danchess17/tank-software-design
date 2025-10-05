package ru.mipt.bit.platformer.util.models;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObstacleModelTest {

    @Test
    public void testPositionIsImmutable() {
        GridPoint2 pos = new GridPoint2(5, 7);
        ObstacleModel obstacle = new ObstacleModel(pos);

        assertEquals(new GridPoint2(5, 7), obstacle.getPosition());

        pos.x = 10;
        assertEquals(new GridPoint2(5, 7), obstacle.getPosition());
    }
}