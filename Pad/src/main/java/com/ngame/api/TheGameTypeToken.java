package com.ngame.api;

import com.google.gson.reflect.TypeToken;
import com.ngame.api.model.Game;
import com.ngame.api.model.Response;

import java.util.List;

/**
 * Created by Administrator on 2017/12/5.
 */

public class TheGameTypeToken extends TypeToken<Response<List<Game>>> {
    protected TheGameTypeToken() {
        super();
    }
}
