package com.jzt.hol.android.jkda.sdk.services.admin;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.admin.AdminGameUpdateBody;
import com.jzt.hol.android.jkda.sdk.bean.game.GameRankListBean;
import com.jzt.hol.android.jkda.sdk.services.GameService;
import com.jzt.hol.android.jkda.sdk.services.gamehub.PostMsgBaseClient;

import rx.Observable;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class GameUpdateClient extends PostMsgBaseClient<GameRankListBean> {
    AdminGameUpdateBody bean;

    public GameUpdateClient(Context cxt, AdminGameUpdateBody bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<GameRankListBean> requestService(GameService askDoctorService) {
        return askDoctorService.queryGameIsUpdate(HostDebug.AppJson, bean);
    }
}
