package com.ngds.pad;

import com.lx.pad.util.LLog;
import com.ngds.pad.Msg.LooperEventManager;
import com.ngds.pad.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/29.
 */

/**
 * 针对蓝牙BLE的通讯包管理类
 */

public class ProtocolBle {
    public abstract class PkgBle{
        protected byte[] m_pkgData;
        protected byte[] m_pkgHeader;
        protected long m_time;
        protected int m_deviceId;

        public PkgBle(){
            super();
            m_pkgData = null;
            m_pkgHeader = null;
            m_time = System.currentTimeMillis();
            m_deviceId = 1;
        }

        public abstract int pkgType();

        public void setDeviceID(int deviceId){
            m_deviceId = deviceId;
        }

        public void setData(byte[] content){
            m_pkgData = content;
        }

        protected void init(){
            m_pkgHeader = new byte[3];
            m_pkgHeader[0] = 0x20;
            m_pkgHeader[2] = (byte)pkgType();
        }

        public boolean encodeData(byte[] data){
            return false;
        }

        public byte[] catPkgBuf(){
            init();
            byte[] result = m_pkgData != null ? Utils.catByteAry(m_pkgHeader, m_pkgData) : m_pkgHeader;
            result[1] = (byte)result.length;
            return result;
        }
    }

    public class PkgBleMgr extends PkgBle{
        int m_LStickY;
        int m_RStickX;
        int m_RStickY;
        List<PadKeyEvent> m_padKeyEventList;
        List<PadMotionEvent> m_padMotionEventList;
        byte[] m_pkgBuf;
        boolean m_keyUpPress;
        boolean m_keyDownPress;
        boolean m_keyLeftPress;
        boolean m_keyRightPress;
        boolean m_keyStartPress;
        boolean m_keyBackPress;
        boolean m_LeftStickPress;
        boolean m_RightStickPress;
        boolean m_keyHelpPress;
        boolean m_keyKeyboardPress;
        boolean m_keyAPress;
        boolean m_keyBPress;
        boolean m_keyXPress;
        boolean m_keyYPress;
        boolean m_keyL1Press;
        boolean m_keyR1Press;
        int m_keyL2Pressure;
        int m_keyR2Pressure;
        int m_LStickX;

        public PkgBleMgr(){
            super();
            m_padKeyEventList = new ArrayList<PadKeyEvent>();
            m_padMotionEventList = new ArrayList<PadMotionEvent>();
            m_pkgBuf = new byte[32];
            m_keyUpPress = false;
            m_keyDownPress = false;
            m_keyLeftPress = false;
            m_keyRightPress = false;
            m_keyStartPress = false;
            m_keyBackPress = false;
            m_LeftStickPress = false;
            m_RightStickPress = false;
            m_keyHelpPress = false;
            m_keyKeyboardPress = false;
            m_keyAPress = false;
            m_keyBPress = false;
            m_keyXPress = false;
            m_keyYPress = false;
            m_keyL1Press = false;
            m_keyR1Press = false;
            m_keyL2Pressure = 0;
            m_keyR2Pressure = 0;
            m_LStickX = 128;
            m_LStickY = 128;
            m_RStickX = 128;
            m_RStickY = 128;
        }

        private float stickPercent(int pressure, boolean isY){
            float result = 0f;
            if(pressure == 128){
                result = 0f;
            }else{
                if(pressure < 0){
                    result = -1f;
                }else if(pressure >= 255){
                    result = 1f;
                }else if(pressure > 128){
                    result = (pressure - 128) / 127f;
                }else {
                    result = (pressure - 128) / 128f;
                }

                if(!isY){
                    return result;
                }

                result = -result;
            }
            return result;
        }

        private void addPadKeyEventToList(int keyCode, int action, float pressure){
//            m_padKeyEventList.add(new PadKeyEvent(m_time, m_deviceId, keyCode, action, pressure));
            LooperEventManager.sendEventMsg(LooperEventManager.MSG_PAD_KEYEVENT, new PadKeyEvent(m_time, m_deviceId, keyCode, action, pressure));
        }

