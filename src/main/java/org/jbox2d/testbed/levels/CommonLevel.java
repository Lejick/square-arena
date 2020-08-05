package org.jbox2d.testbed.levels;

import javafx.scene.Scene;
import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.testbed.Player;
import org.jbox2d.testbed.framework.*;
import org.jbox2d.testbed.framework.game.objects.*;
import org.jbox2d.testbed.framework.utils.GarbageObjectCollector;
import org.jbox2d.testbed.framework.utils.Line;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Oleg Trohov
 */
public abstract class CommonLevel extends PlayLevel {
    protected static final Logger log = LoggerFactory.getLogger(CommonLevel.class);

    protected List<Line> linesList = new ArrayList<>();

    protected static final float commonPersonEdge = 1f;
    protected long last_step = 0;
    private static float bulletDeathVelocity = 75;
    protected List<Fixture> objectForJump = new ArrayList<>();
    protected Map<Integer, Integer> levelToHeroIdsMap = new HashMap<>();
    protected List<Body> bulletList = new ArrayList<>();
    protected AbstractTestbedController controller;
    protected List<Body> movingObject = new ArrayList<>();
    protected List<Gun> gunList = new ArrayList<>();
    protected List<Body> destroyableList = Collections.synchronizedList(new ArrayList<>());
    protected List<Body> objectToExplode = Collections.synchronizedList(new ArrayList<>());
    protected Scene scene;
    protected List<MovingObject> movingObjectList = new ArrayList<>();
    protected List<Player> playersList=new ArrayList<>();
    protected int nextId = 0;
    RayCastClosestCallback ccallback;
    GarbageObjectCollector garbageObjectCollector = new GarbageObjectCollector();

    @Override
    public boolean isSaveLoadEnabled() {
        return true;
    }

    protected CommonLevel(AbstractTestbedController controller, Scene scene) {
        this.controller = controller;
        this.scene = scene;
    }

    @Override
    public void initTest() {
        ccallback = new RayCastClosestCallback();
        garbageObjectCollector = new GarbageObjectCollector();
        last_step = 0;
        createGameBox();
        createPlatforms();
        createGameObject();
    }


    protected abstract void createPlatforms();

    protected abstract void createGameObject();

    protected void createGameBox() {
        BodyDef bd = new BodyDef();
        Body ground = getWorld().createBody(bd);

        EdgeShape shape = new EdgeShape();
        shape.set(new Vec2(-getWidth() / 2, -getHeight() / 2), new Vec2(getWidth() / 2, -getHeight() / 2));
        Fixture f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);

        shape.set(new Vec2(-getWidth() / 2, getHeight() / 2), new Vec2(getWidth() / 2, getHeight() / 2));
        ground.createFixture(shape, 0.0f);

