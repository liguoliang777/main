package com.ngame.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lx.pad.R;
import com.lx.pad.util.LLog;
import com.ngame.DabaBase.KeyEnum;
import com.ngame.api.UrlMgr;
import com.ngame.api.model.Game;
import com.ngame.api.model.GameDetail;
import com.ngame.api.model.KeyMap;
import com.ngame.app.IndexMappingKeyEnumRes;
import com.ngame.widget.FrameLayoutMgrUtils;
import com.ngds.pad.BaseEvent;
import com.ngds.pad.utils.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/12/2.
 */

public class KeyMgrUtils {
    //    public static HashMap<Integer, ConcurrentLinkedQueue<KeyInfoStruct> > m_keyHashMapQueue
    // = new HashMap<Integer, ConcurrentLinkedQueue<KeyInfoStruct> >();
    public static ConcurrentHashMap<KeyEnum, BtnParams> m_keyParamsMap = new
            ConcurrentHashMap<KeyEnum, BtnParams>();
    private static String m_spFileName = "keyboard";
    private static boolean m_isInjectByIpm = false;
    private static boolean m_isVibrateEnable = false;
    private static boolean m_isChanged = false;

    public static final int KEYMODE_NORMAL = 0;
    public static final int KEYMODE_LEFT_STICK = 1;     //关联左摇杆
    public static final int KEYMODE_RIGHT_STICK = 2;    //关联右摇杆

    public static final int MOTION_MODE_NONE = 0;       //摇杆模式无（默认使用摇杆模式）
    public static final int MOTION_MODE_NORMAL = 1;     //摇杆模式
    public static final int MOTION_MODE_FINAL = 2;      //滑动模式--区域
    public static final int MOTION_MODE_DYNAMIC = 3;    //滑动模式--全屏

    static {
        LLog.d("KeyMgrUtils static init <-----------------------");
//        m_keyParamsMap = new ConcurrentHashMap<KeyEnum, BtnParams>();
//        KeyEnum[] keyEnums = KeyEnum.values();
//        for(int nIndex = 0; nIndex < keyEnums.length; nIndex++){
//            m_keyHashMapQueue.put(Integer.valueOf(keyEnums[nIndex].ordinal()), new
// ConcurrentLinkedQueue<KeyInfoStruct>());
//        }
    }

    public static boolean sIsChanged() {
        return m_isChanged;
    }

    public static void sPutPrefBooleanVal(Context context, String name, String key, boolean val) {
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putBoolean(key, val)
                .apply();
    }

    public static boolean sGetPrefBoolean(Context context, String name, String key, boolean
            defVal) {
        return context.getSharedPreferences(name, Context.MODE_MULTI_PROCESS).getBoolean(key,
                defVal);
    }

    public static void sPutPrefIntVal(Context context, String name, String key, int val) {
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putInt(key, val).apply();
    }

    private static int sGetPrefInt(Context context, String name, String key, int val) {
        return context.getSharedPreferences(name, Context.MODE_MULTI_PROCESS).getInt(key, val);
    }

