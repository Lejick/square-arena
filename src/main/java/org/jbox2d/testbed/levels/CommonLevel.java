package org.jbox2d.testbed.levels;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.testbed.Player;
import org.jbox2d.testbed.framework.*;
import org.jbox2d.testbed.framework.game.objects.Gun;
import org.jbox2d.testbed.framework.game.objects.MovingObject;
import org.jbox2d.testbed.framework.game.objects.SwitchType;
import org.jbox2d.testbed.framework.utils.GarbageObjectCollector;
import org.jbox2d.testbed.framework.utils.Line;
import org.jbox2d.testbed.framework.utils.LineIntersectChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Daniel Murphy
 */
public abstract class CommonLevel extends PlayLevel {
    protected static final Logger log = LoggerFactory.getLogger(CommonLevel.class);

    protected List<Line> linesList = new ArrayList<>();

    protected static final float commonPersonEdge = 1f;
    long last_step = 0;
    private static float bulletDeathVelocity = 75;
    protected List<Fixture> objectForJump = new ArrayList<>();
    protected List<Body> bulletList = new ArrayList<>();
    protected Player hero;
    protected int nextId = 0;
    protected AbstractTestbedController controller;
    protected List<Body> movingObject = new ArrayList<>();
    protected List<Object> objToSerialList = new ArrayList<>();
    protected List<Gun> gunList = new ArrayList<>();
    protected List<Body> destroyableList = Collections.synchronizedList(new ArrayList<>());
    protected List<Body> objectToExplode = Collections.synchronizedList(new ArrayList<>());
    protected Scene scene;
    protected List<Fixture> leftBlockedFixtures = new ArrayList<>();
    protected List<Fixture> rightBlockedFixtures = new ArrayList<>();
    protected List<MovingObject> movingObjectList = new ArrayList<>();
    protected List<Player> playersList;
    RayCastClosestCallback ccallback;
    GarbageObjectCollector garbageObjectCollector = new GarbageObjectCollector();

    @Override
    public boolean isSaveLoadEnabled() {
        return true;
    }

    public CommonLevel(AbstractTestbedController controller, Scene scene) {
        this.controller = controller;
        this.scene = scene;
    }

    @Override
    public void initTest(boolean deserialized) {
        ccallback = new RayCastClosestCallback();
        garbageObjectCollector = new GarbageObjectCollector();
        last_step = 0;
        playersList = new ArrayList<>();
        createGameBox();
        createPlatforms();
    }


