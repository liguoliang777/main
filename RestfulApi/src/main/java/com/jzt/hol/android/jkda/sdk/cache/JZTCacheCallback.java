package com.jzt.hol.android.jkda.sdk.cache;

/**
 * Created by tangkun on 16/9/11.
 */
public interface JZTCacheCallback<K> {

    void onSuccess(K k);
    void onFail(Throwable e);

}
