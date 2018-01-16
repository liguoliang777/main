package com.jzt.hol.android.jkda.sdk.base.cookie;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by tangkun on 16/5/3.
 */

public class JZTPersistentCookieJar implements CookieJar {

    private Context cxt;

    private static String SHARED_PREFERENCES_KEY_COOKIE = "COOKIE_PREFERENCE";

    public JZTPersistentCookieJar(Context cxt) {
        this.cxt = cxt;
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = new ArrayList<Cookie>();
        SharedPreferences preference = cxt.getSharedPreferences(SHARED_PREFERENCES_KEY_COOKIE, Context.MODE_PRIVATE);
        Map<String, ?> cookieMap = preference.getAll();
        for (String currentKey : cookieMap.keySet()) {
            if (cookies == null) {
                cookies = new ArrayList<Cookie>();
            }
            String currentValue = (String) cookieMap.get(currentKey);
            cookies.add(Cookie.parse(url, currentKey + "=" + currentValue));
        }
        return cookies;
    }

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies == null || cookies.isEmpty()) {
            return;
        }

        int size = cookies.size();

        String[] str_cookies = new String[size];

        for (int i = 0; i < size; i++) {
            str_cookies[i] = cookies.get(i).toString();
        }
        setCookies(str_cookies);
    }

    private void setCookies(String... cookies) {
        //以 \n 分隔的多个cookie
        SharedPreferences preference = cxt.getSharedPreferences(SHARED_PREFERENCES_KEY_COOKIE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        for (String cookieString : cookies) {
            if (cookieString != null && !cookieString.equals("")) {
                String cookieContent = cookieString.substring(0, cookieString.indexOf(";"));
                //判断cookie包含“=” 并且 “=” 不是在开始和结尾
                if (cookieContent != null && cookieContent.contains("=")) {
                    int index = cookieContent.indexOf("=");
                    if (0 < index && index < cookieContent.length() - 1) {
                        String cookieKey = cookieContent.substring(0, index);
                        String cookieValue = cookieContent.substring(index + 1, cookieContent.length());
                        editor.putString(cookieKey, cookieValue);
                    }

                }
            }
        }
        editor.apply();
    }
}
