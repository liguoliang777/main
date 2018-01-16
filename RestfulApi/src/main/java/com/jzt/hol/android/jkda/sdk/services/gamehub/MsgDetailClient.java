package com.jzt.hol.android.jkda.sdk.services.gamehub;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.MsgDetailBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.MsgDetailBodyBean;
import com.jzt.hol.android.jkda.sdk.services.GameService;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class MsgDetailClient extends PostMsgBaseClient<MsgDetailBean> {
    MsgDetailBodyBean bean;

    public MsgDetailClient(Context cxt, MsgDetailBodyBean bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<MsgDetailBean> requestService(GameService askDoctorService) {
        return askDoctorService.queryMsgDetail(HostDebug.AppJson, bean);
    }
}
