package cn.ngame.store.core.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.exception.NoSDCardException;

/**
 * APP更新工具
 *
 * @author flan
 * @since 2016/5/9
 */
public class AppInstallHelper {

    /**
     * 检测是否有新版本
     *
     * @param remoteVersion 远程服务器上的版本号
     */
    public static boolean hasNewVersion(Context context, int remoteVersion) {

        int localVersion = CommonUtil.getVersionCode(context);
        if (remoteVersion > localVersion) {
            return true;
        }
        return false;
    }

    /**
     * 获取所有已安装的APP的包名
     *
     * @param context
     * @return
     */
    public static List<String> allInstallApkPackage(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName;
    }

    /**
     * 根据app包名检测App是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {

        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }

    /**
     * 安装APK文件
     *
     * @param apkName apk文件名
     */
    public static void installApk(Context context, String apkName) {

        try {
            if (apkName != null && apkName.endsWith(".apk")) {

                File filePath = new File(CommonUtil.getFileLoadBasePath(), apkName);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    String packageName = context.getApplicationContext().getPackageName();
                    String authority =  new StringBuilder(packageName).append(".provider").toString();
                    Uri contentUri = FileProvider.getUriForFile(context, authority, filePath);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(filePath), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent);
            }
        } catch (NoSDCardException e) {
            e.printStackTrace();
        }
    }

    /**
     * 安装APK文件
     *
     * @param file apk文件
     */
    public static void installApk(Context context, File file) {

        if (file != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }


    /**
     * 通过APP的包名打开APP
     *
     * @param packageName APK文件的包名
     */
    public static void openApp(Context context, String packageName) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "没有安装", Toast.LENGTH_LONG).show();
        }
    }

    //卸载应用程序
    public static void unstallApp(Context context, String packageName) {
        try {
            Intent uninstall_intent = new Intent();
            uninstall_intent.setAction(Intent.ACTION_DELETE);
            uninstall_intent.setData(Uri.parse("package:" + packageName));
            context.startActivity(uninstall_intent);
        } catch (Exception e) {
            //Toast.makeText(context, "该应用尚未安装,或可尝试手动卸载哦", Toast.LENGTH_LONG).show();
        }
    }
}