        private void addPadMotionEventToList(int keyCode, int axis_x, int axis_y){
//            m_padMotionEventList.add(new PadMotionEvent(m_time, m_deviceId, keyCode, stickPercent(axis_x, false), stickPercent(axis_y, true)));
            LooperEventManager.sendEventMsg(LooperEventManager.MSG_PAD_MOTIONEVENT, new PadMotionEvent(m_time, m_deviceId, keyCode, stickPercent(axis_x, false), stickPercent(axis_y, true)));
        }

        public float pressurePercent(int pressure){
            float result = 1f;
            if(pressure <= 0){
                result = 0f;
            }else if(pressure < 255){
                result = pressure / 255f;
            }
            return result;
        }

        private void addPressureKeyToList(int keyCode, int pressure){
            addPadKeyEventToList(keyCode, BaseEvent.ACTION_PRESSURE, pressurePercent(pressure));
        }

        @Override
        public int pkgType() {
            return 1;
        }

        public void checkEvent(PkgBleMgr lastKeyInfo){
            if(lastKeyInfo == null){
                lastKeyInfo = new PkgBleMgr();
            }

            if(m_pkgBuf[3] != lastKeyInfo.m_pkgBuf[3]){
                if(m_keyUpPress != lastKeyInfo.m_keyUpPress){
                    addKeyEventToList(BaseEvent.KEYCODE_DPAD_UP, m_keyUpPress);
                }
                if(m_keyDownPress != lastKeyInfo.m_keyDownPress){
                    addKeyEventToList(BaseEvent.KEYCODE_DPAD_DOWN, m_keyDownPress);
                }
                if(m_keyLeftPress != lastKeyInfo.m_keyLeftPress){
                    addKeyEventToList(BaseEvent.KEYCODE_DPAD_LEFT, m_keyLeftPress);
                }
                if(m_keyRightPress != lastKeyInfo.m_keyRightPress){
                    addKeyEventToList(BaseEvent.KEYCODE_DPAD_RIGHT, m_keyRightPress);
                }
                if(m_keyBackPress != lastKeyInfo.m_keyBackPress){
                    addKeyEventToList(BaseEvent.KEYCODE_BACK, m_keyBackPress);
                }
                if(m_keyStartPress != lastKeyInfo.m_keyStartPress){
                    addKeyEventToList(BaseEvent.KEYCODE_BUTTON_START, m_keyStartPress);
                }
                if(m_LeftStickPress != lastKeyInfo.m_LeftStickPress){
                    addKeyEventToList(BaseEvent.KEYCODE_BUTTON_THUMBL, m_LeftStickPress);
                }
                if(m_RightStickPress != lastKeyInfo.m_RightStickPress){
                    addKeyEventToList(BaseEvent.KEYCODE_BUTTON_THUMBR, m_RightStickPress);
                }
            }

            if(m_pkgBuf[4] != lastKeyInfo.m_pkgBuf[4]){
                if(m_keyHelpPress != lastKeyInfo.m_keyHelpPress){
                    addKeyEventToList(BaseEvent.KEYCODE_BUTTON_HELP, m_keyHelpPress);
                }
                if(m_keyKeyboardPress != lastKeyInfo.m_keyKeyboardPress){
                    addKeyEventToList(BaseEvent.KEYCODE_BUTTON_KEYBOARD, m_keyKeyboardPress);
                }
                if(m_keyAPress != lastKeyInfo.m_keyAPress){
                    addKeyEventToList(BaseEvent.KEYCODE_BUTTON_A, m_keyAPress);
                }
                if(m_keyBPress != lastKeyInfo.m_keyBPress){
                    addKeyEventToList(BaseEvent.KEYCODE_BUTTON_B, m_keyBPress);
                }
                if(m_keyXPress != lastKeyInfo.m_keyXPress){
                    addKeyEventToList(BaseEvent.KEYCODE_BUTTON_X, m_keyXPress);
                }
                if(m_keyYPress != lastKeyInfo.m_keyYPress){
                    addKeyEventToList(BaseEvent.KEYCODE_BUTTON_Y, m_keyYPress);
                }
                if(m_keyL1Press != lastKeyInfo.m_keyL1Press){
                    addKeyEventToList(BaseEvent.KEYCODE_BUTTON_L1, m_keyL1Press);
                }
                if(m_keyR1Press != lastKeyInfo.m_keyR1Press){
                    addKeyEventToList(BaseEvent.KEYCODE_BUTTON_R1, m_keyR1Press);
                }
            }
            if(m_keyL2Pressure != lastKeyInfo.m_keyL2Pressure){
                addPressureKeyToList(BaseEvent.KEYCODE_BUTTON_L2, m_keyL2Pressure);
            }
            if(m_keyR2Pressure != lastKeyInfo.m_keyR2Pressure){
                addPressureKeyToList(BaseEvent.KEYCODE_BUTTON_R2, m_keyR2Pressure);
            }
            if(m_LStickX != lastKeyInfo.m_LStickX || m_LStickY != lastKeyInfo.m_LStickY){
                addPadMotionEventToList(BaseEvent.KEYCODE_LEFT_STICK, m_LStickX, m_LStickY);
            }
            if(m_RStickX != lastKeyInfo.m_RStickX || m_RStickY != lastKeyInfo.m_RStickY){
                addPadMotionEventToList(BaseEvent.KEYCODE_RIGHT_STICK, m_RStickX, m_RStickY);
            }
        }

