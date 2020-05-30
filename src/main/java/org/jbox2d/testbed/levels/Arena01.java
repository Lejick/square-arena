/**
 * Copyright (c) 2013, Daniel Murphy
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 */
/**
 * Created at 4:56:29 AM Jan 14, 2011
 */
package org.jbox2d.testbed.levels;

import javafx.scene.Scene;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
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
 * @author Oleg Trohov
 */
public class Arena01 extends CommonLevel {
    private static float width = 80;
    private static float height = 60;

    public Arena01(AbstractTestbedController controller, Scene scene) {
        super(controller, scene);
    }

    @Override
    public void initTest(boolean deserialized) {
        super.initTest(false);
        createGameObjects();
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
                startPointX = startPointX + deltaX;
            }
            startPointY = startPointY + deltaY;
        }

        Body enemyBody = GeometryBodyFactory.createRectangle(0f, 18, commonPersonEdge, commonPersonEdge, BodyType.DYNAMIC, getWorld(), Color3f.RED);
        Integer direction = 0;
        while (direction == 0) {
            direction = rand.nextInt(3) - 1;
            Enemy enemy = new Enemy(enemyBody, getWorld(), new Vec2(direction * 6, 0));
            enemy.delayToFire = 40;
            enemyList.add(enemy);
            destroyableList.add(enemyBody);
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
    public void step(SettingsIF settings) {
        super.step(settings);
        if (hero.getEnemyKilled() == 7) {
            for (MovingObject movingObject : movingObjectList) {
                if (!movingObject.isActive()) {
                    movingObject.setActive(true);
                }
            }
        }
    }

    @Override
    protected int getLevelIndex() {
        return 6;
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
        return "Arena 01";
    }

    @Override
    public String getLevelDescription() {
        return "Kill Enemy";
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
