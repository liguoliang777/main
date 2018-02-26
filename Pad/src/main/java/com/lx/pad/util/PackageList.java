package com.lx.pad.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.lx.pad.ItemType.AppInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/11/27.
 */

public class PackageList {
    public static List<AppInfo> getAllPackageInfoList(Context context){
        List<AppInfo> arrayList = new ArrayList<AppInfo>();
        PackageManager packageManager = context.getPackageManager();
        Iterator<PackageInfo> iter = packageManager.getInstalledPackages(0).iterator();
        while(iter.hasNext()){
            PackageInfo packageInfo = (PackageInfo)iter.next();
            if((packageInfo.applicationInfo.flags & PackageInfo.CONTENTS_FILE_DESCRIPTOR) != 0){
                continue;
            }
            if(packageInfo.packageName.equals("com.lx.pad")){
                continue;
            }

            AppInfo appInfo = new AppInfo();
            appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(packageManager));
            appInfo.setAppName(String.valueOf(packageInfo.applicationInfo.loadLabel(packageManager)));
            appInfo.setPackageName(packageInfo.packageName);
            arrayList.add(appInfo);
        }
        return arrayList;
    }
}
