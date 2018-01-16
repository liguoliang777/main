package cn.ngame.store.core.fileload;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import cn.ngame.store.StoreApplication;
import cn.ngame.store.core.utils.AppInstallHelper;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.TextUtil;

/**
 * 文件下载接口实现类
 * Created by zeng on 2016/10/24.
 */
public class FileLoadManager implements IFileLoad {

    private static FileLoadManager manager;
    private Context context;
    private FileLoadService fileLoadService;

    private boolean isBindServiceSuccess = false;

    private FileLoadManager() {
    }

    private FileLoadManager(Context context) {
        this.context = context;
        doBindService(context);
    }

    private void doBindService(Context context) {

        Intent intent = new Intent(context, FileLoadService.class);
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        isBindServiceSuccess = true;
    }

    public static FileLoadManager getInstance(Context context) {
        if (manager == null) {
            manager = new FileLoadManager(context);
        }
        return manager;
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            FileLoadService.FileLoadServiceBinder binder = (FileLoadService.FileLoadServiceBinder) service;
            fileLoadService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    public void destroy() {

        if (context != null && isBindServiceSuccess) {

            context.unbindService(conn);
            isBindServiceSuccess = false;
        }
    }

    /**
     * 下载文件
     *
     * @param name        文件名
     * @param url         文件的下载地址
     * @param md5         文件的MD5值
     * @param packageName 如果此参数不为null，则表示下载的是可安装文件，否则是不可安装文件
     * @param title       文件名
     * @param previewUrl  文件预览图片
     * @param serverId    文件在服务的ID
     * @param allowAnyNet 是否允许任意网络下下载  默认为false
     * @return 0.正常，1.参数错误，无法下载，
     */
    @Override
    public int load(String name, String url, String md5, String packageName, int versionCode, String title, String previewUrl,
                    long serverId, boolean allowAnyNet) {

        if (TextUtil.isAnyEmpty(name, url, md5, title, previewUrl) || serverId <= 0) {
            return RESULT_PARAMES_ERROR;
        }

        if (!allowAnyNet) {
            if (StoreApplication.net_status == Constant.NET_STATUS_DISCONNECT) {
                return RESULT_NO_NET;
            } else if (StoreApplication.net_status == Constant.NET_STATUS_4G) {
                return RESULT_4G_NET;
            }
        }

        FileLoadInfo info;
        if (TextUtil.isEmpty(packageName)) {
            info = new FileLoadInfo(name, url, md5, versionCode, title, previewUrl, serverId, FileLoadInfo.TYPE_VIDEO);
        } else {
            info = new FileLoadInfo(name, url, md5, versionCode, title, previewUrl, serverId, FileLoadInfo.TYPE_GAME);
            info.setPackageName(packageName);
//            info.setVersionCode(versionCode);
        }

        info.setAllowAnyNet(allowAnyNet);

        if (fileLoadService != null) {
            fileLoadService.startLoad(info);
        }

        return RESULT_SUCCESS;
    }

    @Override
    public int pause(String fileUrl) {

        if (TextUtil.isEmpty(fileUrl)) {
            return 1;
        }

        if (fileLoadService != null) {
            fileLoadService.pauseLoad(fileUrl);
        }

        return 0;
    }

    @Override
    public void pauseAllTemp() {
        if (fileLoadService != null) {
            fileLoadService.pauseAllLoadTemp();
        }
    }

    @Override
    public void loadAllPauseTemp() {
        if (fileLoadService != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    fileLoadService.startLoadTempPause();
                }
            }).start();
        }
    }

    @Override
    public void delete(final String fileUrl) {
        if (fileLoadService != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    fileLoadService.deleteLoad(fileUrl);
                }
            }).start();
        }
    }

    public void deleteByPackage(final String packageName) {
        if (fileLoadService != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    fileLoadService.deleteByPackage(packageName);
                }
            }).start();
        }
    }

    @Override
    public void deleteFinishedLoadRecord() {
        if (fileLoadService != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    fileLoadService.deleteAllFinishedRecord();
                }
            }).start();
        }
    }

    @Override
    public GameFileStatus getGameFileLoadStatus(String fileName, String fileUrl, String packageName, int versionCode) {

        GameFileStatus status = null;
        if (FileLoadService.packageInfoMap == null) {
            return null;
        }
        if (packageName == null) {
            return null;
        }
        PackageInfo packageInfo = FileLoadService.packageInfoMap.get(packageName);
        if (packageInfo != null) {      //已经安装

            if (packageInfo.versionCode >= versionCode) { //已安装的是较新版本

                if (FileLoadService.gameFileStatusMap == null) {
                    status = new GameFileStatus(GameFileStatus.STATE_HAS_INSTALL);
                    status.setLength(100);
                    status.setFinished(100);
                    return status;
                }
                FileLoadInfo info = FileLoadService.gameFileStatusMap.get(fileUrl);
                if (info == null) {
                    status = new GameFileStatus(GameFileStatus.STATE_HAS_INSTALL);
                    status.setLength(100);
                    status.setFinished(100);
                } else {
                    status = new GameFileStatus(info.getUrl(), info.getFinished(), info.getLength(), GameFileStatus
                            .STATE_HAS_INSTALL);
                }

            } else {     //已安装了旧版本

                //TODO 做下载操作
                if (FileLoadService.gameFileStatusMap == null) {
                    return new GameFileStatus(GameFileStatus.STATE_HAS_INSTALL_OLD);
                }
                FileLoadInfo info = FileLoadService.gameFileStatusMap.get(fileUrl);
                if (info == null) {
                    status = new GameFileStatus(GameFileStatus.STATE_HAS_INSTALL_OLD);
                } else {
                    switch (info.getStatus()) {
                        case FileLoadInfo.STATUS_FINISHED:
                            status = new GameFileStatus(info.getUrl(), info.getFinished(), info.getLength(), GameFileStatus
                                    .STATE_HAS_DOWNLOAD);
                            break;
                        case FileLoadInfo.STATUS_LOADING:
                            status = new GameFileStatus(info.getUrl(), info.getFinished(), info.getLength(), GameFileStatus
                                    .STATE_DOWNLOAD);
                            break;
                        case FileLoadInfo.STATUS_PAUSE:
                            status = new GameFileStatus(info.getUrl(), info.getFinished(), info.getLength(), GameFileStatus
                                    .STATE_PAUSE);
                            break;
                        case FileLoadInfo.STATUS_PAUSE_TEMP:
                            status = new GameFileStatus(info.getUrl(), info.getFinished(), info.getLength(), GameFileStatus
                                    .STATE_PAUSE);
                            break;
//                        case FileLoadInfo.STATUS_UPDATE:
//                            status = new GameFileStatus(info.getUrl(),info.getFinished(),info.getLength(),GameFileStatus
// .STATE_HAS_INSTALL_OLD);
                    }
                }
            }

        } else {
            if (FileLoadService.gameFileStatusMap == null) {
                return null;
            }
           // FileLoadInfo info = FileLoadService.gameFileStatusMap.get(fileUrl == null ? "" : fileUrl);
            FileLoadInfo info = FileLoadService.gameFileStatusMap.get(fileUrl);
            if (info == null) {
                status = new GameFileStatus(GameFileStatus.STATE_UN_INSTALL);
            } else {
                switch (info.getStatus()) {
                    case FileLoadInfo.STATUS_FINISHED:
                        status = new GameFileStatus(info.getUrl(), info.getFinished(), info.getLength(), GameFileStatus
                                .STATE_HAS_DOWNLOAD);
                        break;
                    case FileLoadInfo.STATUS_LOADING:
                        status = new GameFileStatus(info.getUrl(), info.getFinished(), info.getLength(), GameFileStatus
                                .STATE_DOWNLOAD);
                        break;
                    case FileLoadInfo.STATUS_PAUSE:
                        status = new GameFileStatus(info.getUrl(), info.getFinished(), info.getLength(), GameFileStatus
                                .STATE_PAUSE);
                        break;
                    case FileLoadInfo.STATUS_PAUSE_TEMP:
                        status = new GameFileStatus(info.getUrl(), info.getFinished(), info.getLength(), GameFileStatus
                                .STATE_PAUSE);
                        break;
//                    case FileLoadInfo.STATUS_UPDATE:
//                        status = new GameFileStatus(info.getUrl(),info.getFinished(),info.getLength(),GameFileStatus
// .STATE_HAS_INSTALL_OLD);
                }
            }
        }

        return status;
    }

    @Override
    public List<FileLoadInfo> getAllFileInfo() {
        ArrayList<FileLoadInfo> infoList = new ArrayList<>();
        ConcurrentHashMap<String, FileLoadInfo> gameFileStatusMap = FileLoadService.gameFileStatusMap;
        if (gameFileStatusMap != null) {
            for (FileLoadInfo info : gameFileStatusMap.values()) {
                infoList.add(info);
            }
        }
        return infoList;
    }
   /* @Override
    public List<FileLoadInfo> getAllFileInfo() {
        ArrayList<FileLoadInfo> infoList = new ArrayList<>();
        if (FileLoadService.gameFileStatusMap != null) {
            for (FileLoadInfo info : FileLoadService.gameFileStatusMap.values()) {
                if (info.getStatus() == FileLoadInfo.STATUS_LOADING || info.getStatus() == FileLoadInfo.STATUS_PAUSE
                        || info.getStatus() == FileLoadInfo.STATUS_PAUSE_TEMP) {
                    infoList.add(info);
                }
            }
        }
        return infoList;
    }*/

    @Override
    public List<FileLoadInfo> getLoadedFileInfo() {
        ArrayList<FileLoadInfo> infoList = new ArrayList<>();
        ConcurrentHashMap<String, FileLoadInfo> gameFileStatusMap = FileLoadService.gameFileStatusMap;
        if (gameFileStatusMap != null) {
            Collection<FileLoadInfo> values = gameFileStatusMap.values();
            for (FileLoadInfo info : values) {
                String packageName = info.getPackageName();
                boolean hasInstalled = AppInstallHelper.isAppInstalled(context, packageName);
                if (!hasInstalled) {// status== GameFileStatus.STATE_HAS_INSTALL
                    infoList.add(info);
                }
            }
        }
        return infoList;
    }

    @Override
    public List<FileLoadInfo> getOpenFileInfo() {
        ArrayList<FileLoadInfo> infoList = new ArrayList<>();
        ConcurrentHashMap<String, FileLoadInfo> gameFileStatusMap = FileLoadService.gameFileStatusMap;
        if (gameFileStatusMap != null) {
            Collection<FileLoadInfo> loadInfos = gameFileStatusMap.values();
            for (FileLoadInfo info : loadInfos) {
                String packageName = info.getPackageName();
                int status = info.getStatus();
                boolean hasInstalled = AppInstallHelper.isAppInstalled(context, packageName);
                if (hasInstalled) {// status== GameFileStatus.STATE_HAS_INSTALL
                    infoList.add(info);
                }

            }
        }
        return infoList;
    }

//    @Override
//    public List<FileLoadInfo> getUpdateFileInfo() {
//        ArrayList<FileLoadInfo> infoList = new ArrayList<>();
//
//        if(FileLoadService.gameFileStatusMap != null){
//            for (FileLoadInfo info : FileLoadService.gameFileStatusMap.values()){
//                if(info.getStatus() == FileLoadInfo.STATUS_UPDATE){
//                    infoList.add(info);
//                }
//            }
//        }
//        return infoList;
//    }
}
