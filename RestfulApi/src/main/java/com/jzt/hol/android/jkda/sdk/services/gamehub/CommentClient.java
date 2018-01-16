package com.jzt.hol.android.jkda.sdk.services.gamehub;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.CommentBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.CommentBodyBean;
import com.jzt.hol.android.jkda.sdk.services.GameService;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class CommentClient extends PostMsgBaseClient<CommentBean> {
    CommentBodyBean bean;

    public CommentClient(Context cxt, CommentBodyBean bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<CommentBean> requestService(GameService askDoctorService) {
        return askDoctorService.commentList(HostDebug.AppJson, bean);
    }
}
