package com.jzt.hol.android.jkda.sdk.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jzt.hol.android.jkda.sdk.api.IApiEndPoint;
import com.jzt.hol.android.jkda.sdk.base.cookie.JZTPersistentCookieJar;
import com.jzt.hol.android.jkda.sdk.base.gson.GsonConverterFactory;
import com.jzt.hol.android.jkda.sdk.base.interceptor.DefaultParamsInterceptor;
import com.jzt.hol.android.jkda.sdk.log.HttpLoggingInterceptor;
import com.jzt.hol.android.jkda.sdk.utils.BuildConfigUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.internal.Platform;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.functions.Func2;

/**
 * Created by tangkun on 16/9/5.
 */
public abstract class AbstractClient {

    protected final String fTag = Platform.get().getPrefix();

    private Gson GSON = new GsonBuilder().disableHtmlEscaping().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    protected IApiEndPoint client;
    protected Context cxt;

    public AbstractClient(IApiEndPoint client, Context cxt) {
        this.client = client;
        this.cxt = cxt;
        if (client == null || cxt == null) {
            throw new IllegalArgumentException("请检查入参");
        }
    }

    private Retrofit instance;

    private void buildRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder();
        String apiEndpoint = client.getApiEndpoint();
        if (!TextUtils.isEmpty(apiEndpoint)) {
            builder.baseUrl(apiEndpoint);
        }

        List<Converter.Factory> list = getConverters();
        if (list != null) {
            for (Converter.Factory convert : list) {
                builder.addConverterFactory(convert);
            }
        }

        //Json convert放到最后
        builder.addConverterFactory(GsonConverterFactory.create(getGSON()));
        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        OkHttpClient client = createClient();
        //
        builder.client(client);
        //
        builder.validateEagerly(true);
        instance = builder.build();
    }

    public Retrofit getRetrofit() {
        Retrofit result = instance;
        if (result == null) {
            synchronized (this) {
                result = instance;
                if (result == null) {
                    buildRetrofit();
                    result = instance;
                }
            }
        }
        return result;
    }

    protected OkHttpClient createClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //cookie
        builder.cookieJar(new JZTPersistentCookieJar(cxt));

        Interceptor defaultInterceptor = getDefaultInterceptor();
        if (defaultInterceptor != null) {
            builder.addInterceptor(defaultInterceptor);
        }

        boolean isDebuggable = BuildConfigUtils.getBuildConfigValue("DEBUG", Boolean.FALSE);

        if (isDebuggable) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(getLoggingLevel());
            builder.addInterceptor(logging);
        }

        List<Interceptor> list = getInterceptors();
        if (list != null) {
            for (Interceptor interceptor : list) {
                builder.addInterceptor(interceptor);
            }
        }

        list = getNetworkInterceptors();
        if (list != null) {
            for (Interceptor interceptor : list) {
                builder.addNetworkInterceptor(interceptor);
            }
        }

        int connectTimeout = getConnectTimeout();
        if (connectTimeout > 0) {
            builder.connectTimeout(connectTimeout, TimeUnit.SECONDS);
        }
        int readTimeout = getReadTimeout();
        if (readTimeout > 0) {
            builder.readTimeout(readTimeout, TimeUnit.SECONDS);
        }
        return builder.build();
    }

    protected HttpLoggingInterceptor.Level getLoggingLevel() {
        return HttpLoggingInterceptor.Level.BODY;
    }

    protected Interceptor getDefaultInterceptor() {
        Map<String, String> params = getDefaultParams();
        if (params == null || params.isEmpty()) {
            return null;
        }
        return new DefaultParamsInterceptor(params);
    }

    public static String S_HEALTHACCOUNT = "healthAccount";

    protected Map<String, String> getDefaultParams() {
        Map<String, String> params = new HashMap<String, String>();
        String appversion = "";
        try {
            PackageManager pm = cxt.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(cxt.getPackageName(), 0);
            appversion = pi.versionName;
        } catch (Exception e) {
        }
        String version = android.os.Build.VERSION.RELEASE;
        SharedPreferences preference = cxt.getSharedPreferences("users", Context.MODE_PRIVATE);
        String healthaccount = preference != null ? preference.getString("healthAccount", "") : null;
        String loginVal = preference != null ? preference.getString("token", "") : null;
        params.put("appVersion", appversion);
        params.put("os", "android");
        params.put("system", version);
        params.put("token", loginVal);
        return params;
    }

    protected List<Converter.Factory> getConverters() {
        return null;
    }

    protected List<Interceptor> getNetworkInterceptors() {
        return null;
    }

    protected List<Interceptor> getInterceptors() {
        return null;
    }

    protected int getConnectTimeout() {
        return 10;
    }

    protected int getReadTimeout() {
        return 10;
    }

    public IApiEndPoint getClient() {
        return client;
    }

    public Context getCxt() {
        return cxt;
    }

    public Gson getGSON() {
        return GSON;
    }

    protected Func2<Integer, Throwable, Boolean> retry() {
        return new Func2<Integer, Throwable, Boolean>() {
            @Override
            public Boolean call(Integer integer, Throwable throwable) {
                if (throwable instanceof HttpException) {
                    //You can do something;
                }
                if (throwable != null) {
                }
                return integer < 3;
            }
        };
    }
}