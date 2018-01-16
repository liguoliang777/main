package cn.ngame.store.bean;

/**
 * 用户登录的Token信息
 * Created by zeng on 2016/6/12.
 */
public class NecessaryItemData {

    private String toolDesc;
    private String parentId;
    private String parentName;
    private String toolName;
    private long toolId;
    private String toolSize;
    private String toolLogo;
    private String toolURL;
    private String md5;
    private String fileName;
    private String packages;
    private String toolVersion;

    public String getToolVersion() {
        return toolVersion;
    }

    public void setToolVersion(String toolVersion) {
        this.toolVersion = toolVersion;
    }

    public NecessaryItemData(String parentId, String parentName, long
            toolId, String toolName, String toolDesc, String toolSize, String toolLogo, String toolURL, String md5, String
            fileName, String packages,String toolVersion) {
        this.toolDesc = toolDesc;
        this.parentId = parentId;
        this.parentName = parentName;
        this.toolName = toolName;
        this.toolSize = toolSize;
        this.toolId = toolId;
        this.toolLogo = toolLogo;
        this.toolURL = toolURL;
        this.md5 = md5;
        this.fileName = fileName;
        this.packages = packages;
        this.toolVersion = toolVersion;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getToolDesc() {
        return toolDesc;
    }

    public void setToolDesc(String toolDesc) {
        this.toolDesc = toolDesc;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public long getToolId() {
        return toolId;
    }

    public void setToolId(long toolId) {
        this.toolId = toolId;
    }

    public String getToolSize() {
        return toolSize;
    }

    public void setToolSize(String toolSize) {
        this.toolSize = toolSize;
    }

    public String getToolLogo() {
        return toolLogo;
    }

    public void setToolLogo(String toolLogo) {
        this.toolLogo = toolLogo;
    }

    public String getToolURL() {
        return toolURL;
    }

    public void setToolURL(String toolURL) {
        this.toolURL = toolURL;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
