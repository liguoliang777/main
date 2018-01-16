package com.jzt.hol.android.jkda.sdk.base;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.IApiEndPoint;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by tangkun on 16/9/3.
 * 不推荐使用
 */
@Deprecated
public abstract class BaseExClient<K> extends AbstractClient {

    public BaseExClient(IApiEndPoint client, Context cxt) {
        super(client, cxt);
    }

    public Observable<Response<K>> observable() {
        return configRunOn(getApiObservable().retry(retry()));
    }

    protected Observable<Response<K>> configRunOn(Observable<Response<K>> observable) {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Response<K>> getApiObservable() {
        return Observable.create(getApiObservable(getRetrofit()));
    }

    protected abstract ApiSubscriber getApiObservable(Retrofit retrofit);

    public abstract class ApiSubscriber
            implements Observable.OnSubscribe<Response<K>>, Callback<K> {

        Subscriber<? super Response<K>> subscriber;

        public ApiSubscriber() {
        }

        @Override
        public void onResponse(Call<K> call, Response<K> response) {
            subscriber.onNext(response);
            subscriber.onCompleted();
        }

        @Override
        public void onFailure(Call<K> call, Throwable t) {
            subscriber.onError(t);
        }

        @Override
        public void call(Subscriber<? super Response<K>> subscriber) {
            this.subscriber = subscriber;
            call(getRetrofit());
        }

        protected abstract void call(Retrofit retrofit);
    }

}
