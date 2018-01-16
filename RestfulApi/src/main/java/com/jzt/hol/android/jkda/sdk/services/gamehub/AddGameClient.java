package com.jzt.hol.android.jkda.sdk.services.gamehub;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.AddGameBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.NormalDataBean;
import com.jzt.hol.android.jkda.sdk.services.GameService;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class AddGameClient extends PostMsgBaseClient<NormalDataBean> {
    AddGameBodyBean bean;

    public AddGameClient(Context cxt, AddGameBodyBean bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<NormalDataBean> requestService(GameService askDoctorService) {
        return askDoctorService.addVoteGame(HostDebug.AppJson, bean);
    }
}
