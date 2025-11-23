package ru.mipt.bit.platformer.util.commands;

import ru.mipt.bit.platformer.util.graphics.HealthBarView;

public class ToggleHealthBarCommand implements Command {
    private final HealthBarView[] healthBarViews;

    public ToggleHealthBarCommand(HealthBarView... healthBarViews) {
        this.healthBarViews = healthBarViews;
    }

    @Override
    public void execute() {
        boolean newState = healthBarViews.length > 0 && !healthBarViews[0].getModel().isVisible();
        for (HealthBarView healthBarView : healthBarViews) {
            healthBarView.getModel().setVisible(newState);
        }
    }
}


















