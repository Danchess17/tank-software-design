package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import static org.junit.Assert.*;

public class DirectionTest {

    @Test
    public void testApplyTo() {
        GridPoint2 origin = new GridPoint2(2, 3);
        assertEquals(new GridPoint2(2, 4), Direction.UP.applyTo(origin));
        assertEquals(new GridPoint2(2, 2), Direction.DOWN.applyTo(origin));
        assertEquals(new GridPoint2(1, 3), Direction.LEFT.applyTo(origin));
        assertEquals(new GridPoint2(3, 3), Direction.RIGHT.applyTo(origin));
    }

    @Test
    public void testRotationValues() {
        assertEquals(90f, Direction.UP.rotation, 0.001f);
        assertEquals(-90f, Direction.DOWN.rotation, 0.001f);
        assertEquals(-180f, Direction.LEFT.rotation, 0.001f);
        assertEquals(0f, Direction.RIGHT.rotation, 0.001f);
    }
}