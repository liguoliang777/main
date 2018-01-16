package com.jzt.hol.android.jkda.sdk.base;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.IApiEndPoint;

import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by tangkun on 16/9/3.
 */
public abstract class BaseClient<K, T> extends AbstractClient {
    public BaseClient(IApiEndPoint client, Context cxt) {
        super(client, cxt);
    }

    public Observable<K> observable() {
        return observable(true);
    }

    public Observable<K> observable(boolean isRetry) {
        Observable<K> observable = configRunOn(getApiObservable(getRetrofit()));
        if (isRetry) {
            observable = observable.retry(retry());
            /*.debounce(100, TimeUnit.MILLISECONDS)*/
        }
        return observable;
    }

    protected Observable<K> configRunOn(Observable<K> observable) {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    protected abstract Observable<K> getApiObservable(Retrofit retrofit);

    public T buildService(Class<T> t) {
        return getRetrofit().create(t);
    }
}
