package org.jbox2d.testbed.framework.game.objects;

import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.util.ArrayList;
import java.util.List;

public class MovingObject {
    private Body movingBody;
    private Body switcher;
    private Vec2 nextDestination;
    private Vec2 prevDestination;
    private boolean isActive = false;
    private int nextDestinationIndex;
    private int maxDestinationIndex;
    private List<Vec2> coordinatesList;
    SwitchType switchType;

    public MovingObject(Body movingBody, List<Vec2> coordinatesList) {
        this.coordinatesList = coordinatesList;
        Vec2 startCoordinate = new Vec2(movingBody.getPosition().x, movingBody.getPosition().y);
        prevDestination = startCoordinate;
        nextDestination = coordinatesList.get(0);
        this.coordinatesList.add(0, startCoordinate);
        maxDestinationIndex = coordinatesList.size() - 1;
        nextDestinationIndex = 1;
        this.movingBody = movingBody;
    }

    public void calculateVelocity() {
        if (movingBody == null) return;
        if (isActive) {
            nextDestinationIndex++;
            if (nextDestinationIndex > maxDestinationIndex) {
                nextDestinationIndex = 0;
            }
            prevDestination = nextDestination;
            nextDestination = coordinatesList.get(nextDestinationIndex);
            float norm = (float) Math.sqrt
                    (Math.pow(prevDestination.x - nextDestination.x, 2) +
                            Math.pow(prevDestination.y - nextDestination.y, 2));
            Vec2 normVector = new Vec2((nextDestination.x - prevDestination.x) / norm,
                    (nextDestination.y - prevDestination.y) / norm);
            Vec2 currentVelocity = new Vec2(normVector.x, normVector.y);
            movingBody.setLinearVelocity(currentVelocity);
        } else {
            movingBody.setLinearVelocity(new Vec2(0, 0));
        }

    }

    public void calculateStep() {
        if (checkPosition()) {
            calculateVelocity();
        }
    }

    public boolean checkPosition() {
        float dx = Math.abs(movingBody.getPosition().x - nextDestination.x);
        float dy = Math.abs(movingBody.getPosition().y - nextDestination.y);
        return dx < 0.1 && dy < 0.1;
    }

    public Body getSwitcher() {
        return switcher;
    }

    public void setSwitcher(Body switcher) {
        this.switcher = switcher;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
        if (active == true) {
            calculateVelocity();
            if (switcher != null) {
                switcher.shapeColor = Color3f.GREEN;
            }
        } else {
            if (switcher != null) {
                switcher.shapeColor = Color3f.ORANGE;
            }
            movingBody.setLinearVelocity(new Vec2(0, 0));
        }
    }

    public SwitchType getSwitchType() {
        return switchType;
    }

    public void setSwitchType(SwitchType switchType) {
        this.switchType = switchType;
    }
}
