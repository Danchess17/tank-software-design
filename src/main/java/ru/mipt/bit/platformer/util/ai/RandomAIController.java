package ru.mipt.bit.platformer.util.ai;

import java.util.Random;

import ru.mipt.bit.platformer.util.Direction;

public class RandomAIController {
    private final Random random;

    public RandomAIController() {
        this(new Random());
    }

    public RandomAIController(Random random) {
        this.random = random;
    }

    public Direction chooseDirection() {
        Direction[] values = Direction.values();
        return values[random.nextInt(values.length)];
    }
}


