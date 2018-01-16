package com.jzt.hol.android.jkda.sdk.services.search;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.ApiClent;
import com.jzt.hol.android.jkda.sdk.api.HostDebug;
import com.jzt.hol.android.jkda.sdk.base.BaseClient;
import com.jzt.hol.android.jkda.sdk.services.GameService;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public abstract class SearchBaseClient<K> extends BaseClient<K, GameService> {


    public SearchBaseClient(Context cxt) {
        super(new ApiClent(HostDebug.HOST), cxt);
    }

    @Override
    protected Observable<K> getApiObservable(Retrofit retrofit) {
        GameService askDoctorService = buildService(GameService.class);
        return requestService(askDoctorService);
    }

    protected abstract Observable<K> requestService(GameService askDoctorService);
}
