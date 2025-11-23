package ru.mipt.bit.platformer.util.logic;

import com.badlogic.gdx.math.GridPoint2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mipt.bit.platformer.util.models.BulletModel;
import ru.mipt.bit.platformer.util.models.EntityManager;
import ru.mipt.bit.platformer.util.models.ObstacleModel;
import ru.mipt.bit.platformer.util.models.TankModel;

import java.util.List;

@Component
public class BulletCollisionHandler {
    private final EntityManager entityManager;
    private final Bounds bounds;

    @Autowired
    public BulletCollisionHandler(EntityManager entityManager, Bounds bounds) {
        this.entityManager = entityManager;
        this.bounds = bounds;
    }

    public void checkCollisions(BulletModel bullet) {
        if (bullet.isDestroyed()) return;

        GridPoint2[] positionsToCheck = {bullet.getPosition(), bullet.getDestination()};

        for (GridPoint2 checkPos : positionsToCheck) {
            if (checkTankCollision(bullet, checkPos)) return;
            if (checkObstacleCollision(bullet, checkPos)) return;
            if (checkBoundsCollision(bullet, checkPos)) return;
            if (checkBulletToBulletCollision(bullet, checkPos)) return;
        }
    }

    private boolean checkTankCollision(BulletModel bullet, GridPoint2 checkPos) {
        for (TankModel tank : entityManager.getTanks()) {
            if (tank == bullet.getShooter() || !tank.isAlive()) continue;

            if (tank.getPosition().equals(checkPos) || tank.getDestination().equals(checkPos)) {
                tank.takeDamage(bullet.getDamage());
                bullet.destroy();
                return true;
            }
        }
        return false;
    }

    private boolean checkObstacleCollision(BulletModel bullet, GridPoint2 checkPos) {
        List<ObstacleModel> obstacles = entityManager.getObstacles();
        for (ObstacleModel obstacle : obstacles) {
            if (obstacle.getPosition().equals(checkPos)) {
                bullet.destroy();
                return true;
            }
        }
        return false;
    }

    private boolean checkBoundsCollision(BulletModel bullet, GridPoint2 checkPos) {
        if (!bounds.contains(checkPos)) {
            bullet.destroy();
            return true;
        }
        return false;
    }

    private boolean checkBulletToBulletCollision(BulletModel bullet, GridPoint2 checkPos) {
        List<BulletModel> activeBullets = entityManager.getEntitiesByType(BulletModel.class);
        for (BulletModel otherBullet : activeBullets) {
            if (otherBullet == bullet || otherBullet.isDestroyed()) continue;

            if (otherBullet.getPosition().equals(checkPos) || otherBullet.getDestination().equals(checkPos)) {
                bullet.destroy();
                otherBullet.destroy();
                return true;
            }
        }
        return false;
    }
}

