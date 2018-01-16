package com.jzt.hol.android.jkda.sdk.services.gamehub;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.AddPointBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.NormalDataBean;
import com.jzt.hol.android.jkda.sdk.services.GameService;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class AddPointClient extends PostMsgBaseClient<NormalDataBean> {
    AddPointBodyBean bean;

    public AddPointClient(Context cxt, AddPointBodyBean bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<NormalDataBean> requestService(GameService askDoctorService) {
        return askDoctorService.addPoint(HostDebug.AppJson, bean);
    }
}
