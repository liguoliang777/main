// IPadCommand.aidl
package com.ngds.pad;

import com.ngds.pad.PadInfo;

// Declare any non-default types here with import statements

interface IPadCommand {
    void registerCallback(String packageName);
    void unRegisterCallback(String packageName);
    int execCommand(int type, String mac, in byte[] data);
    PadInfo[] getPadList();
    boolean isKeysDown(int controllerID, in int[] keyArray);
    boolean setName(String mac, in byte[] data);
    boolean ota(String mac, in byte[] data);
    boolean setMotor(int controllerID, int left, int right);
    boolean setVibrate(int controllerID, float left, float right);
    boolean setMode(String mac, byte mode);
    boolean setLight(int controllerID, byte r, byte g, byte b);
}