    public static void copyFile(String oldPath, String newPath) {
        try {
            int byteSum = 0;
            int byteRead = 0;
            File oldFile = new File(oldPath);
            if (oldFile.exists()) {
                InputStream inputStream = new FileInputStream(oldPath);
                FileOutputStream outputStream = new FileOutputStream(newPath);
                byte[] buf = new byte[1444];
                while ((byteRead = inputStream.read(buf)) != -1) {
                    byteSum += byteRead;
                    outputStream.write(buf, 0, byteRead);
                }
                inputStream.close();
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sInitializeKeyInfo(final Context context, final Game game, final
    IUpdateKeyState updateKeyState) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                decodeUpdateKeyInfo(context, UrlMgr.getKeyCodeInfo(game.getId()));
                if (updateKeyState != null) {
                    updateKeyState.updateSuccess();
                }
                sPutPrefBooleanVal(context, m_spFileName, "initialized", true);
            }
        }).start();
    }

    //解析从服务器接收的按键布局消息
    public static void decodeUpdateKeyInfo(Context context, GameDetail gameDetail) {
        LLog.d("KeyMgrUtils->decodeUpdateKeyInfo  <------------------->");
        Iterator<KeyMap> iter = gameDetail.getKey_map().iterator();
        while (iter.hasNext()) {
            KeyMap keyMap = iter.next();
            if (keyMap.getBtn() == null) {
                continue;
            }

            KeyEnum keyEnum = KeyEnum.valueOf(keyMap.getBtn().toUpperCase());
            sReplaceHashMapKeyEnumX(keyEnum, DisplayMetricsMgr.sWidthScaleToPx(context, (float)
                    keyMap.getX()));
            float y = (float) keyMap.getY();
            int scalePx = (y <= 0f) ? (int) y : (int) (y * DisplayMetricsMgr.getHeightScale
                    (context) + 0.5f);
            sReplaceHashMapKeyEnumY(keyEnum, scalePx);
            sReplaceHashMapKeyEnumR(keyEnum, DisplayMetricsMgr.sWidthScaleToPx(context, (float)
                    keyMap.getR()));
            sReplaceHashMapKeyEnumMode(keyEnum, keyMap.getMode());
            sReplaceHashMapKeyEnumStep(keyEnum, keyMap.getStep());
            sReplaceHashMapKeyEnumFrequency(keyEnum, keyMap.getFrequency());
            sReplaceHashMapKeyEnumFlag(keyEnum, keyMap.getFlags());
        }

        boolean ipmMode = (gameDetail.getIpm_mode() == 1) ? true : false;
        m_isInjectByIpm = ipmMode;
        sSavePrefInfo(context);
    }

    public static void sUpdateKeyEnumFromHttpServer(final Context context, final String pkgName,
                                                    final IUpdateKeyState stateCallback) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                LLog.d("KeyMgrUtils->sUpdateKeyEnumFromHttpServer run pkgName:" + pkgName);
                try {
                    List<Game> list = UrlMgr.getUrl(pkgName);
                    if (list != null) {
                        LLog.d("KeyMgrUtils->sUpdateKeyEnumFromHttpServer run list.size:" + list
                                .size());
                    }
                    if (list == null || list.size() == 0) {
                        sPutPrefBooleanVal(context, m_spFileName, "initialized", true);
                        if (stateCallback == null) {
                            return;
                        }
                        stateCallback.updateSuccess();
                        return;
                    }

                    sInitializeKeyInfo(context, list.get(0), stateCallback);

                    copyFile("/data/data/" + context.getPackageName() + "/shared_prefs/keyboard" +
                                    ".xml",
                            "mnt/sdcard/data/" + pkgName + "/keyboard.xml");
                } catch (Exception e) {
                    if (stateCallback != null) {
                        stateCallback.updateFail();
                    }
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void sUpdateKeyEnumHashMap(Context context) {
        m_keyParamsMap.clear();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant
                .NAME_FANGLE_PAD_SP, Context
                .MODE_MULTI_PROCESS);//获取
        m_spFileName = sharedPreferences.getString(Constant.KEY_PKG_NAME_VALUE_FANGLE_PAD_SP,
                Constant.DEF_VALUE_PKG_NAME_FANGLE_PAD_SP);
        LLog.d("存储名字:" + m_spFileName);
        KeyEnum[] keyEnums = KeyEnum.values();
        for (int nIndex = 0; nIndex < keyEnums.length; nIndex++) {
            KeyEnum keyEnum = keyEnums[nIndex];
            BtnParams btnParams = new BtnParams();
            btnParams.m_x = sGetPrefInt(context, m_spFileName, keyEnum.getPref_x(), -1);
            btnParams.m_y = sGetPrefInt(context, m_spFileName, keyEnum.getPref_y(), -1);
            btnParams.m_r = sGetPrefInt(context, m_spFileName, keyEnum.getPref_r(), -1);
            btnParams.m_flags = sGetPrefInt(context, m_spFileName, keyEnum.getPref_Flags(), -1);
            if (keyEnum == KeyEnum.L || keyEnum == KeyEnum.R) {
                btnParams.m_mode = sGetPrefInt(context, m_spFileName, keyEnum.getPref_m(), 0);
                btnParams.m_step = sGetPrefInt(context, m_spFileName, keyEnum.getPref_step(), 15);
                btnParams.m_frequency = sGetPrefInt(context, m_spFileName, keyEnum
                        .getPref_frequency(), 30);
            } else {
                btnParams.m_mode = sGetPrefInt(context, m_spFileName, keyEnum.getPref_m(), -1);
                btnParams.m_step = sGetPrefInt(context, m_spFileName, keyEnum.getPref_step(), -1);
                btnParams.m_frequency = sGetPrefInt(context, m_spFileName, keyEnum
                        .getPref_frequency(), -1);
            }
            m_keyParamsMap.put(keyEnum, btnParams);
        }

        m_isInjectByIpm = sGetPrefBoolean(context, m_spFileName, "pref_is_inject_by_ipm", false);
        if (!sGetPrefBoolean(context, m_spFileName, "initialized", false)) {
            m_isVibrateEnable = sGetPrefBoolean(context, m_spFileName, "pref_is_vibrate_enable",
                    false);
            sUpdateKeyEnumFromHttpServer(context, context.getPackageName(), null);
            //..........pkgName error
        }
        m_isChanged = false;
    }

    public static void sReplaceHashMapKeyEnumX(KeyEnum keyEnum, int val) {
        if (keyEnum != null && m_keyParamsMap != null && m_keyParamsMap.containsKey(keyEnum)) {
            BtnParams btnParams = m_keyParamsMap.get(keyEnum);
            btnParams.m_x = val;
            m_keyParamsMap.replace(keyEnum, btnParams);
            m_isChanged = true;
        }
    }

    public static void sReplaceHashMapKeyEnumY(KeyEnum keyEnum, int val) {
        if (keyEnum != null && m_keyParamsMap != null && m_keyParamsMap.containsKey(keyEnum)) {
            BtnParams btnParams = m_keyParamsMap.get(keyEnum);
            btnParams.m_y = val;
            m_keyParamsMap.replace(keyEnum, btnParams);
            m_isChanged = true;
        }
    }

    public static void sReplaceHashMapKeyEnumR(KeyEnum keyEnum, int val) {
        if (keyEnum != null && m_keyParamsMap != null && m_keyParamsMap.containsKey(keyEnum)) {
            BtnParams btnParams = m_keyParamsMap.get(keyEnum);
            btnParams.m_r = val;
            m_keyParamsMap.replace(keyEnum, btnParams);
            m_isChanged = true;
        }
    }

    public static void sReplaceHashMapKeyEnumFlag(KeyEnum keyEnum, int val) {
        if (keyEnum != null && m_keyParamsMap != null && m_keyParamsMap.containsKey(keyEnum)) {
            BtnParams btnParams = m_keyParamsMap.get(keyEnum);
            btnParams.m_flags = val;
            m_keyParamsMap.replace(keyEnum, btnParams);
            m_isChanged = true;
        }
    }

    public static void sReplaceHashMapKeyEnumMode(KeyEnum keyEnum, int val) {
        if (keyEnum != null && m_keyParamsMap != null && m_keyParamsMap.containsKey(keyEnum)) {
            BtnParams btnParams = m_keyParamsMap.get(keyEnum);
            btnParams.m_mode = val;
            m_keyParamsMap.replace(keyEnum, btnParams);
            m_isChanged = true;
        }
    }

    public static void sReplaceHashMapKeyEnumStep(KeyEnum keyEnum, int val) {
        if (keyEnum != null && m_keyParamsMap != null && m_keyParamsMap.containsKey(keyEnum)) {
            BtnParams btnParams = m_keyParamsMap.get(keyEnum);
            btnParams.m_step = val;
            m_keyParamsMap.replace(keyEnum, btnParams);
            m_isChanged = true;
        }
    }

    public static void sReplaceHashMapKeyEnumFrequency(KeyEnum keyEnum, int val) {
        if (keyEnum != null && m_keyParamsMap != null && m_keyParamsMap.containsKey(keyEnum)) {
            BtnParams btnParams = m_keyParamsMap.get(keyEnum);
            btnParams.m_frequency = val;
            m_keyParamsMap.replace(keyEnum, btnParams);
            m_isChanged = true;
        }
    }

    public static void sInitKeyEnum(KeyEnum keyEnum) {
        if (keyEnum != null) {
            sReplaceHashMapKeyEnumX(keyEnum, -1);
            sReplaceHashMapKeyEnumY(keyEnum, -1);
            sReplaceHashMapKeyEnumR(keyEnum, -1);
            sReplaceHashMapKeyEnumFlag(keyEnum, -1);
            if (keyEnum == KeyEnum.L || keyEnum == KeyEnum.R) {
                sReplaceHashMapKeyEnumMode(keyEnum, 0);
                sReplaceHashMapKeyEnumStep(keyEnum, 15);
                sReplaceHashMapKeyEnumFrequency(keyEnum, 30);
            } else {
                sReplaceHashMapKeyEnumMode(keyEnum, -1);
                sReplaceHashMapKeyEnumStep(keyEnum, -1);
                sReplaceHashMapKeyEnumFrequency(keyEnum, -1);
            }
            m_isChanged = true;
        }
    }

    public static int sGetKeyInfoX(KeyEnum keyEnum) {
        return (keyEnum == null || m_keyParamsMap == null || !m_keyParamsMap.containsKey(keyEnum)
        ) ? -1 : m_keyParamsMap.get(keyEnum).m_x;
    }

    public static int sGetKeyInfoY(KeyEnum keyEnum) {
        return (keyEnum == null || m_keyParamsMap == null || !m_keyParamsMap.containsKey(keyEnum)
        ) ? -1 : m_keyParamsMap.get(keyEnum).m_y;
    }

    public static int sGetKeyInfoR(KeyEnum keyEnum) {
        return (keyEnum == null || m_keyParamsMap == null || !m_keyParamsMap.containsKey(keyEnum)
        ) ? -1 : m_keyParamsMap.get(keyEnum).m_r;
    }

    public static int sGetKeyInfoMode(KeyEnum keyEnum) {
        return (keyEnum == null || m_keyParamsMap == null || !m_keyParamsMap.containsKey(keyEnum)
        ) ? -1 : m_keyParamsMap.get(keyEnum).m_mode;
    }

    public static int sGetKeyInfoStep(KeyEnum keyEnum) {
        return (keyEnum == null || m_keyParamsMap == null || !m_keyParamsMap.containsKey(keyEnum)
        ) ? -1 : m_keyParamsMap.get(keyEnum).m_step;
    }

    public static int sGetKeyInfoFrequency(KeyEnum keyEnum) {
        return (keyEnum == null || m_keyParamsMap == null || !m_keyParamsMap.containsKey(keyEnum)
        ) ? -1 : m_keyParamsMap.get(keyEnum).m_frequency;
    }

    public static int sGetKeyInfoFlags(KeyEnum keyEnum) {
        return (keyEnum == null || m_keyParamsMap == null || !m_keyParamsMap.containsKey(keyEnum)
        ) ? -1 : m_keyParamsMap.get(keyEnum).m_flags;
    }

    // 保存修改后的按键配置信息
    public static void sSavePrefInfo(Context context) {
        KeyEnum[] keyEnums = KeyEnum.values();
        int nLen = keyEnums.length;
        for (int nIndex = 0; nIndex < nLen; nIndex++) {
            KeyEnum keyEnum = keyEnums[nIndex];
            sPutPrefIntVal(context, m_spFileName, keyEnum.getPref_x(), sGetKeyInfoX(keyEnum));
            sPutPrefIntVal(context, m_spFileName, keyEnum.getPref_y(), sGetKeyInfoY(keyEnum));
            sPutPrefIntVal(context, m_spFileName, keyEnum.getPref_r(), sGetKeyInfoR(keyEnum));
            sPutPrefIntVal(context, m_spFileName, keyEnum.getPref_m(), sGetKeyInfoMode(keyEnum));
            sPutPrefIntVal(context, m_spFileName, keyEnum.getPref_step(), sGetKeyInfoStep(keyEnum));
            sPutPrefIntVal(context, m_spFileName, keyEnum.getPref_frequency(),
                    sGetKeyInfoFrequency(keyEnum));
            sPutPrefIntVal(context, m_spFileName, keyEnum.getPref_Flags(), sGetKeyInfoFlags
                    (keyEnum));
        }

        //ipm模式在这里暂时不需要管，是属于游戏包中摸拟自身消息的一个映射方式，本程序暂时用不到
        sPutPrefBooleanVal(context, m_spFileName, "pref_is_inject_by_ipm", m_isInjectByIpm);
        m_isChanged = false;
    }

    public static void promptFrameLayoutView(Context context) {  //显示或隐藏按键提示层
        //如果版本大于6.0则申请权限
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(context)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                context.startActivity(intent);
                return;
            }
        }
        if (FrameLayoutMgrUtils.getInstance(context).hasFrameLayout()) {
            FrameLayoutMgrUtils.getInstance(context).removeAllFrameLayoutView();
        } else {
            FrameLayoutMgrUtils frameLayoutMgrUtils = FrameLayoutMgrUtils.getInstance(context);
            frameLayoutMgrUtils.m_halfWidth = frameLayoutMgrUtils.m_windowManager
                    .getDefaultDisplay().getWidth() / 2;
            frameLayoutMgrUtils.m_halfHeight = frameLayoutMgrUtils.m_windowManager
                    .getDefaultDisplay().getHeight() / 2;
            sUpdateKeyEnumHashMap(frameLayoutMgrUtils.m_context);
            KeyEnum[] keyEnums = KeyEnum.values();
            for (int i = 0; i < keyEnums.length; i++) {
                KeyEnum keyEnum = keyEnums[i];
                int x = sGetKeyInfoX(keyEnum);
                int y = sGetKeyInfoY(keyEnum);
                FrameLayout layout = frameLayoutMgrUtils.m_frameLayouts[keyEnum.ordinal()];
                if (x >= 0 && y >= 0 && layout == null) {
                    FrameLayout frameLayout = (FrameLayout) LayoutInflater.from
                            (frameLayoutMgrUtils.m_context).inflate(R.layout.item_float_key, null);
                    ImageView imgKey = frameLayout.findViewById(R.id.iv_key);
                    Drawable drawable = IndexMappingKeyEnumRes.sGetDrawable(keyEnum,
                            frameLayoutMgrUtils.m_context);
                    if (drawable != null) {
                        imgKey.setImageDrawable(drawable);
                    }
                    imgKey.setClickable(false);
                    imgKey.setEnabled(false);
                    int width = DisplayMetricsMgr.dip2px(frameLayoutMgrUtils.m_context, 35f);
                    int n = DisplayMetricsMgr.dip2px(frameLayoutMgrUtils.m_context, 18f);
                    frameLayoutMgrUtils.m_layoutParams.x = x;
                    frameLayoutMgrUtils.m_layoutParams.y = y;
                    frameLayoutMgrUtils.m_layoutParams.width = width;
                    frameLayoutMgrUtils.m_layoutParams.height = width;
                    int r = sGetKeyInfoR(keyEnum);
                    if (r > 0) {
                        if (x < frameLayoutMgrUtils.m_halfWidth && y < frameLayoutMgrUtils
                                .m_halfHeight) {
                            ((FrameLayout.LayoutParams) imgKey.getLayoutParams()).gravity =
                                    Gravity.BOTTOM | Gravity.RIGHT;
                            frameLayout.setBackgroundResource(R.mipmap.ic_key_bg_top_left);
                            frameLayoutMgrUtils.m_layoutParams.x += r - n;
                            frameLayoutMgrUtils.m_layoutParams.y += r - n;
                        } else if (x > frameLayoutMgrUtils.m_halfWidth && y < frameLayoutMgrUtils
                                .m_halfHeight) {
                            ((FrameLayout.LayoutParams) imgKey.getLayoutParams()).gravity =
                                    Gravity.BOTTOM | Gravity.LEFT;
                            frameLayout.setBackgroundResource(R.mipmap.ic_key_bg_top_right);
                            frameLayoutMgrUtils.m_layoutParams.x -= width;
                            frameLayoutMgrUtils.m_layoutParams.x -= r - n;
                            frameLayoutMgrUtils.m_layoutParams.y += r - n;
                        } else if (x < frameLayoutMgrUtils.m_halfWidth && y > frameLayoutMgrUtils
                                .m_halfHeight) {
                            ((FrameLayout.LayoutParams) imgKey.getLayoutParams()).gravity =
                                    Gravity.TOP | Gravity.RIGHT;
                            frameLayout.setBackgroundResource(R.mipmap.ic_key_bg_bottom_left);
                            frameLayoutMgrUtils.m_layoutParams.y -= width;
                            frameLayoutMgrUtils.m_layoutParams.x += r - n;
                            frameLayoutMgrUtils.m_layoutParams.y -= r - n;
                        } else if (x > frameLayoutMgrUtils.m_halfWidth && y > frameLayoutMgrUtils
                                .m_halfHeight) {
                            ((FrameLayout.LayoutParams) imgKey.getLayoutParams()).gravity =
                                    Gravity.TOP | Gravity.LEFT;
                            frameLayout.setBackgroundResource(R.mipmap.ic_key_bg_bottom_right);
                            frameLayoutMgrUtils.m_layoutParams.x -= width;
                            frameLayoutMgrUtils.m_layoutParams.y -= width;
                            frameLayoutMgrUtils.m_layoutParams.x -= r - n;
                            frameLayoutMgrUtils.m_layoutParams.y -= r - n;
                        } else {
                            ((FrameLayout.LayoutParams) imgKey.getLayoutParams()).gravity =
                                    Gravity.BOTTOM | Gravity.RIGHT;
                            frameLayout.setBackgroundResource(R.mipmap.ic_key_bg_top_left);
                        }
                    }
                    frameLayoutMgrUtils.m_windowManager.addView(frameLayout, frameLayoutMgrUtils
                            .m_layoutParams);
                    frameLayoutMgrUtils.m_frameLayouts[keyEnum.ordinal()] = frameLayout;
                }
            }
        }
    }

    private static boolean sIsAvailableKeyInfoX(KeyEnum keyEnum) {
        return sGetKeyInfoX(keyEnum) > 0 ? true : false;
    }

    private static boolean sIsAvailableKeyInfoY(KeyEnum keyEnum) {
        return sGetKeyInfoY(keyEnum) > 0 ? true : false;
    }

    public static int sGetKeyInfoXByKeyCode(int keyCode) {
        int x = -1;
        switch (keyCode) {
            case BaseEvent.KEYCODE_BACK: {
                x = sGetKeyInfoX(KeyEnum.BACK);
                break;
            }
            case BaseEvent.KEYCODE_DPAD_UP: {
                if (sIsAvailableKeyInfoX(KeyEnum.F)) {
                    x = sGetKeyInfoX(KeyEnum.F);
                } else {
                    x = sGetKeyInfoX(KeyEnum.UP);
                }
                break;
            }
            case BaseEvent.KEYCODE_DPAD_DOWN: {
                if (sIsAvailableKeyInfoX(KeyEnum.F)) {
                    x = sGetKeyInfoX(KeyEnum.F);
                } else {
                    x = sGetKeyInfoX(KeyEnum.DOWN);
                }
                break;
            }
            case BaseEvent.KEYCODE_DPAD_LEFT: {
                if (sIsAvailableKeyInfoX(KeyEnum.F)) {
                    x = sGetKeyInfoX(KeyEnum.F) - sGetKeyInfoR(KeyEnum.F);
                } else {
                    x = sGetKeyInfoX(KeyEnum.LEFT);
                }
                break;
            }
            case BaseEvent.KEYCODE_DPAD_RIGHT: {
                if (sIsAvailableKeyInfoX(KeyEnum.F)) {
                    x = sGetKeyInfoX(KeyEnum.F) + sGetKeyInfoR(KeyEnum.F);
                } else {
                    x = sGetKeyInfoX(KeyEnum.RIGHT);
                }
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_A: {
                x = sGetKeyInfoX(KeyEnum.A);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_B: {
                x = sGetKeyInfoX(KeyEnum.B);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_X: {
                x = sGetKeyInfoX(KeyEnum.X);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_Y: {
                x = sGetKeyInfoX(KeyEnum.Y);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_L1: {
                x = sGetKeyInfoX(KeyEnum.L1);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_R1: {
                if (MouseViewMgrUtils.sMouseViewState()) {
                    x = (int) MouseViewMgrUtils.sGetMouseViewPtX();
                } else {
                    x = sGetKeyInfoX(KeyEnum.R1);
                }
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_L2: {
                x = sGetKeyInfoX(KeyEnum.L2);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_R2: {
                x = sGetKeyInfoX(KeyEnum.R2);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_THUMBL: {
                x = sGetKeyInfoX(KeyEnum.THUMBL);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_THUMBR: {
                x = sGetKeyInfoX(KeyEnum.THUMBR);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_START: {
                x = sGetKeyInfoX(KeyEnum.START);
                break;
            }
            case BaseEvent.KEYCODE_LEFT_STICK: {
                x = sGetKeyInfoX(KeyEnum.L);
                break;
            }
            case BaseEvent.KEYCODE_RIGHT_STICK: {
                x = sGetKeyInfoX(KeyEnum.R);
                break;
            }
        }
        return x;
    }

    public static int sGetKeyInfoYByKeyCode(int keyCode) {
        int y = -1;
        switch (keyCode) {
            case BaseEvent.KEYCODE_BACK: {
                y = sGetKeyInfoY(KeyEnum.BACK);
                break;
            }
            case BaseEvent.KEYCODE_DPAD_UP: {
                if (sIsAvailableKeyInfoY(KeyEnum.F)) {
                    y = sGetKeyInfoY(KeyEnum.F) - sGetKeyInfoR(KeyEnum.F);
                } else {
                    y = sGetKeyInfoY(KeyEnum.UP);
                }
                break;
            }
            case BaseEvent.KEYCODE_DPAD_DOWN: {
                if (sIsAvailableKeyInfoY(KeyEnum.F)) {
                    y = sGetKeyInfoY(KeyEnum.F) + sGetKeyInfoR(KeyEnum.F);
                } else {
                    y = sGetKeyInfoY(KeyEnum.DOWN);
                }
                break;
            }
            case BaseEvent.KEYCODE_DPAD_LEFT: {
                if (sIsAvailableKeyInfoY(KeyEnum.F)) {
                    y = sGetKeyInfoY(KeyEnum.F);
                } else {
                    y = sGetKeyInfoY(KeyEnum.LEFT);
                }
                break;
            }
            case BaseEvent.KEYCODE_DPAD_RIGHT: {
                if (sIsAvailableKeyInfoY(KeyEnum.F)) {
                    y = sGetKeyInfoY(KeyEnum.F);
                } else {
                    y = sGetKeyInfoY(KeyEnum.RIGHT);
                }
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_A: {
                y = sGetKeyInfoY(KeyEnum.A);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_B: {
                y = sGetKeyInfoY(KeyEnum.B);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_X: {
                y = sGetKeyInfoY(KeyEnum.X);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_Y: {
                y = sGetKeyInfoY(KeyEnum.Y);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_L1: {
                y = sGetKeyInfoY(KeyEnum.L1);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_R1: {
                if (MouseViewMgrUtils.sMouseViewState()) {
                    y = (int) MouseViewMgrUtils.sGetMouseViewPtY();
                } else {
                    y = sGetKeyInfoY(KeyEnum.R1);
                }
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_L2: {
                y = sGetKeyInfoY(KeyEnum.L2);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_R2: {
                y = sGetKeyInfoY(KeyEnum.R2);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_THUMBL: {
                y = sGetKeyInfoY(KeyEnum.THUMBL);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_THUMBR: {
                y = sGetKeyInfoY(KeyEnum.THUMBR);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_START: {
                y = sGetKeyInfoY(KeyEnum.START);
                break;
            }
            case BaseEvent.KEYCODE_LEFT_STICK: {
                y = sGetKeyInfoY(KeyEnum.L);
                break;
            }
            case BaseEvent.KEYCODE_RIGHT_STICK: {
                y = sGetKeyInfoY(KeyEnum.R);
                break;
            }
        }
        return y;
    }

    public static int sGetKeyInfoRByKeyCode(int keyCode) {
        int r = -1;
        switch (keyCode) {
            case BaseEvent.KEYCODE_BACK: {
                r = sGetKeyInfoR(KeyEnum.BACK);
                break;
            }
            case BaseEvent.KEYCODE_DPAD_UP: {
                if (sIsAvailableKeyInfoX(KeyEnum.F)) {
                    r = sGetKeyInfoR(KeyEnum.F);
                } else {
                    r = sGetKeyInfoR(KeyEnum.UP);
                }
                break;
            }
            case BaseEvent.KEYCODE_DPAD_DOWN: {
                if (sIsAvailableKeyInfoX(KeyEnum.F)) {
                    r = sGetKeyInfoR(KeyEnum.F);
                } else {
                    r = sGetKeyInfoR(KeyEnum.DOWN);
                }
                break;
            }
            case BaseEvent.KEYCODE_DPAD_LEFT: {
                if (sIsAvailableKeyInfoX(KeyEnum.F)) {
                    r = sGetKeyInfoR(KeyEnum.F);
                } else {
                    r = sGetKeyInfoR(KeyEnum.LEFT);
                }
                break;
            }
            case BaseEvent.KEYCODE_DPAD_RIGHT: {
                if (sIsAvailableKeyInfoX(KeyEnum.F)) {
                    r = sGetKeyInfoR(KeyEnum.F);
                } else {
                    r = sGetKeyInfoR(KeyEnum.RIGHT);
                }
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_A: {
                r = sGetKeyInfoR(KeyEnum.A);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_B: {
                r = sGetKeyInfoR(KeyEnum.B);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_X: {
                r = sGetKeyInfoR(KeyEnum.X);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_Y: {
                r = sGetKeyInfoR(KeyEnum.Y);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_L1: {
                r = sGetKeyInfoR(KeyEnum.L1);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_R1: {
                r = sGetKeyInfoR(KeyEnum.R1);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_L2: {
                r = sGetKeyInfoR(KeyEnum.L2);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_R2: {
                r = sGetKeyInfoR(KeyEnum.R2);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_THUMBL: {
                r = sGetKeyInfoR(KeyEnum.THUMBL);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_THUMBR: {
                r = sGetKeyInfoR(KeyEnum.THUMBR);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_START: {
                r = sGetKeyInfoR(KeyEnum.START);
                break;
            }
            case BaseEvent.KEYCODE_LEFT_STICK: {
                r = sGetKeyInfoR(KeyEnum.L);
                break;
            }
            case BaseEvent.KEYCODE_RIGHT_STICK: {
                r = sGetKeyInfoR(KeyEnum.R);
                break;
            }
        }
        return r;
    }

    public static int sGetKeyInfoModeByKeyCode(int keyCode) {
        int mode = -1;
        switch (keyCode) {
            case BaseEvent.KEYCODE_BACK: {
                mode = sGetKeyInfoMode(KeyEnum.BACK);
                break;
            }
            case BaseEvent.KEYCODE_DPAD_UP: {
                if (sIsAvailableKeyInfoX(KeyEnum.F)) {
                    mode = sGetKeyInfoMode(KeyEnum.F);
                } else {
                    mode = sGetKeyInfoMode(KeyEnum.UP);
                }
                break;
            }
            case BaseEvent.KEYCODE_DPAD_DOWN: {
                if (sIsAvailableKeyInfoX(KeyEnum.F)) {
                    mode = sGetKeyInfoMode(KeyEnum.F);
                } else {
                    mode = sGetKeyInfoMode(KeyEnum.DOWN);
                }
                break;
            }
            case BaseEvent.KEYCODE_DPAD_LEFT: {
                if (sIsAvailableKeyInfoX(KeyEnum.F)) {
                    mode = sGetKeyInfoMode(KeyEnum.F);
                } else {
                    mode = sGetKeyInfoMode(KeyEnum.LEFT);
                }
                break;
            }
            case BaseEvent.KEYCODE_DPAD_RIGHT: {
                if (sIsAvailableKeyInfoX(KeyEnum.F)) {
                    mode = sGetKeyInfoMode(KeyEnum.F);
                } else {
                    mode = sGetKeyInfoMode(KeyEnum.RIGHT);
                }
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_A: {
                mode = sGetKeyInfoMode(KeyEnum.A);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_B: {
                mode = sGetKeyInfoMode(KeyEnum.B);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_X: {
                mode = sGetKeyInfoMode(KeyEnum.X);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_Y: {
                mode = sGetKeyInfoMode(KeyEnum.Y);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_L1: {
                mode = sGetKeyInfoMode(KeyEnum.L1);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_R1: {
                mode = sGetKeyInfoMode(KeyEnum.R1);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_L2: {
                mode = sGetKeyInfoMode(KeyEnum.L2);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_R2: {
                mode = sGetKeyInfoMode(KeyEnum.R2);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_THUMBL: {
                mode = sGetKeyInfoMode(KeyEnum.THUMBL);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_THUMBR: {
                mode = sGetKeyInfoMode(KeyEnum.THUMBR);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_START: {
                mode = sGetKeyInfoMode(KeyEnum.START);
                break;
            }
            case BaseEvent.KEYCODE_LEFT_STICK: {
                mode = sGetKeyInfoMode(KeyEnum.L);
                break;
            }
            case BaseEvent.KEYCODE_RIGHT_STICK: {
                mode = sGetKeyInfoMode(KeyEnum.R);
                break;
            }
        }
        return mode;
    }

    public static int sGetKeyInfoFlagByKeyCode(int keyCode) {
        int f = -1;
        switch (keyCode) {
            case BaseEvent.KEYCODE_BACK: {
                f = sGetKeyInfoFlags(KeyEnum.BACK);
                break;
            }
            case BaseEvent.KEYCODE_DPAD_UP: {
                if (sIsAvailableKeyInfoX(KeyEnum.F)) {
                    f = sGetKeyInfoFlags(KeyEnum.F);
                } else {
                    f = sGetKeyInfoFlags(KeyEnum.UP);
                }
                break;
            }
            case BaseEvent.KEYCODE_DPAD_DOWN: {
                if (sIsAvailableKeyInfoX(KeyEnum.F)) {
                    f = sGetKeyInfoFlags(KeyEnum.F);
                } else {
                    f = sGetKeyInfoFlags(KeyEnum.DOWN);
                }
                break;
            }
            case BaseEvent.KEYCODE_DPAD_LEFT: {
                if (sIsAvailableKeyInfoX(KeyEnum.F)) {
                    f = sGetKeyInfoFlags(KeyEnum.F);
                } else {
                    f = sGetKeyInfoFlags(KeyEnum.LEFT);
                }
                break;
            }
            case BaseEvent.KEYCODE_DPAD_RIGHT: {
                if (sIsAvailableKeyInfoX(KeyEnum.F)) {
                    f = sGetKeyInfoFlags(KeyEnum.F);
                } else {
                    f = sGetKeyInfoFlags(KeyEnum.RIGHT);
                }
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_A: {
                f = sGetKeyInfoFlags(KeyEnum.A);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_B: {
                f = sGetKeyInfoFlags(KeyEnum.B);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_X: {
                f = sGetKeyInfoFlags(KeyEnum.X);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_Y: {
                f = sGetKeyInfoFlags(KeyEnum.Y);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_L1: {
                f = sGetKeyInfoFlags(KeyEnum.L1);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_R1: {
                f = sGetKeyInfoFlags(KeyEnum.R1);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_L2: {
                f = sGetKeyInfoFlags(KeyEnum.L2);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_R2: {
                f = sGetKeyInfoFlags(KeyEnum.R2);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_THUMBL: {
                f = sGetKeyInfoFlags(KeyEnum.THUMBL);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_THUMBR: {
                f = sGetKeyInfoFlags(KeyEnum.THUMBR);
                break;
            }
            case BaseEvent.KEYCODE_BUTTON_START: {
                f = sGetKeyInfoFlags(KeyEnum.START);
                break;
            }
            case BaseEvent.KEYCODE_LEFT_STICK: {
                f = sGetKeyInfoFlags(KeyEnum.L);
                break;
            }
            case BaseEvent.KEYCODE_RIGHT_STICK: {
                f = sGetKeyInfoFlags(KeyEnum.R);
                break;
            }
        }
        return f;
    }

    public static int sGetKeyInfoFrequencyByKeyCode(int keyCode) {
        int frequency = -1;
        switch (keyCode) {
            case BaseEvent.KEYCODE_LEFT_STICK: {
                frequency = sGetKeyInfoFrequency(KeyEnum.L);
                break;
            }
            case BaseEvent.KEYCODE_RIGHT_STICK: {
                frequency = sGetKeyInfoFrequency(KeyEnum.R);
                break;
            }
        }
        return frequency;
    }

    public static int sGetKeyInfoStepByKeyCode(int keyCode) {
        int step = -1;
        switch (keyCode) {
            case BaseEvent.KEYCODE_LEFT_STICK: {
                step = sGetKeyInfoStep(KeyEnum.L);
                break;
            }
            case BaseEvent.KEYCODE_RIGHT_STICK: {
                step = sGetKeyInfoStep(KeyEnum.R);
                break;
            }
        }
        return step;
    }
}
