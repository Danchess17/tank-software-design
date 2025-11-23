package ru.mipt.bit.platformer.util.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mipt.bit.platformer.util.TileMovement;
import ru.mipt.bit.platformer.util.graphics.BulletView;
import ru.mipt.bit.platformer.util.logic.CollisionContext;
import ru.mipt.bit.platformer.util.logic.BulletCollisionHandler;
import ru.mipt.bit.platformer.util.models.BulletModel;
import ru.mipt.bit.platformer.util.models.EntityManager;
import ru.mipt.bit.platformer.util.models.GameEntity;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BulletManager {
    private final GameInitializer gameInitializer;
    private final EntityManager entityManager;
    private final BulletCollisionHandler bulletCollisionHandler;
    private final List<BulletView> bulletViews;

    @Autowired
    public BulletManager(GameInitializer gameInitializer,
                        EntityManager entityManager,
                        BulletCollisionHandler bulletCollisionHandler) {
        this.gameInitializer = gameInitializer;
        this.entityManager = entityManager;
        this.bulletCollisionHandler = bulletCollisionHandler;
        this.bulletViews = new ArrayList<>();
    }

    public void update(float deltaTime, CollisionContext collisionContext) {
        List<BulletModel> allBullets = entityManager.getEntitiesByType(BulletModel.class);
        
        removeDestroyedBullets(allBullets);
        // Filter out destroyed bullets for processing
        List<BulletModel> activeBullets = allBullets.stream()
                .filter(bullet -> !bullet.isDestroyed())
                .collect(Collectors.toList());
        
        processBulletMovement(activeBullets, deltaTime, collisionContext);
        updateBulletViews(activeBullets, deltaTime);
    }

    public List<BulletView> getBulletViews() {
        return bulletViews;
    }

    private void removeDestroyedBullets(List<BulletModel> allBullets) {
        for (BulletModel bullet : allBullets) {
            if (bullet.isDestroyed()) entityManager.removeEntity(bullet);
        }
    }

    private void processBulletMovement(List<BulletModel> bullets, float deltaTime, CollisionContext collisionContext) {
        for (BulletModel bullet : bullets) {
            bullet.update(deltaTime);
            
            if (bullet.isMovementCompleted() && !bullet.isDestroyed()) {
                GameEntity[] blockingEntities = collisionContext.buildBlockingEntities(bullet);
                bullet.tryMove(bullet.getDirection(), blockingEntities);
            }
            
            bulletCollisionHandler.checkCollisions(bullet);
        }
    }

    private void updateBulletViews(List<BulletModel> bullets, float deltaTime) {
        var bulletTexture = gameInitializer.getBulletTexture();
        var tileMovement = gameInitializer.getTileMovement();
        
        bulletViews.clear();
        for (BulletModel bullet : bullets) {
            if (!bullet.isDestroyed()) {
                BulletView view = new BulletView(bullet, bulletTexture);
                view.update(deltaTime, tileMovement);
                bulletViews.add(view);
            }
        }
    }
}

