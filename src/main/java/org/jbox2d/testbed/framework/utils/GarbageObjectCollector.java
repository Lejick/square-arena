package org.jbox2d.testbed.framework.utils;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.testbed.levels.Level1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GarbageObjectCollector {
    Map<Long, List<Body>> objectsMap = new HashMap<Long, List<Body>>();
    protected static final Logger log = LoggerFactory.getLogger(Level1.class);

    public void add(Body body, long stepToLive) {
        List<Body> bodyList = objectsMap.getOrDefault(stepToLive, new ArrayList<>());
        bodyList.add(body);
        objectsMap.put(stepToLive, bodyList);
    }

    public void clear(long step, World world) {
        try {
            for (Map.Entry<Long, List<Body>> entry : objectsMap.entrySet()) {
                if (entry.getKey() <= step) {
                    for (Body body : entry.getValue()) {
                        if (body != null && body.getFixtureList() != null) {
                            world.destroyBody(body);
                            body.setDestroy(true);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
