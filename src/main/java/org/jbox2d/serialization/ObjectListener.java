package org.jbox2d.serialization;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;

public  interface ObjectListener{
		
		public void processWorld(World world, Long tag);
		
		public void processBody(Body body, Long tag);
		
		public void processFixture(Fixture fixture, Long tag);
		
		public void processShape(Shape shape, Long tag);
		
		public void processJoint(Joint joint, Long tag);
	}
