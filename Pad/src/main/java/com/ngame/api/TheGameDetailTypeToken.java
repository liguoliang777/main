package com.ngame.api;

import com.google.gson.reflect.TypeToken;
import com.ngame.api.model.GameDetail;
import com.ngame.api.model.Response;

/**
 * Created by Administrator on 2017/12/5.
 */

public class TheGameDetailTypeToken extends TypeToken<Response<GameDetail>> {
    protected TheGameDetailTypeToken() {
        super();
    }
}
