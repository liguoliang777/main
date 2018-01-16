package cn.ngame.store.ota.model;

/**
 * 手柄固件升级业务接口
 * Created by zeng on 2016/8/10.
 */
public interface IOtaModel {

    /**
     * 加载ota升级文件
     * @param success
     * @param failed
     * @return
     */
    boolean loadFile(LoadSuccessListener success, LoadFailedListener failed);

    /**
     * 启动bootloader模式
     */
    void launchBootloader();




    interface LoadSuccessListener{
        void onSuccess();
    }

    interface LoadFailedListener{
        void onFailed();
    }

}
