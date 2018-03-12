package cn.ngame.store.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.security.MessageDigest;

/**
 * Created by Administrator on 2018/3/8.
 */

public class MD5Utils {
    public static String getMD5(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            Signature sig = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
                    .signatures[0];
            String md5Fingerprint = doFingerprint(sig.toByteArray(), "MD5");
            return md5Fingerprint;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @param certificateBytes 获取到应用的signature值
     * @param algorithm        在上文指定MD5算法
     * @return md5签名
     */
    public static String doFingerprint(byte[] certificateBytes, String algorithm) throws Exception {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(certificateBytes);
        byte[] digest = md.digest();

        String toRet = "";
        for (int i = 0; i < digest.length; i++) {
            if (i != 0) {
                toRet += ":";
            }
            int b = digest[i] & 0xff;
            String hex = Integer.toHexString(b);
            if (hex.length() == 1) {
                toRet += "0";
            }
            toRet += hex;
        }
        return toRet;
    }
}
