package com.jzt.hol.android.jkda.sdk.services.gamehub;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.CommentBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.VoteListBean;
import com.jzt.hol.android.jkda.sdk.services.GameService;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class BrowseClient extends PostMsgBaseClient<VoteListBean> {
    CommentBodyBean bean;

    public BrowseClient(Context cxt, CommentBodyBean bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<VoteListBean> requestService(GameService askDoctorService) {
        return askDoctorService.browseList(HostDebug.AppJson, bean);
    }
}
