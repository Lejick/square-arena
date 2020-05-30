package org.jbox2d.testbed.levels;

import javafx.scene.Scene;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.testbed.Enemy;
import org.jbox2d.testbed.Hero;
import org.jbox2d.testbed.framework.AbstractTestbedController;
import org.jbox2d.testbed.framework.SettingsIF;
import org.jbox2d.testbed.framework.game.objects.GameObjectFactory;
import org.jbox2d.testbed.framework.game.objects.GeometryBodyFactory;
import org.jbox2d.testbed.framework.game.objects.MovingObject;
import org.jbox2d.testbed.framework.utils.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Daniel Murphy
 */
public class Level6 extends CommonLevel {
    private static float width = 80;
    private static float height = 60;

    public Level6(AbstractTestbedController controller, Scene scene) {
        super(controller, scene);
    }

    @Override
    public void initTest(boolean deserialized) {
        super.initTest(false);
        createGameObjects();
        exit = GeometryBodyFactory.createRectangle(getWidth() / 2 - 0.25f,
                getHeight() / 2 - 4 * commonPersonEdge, 0.25f, 4, BodyType.STATIC, getWorld(), Color3f.GREEN);
        rightBlockedFixtures.add(exit.getFixtureList());

    }

    protected void createGameObjects() {
        Body heroBody = GeometryBodyFactory.createRectangle(-30, -23, commonPersonEdge, commonPersonEdge, BodyType.DYNAMIC, getWorld(), Color3f.BLUE);
        destroyableList.add(heroBody);
        hero = new Hero(heroBody, getWorld());

    }


    protected void createPlatforms() {
        Random rand = new Random();
        float startPointY = -25;
        float deltaX = 20;
        float deltaY = 12;
        for (int j = 0; j < 5; j++) {
            float startPointX = -30;
            for (int i = 0; i <= 3; i++) {
                Body b = GeometryBodyFactory.createGameBrick(startPointX, startPointY, 4f, 0.5f, BodyType.STATIC, getWorld());
                List<Line> lines = GeometryBodyFactory.splitRectangle(startPointX, startPointY, 6f, 0.5f);
                linesList.addAll(lines);
                b.getFixtureList().m_friction = 2;
                objectForJump.add(b.getFixtureList());

                if (j > 3) {
                    Body enemyBody = GeometryBodyFactory.createRectangle(startPointX, startPointY + 1f, commonPersonEdge, commonPersonEdge, BodyType.DYNAMIC, getWorld(), Color3f.RED);
                    Integer direction =0;
                    while (direction==0) {
                        direction = rand.nextInt(3) - 1;
                    }
                    Enemy enemy = new Enemy(enemyBody, getWorld(),new Vec2(direction*6, 0));
                    enemy.delayToFire=50;
                    enemyList.add(enemy);
                    destroyableList.add(enemyBody);
                }
                startPointX = startPointX + deltaX;
            }
            startPointY = startPointY + deltaY;
        }

        startPointY = -19;
        deltaX = 20;
        deltaY = 12;
        for (int j = 0; j < 4; j++) {
            float startPointX = -20;
            for (int i = 0; i <= 2; i++) {
                Body b = GeometryBodyFactory.createGameBrick(startPointX, startPointY, 4f, 0.5f, BodyType.STATIC, getWorld());
                List<Line> lines = GeometryBodyFactory.splitRectangle(startPointX, startPointY, 6f, 0.5f);
                linesList.addAll(lines);
                b.getFixtureList().m_friction = 2;
                objectForJump.add(b.getFixtureList());
                startPointX = startPointX + deltaX;
            }
            startPointY = startPointY + deltaY;
        }
    }

    @Override
    protected int getLevelIndex() {
        return 5;
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
        return "Level 6";
    }

    @Override
    public String getLevelDescription() {
        return "Avoid Enemies\nYou have NOT a gun";
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
