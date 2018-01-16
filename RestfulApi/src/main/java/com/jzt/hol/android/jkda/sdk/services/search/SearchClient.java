package com.jzt.hol.android.jkda.sdk.services.search;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.search.SearchBean;
import com.jzt.hol.android.jkda.sdk.bean.search.SearchBodyBean;
import com.jzt.hol.android.jkda.sdk.services.GameService;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class SearchClient extends SearchGVBaseClient<SearchBean> {
    SearchBodyBean bean;

    public SearchClient(Context cxt, SearchBodyBean bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<SearchBean> requestService(GameService askDoctorService) {
        return askDoctorService.runSearch(HostDebug.AppJson, bean);
    }
}
