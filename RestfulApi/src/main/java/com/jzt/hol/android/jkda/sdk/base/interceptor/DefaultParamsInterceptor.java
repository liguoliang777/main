package com.jzt.hol.android.jkda.sdk.base.interceptor;

import com.jzt.hol.android.jkda.sdk.base.AbstractClient;
import com.jzt.hol.android.jkda.sdk.utils.ParseUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.ByteString;

/**
 * Created by tangkun on 16/9/6.
 */
public class DefaultParamsInterceptor implements Interceptor {

    private Map<String,String> params;

    public DefaultParamsInterceptor(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if(params != null && params.size() > 0) {
            switch (request.method()) {
                case "GET":
                case "DELETE":
                case "OPTIONS":
                case "HEAD":
                    request = insertParamsToGet(request);
                    break;
                default:
                    request = insertParamsToOther(request);
                    break;
            }
        }
        Response response = chain.proceed(request);
        return response;
    }

    private Request insertParamsToGet(Request request) {
        HttpUrl url = request.url();
        HttpUrl.Builder urlBuilder = url.newBuilder();
        //传入参数包含S_HEALTHACCOUNT则去掉公共参数S_HEALTHACCOUNT
        if(url.queryParameter(AbstractClient.S_HEALTHACCOUNT) != null) {
            params.remove(AbstractClient.S_HEALTHACCOUNT);
        }
        for (String key: params.keySet()) {
            urlBuilder.addQueryParameter(key, params.get(key));
        }
        Request.Builder requestBuilder = request.newBuilder();
        requestBuilder.url(urlBuilder.build());
        return requestBuilder.build();
    }

    private Request insertParamsToOther(Request request) {
        Request.Builder requestBuilder = request.newBuilder();
        RequestBody requestBody = request.body();
        MediaType contentType = requestBody.contentType();
        if(contentType == null) {
            return request;
        }
        if(MediaType.parse("application/json; charset=UTF-8").equals(contentType)) {
            requestBody = transformJsonBody(requestBody);
        } else if(MediaType.parse("application/x-www-form-urlencoded").equals(contentType)) {
            FormBody formBody = (FormBody) requestBody;
            requestBody = transformFormBody(formBody);
        } else {
            return request;
        }

        //重新计算Content-Length
        ParseUtils.reBuildHeaders(requestBody, requestBuilder);

        return requestBuilder.method(request.method(), requestBody).build();
    }

    private RequestBody transformJsonBody(RequestBody requestBody) {
        String body = ParseUtils.getBody(requestBody);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(body);
        } catch (JSONException e) {
            //doNothing
        }
        if(jsonObject == null) {
            jsonObject = new JSONObject();
        }
        //传入参数包含S_HEALTHACCOUNT则去掉公共参数S_HEALTHACCOUNT
        if(!jsonObject.isNull(AbstractClient.S_HEALTHACCOUNT)) {
            params.remove(AbstractClient.S_HEALTHACCOUNT);
        }
        for (String key: params.keySet()) {
            try {
                jsonObject.put(key, params.get(key));
            } catch (JSONException e) {
                //doNothing
            }
        }
        return RequestBody.create(requestBody.contentType(), ByteString.encodeUtf8(jsonObject.toString()));
    }

    private RequestBody transformFormBody(FormBody formBody) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        //传入参数包含S_HEALTHACCOUNT则去掉公共参数S_HEALTHACCOUNT
        boolean isContain = false;
        for (int i = 0; i < formBody.size(); i ++ ) {
            if(AbstractClient.S_HEALTHACCOUNT.equals(formBody.name(i))) {
                isContain = true;
                break;
            }
        }
        if(isContain) {
            params.remove(AbstractClient.S_HEALTHACCOUNT);
        }
        if(formBody != null) {
            for (int i = 0; i < formBody.size(); i++) {
                formBodyBuilder.add(formBody.name(i), formBody.value(i));
            }
        }
        for (String key: params.keySet()) {
            formBodyBuilder.add(key, params.get(key));
        }
        return formBodyBuilder.build();
    }
}