/*******************************************************************************
 * Copyright (c) 2013, Daniel Murphy All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: * Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. * Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package org.jbox2d.fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jbox2d.testbed.framework.AbstractTestbedController;
import org.jbox2d.testbed.framework.AbstractTestbedController.MouseBehavior;
import org.jbox2d.testbed.framework.AbstractTestbedController.UpdateBehavior;
import org.jbox2d.testbed.framework.LevelsList;
import org.jbox2d.testbed.framework.PlayLevel;
import org.jbox2d.testbed.framework.PlayModel;

import java.util.ArrayList;
import java.util.List;

/**
 * The entry point for the testbed application
 *
 * @author Daniel Murphy
 */
public class ClientMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        final PlayControllerJavaFX serverController = new PlayControllerJavaFX(new PlayModel(),
                UpdateBehavior.UPDATE_CALLED, MouseBehavior.NORMAL, (Exception e, String message) -> {
            new Alert(Alert.AlertType.ERROR).showAndWait();
        });
        Stage serverStage = createStage("War of Shapes", serverController);
        serverStage.show();

        final PlayControllerJavaFX clientController1 = new PlayControllerJavaFX(new PlayModel(),
                UpdateBehavior.UPDATE_CALLED, MouseBehavior.NORMAL, (Exception e, String message) -> {
            new Alert(Alert.AlertType.ERROR).showAndWait();
        });
        Stage clientStage1 = createStage("War of Shapes", clientController1);
        clientStage1.show();


        final PlayControllerJavaFX clientController2 = new PlayControllerJavaFX(new PlayModel(),
                UpdateBehavior.UPDATE_CALLED, MouseBehavior.NORMAL, (Exception e, String message) -> {
            new Alert(Alert.AlertType.ERROR).showAndWait();
        });
        Stage clientStage2 = createStage("War of Shapes", clientController2);
        clientStage2.show();

        Platform.runLater(() -> {
            serverController.playTest(1);
            serverController.getModel().getCurrTest().setServerLevel(serverController.getModel().getCurrTest());
            serverController.getModel().getCurrTest().setId(0);
            serverController.start();
            clientController1.playTest(0);
            clientController1.getModel().getCurrTest().setServerLevel(serverController.getModel().getCurrTest());
            clientController1.getModel().getCurrTest().setId(1);
            clientController1.start();
            clientController2.playTest(0);
            clientController2.getModel().getCurrTest().setServerLevel(serverController.getModel().getCurrTest());
            clientController2.getModel().getCurrTest().setId(2);
            clientController2.start();
            List<PlayLevel> levelList=new ArrayList<>();
            levelList.add(clientController1.getModel().getCurrTest());
            levelList.add(clientController2.getModel().getCurrTest());
            serverController.getModel().getCurrTest().setClientLevelList(levelList);
        });
    }

    private Stage createStage(String title, AbstractTestbedController controller) {
        PlayModel clientModel = (PlayModel) controller.getModel();
        BorderPane clientTestbed = new BorderPane();

        PlayPanelJavaFX clientPanel = new PlayPanelJavaFX(clientModel, controller, clientTestbed);
        clientModel.setPanel(clientPanel);
        clientModel.setDebugDraw(new DebugPlayDrawJavaFX(clientPanel, true));

        Scene clientScene = new Scene(clientTestbed, PlayPanelJavaFX.INIT_WIDTH, PlayPanelJavaFX.INIT_HEIGHT);
        LevelsList.populateModel(clientModel, controller, clientScene);


        clientTestbed.setCenter(clientPanel);

        clientTestbed.setRight(new ScrollPane(new PlaySidePanel(clientModel, controller)));

        Stage stage = new Stage();

        stage.setScene(clientScene);
        stage.setTitle(title);
        return stage;
    }
}