        private void addKeyEventToList(int keyCode, boolean press){
            addPadKeyEventToList(keyCode, press ? BaseEvent.ACTION_DOWN : BaseEvent.ACTION_UP, 1f);
        }

        @Override
        public boolean encodeData(byte[] data) {
            boolean result = true;
            LLog.d("ProtocolBle->PkgBleMgr->encodeData data:" + LLog.covertHexStr(data));

            try{
                m_pkgBuf = new byte[data.length];
                System.arraycopy(data, 0, m_pkgBuf, 0, m_pkgBuf.length);
                m_keyUpPress = (data[3] & 0x1) > 0 ? true : false;
                m_keyDownPress = (data[3] & 0x2) > 0 ? true : false;
                m_keyLeftPress = (data[3] & 0x4) > 0 ? true : false;
                m_keyRightPress = (data[3] & 0x8) > 0 ? true : false;
                m_keyStartPress = (data[3] & 0x10) > 0 ? true : false;
                m_keyBackPress = (data[3] & 0x20) > 0 ? true : false;
                m_LeftStickPress = (data[3] & 0x40) > 0 ? true : false;
                m_RightStickPress = (data[3] & 0x80) > 0 ? true : false;
                m_keyHelpPress = (data[4] & 0x1) > 0 ? true : false;
                m_keyKeyboardPress = (data[4] & 0x2) > 0 ? true : false;
                m_keyAPress = (data[4] & 0x4) > 0 ? true : false;
                m_keyBPress = (data[4] & 0x8) > 0 ? true : false;
                m_keyXPress = (data[4] & 0x10) > 0 ? true : false;
                m_keyYPress = (data[4] & 0x20) > 0 ? true : false;
                m_keyL1Press = (data[4] & 0x40) > 0 ? true : false;
                m_keyR1Press = (data[4] & 0x80) > 0 ? true : false;
                m_keyL2Pressure = data[5] & 0xFF;
                m_keyR2Pressure = data[6] & 0xFF;
                m_LStickX = data[7] & 0xFF;
                m_LStickY = data[9] & 0xFF;
                m_RStickX = data[11] & 0xFF;
                m_RStickY = data[13] & 0xFF;
            }catch(Exception e){
                e.printStackTrace();
                result = false;
            }
            return result;
        }

        public List<PadKeyEvent> getPadKeyEventList(){
            return m_padKeyEventList;
        }

        public List<PadMotionEvent> getPadMotionEventList(){
            return m_padMotionEventList;
        }
    }

    public class PkgVibration extends PkgBle{
        public PkgVibration() {
            super();
        }

        @Override
        public int pkgType() {
            return Protocol.CMD_MOTOR;
        }
    }

    public class PkgSensor extends PkgBle{
        public PkgSensor() {
            super();
        }

        @Override
        public int pkgType() {
            return 11;
        }
    }

    public class PkgHardId extends PkgBle{
        public PkgHardId() {
            super();
        }

        @Override
        public int pkgType() {
            return 6;
        }
    }

    public class PkgInputModeSendToPad extends PkgBle{
        public PkgInputModeSendToPad() {
            super();
        }

        @Override
        public int pkgType() {
            return 8;
        }
    }

    public class PkgInputModeRecvFromPad extends PkgBle{
        public PkgInputModeRecvFromPad() {
            super();
        }

        @Override
        public int pkgType() {
            return 8;
        }
    }
}
