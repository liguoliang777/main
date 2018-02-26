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
 * 针对蓝牙SSP的通讯包管理类
 */

public class Protocol {
    public static final int CENTER_AXIS = 128;
    public static final byte CMD_DEVICE_CONNECTED_COUNT = -96;
    public static final byte CMD_HID_DISABLE = -80;
    public static final byte CMD_HID_ENABLE = -79;
    public static final byte CMD_ID_SET = 4;
    public static final byte CMD_KEYEVENT = 1;
    public static final byte CMD_KEY_NOTIFY = 17;
    public static final byte CMD_LIGHT_SET = 5;
    public static final byte CMD_MODE_QUERY = 36;
    public static final byte CMD_MODE_SET = 37;
    public static final byte CMD_MOTOR = 2;
    public static final byte CMD_OTA = 9;
    public static final byte CMD_OTA_CONTENT = 10;
    public static final byte CMD_SENSOR_QUERY = 33;
    public static final byte CMD_SET_NAME = 16;
    public static final byte CMD_SPP_OFF = 0;
    public static final byte CMD_SPP_ON = 8;
    public static final byte CMD_STATE_QUERY = 3;
    public static final byte CMD_STATE_RESULT = 5;
    public static final byte CMD_UUID_QUERY = 19;
    public static final byte CMD_UUID_RESULT = 20;
    public static final byte CMD_VERSION_QUERY = 6;
    public static final byte CMD_VERSION_RESULT = 7;
    public static final byte CMD_VERSION_RESULT_THIRD_PARTH = -10;
    public static final int COMMAND_HEADER_SIZE = 5;
    public static final int HEADER_G = 71;
    public static final int HEADER_N = 78;
    public static final byte INDEX_HEADER_0 = 0;
    public static final byte INDEX_HEADER_1 = 1;
    public static final byte INDEX_VERSION = 2;
    public static final byte INDEX_PACKAGE_SIZE = 3;
    public static final byte INDEX_PACKAGE_TYPE = 4;
    public static final byte INDEX_KEY_BASE = 5;
    public static final byte INDEX_KEY_EXT = 6;
    public static final byte INDEX_KEY_A = 7;
    public static final byte INDEX_KEY_B = 8;
    public static final byte INDEX_KEY_X = 9;
    public static final byte INDEX_KEY_Y = 10;
    public static final byte INDEX_KEY_L1 = 11;
    public static final byte INDEX_KEY_L2 = 13;
    public static final byte INDEX_KEY_R1 = 12;
    public static final byte INDEX_KEY_R2 = 14;
    public static final byte INDEX_LEFTSTICK_X = 15;
    public static final byte INDEX_LEFTSTICK_Y = 17;
    public static final byte INDEX_RIGHTSTICK_X = 19;
    public static final byte INDEX_RIGHTSTICK_Y = 21;
    public static final int MAX_PRESSURE = 255;
    public static final int VERSION = 16;

    public Protocol(){
        super();
    }
    public abstract class PkgSSP{
        protected byte[] m_pkgData;
        protected byte[] m_pkgHeader;
        protected long m_time;
        protected int m_deviceId;

        public PkgSSP(){
            super();
            m_pkgData = null;
            m_pkgHeader = null;
            m_time = System.currentTimeMillis();
            m_deviceId = 1;
        }

        public void setPkgData(byte[] content){
            m_pkgData = content;
        }

        public abstract int pkgType();

        public boolean encodeData(byte[] bytes){
            return false;
        }

        public void setDeviceId(int deviceId){
            m_deviceId = deviceId;
        }

        protected void initPkg(){
            m_pkgHeader = new byte[COMMAND_HEADER_SIZE];
            m_pkgHeader[INDEX_HEADER_0] = HEADER_N;
            m_pkgHeader[INDEX_HEADER_1] = HEADER_G;
            m_pkgHeader[INDEX_VERSION] = VERSION;
            m_pkgHeader[INDEX_PACKAGE_TYPE] = (byte)pkgType();
        }

        public byte[] catPkg(){
            initPkg();
            byte[] result = m_pkgData != null ? Utils.catByteAry(m_pkgHeader, m_pkgData) : m_pkgHeader;
            result[INDEX_PACKAGE_SIZE] = (byte)result.length;
            return result;
        }
    }

    public class PkgMgrSSP extends PkgSSP{
        int m_LStickY;
        int m_RStickX;
        int m_RStickY;
        List<PadKeyEvent> m_padKeyEventList;
        List<PadMotionEvent> m_padMotionEventList;
        byte[] m_cmdBuf;
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
        int m_keyAPress;
        int m_keyBPress;
        int m_keyXPress;
        int m_keyYPress;
        int m_keyL1Press;
        int m_keyR1Press;
        int m_keyL2Press;
        int m_keyR2Press;
        int m_LStickX;

