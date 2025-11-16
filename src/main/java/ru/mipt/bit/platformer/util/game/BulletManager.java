package ru.mipt.bit.platformer.util.game;

import com.badlogic.gdx.graphics.Texture;
import ru.mipt.bit.platformer.util.TileMovement;
import ru.mipt.bit.platformer.util.graphics.BulletView;
import ru.mipt.bit.platformer.util.logic.BulletCollisionHandler;
import ru.mipt.bit.platformer.util.logic.CollisionContext;
import ru.mipt.bit.platformer.util.models.BulletModel;
import ru.mipt.bit.platformer.util.models.EntityManager;
import ru.mipt.bit.platformer.util.models.GameEntity;

import java.util.ArrayList;
import java.util.List;

public class BulletManager {
    private final EntityManager entityManager;
    private final BulletCollisionHandler bulletCollisionHandler;
    private final Texture bulletTexture;
    private final TileMovement tileMovement;
    private final List<BulletModel> bullets;
    private final List<BulletView> bulletViews;

    public BulletManager(EntityManager entityManager, BulletCollisionHandler bulletCollisionHandler,
                        Texture bulletTexture, TileMovement tileMovement, List<BulletModel> bullets) {
        this.entityManager = entityManager;
        this.bulletCollisionHandler = bulletCollisionHandler;
        this.bulletTexture = bulletTexture;
        this.tileMovement = tileMovement;
        this.bullets = bullets;
        this.bulletViews = new ArrayList<>();
    }

    public void update(float deltaTime, CollisionContext collisionContext) {
        List<BulletModel> allBullets = entityManager.getEntitiesByType(BulletModel.class);
        
        removeDestroyedBullets(allBullets);
        updateActiveBulletsList(allBullets);
        processBulletMovement(deltaTime, collisionContext);
        updateBulletViews(deltaTime);
    }

    public List<BulletView> getBulletViews() {
        return bulletViews;
    }

    private void removeDestroyedBullets(List<BulletModel> allBullets) {
        for (BulletModel bullet : allBullets) {
            if (bullet.isDestroyed()) entityManager.removeEntity(bullet);
        }
    }

    private void updateActiveBulletsList(List<BulletModel> allBullets) {
        bullets.clear();
        for (BulletModel bullet : allBullets) {
            if (!bullet.isDestroyed()) bullets.add(bullet);
        }
    }

    private void processBulletMovement(float deltaTime, CollisionContext collisionContext) {
        for (BulletModel bullet : bullets) {
            bullet.update(deltaTime);
            
            if (bullet.isMovementCompleted() && !bullet.isDestroyed()) {
                GameEntity[] blockingEntities = collisionContext.buildBlockingEntities(bullet);
                bullet.tryMove(bullet.getDirection(), blockingEntities);
            }
            
            bulletCollisionHandler.checkCollisions(bullet);
        }
    }

    private void updateBulletViews(float deltaTime) {
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

