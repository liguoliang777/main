package com.jzt.hol.android.jkda.sdk.services.recommend;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.recommend.RecommendListBean;
import com.jzt.hol.android.jkda.sdk.bean.recommend.RecommendListBody;
import com.jzt.hol.android.jkda.sdk.services.GameService;
import com.jzt.hol.android.jkda.sdk.services.gamehub.PostMsgBaseClient;

import rx.Observable;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class RecommendClient extends PostMsgBaseClient<RecommendListBean> {
    RecommendListBody bean;

    public RecommendClient(Context cxt, RecommendListBody bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<RecommendListBean> requestService(GameService askDoctorService) {
        return askDoctorService.queryGameRecommendList(HostDebug.AppJson, bean);
    }
}
