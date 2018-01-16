package com.jzt.hol.android.jkda.sdk.utils;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * Created by tangkun on 16/9/7.
 */
public class ParseUtils {
    private ParseUtils() {
        throw new IllegalArgumentException("工具类不能实例化");
    }

    public static String getBody(RequestBody requestBody) {

        if (requestBody != null) {
            try {
                final Buffer buffer = new Buffer();
                if (requestBody != null) {
                    requestBody.writeTo(buffer);
                    return buffer.readUtf8();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void reBuildHeaders(RequestBody body, Request.Builder requestBuilder) {
        try {
            if (body != null) {
                MediaType contentType = body.contentType();
                if (contentType != null) {
                    requestBuilder.header("Content-Type", contentType.toString());
                }

                long contentLength = body.contentLength();
                if (contentLength != -1) {
                    requestBuilder.header("Content-Length", Long.toString(contentLength));
                    requestBuilder.removeHeader("Transfer-Encoding");
                } else {
                    requestBuilder.header("Transfer-Encoding", "chunked");
                    requestBuilder.removeHeader("Content-Length");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
