package com.jzt.hol.android.jkda.sdk.services.gamehub;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.AppCarouselBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.BrowseHistoryBodyBean;
import com.jzt.hol.android.jkda.sdk.services.GameService;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class AppCarouselClient extends PostMsgBaseClient<AppCarouselBean> {
    BrowseHistoryBodyBean bean;

    public AppCarouselClient(Context cxt, BrowseHistoryBodyBean bean) {
        super(cxt);
        this.bean = bean;
    }

    @Override
    protected Observable<AppCarouselBean> requestService(GameService askDoctorService) {
        return askDoctorService.queryCarousel(HostDebug.AppJson, bean);
    }
}
