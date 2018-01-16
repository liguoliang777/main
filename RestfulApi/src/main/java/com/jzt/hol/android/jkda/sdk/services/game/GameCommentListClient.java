package com.jzt.hol.android.jkda.sdk.services.game;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.manager.LikeListBean;
import com.jzt.hol.android.jkda.sdk.bean.rank.RankListBody;
import com.jzt.hol.android.jkda.sdk.services.GameService;
import com.jzt.hol.android.jkda.sdk.services.gamehub.PostMsgBaseClient;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class GameCommentListClient extends PostMsgBaseClient<LikeListBean> {
    RankListBody bean;

    public GameCommentListClient(Context cxt, RankListBody bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<LikeListBean> requestService(GameService askDoctorService) {
        return askDoctorService.rankCommentList(HostDebug.AppJson, bean);
    }
}
