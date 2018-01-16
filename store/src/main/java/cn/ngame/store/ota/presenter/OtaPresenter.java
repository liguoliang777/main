package cn.ngame.store.ota.presenter;

import cn.ngame.store.ota.model.DeviceInfo;
import cn.ngame.store.ota.model.OtaService;
import cn.ngame.store.ota.view.OtaUpdateListener;

/**
 * 控制层接口实现类
 * Created by zeng on 2016/8/15.
 */
public class OtaPresenter implements OtaPresentListener {

    private OtaUpdateListener otaView;
    private OtaService service;

    public OtaPresenter(OtaUpdateListener otaView, OtaService service) {
        this.otaView = otaView;
        this.service = service;
    }

    /**
     * 检测更新
     */
    @Override
    public void checkNewVersion() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                int resultCode = service.checkNewVersion();
                otaView.showCheckVersionResult(resultCode);
            }
        }).start();
    }

    /**
     * 扫描可用设备
     */
    @Override
    public void scanDevice() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                service.scanDevice();
            }
        }).start();
    }

    /**
     * 对设备进行OTA升级
     * @param deviceInfo 被升级设备的信息
     */
    @Override
    public void updateDevice(final DeviceInfo deviceInfo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                service.updateDevice(deviceInfo);
            }
        }).start();
    }

}
