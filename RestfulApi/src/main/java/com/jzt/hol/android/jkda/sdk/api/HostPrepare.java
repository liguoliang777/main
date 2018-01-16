package com.jzt.hol.android.jkda.sdk.api;

/**
 * Created by tangkun on 16/9/3.
 * 预发布环境HOST对象
 */
public final class HostPrepare implements IHostFetcher {
    private final String fHOST = "test-api.998jk.com";
    private final String fHOST_STATIC = "test-static.998jk.com";
    private final String fHOST_OLD = "test-api.998jk.com";

    private final String fHOST_JKDA = fHOST + "/jkda";
    private final String fHOST_JKDA_S_H5 = "test-jkda.998jk.com";
    private final String fHOST_MDS = "prepare.qumaiyao.com";
    private final String fHOST_MDS_IMG = "preimage.qumaiyao.com";
    private final String fHOST_ANCHOR = fHOST_STATIC;
    private final String fHOST_APK = "test.998jk.com";
    private final String fHOST_BOPS = fHOST + "/bops";
    private final String fHOST_BOPS_S_H5 = "test-bops.998jk.com";
    private final String fHOST_BOPS_REGISTERED = fHOST + "/registered";
    private final String fHOST_HDF_REGISTERING = "weixintest59.haodf.com";
    private final String fHOST_BOPS_HEALTH_RECORD = fHOST_BOPS_S_H5 + "/cs/bops2/preview/report.htm";
    private final String fHOST_BOPS_DEMONSTRINE = fHOST_JKDA_S_H5 + "/html/preview/report.html";
    private final String fHOST_CMS = fHOST + "/cms";
    private final String fHOST_CMS_H5 = "test-cms.998jk.com/static/views";
    private final String fHOST_PZ_HOME = "test-wb-pz.998jk.com/view/index.html";
    private final String fHOST_PE = fHOST + "/exam";
    private final String fH5_HEAD = fHOST_STATIC + "/static";
    private final String fH5_SEARCH = fHOST_STATIC + "/jkss";
    private final String fANYCHAT = "ycsf.jzteyao.com.cn";
    private final String fHUANXINIM = fHOST + "/wys";
    private final String fHDF_UPLOAD = fHOST + "/filecenter";//
    private final String fHOST_ANALYZER = fHOST + "/analyzer";//
    private final String fHOST_MSG = fHOST + "/msgcenter";//

    @Override
    public String getHost_OLD() {
        return fHOST_OLD;
    }

    @Override
    public String getHOST_JKDA() {
        return fHOST_JKDA;
    }

    @Override
    public String getHOST_MDS() {
        return fHOST_MDS;
    }

    @Override
    public String getHOST_MDS_IMG() {
        return fHOST_MDS_IMG;
    }

    @Override
    public String getHOST_ANCHOR() {
        return fHOST_ANCHOR;
    }

    @Override
    public String getHOST_APK() {
        return fHOST_APK;
    }

    @Override
    public String getHOST_BOPS() {
        return fHOST_BOPS;
    }

    @Override
    public String getHOST_BOPS_REGISTERED() {
        return fHOST_BOPS_REGISTERED;
    }

    @Override
    public String getHDF_REGISTERING() {
        return fHOST_HDF_REGISTERING;
    }

    @Override
    public String getHOST_BOPS_HEALTH_RECORD() {
        return fHOST_BOPS_HEALTH_RECORD;
    }

    @Override
    public String getHOST_BOPS_DEMONSTRINE() {
        return fHOST_BOPS_DEMONSTRINE;
    }

    @Override
    public String getHOST_CMS() {
        return fHOST_CMS;
    }

    @Override
    public String getHOST_CMS_H5() {
        return fHOST_CMS_H5;
    }

    @Override
    public String getHOST_PZ_HOME() {
        return fHOST_PZ_HOME;
    }

    @Override
    public String getHOST_PE() {
        return fHOST_PE;
    }

    @Override
    public String getH5_HEAD() {
        return fH5_HEAD;
    }

    @Override
    public String getH5_SEARCH() {
        return fH5_SEARCH;
    }

    @Override
    public String getANYCHAT() {
        return fANYCHAT;
    }

    @Override
    public String getHUANXINIM() {
        return fHUANXINIM;
    }

    @Override
    public String getHDF_UPLOAD() {
        return fHDF_UPLOAD;
    }

    @Override
    public String getHOST_ANALYZER() {
        return fHOST_ANALYZER;
    }

    @Override
    public String getfHOST_JKDA_S_H5() {
        return fHOST_JKDA_S_H5;
    }

    @Override
    public String getfHOST_BOPS_S_H5() {
        return fHOST_BOPS_S_H5;
    }

    @Override
    public String getfHOST_MSG() {
        return fHOST_MSG;
    }
}
