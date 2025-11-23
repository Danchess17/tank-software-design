package ru.mipt.bit.platformer.util.ai;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

import ru.mipt.bit.platformer.util.Direction;

public class RandomAIControllerTest {
    @Test
    public void testChooseDirectionNotNull() {
        RandomAIController ai = new RandomAIController(new Random(123));
        Direction d = ai.chooseDirection();
        assertNotNull(d);
    }
}

















