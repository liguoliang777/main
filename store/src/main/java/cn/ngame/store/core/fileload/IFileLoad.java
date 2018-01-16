package cn.ngame.store.core.fileload;

import java.util.List;

/**
 * 文件下载的公共类接口
 * Created by zeng on 2016/10/24.
 */
public interface IFileLoad {

    int RESULT_SUCCESS       =   0;
    int RESULT_PARAMES_ERROR =   1;
    int RESULT_NO_NET        =   2;
    int RESULT_4G_NET        =   3;
    int RESULT_WIFI_NET      =   4;

    /**
     * 开始下载文件
     * @param name   文件名
     * @param url    文件的下载地址
     * @param packageName    如果此参数不为null，则表示下载的是可安装文件，否则是不可安装文件
     * @param allowAnyNet   是否允许任意网络下下载  默认为false
     * @return
     */
    int load(String name, String url, String md5, String packageName,int versionCode, String title, String previewUrl, long serverId,boolean allowAnyNet);

    /**
     * 暂停文件的下载
     * @param fileUrl   正在被下载文件的URL
     * @return
     */
    int pause(String fileUrl);

    /***
     * 临时暂停所有下载
     */
    void pauseAllTemp();

    /**
     * 继续下载所有临时暂停的任务
     */
    void loadAllPauseTemp();

    /**
     * 删除正在被下载的文件
     * @param fileUrl   正在被下载文件的URL
     * @return
     */
    void delete(String fileUrl);

    /**
     * 删除所有已经下载完成的下载记录，及源文件
     * @return
     */
    void deleteFinishedLoadRecord();

    /**
     * 查询游戏文件下载状态
     * @param fileName   游戏名
     * @param fileUrl   文件下载的RUL
     * @param packageName   APP类型文件的包名
     * @return  下载状态实体类
     */
    GameFileStatus getGameFileLoadStatus(String fileName, String fileUrl,String packageName,int versionCode);

    /**
     * 获取下载中的文件信息
     * @return
     */
    List<FileLoadInfo> getAllFileInfo();

    /**
     * 获取下载完成的文件信息
     * @return
     */
    List<FileLoadInfo> getLoadedFileInfo();
    /**
     * 获取下载完成的文件信息
     * @return
     */
    List<FileLoadInfo> getOpenFileInfo();

//    /**
//     * 获取下载完成后 是否需要更新
//     * @return
//     */
//    List<FileLoadInfo> getUpdateFileInfo();
}
