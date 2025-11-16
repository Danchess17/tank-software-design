package ru.mipt.bit.platformer.util.commands;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.logic.Bounds;
import ru.mipt.bit.platformer.util.logic.CollisionContext;
import ru.mipt.bit.platformer.util.models.GameEntity;
import ru.mipt.bit.platformer.util.models.MovableGameEntity;
import ru.mipt.bit.platformer.util.models.ObstacleModel;
import ru.mipt.bit.platformer.util.models.TankModel;

public class MoveCommandTest {

    @Test
    public void testMoveBlockedByBounds() {
        TankModel tank = new TankModel(new GridPoint2(0, 0));
        Bounds bounds = new Bounds(1, 1);
        CollisionContext ctx = new CollisionContext(Collections.emptyList(), Collections.singletonList(tank));

        new MoveCommand(tank, Direction.LEFT, bounds, ctx).execute();
        assertTrue(tank.isMovementCompleted());
        assertEquals(new GridPoint2(0, 0), tank.getPosition());

        new MoveCommand(tank, Direction.DOWN, bounds, ctx).execute();
        assertTrue(tank.isMovementCompleted());
        assertEquals(new GridPoint2(0, 0), tank.getPosition());
    }

    @Test
    public void testMoveBlockedByObstacle() {
        TankModel tank = new TankModel(new GridPoint2(1, 1));
        ObstacleModel obstacle = new ObstacleModel(new GridPoint2(2, 1));
        Bounds bounds = new Bounds(10, 10);
        CollisionContext ctx = new CollisionContext(Collections.singletonList(obstacle), Collections.singletonList(tank));

        new MoveCommand(tank, Direction.RIGHT, bounds, ctx).execute();
        assertTrue(tank.isMovementCompleted());
        assertEquals(new GridPoint2(1, 1), tank.getPosition());
    }

    @Test
    public void testOccupancyBlocksCurrentAndDestination() {
        TankModel a = new TankModel(new GridPoint2(1, 1));
        TankModel b = new TankModel(new GridPoint2(1, 2));
        Bounds bounds = new Bounds(5, 5);
        CollisionContext ctx = new CollisionContext(Collections.emptyList(), Arrays.asList(new MovableGameEntity[]{a, b}));

        // A attempts to move UP into B's current cell -> should be blocked due to entity at destination
        new MoveCommand(a, Direction.UP, bounds, ctx).execute();
        assertTrue(a.isMovementCompleted());
        assertEquals(new GridPoint2(1, 1), a.getPosition());

        // Now free B's next cell and have A move UP into (1,2) by moving B away first
        new MoveCommand(b, Direction.UP, bounds, ctx).execute();
        assertFalse(b.isMovementCompleted());
        assertEquals(new GridPoint2(1, 3), b.getDestination());

        // While B is moving (occupies (1,2) and (1,3)), A cannot move UP into (1,2)
        new MoveCommand(a, Direction.UP, bounds, ctx).execute();
        assertTrue(a.isMovementCompleted());
        assertEquals(new GridPoint2(1, 1), a.getPosition());

        // While B is moving, A also cannot move DOWN into its own current cell? It can choose LEFT which is free
        new MoveCommand(a, Direction.LEFT, bounds, ctx).execute();
        assertFalse(a.isMovementCompleted());
        assertEquals(new GridPoint2(0, 1), a.getDestination());
    }
}


