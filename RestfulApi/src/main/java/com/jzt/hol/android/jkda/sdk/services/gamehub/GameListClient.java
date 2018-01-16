package com.jzt.hol.android.jkda.sdk.services.gamehub;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.GameListBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.PostMsgBodyBean;
import com.jzt.hol.android.jkda.sdk.services.GameService;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class GameListClient extends PostMsgBaseClient<GameListBean> {
    PostMsgBodyBean bean;

    public GameListClient(Context cxt, PostMsgBodyBean bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<GameListBean> requestService(GameService askDoctorService) {
        return askDoctorService.getGameList(HostDebug.AppJson, bean);
    }
}
