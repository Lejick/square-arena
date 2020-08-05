package org.jbox2d.testbed.levels;

import javafx.scene.Scene;
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
import org.jbox2d.testbed.framework.AbstractTestbedController;
import org.jbox2d.testbed.framework.PlayLevel;
import org.jbox2d.testbed.framework.SettingsIF;
import org.jbox2d.testbed.framework.game.objects.*;
import org.jbox2d.testbed.framework.utils.GarbageObjectCollector;
import org.jbox2d.testbed.framework.utils.Line;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Oleg Trohov
 */
public abstract class CommonLevelServer extends CommonLevel {
    public CommonLevelServer(AbstractTestbedController controller, Scene scene) {
        super(controller,scene);
    }
    @Override
    protected  void createGameObject(){
        List<SerialDTO> objectsToSend = new ArrayList<>();
        int startY=-23;
        for (PlayLevel playLevel : clientLevelList) {
            int playerId = getNextId();
            levelToHeroIdsMap.putIfAbsent(playLevel.getId(), playerId);
            Body playerBody = GeometryBodyFactory.createRectangle(-30, startY, commonPersonEdge, commonPersonEdge, BodyType.DYNAMIC, getWorld(), Color3f.RED);
            startY+=15;
            Player player = new Player(playerBody, getWorld());
            player.setId(playerId);
            player.setLevelId(playLevel.getId());
            playersList.add(player);
            SerialDTO heroDTO = new SerialDTO(last_step, player.getId(), player.getClass().getName(), player.getBody().getLinearVelocity(), player.getBody().getAngularVelocity(),
                    player.getBody().getPosition(), playLevel.getId());
            objectsToSend.add(heroDTO);
            sendToClients(objectsToSend, playLevel.getId());
        }
    }
      protected void applyAndResolveConflicts() {
        for (Player player : playersList) {
            getWorld().destroyBody(player.getBody());
        }
        playersList.clear();
        List<SerialDTO> list = getServerLevel().getObjToSerialList();
        for (SerialDTO serialDTO : list) {
            Color3f color3f = Color3f.RED;
            if (serialDTO.getLevelId() == getId()) {
                color3f = Color3f.BLUE;
            }
            Body playerBody = GeometryBodyFactory.createRectangle(serialDTO.getPosition().x, serialDTO.getPosition().y, commonPersonEdge, commonPersonEdge, BodyType.DYNAMIC, getWorld(), color3f);
            Player player = new Player(playerBody, getWorld());
            player.setLevelId(serialDTO.getLevelId());
            if (serialDTO.getLevelId() == getId()) {
                player.setHero(true);
            }
            playersList.add(player);
        }
    }

    @Override
    public void step(SettingsIF settings) {
        applyAndResolveConflicts();
        super.step(settings);
    }

}