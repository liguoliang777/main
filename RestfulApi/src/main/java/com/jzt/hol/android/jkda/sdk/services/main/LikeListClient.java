package com.jzt.hol.android.jkda.sdk.services.main;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.manager.LikeListBean;
import com.jzt.hol.android.jkda.sdk.bean.manager.LikeListBody;
import com.jzt.hol.android.jkda.sdk.services.GameService;
import com.jzt.hol.android.jkda.sdk.services.gamehub.PostMsgBaseClient;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class LikeListClient extends PostMsgBaseClient<LikeListBean> {
    LikeListBody bean;

    public LikeListClient(Context cxt, LikeListBody bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<LikeListBean> requestService(GameService askDoctorService) {
        return askDoctorService.listFavorite(HostDebug.AppJson, bean);
    }
}
