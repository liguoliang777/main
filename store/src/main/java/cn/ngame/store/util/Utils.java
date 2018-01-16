package cn.ngame.store.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.security.MessageDigest;

/**
 * Created by gp on 16/10/8.
 * <p>
 * Build.VERSION.SDK_INT>=23
 */

public class Utils {

    @SuppressLint("WrongConstant")
    public static String getSign(Context paramContext, String paramString) {

        PackageInfo localPackageInfo;
        int i;
        byte[] arrayOfByte;
        try {
            localPackageInfo = paramContext.getPackageManager().getPackageInfo(paramString, 64);
            i = 0;
            if (i >= localPackageInfo.signatures.length) {

                return null;
            }
            System.out.println(i);
            arrayOfByte = localPackageInfo.signatures[i].toByteArray();
            String signStr = hexdigest(arrayOfByte);
            System.out.println(arrayOfByte);
            return signStr;
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            return "";
        }

    }

    private static final char[] hexDigits = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };

    public static String hexdigest(String paramString)
    {
        try
        {
            String str = hexdigest(paramString.getBytes());
            return str;
        }
        catch (Exception localException)
        {
        }
        return null;
    }

    public static String hexdigest(byte[] paramArrayOfByte)
    {
        try
        {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            System.out.println(paramArrayOfByte);
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            System.out.println(arrayOfByte);

            char[] arrayOfChar = new char[32];
            int i = 0;
            int j = 0;
            for(i=0;i<16;i++){

                int k = arrayOfByte[i];
                int l = j + 1;
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                j = l + 1;
                arrayOfChar[l] = hexDigits[(k & 0xF)];
            }
            if (i >= 16)
                return new String(arrayOfChar);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
