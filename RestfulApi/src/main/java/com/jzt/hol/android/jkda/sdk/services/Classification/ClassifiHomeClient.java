package com.jzt.hol.android.jkda.sdk.services.Classification;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.classification.AllClassifyBean;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBodyBean;
import com.jzt.hol.android.jkda.sdk.services.GameService;
import com.jzt.hol.android.jkda.sdk.services.gamehub.PostMsgBaseClient;

import rx.Observable;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class ClassifiHomeClient extends PostMsgBaseClient<AllClassifyBean> {
    YunduanBodyBean bean;

    public ClassifiHomeClient(Context cxt, YunduanBodyBean bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<AllClassifyBean> requestService(GameService askDoctorService) {
        return askDoctorService.queryClassifiHome(HostDebug.AppJson, bean);
    }
}
