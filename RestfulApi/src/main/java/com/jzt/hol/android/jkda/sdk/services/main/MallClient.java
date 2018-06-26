package com.jzt.hol.android.jkda.sdk.services.main;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.main.DiscoverListBean;
import com.jzt.hol.android.jkda.sdk.bean.main.MallBean;
import com.jzt.hol.android.jkda.sdk.bean.recommend.RecommendListBody;
import com.jzt.hol.android.jkda.sdk.services.GameService;
import com.jzt.hol.android.jkda.sdk.services.gamehub.PostMsgBaseClient;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class MallClient extends PostMsgBaseClient<MallBean> {
    RecommendListBody bean;

    public MallClient(Context cxt, RecommendListBody bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<MallBean> requestService(GameService askDoctorService) {
        return askDoctorService.queryMall(HostDebug.AppJson, bean);
    }
}
