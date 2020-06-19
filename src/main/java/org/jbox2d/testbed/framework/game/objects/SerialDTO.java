package org.jbox2d.testbed.framework.game.objects;

import org.jbox2d.common.Vec2;

public class SerialDTO {
    private int id;
    private String className;
    private Vec2 linearVelocity;
    private Float angularVelocity;
    private Vec2 position;

    public SerialDTO(int id, String className, Vec2 linearVelocity, Float angularVelocity, Vec2 position) {
        this.id = id;
        this.className = className;
        this.linearVelocity = linearVelocity;
        this.angularVelocity = angularVelocity;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Vec2 getLinearVelocity() {
        return linearVelocity;
    }

    public void setLinearVelocity(Vec2 linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    public Float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(Float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public Vec2 getPosition() {
        return position;
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }
}
