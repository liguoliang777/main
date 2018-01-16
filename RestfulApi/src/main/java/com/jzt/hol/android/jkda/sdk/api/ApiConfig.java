package com.jzt.hol.android.jkda.sdk.api;


import com.jzt.hol.android.jkda.sdk.utils.BuildConfigUtils;

/**
 * Created by tangkun on 16/9/3.
 */
public class ApiConfig implements IHostFetcher {

    private IHostFetcher iHostFetcher;

    private ApiConfig() {
        throw new IllegalArgumentException("单例模式不能创建实例");
    }

    private ApiConfig(IHostFetcher iHostFetcher) {
        this.iHostFetcher = iHostFetcher;
    }

    private static final IHostFetcher sApiConfig;

    static {
        int type = BuildConfigUtils.getBuildConfigValue("HOST_TYPE", Integer.valueOf(-1));

        IHostFetcher iHostFetcher;
        switch (type) {
            //release
            case 1:
                iHostFetcher = new HostRelease();
                break;
            //debug
            case 2:
                iHostFetcher = new HostDebug();
                break;
            //prepare
            case 3:
                iHostFetcher = new HostPrepare();
                break;
            //默认为release
            default:
                iHostFetcher = new HostRelease();
        }
        sApiConfig = new ApiConfig(iHostFetcher);
    }

    public static IHostFetcher getInstance() {
        return sApiConfig;
    }

    @Override
    public String getHOST_JKDA() {
        return iHostFetcher.getHOST_JKDA();
    }

    @Override
    public String getHOST_MDS() {
        return iHostFetcher.getHOST_MDS();
    }

    @Override
    public String getHOST_MDS_IMG() {
        return iHostFetcher.getHOST_MDS_IMG();
    }

    @Override
    public String getHOST_ANCHOR() {
        return iHostFetcher.getHOST_ANCHOR();
    }

    @Override
    public String getHOST_APK() {
        return iHostFetcher.getHOST_APK();
    }

    @Override
    public String getHOST_BOPS() {
        return iHostFetcher.getHOST_BOPS();
    }

    @Override
    public String getHOST_BOPS_REGISTERED() {
        return iHostFetcher.getHOST_BOPS_REGISTERED();
    }

    @Override
    public String getHOST_BOPS_HEALTH_RECORD() {
        return "http://" + iHostFetcher.getHOST_BOPS_HEALTH_RECORD();
    }

    @Override
    public String getHOST_BOPS_DEMONSTRINE() {
        return "http://" + iHostFetcher.getHOST_BOPS_DEMONSTRINE();
    }

    @Override
    public String getHOST_CMS() {
        return iHostFetcher.getHOST_CMS();
    }

    @Override
    public String getHOST_CMS_H5() {
        return iHostFetcher.getHOST_CMS_H5();
    }

    @Override
    public String getHOST_PZ_HOME() {
        return "http://" + iHostFetcher.getHOST_PZ_HOME();
    }

    @Override
    public String getHOST_PE() {
        return iHostFetcher.getHOST_PE();
    }

    @Override
    public String getH5_HEAD() {
        return iHostFetcher.getH5_HEAD();
    }

    @Override
    public String getH5_SEARCH() {
        return iHostFetcher.getH5_SEARCH();
    }

    @Override
    public String getANYCHAT() {
        return iHostFetcher.getANYCHAT();
    }

    @Override
    public String getHUANXINIM() {
        return iHostFetcher.getHUANXINIM();
    }

    @Override
    public String getHDF_UPLOAD() {
        return iHostFetcher.getHDF_UPLOAD();
    }

    @Override
    public String getHDF_REGISTERING() {
        return "http://" + iHostFetcher.getHDF_REGISTERING();
    }

    @Override
    public String getHOST_ANALYZER() {
        return iHostFetcher.getHOST_ANALYZER();
    }

    public String getfHOST_JKDA_S_H5() {
        return iHostFetcher.getfHOST_JKDA_S_H5();
    }

    public String getfHOST_BOPS_S_H5() {
        return iHostFetcher.getfHOST_BOPS_S_H5();
    }

    @Override
    public String getHost_OLD() {
        return iHostFetcher.getHost_OLD();
    }

    public String getfHOST_MSG() {
        return iHostFetcher.getfHOST_MSG();
    }
}
