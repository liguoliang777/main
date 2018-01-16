package com.jzt.hol.android.jkda.sdk.services.main;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBean;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBodyBean;
import com.jzt.hol.android.jkda.sdk.services.GameService;
import com.jzt.hol.android.jkda.sdk.services.gamehub.PostMsgBaseClient;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class YunduanClient extends PostMsgBaseClient<YunduanBean> {
    YunduanBodyBean bean;

    public YunduanClient(Context cxt, YunduanBodyBean bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<YunduanBean> requestService(GameService askDoctorService) {
        return askDoctorService.gameType(HostDebug.AppJson, bean);
    }
}
