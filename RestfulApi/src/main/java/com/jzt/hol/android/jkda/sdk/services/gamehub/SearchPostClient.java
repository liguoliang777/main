package com.jzt.hol.android.jkda.sdk.services.gamehub;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.PostSearchListBean;
import com.jzt.hol.android.jkda.sdk.bean.search.SearchBodyBean;
import com.jzt.hol.android.jkda.sdk.services.GameService;
import com.jzt.hol.android.jkda.sdk.services.search.SearchGVBaseClient;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class SearchPostClient extends SearchGVBaseClient<PostSearchListBean> {
    SearchBodyBean bean;

    public SearchPostClient(Context cxt, SearchBodyBean bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<PostSearchListBean> requestService(GameService askDoctorService) {
        return askDoctorService.searchPost(HostDebug.AppJson, bean);
    }
}
