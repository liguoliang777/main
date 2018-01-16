package com.jzt.hol.android.jkda.sdk.services.gamehub;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.CommentListBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.CommentListBodyBean;
import com.jzt.hol.android.jkda.sdk.services.GameService;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class CommentListClient extends PostMsgBaseClient<CommentListBean> {
    CommentListBodyBean bean;

    public CommentListClient(Context cxt, CommentListBodyBean bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<CommentListBean> requestService(GameService askDoctorService) {
        return askDoctorService.queryCommentList(HostDebug.AppJson, bean);
    }
}
