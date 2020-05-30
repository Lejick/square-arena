package org.jbox2d.testbed.framework;

import org.jbox2d.callbacks.DebugDraw;

import javax.swing.*;
import java.util.List;

public interface GamingModelIF {
    void setImplSpecificHelp(List<String> help);

    PlayLevel getCurrTest();

    boolean[] getCodedKeys();

    TestbedPanel getPanel();

    DebugDraw getDebugDraw();

    WorldCreator getWorldCreator();

    float getCalculatedFps();

    List<String> getImplSpecificHelp();

    boolean[] getKeys();

    SettingsIF getSettings();

     void addTestChangeListener(TestChangedListener argListener);

    int getCurrTestIndex();

    void resetKeys();

    int getTestsSize();

    boolean isTestAt(int index);

    void setCurrTestIndex(int index);

    void setRunningTest(PlayLevel nextTest);

    void setCalculatedFps(float frameRate);

    DefaultComboBoxModel getComboModel();
}
