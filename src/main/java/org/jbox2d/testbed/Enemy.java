package org.jbox2d.testbed;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

public class Enemy {
    protected Body enemyBody;
    public Body activeBullet;
    protected World world;
    public long weapon1CoolDown = 50;
    public long lastFireWeapon1 = 0;
    public long delayToFire = 0;
    public long stepToWait = 0;
    public Vec2 constantVelocity;

    public Enemy(Body enemyBody, World world, Vec2 constantVelocity) {
        this.constantVelocity=constantVelocity;
        this.enemyBody = enemyBody;
        this.world = world;
    }

    public boolean isDestroyed(){
        return enemyBody.isDestroy();
    }
    public Body getBody() {
        return enemyBody;
    }

    public Body fireWeapon1(Vec2 targetPosition) {
        if (stepToWait < delayToFire) {
            stepToWait++;
            return null;
        }
        Vec2 orientation = new Vec2(targetPosition.x - enemyBody.getPosition().x,
                targetPosition.y - enemyBody.getPosition().y);
        orientation = orientation.mul(1 / orientation.length());

        CircleShape shape = new CircleShape();
        shape.m_radius = 0.15f;

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = 1.0f;
        fd.restitution = 0.05f;
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.bullet = true;
        bd.position.set(enemyBody.getPosition().x + 1 * orientation.x, enemyBody.getPosition().y + 1 * orientation.y);
        Body enemy_bullet = world.createBody(bd);
        enemy_bullet.createFixture(fd);
        enemy_bullet.setLinearVelocity(new Vec2(orientation.x * 600, orientation.y * 600));
        enemy_bullet.setBullet(true);
        activeBullet = enemy_bullet;
        stepToWait=0;
        return enemy_bullet;
    }

}
