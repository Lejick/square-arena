/**
 * Copyright (c) 2013, Daniel Murphy
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <p>
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
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 */
/**
 * Created at 4:56:29 AM Jan 14, 2011
 */
package org.jbox2d.testbed.levels;

import javafx.scene.Scene;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.testbed.Player;
import org.jbox2d.testbed.framework.AbstractTestbedController;
import org.jbox2d.testbed.framework.PlayLevel;
import org.jbox2d.testbed.framework.SettingsIF;
import org.jbox2d.testbed.framework.game.objects.GeometryBodyFactory;
import org.jbox2d.testbed.framework.game.objects.SerialDTO;
import org.jbox2d.testbed.framework.utils.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Oleg Trohov
 */
public class Arena02 extends CommonLevel {
    private static float width = 80;
    private static float height = 60;

    public Arena02(AbstractTestbedController controller, Scene scene) {
        super(controller, scene);
    }

    @Override
    public void initTest() {
        super.initTest();
        createGameObjects();
    }

    protected void createGameObjects() {
        List<SerialDTO> objectsToSend = new ArrayList<>();
        int startY=-23;
        if (isServer) {
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
        } else {
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
    }

    protected void createPlatforms() {
        float startPointY = -25;
        float deltaX = 20;
        float deltaY = 12;
        for (int j = 0; j < 5; j++) {
            float startPointX = -30;
            for (int i = 0; i <= 3; i++) {
                Body b = GeometryBodyFactory.createGameBrick(startPointX, startPointY, 4f, 0.5f, BodyType.STATIC, getWorld());
                List<Line> lines = GeometryBodyFactory.splitRectangle(startPointX, startPointY, 6f, 0.5f);
                linesList.addAll(lines);
                b.getFixtureList().m_friction = 2;
                objectForJump.add(b.getFixtureList());
                startPointX = startPointX + deltaX;
            }
            startPointY = startPointY + deltaY;
        }
        startPointY = -19;
        deltaX = 20;
        deltaY = 12;
        for (int j = 0; j < 4; j++) {
            float startPointX = -20;
            for (int i = 0; i <= 2; i++) {
                Body b = GeometryBodyFactory.createGameBrick(startPointX, startPointY, 4f, 0.5f, BodyType.STATIC, getWorld());
                List<Line> lines = GeometryBodyFactory.splitRectangle(startPointX, startPointY, 6f, 0.5f);
                linesList.addAll(lines);
                b.getFixtureList().m_friction = 2;
                objectForJump.add(b.getFixtureList());
                startPointX = startPointX + deltaX;
            }
            startPointY = startPointY + deltaY;
        }


    }

    @Override
    public void step(SettingsIF settings) {
        super.step(settings);
    }

    @Override
    protected int getLevelIndex() {
        return 1;
    }

    @Override
    public String getLevelName() {
        return "Arena 02";
    }

    @Override
    public String getLevelDescription() {
        return "Kill Enemy";
    }

    @Override
    protected float getWidth() {
        return width;
    }

    @Override
    protected float getHeight() {
        return height;
    }
}
