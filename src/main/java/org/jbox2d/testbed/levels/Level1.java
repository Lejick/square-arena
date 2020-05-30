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
 */
/**
 * Created at 4:56:29 AM Jan 14, 2011
 */
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
public class Level1 extends CommonLevel {
    private static float width = 60;
    private static float height = 40;

    public Level1(AbstractTestbedController controller, Scene scene) {
        super(controller, scene);
    }


    @Override
    public boolean isSaveLoadEnabled() {
        return true;
    }

    @Override
    public void initTest(boolean deserialized) {
        super.initTest(false);
        Body heroBody = GeometryBodyFactory.createRectangle(-25, 15, commonPersonEdge, commonPersonEdge, BodyType.DYNAMIC, getWorld(), Color3f.BLUE);
        destroyableList.add(heroBody);
        hero=new Hero(heroBody,getWorld());
        Body simpleBox = GeometryBodyFactory.createRectangle(20, 3, commonPersonEdge, commonPersonEdge, BodyType.DYNAMIC, getWorld());
        Body simpleBox2 = GeometryBodyFactory.createRectangle(0, getHeight() / 2 - commonPersonEdge * 29, commonPersonEdge, commonPersonEdge, BodyType.DYNAMIC, getWorld());
        movingObject.add(simpleBox);
        movingObject.add(simpleBox2);
        destroyableList.add(simpleBox);
        destroyableList.add(simpleBox2);

        createGuns();
        exit = GeometryBodyFactory.createRectangle(getWidth() / 2 -0.25f, -getHeight() / 2 + 4 * commonPersonEdge, 0.25f, 4, BodyType.STATIC, getWorld(), Color3f.GREEN);
    }


    protected void createPlatforms() {
        BodyDef bd = new BodyDef();
        Body ground = getWorld().createBody(bd);
        EdgeShape shape = new EdgeShape();

        shape.set(new Vec2(-getWidth() / 2, getHeight() / 2 - commonPersonEdge * 6), new Vec2(getWidth() / 3, getHeight() / 2 - commonPersonEdge * 6));
        Fixture f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);
        shape.set(new Vec2(-getWidth() / 2, getHeight() / 2 - commonPersonEdge * 6 - 0.1f), new Vec2(getWidth() / 3, getHeight() / 2 - commonPersonEdge * 6 - 0.1f));
        f = ground.createFixture(shape, 0.0f);
        leftBlockedFixtures.add(f);

        shape.set(new Vec2(-getWidth() / 3, getHeight() / 2 - commonPersonEdge * 12), new Vec2(getWidth() / 2, getHeight() / 2 - commonPersonEdge * 12));
        f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);
        shape.set(new Vec2(-getWidth() / 3, getHeight() / 2 - commonPersonEdge * 12 - 0.1f), new Vec2(getWidth() / 2, getHeight() / 2 - commonPersonEdge * 12 - 0.1f));
        ground.createFixture(shape, 0.0f);

        shape.set(new Vec2(-getWidth() / 2, getHeight() / 2 - commonPersonEdge * 18), new Vec2(getWidth() / 8, getHeight() / 2 - commonPersonEdge * 18));
        f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);
        shape.set(new Vec2(-getWidth() / 2, getHeight() / 2 - commonPersonEdge * 18 - 0.1f), new Vec2(getWidth() / 8, getHeight() / 2 - commonPersonEdge * 18 - 0.1f));
        ground.createFixture(shape, 0.0f);

        shape.set(new Vec2(getWidth() / 4, getHeight() / 2 - commonPersonEdge * 18), new Vec2(getWidth() / 2, getHeight() / 2 - commonPersonEdge * 18));
        f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);
        shape.set(new Vec2(getWidth() / 4, getHeight() / 2 - commonPersonEdge * 18 - 0.1f), new Vec2(getWidth() / 2, getHeight() / 2 - commonPersonEdge * 18 - 0.1f));
        ground.createFixture(shape, 0.0f);

        shape.set(new Vec2(-getWidth() / 3, getHeight() / 2 - commonPersonEdge * 30), new Vec2(getWidth() / 2, getHeight() / 2 - commonPersonEdge * 30));
        f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);
        shape.set(new Vec2(-getWidth() / 3, getHeight() / 2 - commonPersonEdge * 30 - 0.1f), new Vec2(getWidth() / 2, getHeight() / 2 - commonPersonEdge * 30 - 0.1f));
        ground.createFixture(shape, 0.0f);


        shape.set(new Vec2(0, -getHeight() / 2 + commonPersonEdge * 3), new Vec2(0, -getHeight() / 2));
        f = ground.createFixture(shape, 0.0f);
    }

    private void createGuns() {
        Gun gun1 = new Gun(m_world, -getWidth() / 2, commonPersonEdge * 12 - 2, 200, 100, 0.01f);
        gun1.setOrientation(new Vec2(1, 0));
        objectForJump.add(gun1.getGunBodyFixture());
        gunList.add(gun1);

        Gun gun2 = new Gun(m_world, getWidth() / 2 - 2, commonPersonEdge - 2, 400, 400, 0.5f);
        gun2.setDetection(true);
        gun2.setDetectY1(-0.6f);
        gun2.setDetectY2(-0.4f);
        for (Body body : destroyableList) {
            gun2.addObjectToAttack(body);
        }
        gun2.setOrientation(new Vec2(-1, 0));
        objectForJump.add(gun2.getGunBodyFixture());
        gunList.add(gun2);
    }

    @Override
    protected int getLevelIndex() {
        return 0;
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
        return "Level 1";
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
