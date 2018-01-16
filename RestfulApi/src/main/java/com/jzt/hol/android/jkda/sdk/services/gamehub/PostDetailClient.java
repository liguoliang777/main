package com.jzt.hol.android.jkda.sdk.services.gamehub;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.MsgDetailBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.PostDetailBean;
import com.jzt.hol.android.jkda.sdk.services.GameService;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class PostDetailClient extends PostMsgBaseClient<PostDetailBean> {
    MsgDetailBodyBean bean;

    public PostDetailClient(Context cxt, MsgDetailBodyBean bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<PostDetailBean> requestService(GameService askDoctorService) {
        return askDoctorService.queryPostDetail(HostDebug.AppJson, bean);
    }
}