        shape.set(new Vec2(getWidth() / 2, getHeight() / 2), new Vec2(getWidth() / 2, -getHeight() / 2));
        f = ground.createFixture(shape, 0.0f);
        f.m_friction = 0;
        shape.set(new Vec2(-getWidth() / 2, getHeight() / 2), new Vec2(-getWidth() / 2, -getHeight() / 2));
        f = ground.createFixture(shape, 0.0f);
        f.m_friction = 0;
    }


    protected void sendObjToClients() {
        List<SerialDTO> objectsToSend = new ArrayList<>();
        for (Player player : playersList) {
            SerialDTO heroDTO = new SerialDTO(last_step, player.getId(), player.getClass().getName(), player.getBody().getLinearVelocity(), player.getBody().getAngularVelocity(),
                    player.getBody().getPosition(), player.getLevelId());
            objectsToSend.add(heroDTO);
        }
        sendToClients(objectsToSend, getId());
    }

    public void keyPressed() {
        for (Player player : playersList) {
            if (player.isHero()) {
                if (getModel().getKeys()['a'] || getModel().getKeys()[1092]) {
                    player.left();
                }
                if (getModel().getKeys()['d'] || getModel().getKeys()[1074]) {
                    player.right();
                }
                if (getModel().getKeys()[' ']) {
                    player.jump();
                }
            }
        }
    }

    @Override
    protected Logger getLogger() {
        return log;
    }

    protected void checkMovement() {
        if (playersList.size() > 0) {
            for (Player enemy : playersList) {
                Vec2 currentVel = enemy.getBody().getLinearVelocity();
                currentVel.x = enemy.maxSpeedX;
                enemy.getBody().setLinearVelocity(currentVel);
            }
        }
    }

    protected void leftMouseAction() {
        for (Player player : playersList) {
            if (cursorInFireArea() && !player.getBody().isDestroy() && player.getWeapon1CD() == 0 && player.isHero()) {
                Body heroBullet = player.fireWeapon1(getWorldMouse());
                garbageObjectCollector.add(heroBullet, last_step + 400);
                SerialDTO heroBulletDTO = new SerialDTO(last_step, heroBullet.getId(), heroBullet.getClass().getName(), heroBullet.getLinearVelocity(),
                        heroBullet.getAngularVelocity(), heroBullet.getPosition(), 0);
                bulletList.add(heroBullet);
            }
        }
    }

    protected abstract int getLevelIndex();

    public void beginContact(Contact contact) {
        Body bodyToDestroy = null;
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        for (Player player : playersList) {

            if (objectForJump.contains(fixtureB)) {
                player.contactObjForJump.add(fixtureB);
            }


            if (objectForJump.contains(fixtureA)) {
                player.contactObjForJump.add(fixtureA);
            }
            if (movingObject.contains(fixtureB.getBody())) {
                player.objectToPush = fixtureB.getBody();
            }
            if (movingObject.contains(fixtureA.getBody())) {
                player.objectToPush = fixtureA.getBody();
            }
        }
        List<Fixture> enemyFixList = new ArrayList<>();
        for (Player enemy : playersList) {
            enemyFixList.add(enemy.getBody().getFixtureList());
        }
        for (Player player : playersList) {
            Fixture fixtureToContact = null;
            if (fixtureA.getBody() == player.getBody()) {
                fixtureToContact = fixtureB;
            } else if (fixtureB.getBody() == player.getBody()) {
                fixtureToContact = fixtureA;
            }

            if (fixtureToContact != null && enemyFixList.contains(fixtureToContact)
            ) {
                player.getBody().getLinearVelocity().x = -player.maxSpeedX;
            }
        }

        for (Gun gun : gunList) {
            if (fixtureA.m_body == gun.getBullet()) {
                bodyToDestroy = fixtureB.m_body;
            } else if (fixtureB.m_body == gun.getBullet()) {
                bodyToDestroy = fixtureA.m_body;
            }

            if (bodyToDestroy != null && gun.getBullet() != null && destroyableList.contains(bodyToDestroy)) {


                Vec2 bulletVel = gun.getBullet().getLinearVelocity();
                if (bulletVel.length() > bulletDeathVelocity) {
                    objectToExplode.add(bodyToDestroy);
                    bulletVel.x = bulletVel.x - 30;
                    gun.getBullet().setLinearVelocity(bulletVel);
                    return;
                }
            }
        }

        for (MovingObject movingObject : movingObjectList) {
            if (movingObject.getSwitcher() == fixtureA.getBody() || movingObject.getSwitcher() == fixtureB.getBody()) {
                movingObject.setActive(true);
            }
        }
        Body bullet = null;
        if (bulletList.contains(fixtureA.m_body)) {
            bodyToDestroy = fixtureB.m_body;
            bullet = fixtureA.m_body;
        } else if (bulletList.contains(fixtureB.m_body)) {
            bodyToDestroy = fixtureA.m_body;
            bullet = fixtureB.m_body;
        }

        for (Player player : playersList) {
            if (bodyToDestroy == player.getBody() && bullet == player.activeBullet) {
                return;
            }
            if (bodyToDestroy == player.getBody() && bullet == player.activeBullet) {
                return;
            }

            if (bullet != null && (bodyToDestroy == player.getBody() || playersList.contains(bodyToDestroy))) {
                Vec2 bulletVel = bullet.getLinearVelocity();
                if (bulletVel.length() > bulletDeathVelocity) {
                    objectToExplode.add(bodyToDestroy);
                }
            }
        }

        if (bodyToDestroy != null && bullet != null && destroyableList.contains(bodyToDestroy)) {
            Vec2 bulletVel = bullet.getLinearVelocity();
            if (bulletVel.length() > bulletDeathVelocity) {
                objectToExplode.add(bodyToDestroy);
                bulletVel.x = bulletVel.x - 30;
                bullet.setLinearVelocity(bulletVel);
                return;
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        for (Player player : playersList) {
            if (objectForJump.contains(fixtureB)) {
                player.contactObjForJump.remove(fixtureB);
            }
            if (objectForJump.contains(fixtureA)) {
                player.contactObjForJump.remove(fixtureA);
            }

            if (player.objectToPush == fixtureB.getBody()) {
                player.objectToPush = null;
            }

            if (player.objectToPush == fixtureA.getBody()) {
                player.objectToPush = null;
            }
        }

        for (MovingObject movingObject : movingObjectList) {
            if (movingObject.getSwitcher() == fixtureA.getBody() || movingObject.getSwitcher() == fixtureB.getBody()) {
                if (movingObject.getSwitchType() == SwitchType.HOLDING) {
                    movingObject.setActive(false);
                }
            }
        }

    }

    protected boolean cursorInFireArea() {
        return getWorldMouse().x < getWidth() / 2
                && getWorldMouse().x > -getWidth() / 2
                && getWorldMouse().y > -getHeight() / 2
                && getWorldMouse().y < getHeight() / 2;
    }

    protected void environmetsActions() {
        for (Gun gun : gunList) {
            gun.checkFire(last_step);
        }
        for (MovingObject movingObject : movingObjectList) {
            movingObject.calculateStep();
        }
    }

    protected void collectgarbage() {
        if (last_step % 20 == 0) {
            garbageObjectCollector.clear(last_step, getWorld());
        }
    }

    @Override
    public void step(SettingsIF settings) {
        super.step(settings);
        explose();
        keyPressed();
        environmetsActions();
        collectgarbage();
        descWeapon();
        last_step++;

    }

    private void descWeapon(){
        for (Player player : playersList) {
            player.decrWeapon1CD();
        }
    }
    protected void explose() {
        for (Body body : objectToExplode) {
            Vec2 oldPosition = body.getPosition();
            m_world.destroyBody(body);
            body.setDestroy(true);
            for (int i = 0; i < 10; i++) {
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(0.1f, 0.1f);
                FixtureDef fd = new FixtureDef();
                fd.shape = shape;
                fd.density = 1.0f;
                fd.friction = 0.3f;
                BodyDef bd = new BodyDef();
                bd.shapeColor = body.shapeColor;
                bd.type = BodyType.DYNAMIC;
                bd.position.set(oldPosition.x, oldPosition.y);
                Body newBody = getWorld().createBody(bd);
                if (newBody != null) {
                    newBody.createFixture(fd);
                }
            }
        }
        objectToExplode.clear();
    }

    class RayCastClosestCallback implements RayCastCallback {

        boolean m_hit;
        Vec2 m_point;
        Vec2 m_normal;

        public void init() {
            m_hit = false;
        }

        public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
            Body body = fixture.getBody();
            Object userData = body.getUserData();
            if (userData != null) {
                int index = (Integer) userData;
                if (index == 0) {
                    // filter
                    return -1f;
                }
            }
            m_hit = true;
            m_point = point;
            m_normal = normal;
            return fraction;
        }

    }

    protected void sendToClients(List<SerialDTO> objectsToSend, int id) {
        getServerLevel().addObjToSerialList(objectsToSend,id);
    }

    @Override
    public void processWorld(World world, Long tag) {
    }

    @Override
    public void processBody(Body body, Long tag) {
    }

    @Override
    public void processFixture(Fixture fixture, Long tag) {
    }

    @Override
    public void processShape(Shape shape, Long tag) {
    }

    @Override
    public void processJoint(Joint joint, Long tag) {

    }

    protected int getNextId() {
        nextId++;
        return nextId;
    }

    protected abstract float getWidth();

    protected abstract float getHeight();
}
