package org.jbox2d.testbed.framework.game.objects;

import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.testbed.framework.utils.Line;

import java.util.ArrayList;
import java.util.List;

public class GeometryBodyFactory {

    public static Body createRectangle(float x, float y, float hx, float hy, BodyType bodyType, World world, Color3f color) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(hx, hy);
        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = 1.0f;
        fd.friction = 0.3f;
        BodyDef bd = new BodyDef();
        bd.type = bodyType;
        bd.position.set(x, y);
        Body body = world.createBody(bd);
        body.shapeColor = color;
        body.createFixture(fd);
        return body;
    }

    public static Body createGameBrick(float x, float y, float hx, float hy, BodyType bodyType, World world, Color3f color) {
        Body mainRectangle = createRectangle(x, y, hx, hy, bodyType, world, color);
        Body leftEdge = createRectangle(x - hx, y, 0.05f,  hy, bodyType, world, color);
        leftEdge.getFixtureList().m_friction = 0;
        Body rightEdge = createRectangle(x + hx, y, 0.05f,  hy, bodyType, world, color);
        rightEdge.getFixtureList().m_friction = 0;
        Body bottomEdge = createRectangle(x , y-hy, hx,  0.05f, bodyType, world, color);
        bottomEdge.getFixtureList().m_friction = 0;
        return mainRectangle;
    }

    public static Body createRectangle(float x, float y, float hx, float hy, BodyType bodyType, World world) {
        return createRectangle(x, y, hx, hy, bodyType, world, null);
    }

    public static Body createGameBrick(float x, float y, float hx, float hy, BodyType bodyType, World world) {
        return createGameBrick(x, y, hx, hy, bodyType, world, null);
    }

    public static List<Line> splitRectangle(float x, float y, float hx, float hy) {
        List<Line> lines = new ArrayList<>();
        Vec2 lt = new Vec2(x - hx , y + hy );
        Vec2 lb = new Vec2(x - hx , y - hy );
        Vec2 rt = new Vec2(x + hx , y + hy );
        Vec2 rb = new Vec2(x + hx , y - hy );
        lines.add(new Line(lt, rt));
        lines.add(new Line(rt, rb));
        lines.add(new Line(rb, lb));
        lines.add(new Line(lb, lt));
        return lines;
    }

}
