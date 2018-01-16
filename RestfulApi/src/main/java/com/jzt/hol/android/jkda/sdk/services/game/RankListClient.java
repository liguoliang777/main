package com.jzt.hol.android.jkda.sdk.services.game;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.game.GameListBody;
import com.jzt.hol.android.jkda.sdk.bean.game.GameRankListBean;
import com.jzt.hol.android.jkda.sdk.services.GameService;
import com.jzt.hol.android.jkda.sdk.services.gamehub.PostMsgBaseClient;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class RankListClient extends PostMsgBaseClient<GameRankListBean> {
    GameListBody bean;

    public RankListClient(Context cxt, GameListBody bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<GameRankListBean> requestService(GameService askDoctorService) {
        return askDoctorService.rankDownloadList(HostDebug.AppJson, bean);
    }
}
