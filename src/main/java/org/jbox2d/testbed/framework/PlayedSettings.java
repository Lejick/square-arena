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
 * Created at 1:58:18 PM Jul 17, 2010
 */
/**
 * Created at 1:58:18 PM Jul 17, 2010
 */
package org.jbox2d.testbed.framework;

import java.util.*;

/**
 * Stores all the testbed settings.  Automatically populates default settings.
 *
 * @author Daniel Murphy
 */
public class PlayedSettings implements SettingsIF {
    public static final String AllowSleep = "Sleep";
    public static final String WarmStarting = "Warm Starting";
    public static final String SubStepping = "SubStepping";
    public static final String ContinuousCollision = "Continuous Collision";
    public static final String DrawShapes = "Shapes";
    public static final String DrawJoints = "Joints";
    public static final String DrawAABBs = "AABBs";
    public static final String DrawContactPoints = "Contact Points";
    public static final String DrawContactNormals = "Contact Normals";
    public static final String DrawContactImpulses = "Contact Impulses";
    public static final String DrawFrictionImpulses = "Friction Impulses";
    public static final String DrawCOMs = "Center of Mass";
    public static final String DrawStats = "Stats";
    public static final String DrawHelp = "Help";
    public static final String DrawTree = "Dynamic Tree";

    public boolean pause = false;
    public boolean singleStep = false;

    private List<TestbedSetting> settings;
    private final Map<String, TestbedSetting> settingsMap;

    public PlayedSettings() {
        settings = new ArrayList<>();
        settingsMap = new HashMap<>();
        populateDefaultSettings();
    }

    private void populateDefaultSettings() {
        //addSetting(new TestbedSetting(AllowSleep, SettingType.ENGINE, true));
      //  addSetting(new TestbedSetting(WarmStarting, SettingType.ENGINE, true));
       // addSetting(new TestbedSetting(ContinuousCollision, SettingType.ENGINE, true));
       // addSetting(new TestbedSetting(SubStepping, SettingType.ENGINE, false));
     //   addSetting(new TestbedSetting(DrawShapes, SettingType.DRAWING, true));
      //  addSetting(new TestbedSetting(DrawJoints, SettingType.DRAWING, true));
     //   addSetting(new TestbedSetting(DrawAABBs, SettingType.DRAWING, false));
       // addSetting(new TestbedSetting(DrawContactPoints, SettingType.DRAWING, false));
    //    addSetting(new TestbedSetting(DrawContactNormals, SettingType.DRAWING, false));
     //   addSetting(new TestbedSetting(DrawContactImpulses, SettingType.DRAWING, false));
      //  addSetting(new TestbedSetting(DrawFrictionImpulses, SettingType.DRAWING, false));
     //   addSetting(new TestbedSetting(DrawCOMs, SettingType.DRAWING, false));
     //   addSetting(new TestbedSetting(DrawStats, SettingType.DRAWING, true));
     //   addSetting(new TestbedSetting(DrawHelp, SettingType.DRAWING, false));
      //  addSetting(new TestbedSetting(DrawTree, SettingType.DRAWING, false));
    }

    /**
     * Adds a settings to the settings list
     * @param argSetting
     */
    public void addSetting(TestbedSetting argSetting) {
        if (settingsMap.containsKey(argSetting.name)) {
            throw new IllegalArgumentException("Settings already contain a setting with name: "
                    + argSetting.name);
        }
        settings.add(argSetting);
        settingsMap.put(argSetting.name, argSetting);
    }

    /**
     * Returns an unmodifiable list of settings
     * @return
     */
    public List<TestbedSetting> getSettings() {
        return Collections.unmodifiableList(settings);
    }

    @Override
    public boolean isSingleStep() {
        return singleStep;
    }

    @Override
    public void setSingleStep(boolean singleStep) {
        this.singleStep = singleStep;
    }

    @Override
    public boolean isPause() {
        return pause;
    }

    @Override
    public void setPause(boolean pause) {
        this.pause = pause;
    }

    /**
     * Gets a setting by name.
     * @param argName
     * @return
     */
    public TestbedSetting getSetting(String argName) {
        return settingsMap.get(argName);
    }
}
