package com.jzt.hol.android.jkda.sdk.rx;

import rx.Observer;

/**
 * Created by tangkun on 16/9/7.
 */
public abstract class ObserverWrapper<T> implements Observer<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public abstract void onError(Throwable e);

    @Override
    public abstract void onNext(T t);
}
