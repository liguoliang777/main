package com.jzt.hol.android.jkda.sdk.services.gamehub;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.VoteListBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.VoteRankBodyBean;
import com.jzt.hol.android.jkda.sdk.services.GameService;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class VoteRankListClient extends PostMsgBaseClient<VoteListBean> {
    VoteRankBodyBean bean;

    public VoteRankListClient(Context cxt, VoteRankBodyBean bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<VoteListBean> requestService(GameService askDoctorService) {
        return askDoctorService.queryPostVoteRankList(HostDebug.AppJson, bean);
    }
}
