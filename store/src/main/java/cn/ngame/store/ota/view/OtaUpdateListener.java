package cn.ngame.store.ota.view;

/**
 * 视图层接口
 * Created by zeng on 2016/8/15.
 */
public interface OtaUpdateListener {

    /**
     * 检测手柄是否需要升级
     * @param state -2,正在检测; -1.没有设备连接; 0.当前所有设备都是最新版本; 大于0则为需要更新的设备数
     */
    void showCheckVersionResult(int state);

}
