package com.jzt.hol.android.jkda.sdk;

import android.content.Context;

import com.jzt.hol.android.jkda.sdk.api.IApiEndPoint;
import com.jzt.hol.android.jkda.sdk.base.BaseExClient;

/**
 * Created by tangkun on 16/9/3.
 */
public abstract class JK998ExClient<K> extends BaseExClient<K> {
    public JK998ExClient(IApiEndPoint client, Context cxt) {
        super(client, cxt);
    }
}
