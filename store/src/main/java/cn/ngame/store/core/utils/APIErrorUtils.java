package cn.ngame.store.core.utils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by tangkun on 16/9/8.
 */
public class APIErrorUtils {

    private APIErrorUtils() {
        throw new IllegalArgumentException("工具类不能实例化");
    }

    public static String getMessage(Throwable error) {

        if (error instanceof SocketTimeoutException) {
            return "网络连接超时";
        }else if (error instanceof UnknownHostException) {
            return "网络异常,请检查网络";
        } else if (error instanceof IOException) {
            return "服务器错误";
        } else if (error instanceof IllegalArgumentException) {
            return "请求参数错误";
        }
        return "服务器错误";
    }
}
