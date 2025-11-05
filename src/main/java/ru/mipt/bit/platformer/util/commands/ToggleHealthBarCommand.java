package ru.mipt.bit.platformer.util.commands;

import ru.mipt.bit.platformer.util.graphics.HealthBarDecorator;

public class ToggleHealthBarCommand implements Command {
    private final HealthBarDecorator[] decorators;

    public ToggleHealthBarCommand(HealthBarDecorator... decorators) {
        this.decorators = decorators;
    }

    @Override
    public void execute() {
        boolean newState = decorators.length > 0 && !decorators[0].isShowHealthBar();
        for (HealthBarDecorator decorator : decorators) {
            decorator.setShowHealthBar(newState);
        }
    }
}

