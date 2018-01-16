package com.jzt.hol.android.jkda.sdk.services.search;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.search.RequestSearchBean;
import com.jzt.hol.android.jkda.sdk.bean.search.SearchGameVideoBean;
import com.jzt.hol.android.jkda.sdk.services.GameService;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class SearchGVClient extends SearchGVBaseClient<SearchGameVideoBean> {
    RequestSearchBean bean;

    public SearchGVClient(Context cxt, RequestSearchBean bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<SearchGameVideoBean> requestService(GameService askDoctorService) {
        return askDoctorService.getSearchGV(HostDebug.AppJson, bean);
    }
}
