/*******************************************************************************
 * Copyright (c) 2013, Daniel Murphy
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
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
 ******************************************************************************/
package org.jbox2d.testbed.framework;

import java.util.*;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.callbacks.DestructionListener;
import org.jbox2d.callbacks.ParticleDestructionListener;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.Collision;
import org.jbox2d.collision.Collision.PointState;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.WorldManifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Settings;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.MouseJoint;
import org.jbox2d.dynamics.joints.MouseJointDef;
import org.jbox2d.particle.ParticleGroup;
import org.jbox2d.serialization.*;
import org.jbox2d.testbed.framework.game.objects.SerialDTO;
import org.jbox2d.testbed.framework.utils.ParticleVelocityQueryCallback;
import org.jbox2d.testbed.framework.utils.TestQueryCallback;
import org.slf4j.Logger;

/**
 * @author Daniel Murphy
 */
public abstract class PlayLevel implements ContactListener, ObjectListener,
        UnsupportedListener {
    public static final int MAX_CONTACT_POINTS = 4048;
    public static final float ZOOM_SCALE_DIFF = .05f;
    public static final int TEXT_LINE_SPACE = 13;
    public static final int MOUSE_JOINT_BUTTON = 1;
    public static final int BOMB_SPAWN_BUTTON = 10;
    protected boolean isServer;
    protected int id;
    protected List<PlayLevel> clientLevelList;
    protected PlayLevel serverLevel;
    protected static final long GROUND_BODY_TAG = 1897450239847L;
    protected static final long BOMB_TAG = 98989788987L;
    protected static final long MOUSE_JOINT_TAG = 4567893364789L;

    public final ContactPoint[] points = new ContactPoint[MAX_CONTACT_POINTS];

    /**
     * Only visible for compatibility. Should use {@link #getWorld()} instead.
     */
    protected World m_world;
    protected Body groundBody;
    private MouseJoint mouseJoint;

    private Body bomb;
    private final Vec2 bombMousePoint = new Vec2();
    private final Vec2 bombSpawnPoint = new Vec2();
    private boolean bombSpawning = false;
    protected Map<Integer,List<SerialDTO>> objToSerialMap = new Hashtable<>();
    protected Map<Integer,List<SerialDTO>> objToSerialMapServer = new Hashtable<>();
    protected boolean mouseTracing;
    private Vec2 mouseTracerPosition = new Vec2();
    private Vec2 mouseTracerVelocity = new Vec2();

    private final Vec2 mouseWorld = new Vec2();
    private int pointCount;
    private int stepCount;

    private GamingModelIF model;
    protected DestructionListener destructionListener;
    protected ParticleDestructionListener particleDestructionListener;


    private String title = null;
    protected int m_textLine;
    private final LinkedList<String> textList = new LinkedList<String>();

    private TestbedCamera camera;
    private final Transform identity = new Transform();

    public PlayLevel() {
        identity.setIdentity();
        for (int i = 0; i < MAX_CONTACT_POINTS; i++) {
            points[i] = new ContactPoint();
        }
        destructionListener = new DestructionListener() {
            public void sayGoodbye(Fixture fixture) {
                fixtureDestroyed(fixture);
            }

            public void sayGoodbye(Joint joint) {
                if (mouseJoint == joint) {
                    mouseJoint = null;
                } else {
                    jointDestroyed(joint);
                }
            }
        };

        particleDestructionListener = new ParticleDestructionListener() {
            @Override
            public void sayGoodbye(int index) {
                particleDestroyed(index);
            }

            @Override
            public void sayGoodbye(ParticleGroup group) {
                particleGroupDestroyed(group);
            }
        };
        camera = new TestbedCamera(getDefaultCameraPos(), getDefaultCameraScale(), ZOOM_SCALE_DIFF);
    }

    public void init(GamingModelIF model, List<SerialDTO> serialDTOList) {
        this.model = model;
        Vec2 gravity = new Vec2(0, -10f);
        m_world = model.getWorldCreator().createWorld(gravity);
        m_world.setParticleGravityScale(0.4f);
        m_world.setParticleDensity(1.2f);
        bomb = null;
        mouseJoint = null;

        mouseTracing = false;
        mouseTracerPosition.setZero();
        mouseTracerVelocity.setZero();

        BodyDef bodyDef = new BodyDef();
        groundBody = m_world.createBody(bodyDef);

        init(m_world);
    }

    public void init(World world) {
        m_world = world;
        pointCount = 0;
        stepCount = 0;
        bombSpawning = false;
        model.getDebugDraw().setViewportTransform(camera.getTransform());

        world.setDestructionListener(destructionListener);
        world.setParticleDestructionListener(particleDestructionListener);
        world.setContactListener(this);
        world.setDebugDraw(model.getDebugDraw());
        title = getLevelName() + "\n" + getLevelDescription();
        initTest();
    }

    /**
     * Gets the current world
     */
    public World getWorld() {
        return m_world;
    }

    /**
     * Gets the testbed model
     */
    public GamingModelIF getModel() {
        return model;
    }

    /**
     * Gets the contact points for the current test
     */
    public ContactPoint[] getContactPoints() {
        return points;
    }

    /**
     * Gets the ground body of the world, used for some joints
     */
    public Body getGroundBody() {
        return groundBody;
    }

    /**
     * Gets the debug draw for the testbed
     */
    public DebugDraw getDebugDraw() {
        return model.getDebugDraw();
    }

    /**
     * Gets the world position of the mouse
     */
    public Vec2 getWorldMouse() {
        return mouseWorld;
    }

    public int getStepCount() {
        return stepCount;
    }

    /**
     * The number of contact points we're storing
     */
    public int getPointCount() {
        return pointCount;
    }

    public TestbedCamera getCamera() {
        return camera;
    }

    /**
     * Gets the 'bomb' body if it's present
     */
    public Body getBomb() {
        return bomb;
    }

    /**
     * Override for a different default camera position
     */
    public Vec2 getDefaultCameraPos() {
        return new Vec2(0, 0);
    }

    /**
     * Override for a different default camera scale
     */
    public float getDefaultCameraScale() {
        return 10;
    }

    public boolean isMouseTracing() {
        return mouseTracing;
    }

    public Vec2 getMouseTracerPosition() {
        return mouseTracerPosition;
    }

    public Vec2 getMouseTracerVelocity() {
        return mouseTracerVelocity;
    }

    /**
     * Gets the filename of the current test. Default implementation uses the test name with no
     * spaces".
     */
    public String getFilename() {
        return getLevelName().toLowerCase().replaceAll(" ", "_") + ".box2d";
    }

    /**
     * @deprecated use {@link #getCamera()}
     */
    public void setCamera(Vec2 argPos) {
        camera.setCamera(argPos);
    }

    /**
     * @deprecated use {@link #getCamera()}
     */
    public void setCamera(Vec2 argPos, float scale) {
        camera.setCamera(argPos, scale);
    }

    /**
     * The name of the test
     */
    public abstract String getLevelName();

    public abstract String getLevelDescription();

    /**
     * Adds a text line to the reporting area
     */
    public void addTextLine(String line) {
        textList.add(line);
    }

    /**
     * called when the tests exits
     */
    public void exit() {
    }

    public abstract void initTest();

    protected abstract Logger getLogger();


    private final Color3f color1 = new Color3f(.3f, .95f, .3f);
    private final Color3f color2 = new Color3f(.3f, .3f, .95f);
    private final Color3f color3 = new Color3f(.9f, .9f, .9f);
    private final Color3f color4 = new Color3f(.6f, .61f, 1);
    private final Color3f color5 = new Color3f(.9f, .9f, .3f);
    private final Color3f mouseColor = new Color3f(0f, 1f, 0f);
    private final Vec2 p1 = new Vec2();
    private final Vec2 p2 = new Vec2();
    private final Vec2 tangent = new Vec2();
    private final List<String> statsList = new ArrayList<String>();

    private final Vec2 acceleration = new Vec2();
    private final CircleShape pshape = new CircleShape();
    private final ParticleVelocityQueryCallback pcallback = new ParticleVelocityQueryCallback();
    private final AABB paabb = new AABB();

    public void step(SettingsIF settings) {
        float hz = 40;
        float timeStep = hz > 0f ? 1f / hz : 0;
        if (settings.isSingleStep() && !settings.isPause()) {
            settings.setPause(true);
        }

        final DebugDraw debugDraw = model.getDebugDraw();
        m_textLine = 20;

        if (title != null) {
            debugDraw.drawString(camera.getTransform().getExtents().x, 15, title, Color3f.WHITE);
            m_textLine += TEXT_LINE_SPACE;
        }

        if (settings.isPause()) {
            if (settings.isSingleStep()) {
                settings.setSingleStep(false);
            } else {
                timeStep = 0;
            }

            debugDraw.drawString(5, m_textLine, "****PAUSED****", Color3f.WHITE);
            m_textLine += TEXT_LINE_SPACE;
        }

        int flags = 0;
        flags += DebugDraw.e_shapeBit;
        debugDraw.setFlags(flags);

        m_world.setAllowSleep(false);
        m_world.setWarmStarting(false);
        m_world.setSubStepping(false);
        m_world.setContinuousPhysics(true);

        pointCount = 0;

        m_world.step(timeStep, 5,
                5);

        m_world.drawDebugData();

        if (timeStep > 0f) {
            ++stepCount;
        }

        debugDraw.drawString(5, m_textLine, "World mouse position: " + mouseWorld.toString(), Color3f.WHITE);
       /* if (settings.getSetting(PlayedSettings.DrawStats).enabled) {
        debugDraw.drawString(5, m_textLine, "World mouse position: " + mouseWorld.toString(),Color3f.WHITE);
            m_textLine += TEXT_LINE_SPACE;

            statsList.clear();
            for (String s : statsList) {
                debugDraw.drawString(5, m_textLine, s, Color3f.WHITE);
                m_textLine += TEXT_LINE_SPACE;
            }
            m_textLine += TEXT_SECTION_SPACE;
        }*/

     /*   if (settings.getSetting(PlayedSettings.DrawHelp).enabled) {
            debugDraw.drawString(5, m_textLine, "Help", color4);
            m_textLine += TEXT_LINE_SPACE;
            List<String> help = model.getImplSpecificHelp();
            for (String string : help) {
                debugDraw.drawString(5, m_textLine, string, Color3f.WHITE);
                m_textLine += TEXT_LINE_SPACE;
            }
            m_textLine += TEXT_SECTION_SPACE;
        }*/

        if (!textList.isEmpty()) {
            debugDraw.drawString(5, m_textLine, "Test Info", color4);
            m_textLine += TEXT_LINE_SPACE;
            for (String s : textList) {
                debugDraw.drawString(5, m_textLine, s, Color3f.WHITE);
                m_textLine += TEXT_LINE_SPACE;
            }
            textList.clear();
        }

        if (mouseTracing && mouseJoint == null) {
            float delay = 0.1f;
            acceleration.x =
                    2 / delay * (1 / delay * (mouseWorld.x - mouseTracerPosition.x) - mouseTracerVelocity.x);
            acceleration.y =
                    2 / delay * (1 / delay * (mouseWorld.y - mouseTracerPosition.y) - mouseTracerVelocity.y);
            mouseTracerVelocity.x += timeStep * acceleration.x;
            mouseTracerVelocity.y += timeStep * acceleration.y;
            mouseTracerPosition.x += timeStep * mouseTracerVelocity.x;
            mouseTracerPosition.y += timeStep * mouseTracerVelocity.y;
            pshape.m_p.set(mouseTracerPosition);
            pshape.m_radius = 2;
            pcallback.init(m_world, pshape, mouseTracerVelocity);
            pshape.computeAABB(paabb, identity, 0);
            m_world.queryAABB(pcallback, paabb);
        }

        if (mouseJoint != null) {
            mouseJoint.getAnchorB(p1);
            Vec2 p2 = mouseJoint.getTarget();

            debugDraw.drawSegment(p1, p2, mouseColor);
        }
    }

    /************ INPUT ************/

    /**
     * Called for mouse-up
     */
    public void mouseUp(Vec2 p, int button) {
        mouseTracing = false;
        if (button == MOUSE_JOINT_BUTTON) {
            destroyMouseJoint();
        }
        completeBombSpawn(p);
    }

    public void keyPressed(char keyChar, int keyCode) {
    }

    public void keyReleased(char keyChar, int keyCode) {
    }

    public void mouseDown(Vec2 p, int button) {
        mouseWorld.set(p);
        mouseTracing = true;
        mouseTracerVelocity.setZero();
        mouseTracerPosition.set(p);

        if (button == BOMB_SPAWN_BUTTON) {
            beginBombSpawn(p);
        }
        if (button == MOUSE_JOINT_BUTTON) {
            leftMouseAction();
        }
    }

    protected abstract void leftMouseAction();

    public void mouseMove(Vec2 p) {
        mouseWorld.set(p);
    }

    public void mouseDrag(Vec2 p, int button) {
        mouseWorld.set(p);
        if (button == MOUSE_JOINT_BUTTON) {
            updateMouseJoint(p);
        }
        if (button == BOMB_SPAWN_BUTTON) {
            bombMousePoint.set(p);
        }
    }

    /************ MOUSE JOINT ************/

    private final AABB queryAABB = new AABB();
    private final TestQueryCallback callback = new TestQueryCallback();

    private void spawnMouseJoint(Vec2 p) {
        if (mouseJoint != null) {
            return;
        }
        queryAABB.lowerBound.set(p.x - .001f, p.y - .001f);
        queryAABB.upperBound.set(p.x + .001f, p.y + .001f);
        callback.point.set(p);
        callback.fixture = null;
        m_world.queryAABB(callback, queryAABB);

        if (callback.fixture != null) {
            Body body = callback.fixture.getBody();
            MouseJointDef def = new MouseJointDef();
            def.bodyA = groundBody;
            def.bodyB = body;
            def.collideConnected = true;
            def.target.set(p);
            def.maxForce = 1000f * body.getMass();
            mouseJoint = (MouseJoint) m_world.createJoint(def);
            body.setAwake(true);
        }
    }

    private void updateMouseJoint(Vec2 target) {
        if (mouseJoint != null) {
            mouseJoint.setTarget(target);
        }
    }

    private void destroyMouseJoint() {
        if (mouseJoint != null) {
            m_world.destroyJoint(mouseJoint);
            mouseJoint = null;
        }
    }

    /********** BOMB ************/

    private final Vec2 p = new Vec2();
    private final Vec2 v = new Vec2();

    public void lanchBomb() {
        p.set((float) (Math.random() * 30 - 15), 30f);
        v.set(p).mulLocal(-5f);
        launchBomb(p, v);
    }

    private final AABB aabb = new AABB();

    private void launchBomb(Vec2 position, Vec2 velocity) {
        if (bomb != null) {
            m_world.destroyBody(bomb);
            bomb = null;
        }
        // todo optimize this
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(position);
        bd.bullet = true;
        bomb = m_world.createBody(bd);
        bomb.setLinearVelocity(velocity);

        CircleShape circle = new CircleShape();
        circle.m_radius = 0.3f;

        FixtureDef fd = new FixtureDef();
        fd.shape = circle;
        fd.density = 20f;
        fd.restitution = 0;

        Vec2 minV = new Vec2(position);
        Vec2 maxV = new Vec2(position);

        minV.subLocal(new Vec2(.3f, .3f));
        maxV.addLocal(new Vec2(.3f, .3f));

        aabb.lowerBound.set(minV);
        aabb.upperBound.set(maxV);

        bomb.createFixture(fd);
    }

    private void beginBombSpawn(Vec2 worldPt) {
        bombSpawnPoint.set(worldPt);
        bombMousePoint.set(worldPt);
        bombSpawning = true;
    }

    private final Vec2 vel = new Vec2();

    private void completeBombSpawn(Vec2 p) {
        if (bombSpawning == false) {
            return;
        }

        float multiplier = 30f;
        vel.set(bombSpawnPoint).subLocal(p);
        vel.mulLocal(multiplier);
        launchBomb(bombSpawnPoint, vel);
        bombSpawning = false;
    }

    public boolean isSaveLoadEnabled() {
        return false;
    }

    @Override
    public boolean isUnsupported(UnsupportedObjectException exception) {
        return true;
    }

    public void fixtureDestroyed(Fixture fixture) {
    }

    public void jointDestroyed(Joint joint) {
    }

    public void beginContact(Contact contact) {
    }

    public void endContact(Contact contact) {
    }

    public void particleDestroyed(int particle) {
    }

    public void particleGroupDestroyed(ParticleGroup group) {
    }

    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private final PointState[] state1 = new PointState[Settings.maxManifoldPoints];
    private final PointState[] state2 = new PointState[Settings.maxManifoldPoints];
    private final WorldManifold worldManifold = new WorldManifold();

    public void preSolve(Contact contact, Manifold oldManifold) {
        Manifold manifold = contact.getManifold();

        if (manifold.pointCount == 0) {
            return;
        }

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Collision.getPointStates(state1, state2, oldManifold, manifold);

        contact.getWorldManifold(worldManifold);

        for (int i = 0; i < manifold.pointCount && pointCount < MAX_CONTACT_POINTS; i++) {
            ContactPoint cp = points[pointCount];
            cp.fixtureA = fixtureA;
            cp.fixtureB = fixtureB;
            cp.position.set(worldManifold.points[i]);
            cp.normal.set(worldManifold.normal);
            cp.state = state2[i];
            cp.normalImpulse = manifold.points[i].normalImpulse;
            cp.tangentImpulse = manifold.points[i].tangentImpulse;
            cp.separation = worldManifold.separations[i];
            ++pointCount;
        }
    }

    public List<SerialDTO> getObjToSerialList(Integer levelId) {
        return objToSerialMap.get(levelId);
    }

    public void addObjToSerialList(List<SerialDTO> objToSerialList,Integer levelId) {
        if(objToSerialList.size()>0){
            objToSerialMap.put(levelId, objToSerialList);
        }
    }

    public void addObjToSerialListServer(List<SerialDTO> objToSerialList,Integer levelId) {
        if(objToSerialList.size()>0){
            objToSerialMapServer.put(levelId, objToSerialList);
        }
    }

    public boolean isServer() {
        return isServer;
    }

    public void setServer(boolean server) {
        isServer = server;
    }

    public List<PlayLevel> getClientLevelList() {
        return clientLevelList;
    }

    public void setClientLevelList(List<PlayLevel> clientLevelList) {
        this.clientLevelList = clientLevelList;
    }

    public PlayLevel getServerLevel() {
        return serverLevel;
    }

    public void setServerLevel(PlayLevel serverLevel) {
        this.serverLevel = serverLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}










