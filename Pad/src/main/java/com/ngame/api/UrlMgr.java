package com.ngame.api;

import com.google.gson.Gson;
import com.ngame.api.model.Game;
import com.ngame.api.model.GameDetail;
import com.ngame.api.model.Response;

import java.util.List;

/**
 * Created by Administrator on 2017/12/4.
 */

public final class UrlMgr {
    private static final String URL_CLOUD_KEY_PKG = "http://fis.xflqv.cn/v2game";//同步 云端键位

    // 从云端获取游戏包名, ID等信息
    public static List<Game> getUrl(String pkgName){
        return (List<Game>)((Response<Game>)new Gson().fromJson(URLConnectionMgr.sGET(URL_CLOUD_KEY_PKG + "/cloud_map/" + pkgName)
        .setRequestProperty("Platform", "1").requestData(), new TheGameTypeToken().getType())).getData();
    }


    // 读取游戏中的所有按键信息
    public static GameDetail getKeyCodeInfo(int infoId){
        return (GameDetail)((Response<GameDetail>)new Gson().fromJson(URLConnectionMgr.sGET(URL_CLOUD_KEY_PKG + "/cloud_map/info/" + infoId)
        .setRequestProperty("Platform", "1").requestData(), new TheGameDetailTypeToken().getType())).getData();
    }
}
