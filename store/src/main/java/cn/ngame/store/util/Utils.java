package cn.ngame.store.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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

    private static final char[] hexDigits = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99,
            100, 101, 102};

    public static String hexdigest(String paramString) {
        try {
            String str = hexdigest(paramString.getBytes());
            return str;
        } catch (Exception localException) {
        }
        return null;
    }

    public static String hexdigest(byte[] paramArrayOfByte) {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            System.out.println(paramArrayOfByte);
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            System.out.println(arrayOfByte);

            char[] arrayOfChar = new char[32];
            int i = 0;
            int j = 0;
            for (i = 0; i < 16; i++) {

                int k = arrayOfByte[i];
                int l = j + 1;
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                j = l + 1;
                arrayOfChar[l] = hexDigits[(k & 0xF)];
            }
            if (i >= 16)
                return new String(arrayOfChar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isRooted() {
        // nexus 5x "/su/bin/"
        String[] paths = {"/system/xbin/", "/system/bin/", "/system/sbin/", "/sbin/",
                "/vendor/bin/", "/su/bin/"};
        try {
            for (int i = 0; i < paths.length; i++) {
                String path = paths[i] + "su";
                if (new File(path).exists()) {
                    String execResult = exec(new String[]{"ls", "-l", path});
                    if (TextUtils.isEmpty(execResult) || execResult.indexOf("root") == execResult
                            .lastIndexOf("root")) {
                        return false;
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String exec(String[] exec) {
        String ret = "";
        ProcessBuilder processBuilder = new ProcessBuilder(exec);
        try {
            Process process = processBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process
                    .getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                ret += line;
            }
            process.getInputStream().close();
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    // 执行命令并且输出结果
    public static String execRootCmd(String cmd) {
        String result = "";
        DataOutputStream dos = null;
        DataInputStream dis = null;

        try {
            Process p = Runtime.getRuntime().exec("su");// 经过Root处理的android系统即有su命令
            dos = new DataOutputStream(p.getOutputStream());
            dis = new DataInputStream(p.getInputStream());

            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            String line = null;
            while ((line = dis.readLine()) != null) {
                Log.d("result", line);
                result += line;
            }
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
