package com.jzt.hol.android.jkda.sdk.cache;

/**
 * Created by tangkun on 16/4/28.
 *
 * 请求策略类型
 */
public enum RequestPolicyType {
    /**
     * 有且仅走网络
     */
    NETWORK_NO_CACHE,
    /**
     * 走网络,并保存到缓存
     */
    NETWORK_SAVE_CACHE,
    /**
     * 只取缓存
     */
    NETWORK_ONLY_CACHE,
    /**
     * 优先取缓存，没有缓存再取网络
     */
    NETWORK_FIRST_CACHE,
    /**
     * 先取缓存，调用UI进行展示，然后再调用网络，再调用UI进行展示
     */
    NETWORK_USE_CACHE,
    /**
     * 基于NETWORK_USE_CACHE判断缓存时效性
     */
    NETWORK_USE_AVAILABLE_CACHE
}