    protected abstract void createPlatforms();

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
        rightBlockedFixtures.add(f);
        f.m_friction = 0;
        shape.set(new Vec2(-getWidth() / 2, getHeight() / 2), new Vec2(-getWidth() / 2, -getHeight() / 2));
        f = ground.createFixture(shape, 0.0f);
        leftBlockedFixtures.add(f);
        f.m_friction = 0;
    }

    public void keyPressed() {
        if (hero == null) {
            return;
        }
        if (getModel().getKeys()['a'] || getModel().getKeys()[1092]) {
            hero.left();
        }
        if (getModel().getKeys()['d'] || getModel().getKeys()[1074]) {
            hero.right();
        }
        if (getModel().getKeys()[' ']) {
            hero.jump();
        }
    }

    protected void endLevel() {
        getModel().getSettings().setPause(true);
        getModel().resetKeys();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText("Good game. You won! Click OK to restart.");
        alert.setOnHidden(evt -> {
            Platform.runLater(() -> {
                getModel().getSettings().setPause(false);
                controller.playTest(getLevelIndex());
                controller.reset();
            });
        });
        alert.show();
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

    protected void checkEnemyFireAction() {
        if (playersList.size() > 0 && hero != null) {
            for (Player enemy : playersList) {
                if (!enemy.getBody().isDestroy() && !hero.getBody().isDestroy() && enemy.getWeapon1CD() == 0) {
                    Line fireLine = new Line(hero.getBody().getPosition(), enemy.getBody().getPosition());
                    boolean isVisible = true;
                    for (Line line : linesList) {
                        if (LineIntersectChecker.doIntersect(fireLine, line)) {
                            isVisible = false;
                            break;
                        }
                    }

                    if (isVisible) {
                        Body enemy_bullet = enemy.fireWeapon1(hero.getBody().getPosition());
                        if (enemy_bullet != null) {
                            bulletList.add(enemy_bullet);
                            garbageObjectCollector.add(enemy_bullet, last_step + 400);
                            objToSerialList.add(enemy_bullet);
                        }
                    }
                }
            }
        }
        for (Player player : playersList) {
            player.decrWeapon1CD();
        }
        hero.decrWeapon1CD();
    }


    protected void leftMouseAction() {
        if (cursorInFireArea() && !hero.getBody().isDestroy() && hero.getWeapon1CD() == 0) {
            Body heroBullet = hero.fireWeapon1(getWorldMouse());
            garbageObjectCollector.add(heroBullet, last_step + 400);
            bulletList.add(heroBullet);
            objToSerialList.add(heroBullet);
        }
    }

    protected abstract int getLevelIndex();

    public void beginContact(Contact contact) {
        Body bodyToDestroy = null;
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (isHero(fixtureA.getBody())) {
            if (objectForJump.contains(fixtureB)) {
                hero.contactObjForJump.add(fixtureB);
            }
        }
        if (isHero(fixtureB.getBody())) {
            if (objectForJump.contains(fixtureA)) {
                hero.contactObjForJump.add(fixtureA);
            }
        }

        if (isHero(fixtureA.getBody())) {
            if (movingObject.contains(fixtureB.getBody())) {
                hero.objectToPush = fixtureB.getBody();
            }
        }
        if (isHero(fixtureB.getBody())) {
            if (movingObject.contains(fixtureA.getBody())) {
                hero.objectToPush = fixtureA.getBody();
            }
        }

        if (isHero(fixtureA.getBody())) {
            if (leftBlockedFixtures.contains(fixtureB)) {
                hero.blockedFromLeft = true;
            }
        }
        if (isHero(fixtureB.getBody())) {
            if (leftBlockedFixtures.contains(fixtureA)) {
                hero.blockedFromLeft = true;
            }
        }
        List<Fixture> enemyFixList = new ArrayList<>();
        for (Player enemy : playersList) {
            enemyFixList.add(enemy.getBody().getFixtureList());
        }
        for (Player enemy : playersList) {
            Fixture fixtureToContact = null;
            if (fixtureA.getBody() == enemy.getBody()) {
                fixtureToContact = fixtureB;
            } else if (fixtureB.getBody() == enemy.getBody()) {
                fixtureToContact = fixtureA;
            }

            if (fixtureToContact != null && (leftBlockedFixtures.contains(fixtureToContact) ||
                    rightBlockedFixtures.contains(fixtureToContact) ||
                    enemyFixList.contains(fixtureToContact))
            ) {
                enemy.getBody().getLinearVelocity().x = -enemy.maxSpeedX;
            }
        }

        if (isHero(fixtureA.getBody()) && (leftBlockedFixtures.contains(fixtureB) || rightBlockedFixtures.contains(fixtureB))) {
            hero.canPush = true;
        }

        if (isHero(fixtureB.getBody()) && (leftBlockedFixtures.contains(fixtureA) || rightBlockedFixtures.contains(fixtureA))) {
            hero.canPush = true;
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

        if (bodyToDestroy == hero.getBody() && bullet == hero.activeBullet) {
            return;
        }
        for (Player enemy : playersList) {
            if (bodyToDestroy == enemy.getBody() && bullet == enemy.activeBullet) {
                return;
            }
        }

        if (bullet != null && (bodyToDestroy == hero.getBody() || playersList.contains(bodyToDestroy))) {
            Vec2 bulletVel = bullet.getLinearVelocity();
            if (bulletVel.length() > bulletDeathVelocity) {
                objectToExplode.add(bodyToDestroy);
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
        if (isHero(fixtureA.getBody())) {
            if (objectForJump.contains(fixtureB)) {
                hero.contactObjForJump.remove(fixtureB);
            }
        }
        if (isHero(fixtureB.getBody())) {
            if (objectForJump.contains(fixtureA)) {
                hero.contactObjForJump.remove(fixtureA);
            }
        }

        if (isHero(fixtureA.getBody())) {
            if (leftBlockedFixtures.contains(fixtureB)) {
                hero.blockedFromLeft = false;
            }
        }
        if (isHero(fixtureB.getBody())) {
            if (leftBlockedFixtures.contains(fixtureA)) {
                hero.blockedFromLeft = false;
            }
        }

        if (isHero(fixtureA.getBody()) && hero.objectToPush == fixtureB.getBody()) {
            hero.objectToPush = null;
        }

        if (isHero(fixtureB.getBody()) && hero.objectToPush == fixtureA.getBody()) {
            hero.objectToPush = null;
        }

        if (isHero(fixtureA.getBody()) && (leftBlockedFixtures.contains(fixtureB) || rightBlockedFixtures.contains(fixtureB))) {
            hero.canPush = false;
        }

        if (isHero(fixtureB.getBody()) && (leftBlockedFixtures.contains(fixtureA) || rightBlockedFixtures.contains(fixtureA))) {
            hero.canPush = false;
        }

        for (MovingObject movingObject : movingObjectList) {
            if ((isHero(fixtureA.getBody()) || isHero(fixtureB.getBody())) &&
                    movingObject.getSwitcher() == fixtureA.getBody() || movingObject.getSwitcher() == fixtureB.getBody()) {
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

    protected void drawTargetElements() {
        if (cursorInFireArea()) {
            scene.setCursor(Cursor.CROSSHAIR);
        } else {
            scene.setCursor(Cursor.DEFAULT);
        }

        if (!hero.getBody().isDestroy() && hero.getWeapon1CD() == 0) {
            ccallback.init();
            Vec2 point1 = hero.getBody().getPosition();
            Vec2 point2 = getWorldMouse();
            getWorld().raycast(ccallback, point1, point2);
            if (ccallback.m_hit) {
                getDebugDraw().drawPoint(ccallback.m_point, 3.0f, new Color3f(0, 0, 0));
                getDebugDraw().drawSegment(point1, ccallback.m_point, new Color3f(1, 0f, 0));
            } else {
                getDebugDraw().drawSegment(point1, point2, new Color3f(1, 0, 0));
            }
        }
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
        drawTargetElements();
        explose();
        keyPressed();
        checkMovement();
        checkEnemyFireAction();
        environmetsActions();
        collectgarbage();
        last_step++;
    }

    private void explose() {
        for (Body body : objectToExplode) {
            for (Player player : playersList) {
                if (player.getBody() == body) {
                    int frags = hero.getEnemyKilled();
                    frags++;
                    hero.setEnemyKilled(frags);
                }
            }

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

    protected boolean isHero(Body body) {
        return body == hero.getBody();
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

    @Override
    public Long getTag(World world) {
        return null;
    }

    @Override
    public Long getTag(Body body) {
        return null;
    }

    @Override
    public Long getTag(Shape shape) {
        return null;
    }

    @Override
    public Long getTag(Joint joint) {
        return null;
    }

    @Override
    public Long getTag(Fixture fixture) {
        return null;
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

    protected abstract float getWidth();

    protected abstract float getHeight();
}
