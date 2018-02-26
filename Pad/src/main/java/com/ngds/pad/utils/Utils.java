package com.ngds.pad.utils;

import android.content.Context;

import com.lx.pad.util.LLog;

/**
 * Created by Administrator on 2017/11/28.
 */

public class Utils {
    private static final char[] m_hexTable = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static final String hexToStr(byte[] str){
        return Utils.hexToStr(str, " ");
    }

    public static final String hexToStr(byte[] data, String separator){
        if(separator == null || separator.isEmpty()){
            separator = " ";
        }

        StringBuilder result = new StringBuilder(data.length * 2);
        for(int i = 0; i < data.length; i++){
            result.append(Utils.m_hexTable[(data[i] & 0xF0) >>> 4]);
            result.append(Utils.m_hexTable[data[i] & 0xF]);
            if(i < data.length - 1){
                result.append(separator);
            }
        }
        return result.toString();
    }

    public static short sGetShortVal(byte[] data, int dataIndex){
        return (short)((data[dataIndex + 1] << 8) | (data[dataIndex] & 0xFF));
    }

    public static byte[] catByteAry(byte[] src1, byte[] src2){
        byte[] result = new byte[src1.length + src2.length];
        System.arraycopy(src1, 0, result, 0, src1.length);
        System.arraycopy(src2, 0, result, src1.length, src2.length);
        return result;
    }

    public static int sByteToUnsignedInt(byte val){
        return val & 0xFF;
    }

    public static String getDeviceId(Context context){
        LLog.d("Utils->getDeviceId function is not realize!");
        String result = null;
        if(context == null){
            result = null;
        }else{
            result = ConfigHelper.sGetInstance(context).getStrKeyValue("device_id");
            if(result.isEmpty()){
                result = null;
            }else{
                result = ConfigHelper.sGetInstance(context).getStrKeyValue("device_id");
                if(result.isEmpty()){

                }
            }
        }
        return result;
    }

    public static final byte[] strToHex(String data){
        byte[] buf;
        int bufSize = data.length() >> 1;
        if(bufSize > 0){
            buf = new byte[bufSize];
            int tempIndex = 0;
            int nIndex;
            for(nIndex = 0; tempIndex < bufSize; nIndex = tempIndex << 1){
                buf[tempIndex] = (byte)(Integer.parseInt(data.substring(nIndex, nIndex + 2), 16));
                tempIndex++;
            }
        }else{
            buf = null;
        }
        return buf;
    }

    public static int sSymbolNot(byte bVal){
        int result = bVal < 0x80 ? bVal + 0x80 : bVal - 0x80;
        return result;
    }

    public static final String hexToStrNoSeparator(byte[] data){
        StringBuilder result = new StringBuilder(data.length * 2);
        for(int i = 0; i < data.length; i++){
            result.append(Utils.m_hexTable[(data[i] & 0xF0) >>> 4]);
            result.append(Utils.m_hexTable[data[i] & 0xF]);
        }
        
        return result.toString();
    }
}
