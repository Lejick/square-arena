package org.jbox2d.testbed.framework.utils;

import org.jbox2d.callbacks.ParticleQueryCallback;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class ParticleVelocityQueryCallback implements ParticleQueryCallback {
  World world;
  Shape shape;
  Vec2 velocity;
  final Transform xf = new Transform();

  public ParticleVelocityQueryCallback() {
    xf.setIdentity();
  }

  public void init(World world, Shape shape, Vec2 velocity) {
    this.world = world;
    this.shape = shape;
    this.velocity = velocity;
  }

  @Override
  public boolean reportParticle(int index) {
    Vec2 p = world.getParticlePositionBuffer()[index];
    if (shape.testPoint(xf, p)) {
      Vec2 v = world.getParticleVelocityBuffer()[index];
      v.set(velocity);
    }
    return true;
  }
}
