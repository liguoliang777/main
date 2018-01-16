package com.jzt.hol.android.jkda.sdk.services.admin;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.admin.AdminGameUpdateBody;
import com.jzt.hol.android.jkda.sdk.bean.admin.QuestionListBean;
import com.jzt.hol.android.jkda.sdk.services.GameService;
import com.jzt.hol.android.jkda.sdk.services.gamehub.PostMsgBaseClient;

import rx.Observable;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class ProblemClient extends PostMsgBaseClient<QuestionListBean> {
    AdminGameUpdateBody bean;

    public ProblemClient(Context cxt, AdminGameUpdateBody bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<QuestionListBean> requestService(GameService askDoctorService) {
        return askDoctorService.queryHelpCategoryProblem(HostDebug.AppJson, bean);
    }
}
