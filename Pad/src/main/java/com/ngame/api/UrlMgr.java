package com.ngame.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ngame.api.model.Game;
import com.ngame.api.model.GameDetail;
import com.ngame.api.model.Response;

import java.net.URLConnection;
import java.util.List;

/**
 * Created by Administrator on 2017/12/4.
 */

public final class UrlMgr {
    private static final String m_strUrl = "http://files.ngame.cn/v2game/";

    // 从云端获取游戏包名, ID等信息
    public static List<Game> getUrl(String pkgName){
        return (List<Game>)((Response<Game>)new Gson().fromJson(URLConnectionMgr.sGET(m_strUrl + "/cloud_map/" + pkgName)
        .setRequestProperty("Platform", "1").requestData(), new TheGameTypeToken().getType())).getData();
    }


    // 读取游戏中的所有按键信息
    public static GameDetail getKeyCodeInfo(int infoId){
        return (GameDetail)((Response<GameDetail>)new Gson().fromJson(URLConnectionMgr.sGET(m_strUrl + "/cloud_map/info/" + infoId)
        .setRequestProperty("Platform", "1").requestData(), new TheGameDetailTypeToken().getType())).getData();
    }
}
