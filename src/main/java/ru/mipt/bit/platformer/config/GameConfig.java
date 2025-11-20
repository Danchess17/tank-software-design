package ru.mipt.bit.platformer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.mipt.bit.platformer.util.InputHandler;
import ru.mipt.bit.platformer.util.ai.AIController;
import ru.mipt.bit.platformer.util.game.GameInitializer;
import ru.mipt.bit.platformer.util.graphics.HealthBarRenderer;
import ru.mipt.bit.platformer.util.logic.BulletCollisionHandler;

@Configuration
@ComponentScan(basePackages = "ru.mipt.bit.platformer.util")
public class GameConfig {

    @Bean
    public InputHandler inputHandler() {
        return new InputHandler();
    }

    @Bean
    @Autowired
    public GameInitializer gameInitializer(InputHandler inputHandler, 
                                          HealthBarRenderer healthBarRenderer, 
                                          AIController aiController,
                                          BulletCollisionHandler bulletCollisionHandler) {
        return new GameInitializer(inputHandler, healthBarRenderer, aiController, bulletCollisionHandler);
    }
}


