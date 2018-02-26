package com.lx.pad.util;

import android.util.Log;

/**
 * Created by Administrator on 2017/11/28.
 */

/**
 * 自定义的LOG类，便于调试
 * ！！！出版本时必需整体注释掉
 */

//代码完成后此类要完全删除，否则会残留log相关信息
public class LLog {
    static String TAG = "PAD_LOG";

    //将数据转换成16进制字符便于输出查看相关信息
    public static String covertHexStr(byte[] b, String separator){
        String result = "";
        if(separator == null || separator.isEmpty()){
            separator = " ";
        }
        if(b != null){
            for(int i = 0; i < b.length; i++){
                String tmp = "";
                tmp = Integer.toHexString(((int)b[i]) & 0xFF);
                if(tmp.length() == 1){
                    tmp = "0" + tmp;
                }
                result += tmp;
                if(i != b.length - 1){
                    result += separator;
                }
            }
        }else{
            result = "null";
        }

        return result;
    }

    public static String covertHexStr(byte[] b){
        return covertHexStr(b, " ");
    }

    public static void d(String str){
        Log.d(TAG, str);
    }

    public static void d(String str, Exception e){
        Log.d(TAG, str, e);
    }
}