        public PkgMgrSSP(){
            super();
            m_padKeyEventList = new ArrayList<PadKeyEvent>();
            m_padMotionEventList = new ArrayList<PadMotionEvent>();
            m_cmdBuf = new byte[22];
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
            m_keyAPress = 0;
            m_keyBPress = 0;
            m_keyXPress = 0;
            m_keyYPress = 0;
            m_keyL1Press = 0;
            m_keyR1Press = 0;
            m_keyL2Press = 0;
            m_keyR2Press = 0;
            m_LStickX = 128;
            m_LStickY = 128;
            m_RStickX = 128;
            m_RStickY = 128;
        }

        public void checkEvent(PkgMgrSSP lastKeyInfo){
            if(lastKeyInfo == null){
                lastKeyInfo = new PkgMgrSSP();
            }

            if(m_cmdBuf[5] != lastKeyInfo.m_cmdBuf[5]){
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

            if(m_cmdBuf[6] != lastKeyInfo.m_cmdBuf[6]){
                if(m_keyHelpPress != lastKeyInfo.m_keyHelpPress){
                    addKeyEventToList(BaseEvent.KEYCODE_BUTTON_HELP, m_keyHelpPress);
                }
                if(m_keyKeyboardPress != lastKeyInfo.m_keyKeyboardPress){
                    addKeyEventToList(BaseEvent.KEYCODE_BUTTON_KEYBOARD, m_keyKeyboardPress);
                }
            }
            if(m_keyAPress != lastKeyInfo.m_keyAPress){
                addKeyEventToList(BaseEvent.KEYCODE_BUTTON_A, m_keyAPress > 0 ? true : false);
            }
            if(m_keyBPress != lastKeyInfo.m_keyBPress){
                addKeyEventToList(BaseEvent.KEYCODE_BUTTON_B, m_keyBPress > 0 ? true : false);
            }
            if(m_keyXPress != lastKeyInfo.m_keyXPress){
                addKeyEventToList(BaseEvent.KEYCODE_BUTTON_X, m_keyXPress > 0 ? true : false);
            }
            if(m_keyYPress != lastKeyInfo.m_keyYPress){
                addKeyEventToList(BaseEvent.KEYCODE_BUTTON_Y, m_keyYPress > 0 ? true : false);
            }
            if(m_keyL1Press != lastKeyInfo.m_keyL1Press){
                addKeyEventToList(BaseEvent.KEYCODE_BUTTON_L1, m_keyL1Press > 0 ? true : false);
            }
            if(m_keyR1Press != lastKeyInfo.m_keyR1Press){
                addKeyEventToList(BaseEvent.KEYCODE_BUTTON_R1, m_keyR1Press > 0 ? true : false);
            }
            if(m_keyL2Press != lastKeyInfo.m_keyL2Press){
                addPressureKeyToList(BaseEvent.KEYCODE_BUTTON_L2, m_keyL2Press);
            }
            if(m_keyR2Press != lastKeyInfo.m_keyR2Press){
                addPressureKeyToList(BaseEvent.KEYCODE_BUTTON_R2, m_keyR2Press);
            }
            if(m_LStickX != lastKeyInfo.m_LStickX || m_LStickY != lastKeyInfo.m_LStickY){
                addPadMotionEventToList(BaseEvent.KEYCODE_LEFT_STICK, m_LStickX, m_LStickY);
            }
            if(m_RStickX != lastKeyInfo.m_RStickX || m_RStickY != lastKeyInfo.m_RStickY){
                addPadMotionEventToList(BaseEvent.KEYCODE_RIGHT_STICK, m_RStickX, m_RStickY);
            }
        }

        private float stickValParcent(int pressure, boolean isY){
            float result = 0f;
            if(pressure == 128){
                result = 0f;
            }else{
                if(pressure <= 0){
                    result = -1f;
                }else if(pressure >= MAX_PRESSURE){
                    result = 1f;
                }else if(pressure > 128){
                    result = (pressure - 128) / 127f;
                }else{
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

        private void addPadMotionEventToList(int keyCode, int action, int pressure){
//            m_padMotionEventList.add(new PadMotionEvent(m_time, m_deviceId, keyCode, stickValParcent(action, false), stickValParcent(pressure, true)));
            LooperEventManager.sendEventMsg(LooperEventManager.MSG_PAD_MOTIONEVENT, new PadMotionEvent(m_time, m_deviceId, keyCode, stickValParcent(action, false), stickValParcent(pressure, true)));
        }

        public float pressurePercent(int pressure){
            float precent = 1f;
            if(pressure <= 0){
                precent = 0f;
            }else if(pressure < 255){
                precent = precent * pressure / 255f;
            }
            return precent;
        }

        private void addPressureKeyToList(int keyCode, int pressure){
            addPadKeyEventToList(keyCode, BaseEvent.ACTION_PRESSURE, pressurePercent(pressure));
        }

        @Override
        public int pkgType() {
            return 1;
        }

        @Override
        public boolean encodeData(byte[] bytes){
            boolean parsingSspPkgCmd = true;
//            LLog.d("Protocol->PkgMgrSSP->encodeData data:" + LLog.covertHexStr(bytes));

            try{
                m_cmdBuf = new byte[bytes.length];
                System.arraycopy(bytes, 0, m_cmdBuf, 0, m_cmdBuf.length);
                m_keyUpPress = (bytes[INDEX_KEY_BASE] & 0x1) > 0 ? true : false;
                m_keyDownPress = (bytes[INDEX_KEY_BASE] & 0x2) > 0 ? true : false;
                m_keyLeftPress = (bytes[INDEX_KEY_BASE] & 0x4) > 0 ? true : false;
                m_keyRightPress = (bytes[INDEX_KEY_BASE] & 0x8) > 0 ? true : false;
                m_keyStartPress = (bytes[INDEX_KEY_BASE] & 0x10) > 0 ? true : false;
                m_keyBackPress = (bytes[INDEX_KEY_BASE] & 0x20) > 0 ? true : false;
                m_LeftStickPress = (bytes[INDEX_KEY_BASE] & 0x40) > 0 ? true : false;
                m_RightStickPress = (bytes[INDEX_KEY_BASE] & 0x80) > 0 ? true : false;
                m_keyHelpPress = (bytes[INDEX_KEY_EXT] & 0x1) > 0 ? true : false;
                m_keyKeyboardPress = (bytes[INDEX_KEY_EXT] & 0x2) > 0 ? true : false;
                m_keyAPress = bytes[INDEX_KEY_A] & 0xFF;
                m_keyBPress = bytes[INDEX_KEY_B] & 0xFF;
                m_keyXPress = bytes[INDEX_KEY_X] & 0xFF;
                m_keyYPress = bytes[INDEX_KEY_Y] & 0xFF;
                m_keyL1Press = bytes[INDEX_KEY_L1] & 0xFF;
                m_keyR1Press = bytes[INDEX_KEY_R1] & 0xFF;
                m_keyL2Press = bytes[INDEX_KEY_L2] & 0xFF;
                m_keyR2Press = bytes[INDEX_KEY_R2] & 0xFF;
                m_LStickX = bytes[INDEX_LEFTSTICK_X] & 0xFF;
                m_LStickY = bytes[INDEX_LEFTSTICK_Y] & 0xFF;
                m_RStickX = bytes[INDEX_RIGHTSTICK_X] & 0xFF;
                m_RStickY = bytes[INDEX_RIGHTSTICK_Y] & 0xFF;
            }catch(Exception e){
                e.printStackTrace();
                parsingSspPkgCmd = false;
            }
            return parsingSspPkgCmd;
        }

        public void addKeyEventToList(int keyCode, boolean press){
            int action = press ? BaseEvent.ACTION_DOWN : BaseEvent.ACTION_UP;
            addPadKeyEventToList(keyCode, action, 1f);
        }

//        public List<PadKeyEvent> getPadKeyEventList(){
//            return m_padKeyEventList;
//        }
//
//        public List<PadMotionEvent> getPadMotionEventList(){
//            return m_padMotionEventList;
//        }
    }

    //包分来回，所以可以看到同样的包类型的有两个类
    public class PkgSSPType2 extends PkgSSP{

        @Override
        public int pkgType() {
            return CMD_MOTOR;
        }
    }

    public class PkgSSPType33 extends PkgSSP{
        @Override
        public int pkgType() {
            return CMD_SENSOR_QUERY;
        }
    }

    public class PkgSSPType19 extends PkgSSP{
        @Override
        public int pkgType() {
            return CMD_UUID_QUERY;
        }
    }

    public class PkgSSPType8_send extends PkgSSP{
        @Override
        public int pkgType() {
            return CMD_SPP_ON;
        }
    }

    public class PkgSSPType8 extends PkgSSP{
        @Override
        public int pkgType() {
            return CMD_SPP_ON;
        }
    }

}
