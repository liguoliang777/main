package com.jzt.hol.android.jkda.sdk.services.gamehub;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.GameHubMainBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.GameHubMainBodyBean;
import com.jzt.hol.android.jkda.sdk.services.GameService;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class HelpClient extends PostMsgBaseClient<GameHubMainBean> {
    GameHubMainBodyBean bean;

    public HelpClient(Context cxt, GameHubMainBodyBean bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<GameHubMainBean> requestService(GameService askDoctorService) {
        return askDoctorService.helpPostList(HostDebug.AppJson, bean);
    }
}
