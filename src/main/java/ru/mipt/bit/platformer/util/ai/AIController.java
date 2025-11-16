package ru.mipt.bit.platformer.util.ai;

import ru.mipt.bit.platformer.util.Direction;

public interface AIController {
    Direction chooseDirection();
    boolean shouldShoot();
}

