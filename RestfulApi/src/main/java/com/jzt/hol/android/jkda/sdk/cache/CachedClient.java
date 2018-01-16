package com.jzt.hol.android.jkda.sdk.cache;

import com.jzt.hol.android.jkda.sdk.base.BaseClient;

import org.joda.time.IllegalInstantException;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by tangkun on 16/9/11.
 */
public abstract class CachedClient<K, T> {

    protected RequestPolicyType requestPolicyType;
    protected BaseClient<K, T> baseClient;

    public CachedClient(RequestPolicyType requestPolicyType, BaseClient<K, T> baseClient) {
        if(requestPolicyType == null || baseClient == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        this.requestPolicyType = requestPolicyType;
        this.baseClient = baseClient;
    }

    public Observable<K> observable(JZTCacheCallback<K> callback) {

        boolean isNeedExecuteRequest = true;

        switch (requestPolicyType) {
            case NETWORK_NO_CACHE:
            case NETWORK_SAVE_CACHE:
                // doNothing
                break;
            case NETWORK_ONLY_CACHE: {
                isNeedExecuteRequest = false;
                K k = getLocalCache(false);
                if (k != null) {
                    callback.onSuccess(k);
                }
            }
            break;
            case NETWORK_FIRST_CACHE: {
                K k = getLocalCache(false);
                if (k != null) {
                    callback.onSuccess(k);
                    isNeedExecuteRequest = false;
                }
            }
            break;
            case NETWORK_USE_CACHE: {
                K k = getLocalCache(false);
                if (k != null) {
                    callback.onSuccess(k);
                }
            }
            break;
            case NETWORK_USE_AVAILABLE_CACHE: {
                K k = getLocalCache(true);
                if (k != null) {
                    callback.onSuccess(k);
                }
            }
            break;
        }

        if(isNeedExecuteRequest) {
            return baseClient.observable();
        } else {
            return baseClient.observable().flatMap(new Func1<K, Observable<K>>() {
                @Override
                public Observable<K> call(K k) {
                    throw new IllegalInstantException("此处无网络请求,请检查传入的RequestPolicyType值");
                }
            });
        }
    }

    protected abstract K getLocalCache(boolean isJudgeAvailable);
}
