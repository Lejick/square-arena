package org.jbox2d.testbed.levels;

import javafx.scene.Scene;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.testbed.Hero;
import org.jbox2d.testbed.framework.AbstractTestbedController;
import org.jbox2d.testbed.framework.game.objects.GeometryBodyFactory;
import org.jbox2d.testbed.framework.game.objects.Gun;

/**
 * @author Daniel Murphy
 */
public class Level2 extends CommonLevel {
    private static float width = 60;
    private static float height = 40;
    private int levelIndex = 1;

    public Level2(AbstractTestbedController controller, Scene scene) {
        super(controller, scene);
    }

    @Override
    public void initTest(boolean deserialized) {
        super.initTest(false);
        createGameObjects();
        createGuns();
        exit = GeometryBodyFactory.createRectangle(getWidth() / 2 -0.25f, getHeight() / 2 - commonPersonEdge * 8, 0.25f, 4, BodyType.STATIC, getWorld(),Color3f.GREEN);
    }

    protected void createGameObjects() {
        Body heroBody = GeometryBodyFactory.createRectangle(-getWidth() / 2 + 2, getHeight() / 2 - 2 * commonPersonEdge, commonPersonEdge, commonPersonEdge, BodyType.DYNAMIC, getWorld(), Color3f.BLUE);
        destroyableList.add(heroBody);
        hero = new Hero(heroBody, getWorld());
        Body jumplatform = GeometryBodyFactory.createRectangle(0, commonPersonEdge, commonPersonEdge * 12, commonPersonEdge / 1.5f, BodyType.DYNAMIC, getWorld());
        movingObject.add(jumplatform);
    }

    protected void createPlatforms() {
        BodyDef bd = new BodyDef();
        Body ground = getWorld().createBody(bd);
        EdgeShape shape = new EdgeShape();
        shape.set(new Vec2(-getWidth() / 2 + commonPersonEdge * 6, getHeight() / 2), new Vec2(-getWidth() / 2 + commonPersonEdge * 6, commonPersonEdge * 3));
        ground.createFixture(shape, 0.0f);

        shape.set(new Vec2(-getWidth() / 2 + commonPersonEdge * 5, 0), new Vec2(getWidth() / 8, 0));
        Fixture f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);

        shape.set(new Vec2(getWidth() / 8, 0), new Vec2(getWidth() / 8, -getHeight() / 2));
        ground.createFixture(shape, 0.0f);

        shape.set(new Vec2(-getWidth() / 2 + commonPersonEdge * 5, 0), new Vec2(-getWidth() / 2 + commonPersonEdge * 5, -getHeight() / 2));
        ground.createFixture(shape, 0.0f);

        shape.set(new Vec2(getWidth() / 4 + 3.5f * commonPersonEdge, -1), new Vec2(getWidth() / 4 + 3.5f * commonPersonEdge, -getHeight() / 2));
        ground.createFixture(shape, 0.0f);

        shape.set(new Vec2(getWidth() / 4 + 3.5f * commonPersonEdge, -1), new Vec2(getWidth() / 2, -1));
        ground.createFixture(shape, 0.0f);
        objectForJump.add(f);

    }

    private void createGuns() {
        Gun gun1 = new Gun(m_world, -getWidth() / 2 + 2, -getHeight() / 2 + commonPersonEdge, 70, 400, 0.5f);
        gun1.setOrientation(new Vec2(0, 1));
        objectForJump.add(gun1.getGunBodyFixture());
        gunList.add(gun1);

        Gun gun2 = new Gun(m_world, 9 * commonPersonEdge, -getHeight() / 2 + commonPersonEdge, 500, 400, 0.5f);
        gun2.setOrientation(new Vec2(0, 1));
        gun2.setBulletRadius(0.25f);
        objectForJump.add(gun2.getGunBodyFixture());
        gunList.add(gun2);

        Gun gun3 = new Gun(m_world, 14 * commonPersonEdge, -getHeight() / 2 + commonPersonEdge, 500, 300, 0.5f);
        gun3.setOrientation(new Vec2(0, 1));
        gun3.setBulletRadius(0.25f);
        objectForJump.add(gun3.getGunBodyFixture());
        gunList.add(gun3);
    }

    @Override
    protected int getLevelIndex() {
        return levelIndex;
    }

    @Override
    protected boolean hasGun() {
        return false;
    }

    @Override
    protected void checkEnemyAction() {
    }

    @Override
    public String getLevelName() {
        return "Level 2";
    }

    @Override
    protected float getWidth() {
        return width;
    }

    @Override
    protected float getHeight() {
        return height;
    }
}
