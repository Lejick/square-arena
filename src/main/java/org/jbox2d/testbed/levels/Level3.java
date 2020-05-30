package org.jbox2d.testbed.levels;

import javafx.scene.Scene;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.testbed.Hero;
import org.jbox2d.testbed.framework.AbstractTestbedController;
import org.jbox2d.testbed.framework.game.objects.GameObjectFactory;
import org.jbox2d.testbed.framework.game.objects.GeometryBodyFactory;
import org.jbox2d.testbed.framework.game.objects.Gun;
import org.jbox2d.testbed.framework.game.objects.MovingObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Murphy
 */
public class Level3 extends CommonLevel {
    private static float width = 80;
    private static float height = 60;

    public Level3(AbstractTestbedController controller, Scene scene) {
        super(controller, scene);
    }

    @Override
    public void initTest(boolean deserialized) {
        super.initTest(false);
        createGameObjects();
        createGuns();
        exit = GeometryBodyFactory.createRectangle(getWidth() / 2 - 0.25f,
                -getHeight() / 2 + 4 * commonPersonEdge, 0.25f, 4, BodyType.STATIC, getWorld(), Color3f.GREEN);

    }

    protected void createGameObjects() {
       Body heroBody = GeometryBodyFactory.createRectangle(-35, 22, commonPersonEdge, commonPersonEdge, BodyType.DYNAMIC, getWorld(), Color3f.BLUE);
        destroyableList.add(heroBody);
        hero=new Hero(heroBody,getWorld());
        float deltaY = 0;
        for (int j = 0; j < 3; j++) {
            float deltaX = 0;
            for (int i = 0; i < 2; i++) {
                Body simpleBox = GeometryBodyFactory.createRectangle(31 + deltaX, 21 + deltaY, commonPersonEdge, commonPersonEdge / 1.5f, BodyType.DYNAMIC, getWorld());
                movingObject.add(simpleBox);
                deltaX = deltaX + commonPersonEdge + 6f;
            }
            deltaY = deltaY + commonPersonEdge + 0.7f;
        }

        GeometryBodyFactory.createRectangle(34.5f, 25, 5f, 0.1f, BodyType.DYNAMIC, getWorld());

        Body simpleBox = GeometryBodyFactory.createRectangle(34.5f, 27, commonPersonEdge, commonPersonEdge, BodyType.DYNAMIC, getWorld());
        simpleBox.getFixtureList().m_friction = 5f;
        simpleBox.getFixtureList().m_density = 1f;
        movingObject.add(simpleBox);

        Body switcher = GeometryBodyFactory.createRectangle(15f, 5.1f, 1, 0.05f, BodyType.STATIC, getWorld());
        objectForJump.add(switcher.getFixtureList());
        Body platform = GeometryBodyFactory.createRectangle(5.5f, 5, 5, 0.2f, BodyType.KINEMATIC, getWorld());
        objectForJump.add(platform.getFixtureList());
        List<Vec2> coordinatesList = new ArrayList<>();
        coordinatesList.add(new Vec2(5.5f, 0));
        MovingObject mo = GameObjectFactory.createMovingObject(platform, switcher, coordinatesList, false, new Vec2(0, -1));
        movingObjectList.add(mo);

        Body platform2 = GeometryBodyFactory.createRectangle(35, -25, 0.2f, 5f, BodyType.KINEMATIC, getWorld());
        platform2.getFixtureList().m_friction = 0;
        rightBlockedFixtures.add(platform2.getFixtureList());
        Body switcher2 = GeometryBodyFactory.createRectangle(39.9f, -10, 0.1f, 1f, BodyType.STATIC, getWorld());
        coordinatesList = new ArrayList<>();
        coordinatesList.add(new Vec2(35f, -10));
        MovingObject mo2 = GameObjectFactory.createMovingObject(platform2, switcher2, coordinatesList, false, new Vec2(0, 1));
        movingObjectList.add(mo2);
    }

    protected void createPlatforms() {
        BodyDef bd = new BodyDef();
        Body ground = getWorld().createBody(bd);
        EdgeShape shape = new EdgeShape();

        shape.set(new Vec2(-40, 20), new Vec2(18, 20));
        Fixture f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);
        shape.set(new Vec2(-40, 19.9f), new Vec2(18, 19.9f));
        ground.createFixture(shape, 0.0f);

        shape.set(new Vec2(30, 20), new Vec2(40, 20));
        f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);
        shape.set(new Vec2(30, 19.9f), new Vec2(40, 19.9f));
        ground.createFixture(shape, 0.0f);

        shape.set(new Vec2(-30, 13), new Vec2(40, 13));
        f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);
        shape.set(new Vec2(-30, 12.9f), new Vec2(40, 12.9f));
        ground.createFixture(shape, 0.0f);

        shape.set(new Vec2(-40, 5), new Vec2(0, 5));
        f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);
        shape.set(new Vec2(-40, 4.9f), new Vec2(0, 4.9f));
        ground.createFixture(shape, 0.0f);

        shape.set(new Vec2(11, 5), new Vec2(40, 5));
        f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);

        shape.set(new Vec2(35, -20), new Vec2(40, -20));
        f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);

        shape.set(new Vec2(11, 4.9f), new Vec2(40, 4.9f));
        ground.createFixture(shape, 0.0f);

    }


    private void createGuns() {
        Gun gun2 = new Gun(m_world, 32, 14, 100, 1000, 1f);
        gun2.setDetection(true);
        gun2.setDetectY1(13f);
        gun2.setDetectY2(15f);
        for (Body body : destroyableList) {
            gun2.addObjectToAttack(body);
        }
        gun2.setOrientation(new Vec2(-1, 0));
        objectForJump.add(gun2.getGunBodyFixture());
        gunList.add(gun2);
    }


    @Override
    protected int getLevelIndex() {
        return 2;
    }

    @Override
    protected boolean hasGun() {
        return true;
    }

    @Override
    protected void checkEnemyAction() {
    }

    @Override
    public String getLevelName() {
        return "Level 3";
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
