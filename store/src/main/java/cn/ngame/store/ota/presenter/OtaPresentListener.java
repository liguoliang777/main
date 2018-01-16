package cn.ngame.store.ota.presenter;

import cn.ngame.store.ota.model.DeviceInfo;

/**
 * 控制层接口
 * Created by zeng on 2016/8/15.
 */
public interface OtaPresentListener {

    /** 检测更新按钮  */
    void checkNewVersion();

    /** 扫描可用的设备 */
    void scanDevice();

    /**
     * 对设备进行OTA升级
     * @param deviceInfo 被升级设备的信息
     */
    void updateDevice(DeviceInfo deviceInfo);

}
