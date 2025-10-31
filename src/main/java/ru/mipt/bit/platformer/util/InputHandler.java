package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.Gdx;
import static com.badlogic.gdx.Input.Keys.*;

public class InputHandler {
    public Direction chooseDirection() {
        if (Gdx.input.isKeyPressed(UP) || Gdx.input.isKeyPressed(W)) return Direction.UP;
        else if (Gdx.input.isKeyPressed(LEFT) || Gdx.input.isKeyPressed(A)) return Direction.LEFT;
        else if (Gdx.input.isKeyPressed(DOWN) || Gdx.input.isKeyPressed(S)) return Direction.DOWN;
        else if (Gdx.input.isKeyPressed(RIGHT) || Gdx.input.isKeyPressed(D)) return Direction.RIGHT;
        else return null; 
    }
}
