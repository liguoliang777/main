package com.ngds.pad.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/11/28.
 */

public class ConfigHelper {
    protected SharedPreferences m_pref;
    protected SharedPreferences.Editor m_editor;
    public static String m_strSearchHistory = "search.history";
    private static String m_prefName = "Event_Preferences";
    private static ConfigHelper m_sInstance = null;

    private ConfigHelper(Context context){
        super();
        m_pref = context.getSharedPreferences(ConfigHelper.m_prefName, 0);
        m_editor = m_pref.edit();
    }

    public String getStrKeyValue(String key){
        return m_pref.getString(key, "");
    }

    public static ConfigHelper sGetInstance(Context context){
        if(ConfigHelper.m_sInstance == null){
            ConfigHelper.m_sInstance = new ConfigHelper(context);
        }
        return ConfigHelper.m_sInstance;
    }

    public void putStrKeyValue(String key, String defValue){
        m_editor.putString(key, defValue);
        m_editor.commit();
    }

    public long getLongKeyValue(String key, long value){
        return m_pref.getLong(key, value);
    }

    public void putLongKeyValue(String key, long value){
        m_editor.putLong(key, value);
        m_editor.commit();
    }
}
