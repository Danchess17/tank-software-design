package ru.mipt.bit.platformer.util.ai;

import java.util.Random;

import org.springframework.stereotype.Component;
import ru.mipt.bit.platformer.util.Direction;

@Component
public class RandomAIController implements AIController {
    private final Random random;
    private static final double SHOOT_PROBABILITY = 0.3;

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
    
    public boolean shouldShoot() {
        return random.nextDouble() < SHOOT_PROBABILITY;
    }
}


