package cn.ngame.store.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

import cn.ngame.store.StoreApplication;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.FileLoadService;

/**
 * 用于监听APP安装与卸载
 * Created by zeng on 2016/11/2.
 */
public class InstallStatusReceiver extends BroadcastReceiver {
    public static final String TAG = InstallStatusReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        //接收安装广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            //System.out.println("安装了:" +packageName + "包名的程序");
            String packageName = intent.getDataString();

            if (packageName != null) {
                //packageNma
                String[] packageNames = packageName.split(":");
                if (packageNames.length == 2) {
                    packageName = packageNames[1];
                }
            }
            PackageManager packageManager = context.getPackageManager();
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            if (packageInfos != null) {
                for (int i = 0; i < packageInfos.size(); i++) {
                    PackageInfo info = packageInfos.get(i);
                    String pn = info.packageName;
                    if (packageName.equals(pn)) {
                        if (FileLoadService.packageInfoMap != null)
                            FileLoadService.packageInfoMap.put(pn, info);
                    }
                }
            }

            //删除APK文件
            if (StoreApplication.isDeleteApk) {
                FileLoadManager manager = FileLoadManager.getInstance(context);
                manager.deleteByPackage(packageName);
            }

        }
        //接收卸载广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {

            //System.out.println("卸载了:"  + packageName + "包名的程序");

            String packageName = intent.getDataString();
            if (packageName != null) {
                String[] packageNames = packageName.split(":");
                if (packageNames.length == 2) {
                    packageName = packageNames[1];
                }
            }
            if (FileLoadService.packageInfoMap != null && FileLoadService.packageInfoMap.containsKey(packageName)) {
                FileLoadService.packageInfoMap.remove(packageName);
            }
        }
    }
}
