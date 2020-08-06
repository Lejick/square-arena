package org.jbox2d.testbed.levels;

import javafx.scene.Scene;
import org.jbox2d.common.Color3f;
import org.jbox2d.dynamics.*;
import org.jbox2d.testbed.Player;
import org.jbox2d.testbed.framework.AbstractTestbedController;
import org.jbox2d.testbed.framework.SettingsIF;
import org.jbox2d.testbed.framework.game.objects.*;

import java.util.*;

/**
 * @author Oleg Trohov
 */
public abstract class CommonLevelClient extends CommonLevel {
    protected CommonLevelClient(AbstractTestbedController controller, Scene scene) {
        super(controller, scene);
    }

    protected Player hero;
    private Map<Integer, Player> enemyList = new HashMap<>();

    @Override
    protected void createGameObject() {
        List<SerialDTO> list = getServerLevel().getObjToSerialList(getId());
        for (SerialDTO serialDTO : list) {
            Color3f color3f = Color3f.RED;
            if (serialDTO.getLevelId() == getId()) {
                color3f = Color3f.BLUE;
            }
            Body playerBody = GeometryBodyFactory.createRectangle(serialDTO.getPosition().x, serialDTO.getPosition().y, commonPersonEdge, commonPersonEdge, BodyType.DYNAMIC, getWorld(), color3f);
            Player player = new Player(playerBody, getWorld(), serialDTO.getId());
            player.setLevelId(serialDTO.getLevelId());
            if (serialDTO.getLevelId() == getId()) {
                player.setHero(true);
                hero = player;
            } else {
                enemyList.put(player.getId(), player);
            }
            playersList.put(player.getId(), player);
        }
    }


    protected void applyObjects() {
        List<SerialDTO> list = getServerLevel().getObjToSerialList(getId());
        for (Player enemy : enemyList.values()) {
            getWorld().destroyBody(enemy.getBody());
        }
        for (SerialDTO serialDTO : list) {
            Color3f color3f = Color3f.RED;
            Body playerBody = GeometryBodyFactory.createRectangle(serialDTO.getPosition().x, serialDTO.getPosition().y, commonPersonEdge, commonPersonEdge, BodyType.DYNAMIC, getWorld(), color3f);
            Player player = new Player(playerBody, getWorld(), serialDTO.getId());
            player.setLevelId(serialDTO.getLevelId());
            enemyList.put(player.getId(), player);
        }
    }

    protected void sendObjToServer() {
        List<SerialDTO> objectsToSend = new ArrayList<>();
        SerialDTO heroDTO = new SerialDTO(last_step, hero.getId(), hero.getClass().getName(), hero.getBody().getLinearVelocity(), hero.getBody().getAngularVelocity(),
                hero.getBody().getPosition(), hero.getLevelId());
        objectsToSend.add(heroDTO);
        getServerLevel().addObjToSerialListServer(objectsToSend, id);
    }


    @Override
    public void step(SettingsIF settings) {
        applyObjects();
        keyPressed();
        super.step(settings);
        sendObjToServer();
    }

    protected void leftMouseAction() {
        if (cursorInFireArea() && !hero.getBody().isDestroy() && hero.getWeapon1CD() == 0) {
            Body heroBullet = hero.fireWeapon1(getWorldMouse());
            garbageObjectCollector.add(heroBullet, last_step + 400);
            bulletList.add(heroBullet);
        }
    }

    public void keyPressed() {
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

}
