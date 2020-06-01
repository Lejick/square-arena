package org.jbox2d.testbed.levels;

import javafx.scene.Scene;
import org.jbox2d.testbed.framework.AbstractTestbedController;

public class CommonLevelClient extends CommonLevel {

    public CommonLevelClient(AbstractTestbedController controller, Scene scene) {
        super(controller, scene);
    }

    @Override
    protected void createPlatforms() {

    }

    @Override
    protected int getLevelIndex() {
        return 0;
    }

    @Override
    protected float getWidth() {
        return 0;
    }

    @Override
    protected float getHeight() {
        return 0;
    }

    @Override
    public String getLevelName() {
        return null;
    }

    @Override
    public String getLevelDescription() {
        return null;
    }
}
