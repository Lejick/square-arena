package org.jbox2d.testbed.framework.game.objects;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.util.ArrayList;
import java.util.List;

public class Gun {
    private long lastFireStep;
    private long cooldown;
    private Body bullet;
    private List<Body> objectsToAttack = new ArrayList<>();
    private Body gunBody;
    private Fixture gunBodyFixture;
    private float x;
    private Vec2 orientation;
    private float bulletVel;
    private float y;
    private World world;
    private float bulletRadius = 0.15f;
    private List<Fixture> objectForJump = new ArrayList<>();
    private boolean isDetection;
    private Float detectX1;
    private Float detectX2;
    private Float detectY1;
    private Float detectY2;

    public Gun(World world, float x, float y, long cooldown, float bulletVel, float bulletDensity) {
        this.cooldown = cooldown;
        this.x = x;
        this.y = y;
        this.world = world;
        this.bulletVel = bulletVel;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1, 1);
        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = bulletDensity;
        fd.friction = 0.3f;
        BodyDef bd = new BodyDef();
        bd.shapeColor = Color3f.GREEN;
        bd.type = BodyType.STATIC;
        bd.position.set(x + 1, y);
        gunBody = getWorld().createBody(bd);
        gunBodyFixture = gunBody.createFixture(fd);

    }

    public void checkFire(long lastStep) {

        float shift = ((float) lastStep - (float) lastFireStep) / cooldown;
        if (shift > 1) {
            shift = 1;
        }
        Color3f actualColor = new Color3f(shift, 1 - shift, 0);
        gunBody.shapeColor = actualColor;
        if (isDetection) {
            for (Body body : objectsToAttack) {
                float positionX = body.getPosition().x;
                float positionY = body.getPosition().y;

                boolean condition1 = true;
                if (detectX1 != null) {
                    condition1 = positionX >= detectX1;
                }
                boolean condition2 = true;
                if (detectX2 != null) {
                    condition2 = positionX <= detectX2;
                }
                boolean condition3 = true;
                if (detectY1 != null) {
                    condition3 = positionY >= detectY1;
                }
                boolean condition4 = true;
                if (detectY2 != null) {
                    condition4 = positionY <= detectY2;
                }
                if ((lastStep - lastFireStep > cooldown) && !body.isDestroy() &&
                        condition1 &&
                        condition2 &&
                        condition3 &&
                        condition4) {
                    fireBullet();
                    lastFireStep = lastStep;
                }
            }
        } else if (lastStep - lastFireStep > cooldown) {
            fireBullet();
            lastFireStep = lastStep;
        }

    }

    void fireBullet() {
        if (bullet != null) {
            world.destroyBody(bullet);
            bullet = null;
        }
        {
            CircleShape shape = new CircleShape();
            shape.m_radius = bulletRadius;
            FixtureDef fd = new FixtureDef();
            fd.shape = shape;
            fd.density = 20.0f;
            fd.restitution = 0.05f;

            BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            bd.bullet = true;
            bd.position.set(x + 2 * orientation.x, y + 2 * orientation.y);

            bullet = world.createBody(bd);
            Fixture f = bullet.createFixture(fd);
            objectForJump.add(f);
            bullet.setLinearVelocity(new Vec2(orientation.x * bulletVel, orientation.y * bulletVel));
        }

    }

    public long getLastFireStep() {
        return lastFireStep;
    }

    public void setLastFireStep(long lastFireStep) {
        this.lastFireStep = lastFireStep;
    }


    public long getCooldown() {
        return cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    public Body getBullet() {
        return bullet;
    }

    public void setBullet(Body bullet) {
        this.bullet = bullet;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Body getGunBody() {
        return gunBody;
    }

    public Fixture getGunBodyFixture() {
        return gunBodyFixture;
    }

    public void setGunBody(Body gunBody) {
        this.gunBody = gunBody;
    }

    public boolean isDetection() {
        return isDetection;
    }

    public void setDetection(boolean detection) {
        isDetection = detection;
    }

    public float getDetectX1() {
        return detectX1;
    }

    public void setDetectX1(float detectX1) {
        this.detectX1 = detectX1;
    }

    public float getDetectX2() {
        return detectX2;
    }

    public void setDetectX2(float detectX2) {
        this.detectX2 = detectX2;
    }

    public float getDetectY1() {
        return detectY1;
    }

    public void setDetectY1(float detectY1) {
        this.detectY1 = detectY1;
    }

    public float getDetectY2() {
        return detectY2;
    }

    public void setDetectY2(float detectY2) {
        this.detectY2 = detectY2;
    }

    public void setOrientation(Vec2 orientation) {
        this.orientation = orientation;
    }

    public void addObjectToAttack(Body body) {
        objectsToAttack.add(body);
    }

    public void setObjectForJump(List<Fixture> objectForJump) {
        this.objectForJump = objectForJump;
    }

    public void setBulletRadius(float bulletRadius) {
        this.bulletRadius = bulletRadius;
    }
}
