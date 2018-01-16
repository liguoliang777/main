package com.jzt.hol.android.jkda.sdk.services.main;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.BrowseHistoryBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.GameHubMainBean;
import com.jzt.hol.android.jkda.sdk.services.GameService;
import com.jzt.hol.android.jkda.sdk.services.gamehub.PostMsgBaseClient;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class HomeHotRaiderClient extends PostMsgBaseClient<GameHubMainBean> {
    BrowseHistoryBodyBean bean;

    public HomeHotRaiderClient(Context cxt, BrowseHistoryBodyBean bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<GameHubMainBean> requestService(GameService askDoctorService) {
        return askDoctorService.queryHomeRaider(HostDebug.AppJson, bean);
    }
}
