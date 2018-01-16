package com.jzt.hol.android.jkda.sdk.services.admin;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.admin.AdminGameUpdateBody;
import com.jzt.hol.android.jkda.sdk.bean.admin.SystemMsgBean;
import com.jzt.hol.android.jkda.sdk.services.GameService;
import com.jzt.hol.android.jkda.sdk.services.gamehub.PostMsgBaseClient;

import rx.Observable;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class QuerySystemMsgClient extends PostMsgBaseClient<SystemMsgBean> {
    AdminGameUpdateBody bean;

    public QuerySystemMsgClient(Context cxt, AdminGameUpdateBody bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<SystemMsgBean> requestService(GameService askDoctorService) {
        return askDoctorService.querySystemMessList(HostDebug.AppJson, bean);
    }
}
