package org.jbox2d.testbed;


import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

public class Hero {
    protected Body heroBody;
    public Body activeBullet;
    protected World world;
    public long stepInAir;
    private int weapon1CD = 0;
    private int enemyKilled = 0;

    public Hero(Body heroBody, World world) {
        this.heroBody = heroBody;
        this.world = world;
    }

    public Body fireWeapon1(Vec2 targetPosition) {
        Vec2 orientation = new Vec2(targetPosition.x - heroBody.getPosition().x,
                targetPosition.y - heroBody.getPosition().y);


        orientation = orientation.mul(1 / orientation.length());
        CircleShape shape = new CircleShape();
        shape.m_radius = 0.15f;

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = 20.0f;
        fd.restitution = 0.05f;

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.setBullet(true);
        bd.position.set(heroBody.getPosition().x + 1 * orientation.x, heroBody.getPosition().y + 1 * orientation.y);

        Body hero_bullet = world.createBody(bd);
        hero_bullet.createFixture(fd);
        hero_bullet.setLinearVelocity(new Vec2(orientation.x * 600, orientation.y * 600));
        activeBullet = hero_bullet;
        weapon1CD = 35;
        return hero_bullet;
    }

    public Body getBody() {
        return heroBody;
    }

    public void setBody(Body heroBody) {
        this.heroBody = heroBody;
    }

    public int getWeapon1CD() {
        return weapon1CD;
    }

    public void decrWeapon1CD() {
        if(weapon1CD>0)
        weapon1CD--;
    }

    public int getEnemyKilled() {
        return enemyKilled;
    }

    public void setEnemyKilled(int enemyKilled) {
        this.enemyKilled = enemyKilled;
    }
}
