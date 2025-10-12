package ru.mipt.bit.platformer.util.models;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import ru.mipt.bit.platformer.util.Direction;

import static org.junit.Assert.*;

public class TankModelTest {

    @Test
    public void testInitialPosition() {
        TankModel tank = new TankModel(new GridPoint2(1, 2));
        assertEquals(new GridPoint2(1, 2), tank.getPosition());
        assertTrue(tank.isMovementCompleted());
    }

    @Test
    public void testTryMoveWithObstacle() {
        TankModel tank = new TankModel(new GridPoint2(1, 1));
        ObstacleModel obstacle = new ObstacleModel(new GridPoint2(1, 2));

        tank.tryMove(Direction.UP, obstacle);
        assertTrue(tank.isMovementCompleted());
        assertEquals(new GridPoint2(1, 1), tank.getPosition());
    }

    @Test
    public void testTryMoveWithoutObstacle() {
        TankModel tank = new TankModel(new GridPoint2(1, 1));
        ObstacleModel obstacle = new ObstacleModel(new GridPoint2(-1, -1));

        tank.tryMove(Direction.UP, obstacle);
        assertFalse(tank.isMovementCompleted());
        assertEquals(new GridPoint2(1, 1), tank.getPosition());
        assertEquals(new GridPoint2(1, 2), tank.getDestination());
    }

}